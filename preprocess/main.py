#!/usr/bin/env python

"""Fetches PDB files and preprocesses them."""

import os
import sys
from argparse import ArgumentParser

import numpy as np
from biotite.database.rcsb import fetch
from biotite.structure import AtomArrayStack, filter_amino_acids
from biotite.structure.io import load_structure, save_structure
from hydride import add_hydrogen, relax_hydrogen  # type: ignore

infile: str
outdir: str
override: bool = False
skip: int = 0


def help():
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


def process_files(files: list[str]):
    """Processes the fetched files."""
    with open(infile) as f:
        chains: dict[str, str] = {
            line.strip()[0:4]: line.strip().split(" ")[0][4:]
            for line in f.readlines()[skip:]
        }
    for file in files:
        pdbid = os.path.splitext(os.path.basename(file))[0]
        name, ext = os.path.splitext(os.path.normpath(file))
        print("processing", pdbid)

        # loading the file will automatically select only the first altloc of each atom
        # bonds and charge are needed for hydride to function properly
        noalt_file = name + "_noalt" + ext
        if not override and os.path.exists(noalt_file):
            noalt = None
        else:
            noalt = load_structure(file, include_bonds=True, extra_fields=["charge"])
            if isinstance(noalt, AtomArrayStack):
                noalt = noalt.get_array(0)
                save_structure(noalt_file, noalt)
            else:
                save_structure(noalt_file, noalt)

        aaonly_file = name + "_aaonly" + ext
        if not override and os.path.exists(aaonly_file):
            aaonly = None
        else:
            noalt = noalt or load_structure(
                noalt_file, include_bonds=True, extra_fields=["charge"]
            )
            aaonly = noalt[filter_amino_acids(noalt)]
            save_structure(aaonly_file, aaonly)

        withh_file = name + "_withh" + ext
        if not override and os.path.exists(withh_file):
            withh = None
        else:
            aaonly = aaonly or load_structure(
                aaonly_file, include_bonds=True, extra_fields=["charge"]
            )
            if np.any(aaonly.element == "H"):
                withh = aaonly
            else:
                print("  adding H")
                withh, _ = add_hydrogen(aaonly)
                print("  added, relaxing H")
                withh.coord = relax_hydrogen(withh, iterations=10000)
                print("  relaxed H")
            save_structure(withh_file, withh)

        fchain_file = name + "_fchain" + ext
        if not override and os.path.exists(fchain_file):
            pass
        else:
            withh = withh or load_structure(withh_file)
            fchain = (
                withh[withh.chain_id == chains[pdbid]]
                if (pdbid in chains and chains[pdbid] != "")
                else withh
            )
            save_structure(fchain_file, fchain)


def main():
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
