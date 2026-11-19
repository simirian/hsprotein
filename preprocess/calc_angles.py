import csv
import os
from argparse import ArgumentParser

import numpy as np
from biotite.structure import Atom, AtomArray, AtomArrayStack, dihedral, get_residues
from biotite.structure.io import load_structure

# map from residue names to a list of dihedral angles for that residue
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
    missing: list[tuple[str, str, int, str]]
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


def add_structure_dihedrals(structure: AtomArray | AtomArrayStack, name: str, outdir: str):
    ids, names = get_residues(structure)
    # written as structure, residue type, residue number, missing atom name
    missing: list[tuple[str, str, int, str]] = []
    # map from residue names to the angles for that residue, which are resnum, angles
    angles: dict[str, list[tuple[int, list[int]]]] = {}


    for res_id, res_name in zip(ids, names):
        res_name = res_name.lower()
        if res_name not in dihedrals:
            missing.append((name, res_name, res_id, ""))
            continue

        res_dihedrals = dihedrals[res_name]

        if res_name not in angles:
            angles[res_name] = []

        res_angles: list[int] = []
        for i in range(len(res_dihedrals)):
            atoms = get_dihedral_atoms(structure, res_id, name, res_dihedrals[i], missing)
            if atoms is not None:
                angle = dihedral(*atoms)[0] if atoms else np.nan
                # flip the angle because the dihedral function returns backwards angles
                res_angles.append(int(-np.rad2deg(angle) + 360) % 360)
        angles[res_name].append((res_id, res_angles))

    with open(os.path.join(outdir, "missing.csv"), "a") as f:
        writer = csv.writer(f, lineterminator="\n")
        writer.writerows(missing)
    for res in angles: # noqa
        with open(os.path.join(outdir, f"{res}.csv"), "a") as f:
            writer = csv.writer(f, lineterminator="\n")
            for angle in angles[res]:
                writer.writerow([name, angle[0], 0, 0.0, *angle[1]])


def main(infile: str, outdir: str):
    for file in get_structure_file_list(infile):
        basename = os.path.splitext(os.path.basename(file))[0]
        add_structure_dihedrals(load_structure(file), basename, outdir)

if __name__ == "__main__":
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

    main(args.infile[0], args.outdir[0])
