#!/usr/bin/env python

import os
import sys
from argparse import ArgumentParser

import numpy as np
from biotite.database.rcsb import fetch
from biotite.structure import AtomArray, AtomArrayStack, filter_amino_acids, stack
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


def get_structure_data(cif: CIFFile) -> AtomArray | AtomArrayStack:
    """Gets structure data from a cif file in a way that is compatible with Biojava."""
    return get_structure(
        cif, include_bonds=True, extra_fields=["charge", "B_iso_or_equiv"]
    )


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
    for model in new.keys():  # noqa
        new[model]["struct_asym"]["pdbx_modified"].as_array()[:] = "Y"
    set_structure(new, structure, extra_fields=["B_iso_or_equiv"])
    return new


def remove_altlocs(cif: CIFFile) -> CIFFile:
    """Filters out alternate locations from a CIF file."""
    # reading the structure is enough to remove altlocs
    s = get_structure_data(cif)
    return cif_new_with(cif, s)


def filter_aa(cif: CIFFile) -> CIFFile:
    """Filters a CIF file so it only contains amino acid residues."""
    s = get_structure_data(cif)
    if isinstance(s, AtomArrayStack):
        s = s[:, filter_amino_acids(s)]
    else:
        s = s[filter_amino_acids(s)]
    return cif_new_with(cif, s)


def add_h(structure: AtomArray | AtomArrayStack) -> AtomArray | AtomArrayStack:
    """Uses Hydride to add hydrogen atoms to a structure that doesn't have them."""
    if not isinstance(structure, AtomArrayStack):
        structure = stack(structure)
    arrays = []
    for a in structure:
        a, _ = add_hydrogen(a)
        a.coord = relax_hydrogen(a, iterations=10000)
        arrays.append(a)
    structure = stack(arrays)
    return structure


def convert_d_to_h(s: AtomArray | AtomArrayStack):
    """Converts deuterium atoms to hydrogen."""
    np.putmask(s.element, s.element == "D", "H")
    prefix = np.strings.replace(np.strings.slice(s.atom_name, 1), "D", "H")
    suffix = np.strings.slice(s.atom_name, 1, None)
    s.atom_name = np.strings.add(prefix, suffix)


def fix_h(cif: CIFFile) -> CIFFile:
    """Fixes hydrogen atoms in the structure by adding them or converting D to H."""
    s = get_structure_data(cif)
    if np.any(s.element == "D") or np.any(np.strings.startswith(s.atom_name, "D")):
        convert_d_to_h(s)
    if np.any(s.element == "H"):
        return cif
    return cif_new_with(cif, add_h(s))


def filter_chain(cif: CIFFile, chain_id: str) -> CIFFile:
    """Discards every chain excpt the one which whose ID is given."""
    s = get_structure_data(cif)
    if isinstance(s, AtomArrayStack):
        s = s[:, s.chain_id == chain_id]
    else:
        s = s[s.chain_id == chain_id]
    return cif_new_with(cif, s)


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
        print("\x1b[F\x1b[Kprocessing", pdbid)

        cif: CIFFile = CIFFile.read(file)

        noalt_file = name + "_noalt" + ext
        if not override and os.path.exists(noalt_file):
            cif.read(noalt_file)
        else:
            cif = remove_altlocs(cif)
            cif.write(noalt_file)

        aaonly_file = name + "_aaonly" + ext
        if not override and os.path.exists(aaonly_file):
            cif.read(aaonly_file)
        else:
            cif = filter_aa(cif)
            cif.write(aaonly_file)

        withh_file = name + "_withh" + ext
        if not override and os.path.exists(withh_file):
            cif.read(withh_file)
        else:
            cif = fix_h(cif)
            cif.write(withh_file)

        fchain_file = name + "_fchain" + ext
        if not override and os.path.exists(fchain_file):
            pass
        else:
            if pdbid in chains and chains[pdbid] != "":
                cif = filter_chain(cif, chains[pdbid])
            cif.write(fchain_file)


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
