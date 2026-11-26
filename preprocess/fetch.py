#!/usr/bin/env python

import os
import sys
from argparse import ArgumentParser

from biotite.database.rcsb import fetch
from biotite.structure import AtomArray, AtomArrayStack, filter_amino_acids
from biotite.structure.io.pdbx import CIFBlock, CIFFile, get_structure, set_structure
from hydride import add_hydrogen, relax_hydrogen  # type: ignore

infile: str
outdir: str
override: bool = False
skip: int = 0


def help() -> None:
    """Print usage information."""
    print(
        "fetch.py\n\n"
        + "Fetches PDB files and preprocesses them. The first argument is a file with a list of\n"
        + "PDB ids to fetch, and the second is the directory to save them in.\n\n"
        + "USAGE:\n"
        + "  echo 1A6M | python fetch.py structures/\n"
        + "  python fetch.py pdbids.txt structures/\n"
    )


def validate_io() -> bool:
    """Ensures that IO operations will succeed with the given arguments."""
    ok = True
    if os.path.exists(outdir) and not os.path.isdir(outdir):
        print(f"Output path {outdir} exists and is not a directory.", file=sys.stderr)
        ok = False

    try:
        os.makedirs(outdir)
    except FileExistsError:
        pass
    except OSError:
        print(f"Failed to create directory {outdir}.", file=sys.stderr)
        ok = False

    if not os.path.isfile(infile):
        print(f"Failed to access PDB entry list {infile}.", file=sys.stderr)
        ok = False

    return ok


def fetch_files() -> list[str]:
    """Fetches the list of files from RCSB PDB. Requires internet."""
    pdbids: list[str]
    with open(infile) as f:
        pdbids = [id.strip()[0:4] for id in f.readlines()[skip:]]

    return fetch(pdbids, "cif", outdir, verbose=True, overwrite=override)


def get_structure_data(cif: CIFFile) -> AtomArray:
    """Gets structure data from a cif file in a way that is compatible with Biojava."""
    struct = get_structure(
        cif, include_bonds=True, extra_fields=["charge", "B_iso_or_equiv"]
    )
    return struct if isinstance(struct, AtomArray) else struct[0]


def cif_new_with(cif: CIFFile, structure: AtomArray | AtomArrayStack) -> CIFFile:
    """Creates a new CIF file with metadata from the old file and a structure."""
    new = CIFFile(
        {
            model: CIFBlock(
                {
                    "entity": cif[model]["entity"],
                    "entity_poly": cif[model]["entity_poly"],
                    "entity_poly_seq": cif[model]["entity_poly_seq"],
                    "struct_asym": cif[model]["struct_asym"],
                }
            )
            for model in cif.keys()  # noqa
        }
    )
    set_structure(new, structure, extra_fields=["B_iso_or_equiv"])
    for model in new.keys():  # noqa
        if "struct_conn" in new[model]:
            del new[model]["struct_conn"]
        new[model]["struct_asym"]["pdbx_modified"].as_array()[:] = "Y"
    return new


def process_files(files: list[str]) -> None:
    """Processes the fetched files."""
    with open(infile) as f:
        chains: dict[str, str] = {
            line.strip()[0:4]: line.strip().split(" ")[0][4:]
            for line in f.readlines()[skip:]
        }
    print("\n")
    for file in files:
        pdbid = os.path.splitext(os.path.basename(file))[0]
        name, ext = os.path.splitext(os.path.normpath(file))
        if not override and os.path.exists(name + "_P" + ext):
            continue

        print("\x1b[F\x1b[Kprocessing", pdbid)
        cif: CIFFile = CIFFile.read(file)

        # getting the structure data will remove alternate locations
        struct = get_structure_data(cif)

        # remove non-residue atoms that we don't care about
        struct = struct[filter_amino_acids(struct)]

        # filter for only the chain we want
        if pdbid in chains and chains[pdbid] != "":
            struct = struct[struct.chain_id == chains[pdbid]]

        # filter out existing hydrogen and deuterium
        struct = struct[~(struct.element == "H")]
        struct = struct[~(struct.element == "D")]

        # add hydrogen
        struct, _ = add_hydrogen(struct)
        struct.coord = relax_hydrogen(struct, iterations=4096)

        cif = cif_new_with(cif, struct)
        cif.write(name + "_P" + ext)


def main() -> None:
    global infile, outdir, override, skip

    parser = ArgumentParser()
    parser.add_argument(
        "infile", nargs=1, help="A file with a list of PDB IDs to fetch."
    )
    parser.add_argument(
        "outdir", nargs=1, help="The output directory to place files in."
    )
    parser.add_argument("-o", "--override", action="store_true")
    parser.add_argument("-s", "--skip", action="store", type=int, default=0)
    args = parser.parse_args()

    infile = args.infile[0]
    outdir = args.outdir[0]
    override = args.override
    skip = args.skip

    if not validate_io():
        sys.exit(2)

    fetched = fetch_files()
    process_files(fetched)


if __name__ == "__main__":
    main()
