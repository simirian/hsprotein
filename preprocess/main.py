#!/usr/bin/env python

"""Fetches PDB files and preprocesses them."""

import os
import sys
from argparse import ArgumentParser

import hydride
import numpy as np
from biotite.database.rcsb import fetch
from biotite.structure import AtomArrayStack, filter_amino_acids, stack
from biotite.structure.io import load_structure, save_structure

infile: str
outdir: str
override: bool = False


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
        pdbids = [id.strip() for id in f]

    return fetch(pdbids, "cif", outdir, verbose=True, overwrite=override)


def process_files(files: list[str]):
    """Processes the fetched files."""
    for file in files:
        file = os.path.normpath(file)
        name, ext = os.path.splitext(file)

        noalt_file = name + "_noalt" + ext
        aaonly_file = name + "_aaonly" + ext
        withh_file = name + "_withh" + ext

        if (
            os.path.exists(noalt_file)
            and os.path.exists(aaonly_file)
            and os.path.exists(withh_file)
            and not override
        ):
            continue

        # loading the file will automatically select only the first altloc of each atom
        # bonds and charge are needed for hydride to function properly
        noalt = load_structure(file, include_bonds=True, extra_fields=["charge"])
        save_structure(noalt_file, noalt)

        # strip everything but the protein
        aaonly = noalt[filter_amino_acids(noalt)]
        save_structure(aaonly_file, aaonly)

        # add hydrogen atoms
        if np.any(aaonly.atom_name == "H"):
            save_structure(withh_file, aaonly)
        else:
            if isinstance(aaonly, AtomArrayStack):
                withh = []
                for i in range(aaonly.stack_depth()):
                    array = aaonly[i]
                    withh.append(hydride.add_hydrogen(array))
                    withh[-1].coord = hydride.relax.relax_hydrogen(withh[-1])
                save_structure(withh_file, stack(withh))
            else:
                withh, _ = hydride.add_hydrogen(aaonly)
                withh.coord = hydride.relax.relax_hydrogen(withh)
                save_structure(withh_file, withh)


def main():
    global infile, outdir, override

    parser = ArgumentParser()
    parser.add_argument(
        "infile", nargs=1, help="A file with a list of PDB IDs to fetch."
    )
    parser.add_argument(
        "outdir", nargs=1, help="The output directory to place files in."
    )
    parser.add_argument("-o", "--override", action="store_true")
    args = parser.parse_args()

    infile = args.infile[0]
    outdir = args.outdir[0]
    override = args.override

    if not validate_io():
        sys.exit(2)

    fetched = fetch_files()
    process_files(fetched)


if __name__ == "__main__":
    main()
