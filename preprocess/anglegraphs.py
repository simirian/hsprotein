#!/usr/bin/env python

import csv
import os
from argparse import ArgumentParser

import matplotlib.pyplot as plt
import numpy as np
from biotite.structure import Atom, AtomArray, AtomArrayStack, dihedral, get_residues
from biotite.structure.io import load_structure
from matplotlib.axes import Axes
from numpy.typing import NDArray

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


def process(args):
    for file in get_structure_file_list(args.infile[0]):
        basename = os.path.splitext(os.path.basename(file))[0]
        add_structure_dihedrals(load_structure(file), basename)

    with open(os.path.join(args.outdir[0], "missing.csv"), "w") as f:
        writer = csv.writer(f, lineterminator="\n")
        writer.writerows(missing)

    for res, res_angles in angles.items():
        if len(res_angles) == 0:
            continue
        longest = max([len(x) for x in res_angles])
        rows = []
        for i in range(longest):
            rows.append([x[i] if i < len(x) else None for x in res_angles])
        with open(os.path.join(args.outdir[0], res + ".csv"), "w") as f:
            writer = csv.writer(f, lineterminator="\n")
            writer.writerows(rows)


def read_res_data(indir: str, residue: str) -> NDArray:
    if not os.path.isfile(os.path.join(indir, residue + ".csv")):
        return np.array([])
    with open(os.path.join(indir, residue + ".csv"), "r") as f:
        reader = csv.reader(f)
        data = np.array([row for row in reader])
    data = data.astype(np.float64)
    return data.transpose()


def plot_heatmap(x: NDArray, y: NDArray) -> Axes:
    plt.figure()
    ax = plt.subplot()
    z = np.flip(np.histogram2d(y, x, 72, [(0, 360), (0, 360)])[0], 0)
    ax.imshow(z, "inferno_r", alpha=np.clip(z, 0, 1))
    ax.set_xticks([x * 6 for x in range(12)], [x * 30 for x in range(12)])
    ax.set_yticks([x * 6 + 5 for x in range(12)], [330 - x * 30 for x in range(12)])
    ax.set_xlabel("Χ1")
    ax.set_ylabel("Χ2")
    return ax


def plot_violin(data: NDArray) -> Axes:
    plt.figure()
    ax = plt.subplot()
    ax.violinplot([data[x] for x in range(data.shape[0])])
    ax.set_xticks(
        [x + 1 for x in range(data.shape[0])],
        ["Χ" + str(i + 1) for i in range(data.shape[0])],
    )
    return ax


def plot(args):
    for res, res_angles in angles.items():
        if len(res_angles) == 0:
            continue
        data = np.array(res_angles) if len(res_angles[0]) > 0 else read_res_data(args.indir[0], res)
        data = (np.rad2deg(data) + 360) % 360
        if data.shape[0] > 1:
            plot_heatmap(data[0], data[1])
            plt.savefig(os.path.join(args.outdir[0], res + "01.png"))
            plt.close()
        plot_violin(data)
        plt.savefig(os.path.join(args.outdir[0], res + ".png"))
        plt.close()


def main():
    parser = ArgumentParser()
    subparsers = parser.add_subparsers(required=True)

    process_parser = subparsers.add_parser("process", help="Get dihedral angles from a file.")
    process_parser.set_defaults(func=process)
    process_parser.add_argument(
        "infile",
        nargs=1,
        type=str,
        help="A file with a list of structure file names, or a directory containing structures.",
    )
    process_parser.add_argument(
        "outdir",
        nargs=1,
        type=str,
        help="A directory in which to place output information.",
        default=".",
    )

    plot_parser = subparsers.add_parser("plot", help="Generate plots from dihedral data.")
    plot_parser.set_defaults(func=plot)
    plot_parser.add_argument(
        "indir",
        nargs=1,
        type=str,
        help="A directory which contains dihedral angle data as output by the `process` subcommand.",
    )
    plot_parser.add_argument(
        "outdir",
        nargs=1,
        type=str,
        help="A directory in which to place output information.",
    )

    args = parser.parse_args()
    args.func(args)

if __name__ == "__main__":
    main()
