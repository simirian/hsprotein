#!/usr/bin/env python

import csv
import os
from argparse import ArgumentParser

import matplotlib.pyplot as plt
import numpy as np
from biotite.structure import Atom, AtomArray, AtomArrayStack, dihedral, get_residues
from biotite.structure.io import load_structure

missing: list[tuple[str, str, int, str]] = []

dihedrals: dict[str, list[tuple[str, str, str, str]]] = {
    "ala": [("N", "CA", "CB", "HB1")],
    "cys": [("N", "CA", "CB", "SG"), ("CA", "CB", "SG", "HG")],
    "asp": [("N", "CA", "CB", "CG"), ("CA", "CB", "CG", "OD1")],
    "glu": [
        ("N", "CA", "CB", "CG"),
        ("CA", "CB", "CG", "CD"),
        ("CB", "CG", "CD", "OE1"),
    ],
    "phe": [("N", "CA", "CB", "CG"), ("CA", "CB", "CG", "CD1")],
    "gly": [],
    "his": [("N", "CA", "CB", "CG"), ("CA", "CB", "CG", "ND1")],
    "ile": [
        ("N", "CA", "CB", "CG1"),
        ("CA", "CB", "CG1", "CD1"),
        ("CB", "CG1", "CD1", "HD11"),
        ("CA", "CB", "CG2", "HG21"),
    ],
    "lys": [
        ("N", "CA", "CB", "CG"),
        ("CA", "CB", "CG", "CD"),
        ("CB", "CG", "CD", "CE"),
        ("CG", "CD", "CE", "NZ"),
        ("CD", "CE", "NZ", "HZ1"),
    ],
    "leu": [
        ("N", "CA", "CB", "CG"),
        ("CA", "CB", "CG", "CD1"),
        ("CB", "CG", "CD1", "HD11"),
        ("CB", "CG", "CD2", "HD21"),
    ],
    "met": [
        ("N", "CA", "CB", "CG"),
        ("CA", "CB", "CG", "SD"),
        ("CB", "CG", "SD", "CE"),
        ("CG", "SD", "CE", "HE1"),
    ],
    "asn": [("N", "CA", "CB", "CG"), ("CA", "CB", "CG", "OD1")],
    "pro": [],
    "gln": [
        ("N", "CA", "CB", "CG"),
        ("CA", "CB", "CG", "CD"),
        ("CB", "CG", "CD", "OE1"),
    ],
    "arg": [
        ("N", "CA", "CB", "CG"),
        ("CA", "CB", "CG", "CD"),
        ("CB", "CG", "CD", "NE"),
        ("CG", "CD", "NE", "CZ"),
    ],
    "ser": [
        ("N", "CA", "CB", "OG"),
        ("CA", "CB", "OG", "HG"),
    ],
    "thr": [
        ("N", "CA", "CB", "OG1"),
        ("CA", "CB", "OG1", "HG1"),
        ("CA", "CB", "CG2", "HG21"),
    ],
    "val": [
        ("N", "CA", "CB", "CG1"),
        ("CA", "CB", "CG1", "HG11"),
        ("CA", "CB", "CG2", "HG21"),
    ],
    "trp": [("N", "CA", "CB", "CG"), ("CA", "CB", "CG", "CD1")],
    "tyr": [
        ("N", "CA", "CB", "CG"),
        ("CA", "CB", "CG", "CD1"),
        ("CE1", "CZ", "OH", "HH"),
    ],
}

angles: dict[str, list[list[float]]] = {
    "ala": [[]],
    "cys": [[], []],
    "asp": [[], []],
    "glu": [[], [], []],
    "phe": [[], []],
    "gly": [],
    "his": [[], []],
    "ile": [[], [], [], []],
    "lys": [[], [], [], [], []],
    "leu": [[], [], [], []],
    "met": [[], [], [], []],
    "asn": [[], []],
    "pro": [],
    "gln": [[], [], []],
    "arg": [[], [], [], []],
    "ser": [[], []],
    "thr": [[], [], []],
    "val": [[], [], []],
    "trp": [[], []],
    "tyr": [[], [], []],
}


def get_structure_file_list(list: str) -> list[str]:
    if os.path.isdir(list):
        files = []
        for dir, _, fnames in os.walk(list):
            files += [
                os.path.join(dir, fname) for fname in fnames if fname.endswith(".cif")
            ]
        return files
    else:
        with open(list) as f:
            return [line for line in f]


def get_dihedral_atoms(
    structure: AtomArray | AtomArrayStack,
    res_id: int,
    name: str,
    dihedral: tuple[str, str, str, str],
) -> list[Atom] | None:
    if isinstance(structure, AtomArrayStack):
        residue = structure[0, structure.res_id == res_id]
    else:
        residue = structure[structure.res_id == res_id]
    res_name = residue.res_name[0]
    residue = residue[residue.res_name == res_name]
    atoms = []
    for atom_name in dihedral:
        atom = residue[residue.atom_name == atom_name]
        if not atom:
            missing.append((name, res_name, res_id, atom_name))
        else:
            atoms.append(atom)
    return None if len(atoms) != 4 else atoms


def add_structure_dihedrals(structure: AtomArray | AtomArrayStack, name: str):
    ids, names = get_residues(structure)
    for res_id, res_name in zip(ids, names):
        res_name = res_name.lower()
        if res_name not in dihedrals:
            missing.append((name, res_name, res_id, ""))
            continue
        res_dihedrals = dihedrals[res_name]
        res_angles = angles[res_name]
        for i in range(len(res_dihedrals)):
            atoms = get_dihedral_atoms(structure, res_id, name, res_dihedrals[i])
            angle = dihedral(*atoms)[0] if atoms else np.nan
            res_angles[i].append(angle)


def main():
    parser = ArgumentParser()
    parser.add_argument(
        "infile",
        nargs=1,
        type=str,
        help="A file with a list of structure file names, or a directory containing structures.",
    )
    parser.add_argument(
        "outdir",
        nargs=1,
        type=str,
        help="A directory in which to place output information.",
        default=".",
    )
    args = parser.parse_args()

    for file in get_structure_file_list(args.infile[0]):
        basename = os.path.splitext(os.path.basename(file))[0]
        add_structure_dihedrals(load_structure(file), basename)

    with open(os.path.join(args.outdir[0], "missing.csv"), "w") as f:
        writer = csv.writer(f)
        writer.writerows(missing)

    for res, res_angles in angles.items():
        if len(res_angles) == 0:
            continue
        longest = max([len(x) for x in res_angles])
        rows = []
        for i in range(longest):
            rows.append([x[i] if i < len(x) else None for x in res_angles])
        with open(os.path.join(args.outdir[0], res + ".csv"), "w") as f:
            writer = csv.writer(f)
            writer.writerows(rows)

    x = (np.rad2deg(angles["phe"][0]) + 360) % 360
    y = (np.rad2deg(angles["phe"][1]) + 360) % 360

    ax = plt.subplot()
    z = np.histogram2d(y, x, 72, [(0, 360), (0, 360)])[0]
    ax.imshow(z)
    ax.set_xticks([x * 6 for x in range(12)], [x * 30 for x in range(12)])
    ax.set_yticks([x * 6 for x in range(12)], [x * 30 for x in range(12)])
    plt.show()


if __name__ == "__main__":
    main()
