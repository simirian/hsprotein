import csv
import os
from argparse import ArgumentParser

import matplotlib.pyplot as plt
import numpy as np
from matplotlib.axes import Axes

resnames = [
    "ala",
    "cys",
    "asp",
    "glu",
    "phe",
    "his",
    "ile",
    "lys",
    "leu",
    "met",
    "asn",
    "gln",
    "arg",
    "ser",
    "thr",
    "val",
    "trp",
    "tyr",
]


def add_to_heatmap(heatmap: np.ndarray | None, batch: list[list[str]]) -> np.ndarray | None:
    """
    Adds batch entries to a heatmap.

    heatmap: Numpy array of shape (72, 72) which will be added to.
    batch: List of entries, each entry should have this form with the number of
        angles appropriate to the residue (at least 2):
        `STRUCTURE, RESNUM, TIME_NS, ENERGY, ANGLE1, ANGLE2, ...`
    """
    if len(batch[0]) < 6:
        return None
    if heatmap is None:
        heatmap = np.zeros((72, 72))
    count = len(batch)
    for entry in batch:
        a1 = int(float(entry[4]) / 5)
        a2 = int(float(entry[5]) / 5)
        heatmap[a2][a1] += 1 / count
    return heatmap


def add_to_angles(angles: np.ndarray | None, batch: list[list[str]]) -> np.ndarray | None:
    """
    Adds batch entries to the angle counts.

    angles: Numpy array of shape (72, x) where x is the length of batch[0].
    batch: List of entries, each entry should have this form with the number of
        angles appropriate to the residue:
        `STRUCTURE, RESNUM, TIME_NS, ENERGY, ANGLE1, ...`
    """
    if angles is None:
        angles = np.zeros((72, len(batch[0]) - 4))
    count = len(batch)
    for entry in batch:
        for angle in range(4, len(entry)):
            a = int(float(entry[angle]) / 5)
            angles[a][angle - 4] += 1 / count
    return angles


def read_res_data(
    indir: str, residue: str
) -> tuple[np.ndarray | None, np.ndarray | None]:
    heatmap = None
    angles = None
    with open(os.path.join(indir, residue + ".csv"), "r") as f:
        reader = csv.reader(f, lineterminator="\n")
        batch: list[list[str]] | None = None
        structure: str
        resnum: str
        for row in reader:
            if batch is None:
                batch = [row]
                structure = row[0]
                resnum = row[1]
            elif structure == row[0] and resnum == row[1]:
                batch.append(row)
            else:
                heatmap = add_to_heatmap(heatmap, batch)
                angles = add_to_angles(angles, batch)
                batch = [row]
                structure = row[0]
                resnum = row[0]
        if batch is not None:
            heatmap = add_to_heatmap(heatmap, batch)
            angles = add_to_angles(angles, batch)
    return heatmap, angles


def plot_heatmap(heatmap: np.ndarray, name: str) -> Axes:
    plt.figure()
    ax: Axes = plt.subplot()
    heatmap = np.flip(heatmap, 1)
    ax.imshow(heatmap, "inferno_r", alpha=np.clip(heatmap, 0, 1))
    ax.set_xticks([x * 6 for x in range(12)], [str(x * 30) for x in range(12)])
    ax.set_yticks([x * 6 + 5 for x in range(12)], [str(330 - x * 30) for x in range(12)])
    ax.set_xlabel("Χ1")
    ax.set_ylabel("Χ2")
    ax.set_title(name)
    return ax


def plot_angles(angles: np.ndarray, name: str) -> Axes:
    plt.figure()
    ax: Axes = plt.subplot()
    ax.plot(angles / angles.max(0))
    ax.legend([f"X{x + 1}" for x in range(angles.shape[1])])
    ax.set_xticks([x * 6 for x in range(12)], [f"{x * 30}°" for x in range(12)])
    ax.set_title(name)
    return ax


def main(indir: str, outdir: str) -> None:
    for res in resnames:
        heatmap, angles = read_res_data(indir, res)
        if heatmap is not None:
            plot_heatmap(heatmap, res.upper())
            plt.savefig(os.path.join(outdir, res + "-heatmap.png"))
            plt.close()
        if angles is not None:
            plot_angles(angles, res.upper())
            plt.savefig(os.path.join(outdir, res + ".png"))
            plt.close()


def parse_args() -> tuple[str, str]:
    parser = ArgumentParser()
    parser.add_argument(
        "indir",
        nargs=1,
        type=str,
        help="A directory which contains dihedral angle data as output by the `process` subcommand.",
    )
    parser.add_argument(
        "outdir",
        nargs=1,
        type=str,
        help="A directory in which to place output information.",
    )
    args = parser.parse_args()
    return args.indir[0], args.outdir[0]


if __name__ == "__main__":
    main(*parse_args())
