package edu.wpi.hsprotein.rotations;

import org.biojava.nbio.structure.Atom;
import org.biojava.nbio.structure.Calc;

final class Constants {
  public static final DihedralAngle[] alanine = new DihedralAngle[] {
      new DihedralAngle("N", "CA", "CB", "HB1",
          new String[] { "HB1", "HB2", "HB3" },
          new String[] { "HB1", "HB2", "HB3" },
          new String[] { "N", "C", "HA", "O", "H" })
  };

  public static final DihedralAngle[] cysteine = new DihedralAngle[] {
      new DihedralAngle("N", "CA", "CB", "SG",
          new String[] { "SG", "HB2", "HB3", "HG" },
          new String[] { "SG", "HB2", "HB3" },
          new String[] { "N", "C", "HA", "O", "H" }),
      new DihedralAngle("CA", "CB", "SG", "HG",
          new String[] { "HG" },
          new String[] { "HG" },
          new String[] { "CA", "HB2", "HB3", "O", "H", "N", "C", "HA" })
  };

  public static final DihedralAngle[] asparticAcid = new DihedralAngle[] {
      new DihedralAngle("N", "CA", "CB", "CG",
          new String[] { "CG", "HB2", "HB3", "OD1", "OD2", "HD2" },
          new String[] { "CG", "HB2", "HB3" },
          new String[] { "N", "C", "HA", "O", "H" }),
      new DihedralAngle("CA", "CB", "CG", "OD1",
          new String[] { "OD1", "OD2", "HD2" },
          new String[] { "OD1", "OD2" },
          new String[] { "CA", "HB2", "HB3", "N", "C", "HA", "O", "H" }),
      new DihedralAngle("CB", "CG", "OD2", "HD2",
          new String[] { "HD2" },
          new String[] { "HD2" },
          new String[] { "OD1", "CB", "HB2", "HB3", "O", "H", "N", "C", "HA", "CA" })
  };

  public static final DihedralAngle[] glutamicAcid = new DihedralAngle[] {
      new DihedralAngle("N", "CA", "CB", "CG",
          new String[] { "CG", "HB2", "HB3", "CD", "HG2", "HG3", "OE1", "OE2", "HE2" },
          new String[] { "CG", "HB2", "HB3" },
          new String[] { "N", "C", "HA", "O", "H" }),
      new DihedralAngle("CA", "CB", "CG", "CD",
          new String[] { "CD", "HG2", "HG3", "OE1", "OE2", "HE2" },
          new String[] { "CD", "HG2", "HG3" },
          new String[] { "CA", "HB2", "HB3", "O", "H", "N", "C", "HA" }),
      new DihedralAngle("CB", "CG", "CD", "OE1",
          new String[] { "OE1", "OE2", "HE2" },
          new String[] { "OE1", "OE2" },
          new String[] { "CB", "HG2", "HG3", "HB2", "HB3", "O", "H", "N", "C", "HA", "CB", "CA" }),
      new DihedralAngle("CG", "CD", "OE2", "HE2",
          new String[] { "HE2" },
          new String[] { "HE2" },
          new String[] { "OE1", "CG", "HG2", "HG3", "HB2", "HB3", "O", "H", "N", "C", "HA", "CB", "CA" })
  };

  public static final DihedralAngle[] phenylalanine = new DihedralAngle[] {
      new DihedralAngle("N", "CA", "CB", "CG",
          new String[] { "CG", "HB2", "HB3", "HD1", "HD2", "HE1", "HE2", "HZ", "CD1", "CD2", "CZ", "CE1", "CE2" },
          new String[] { "CG", "HB2", "HB3" },
          new String[] { "N", "C", "HA", "O", "H" }),
      new DihedralAngle("CA", "CB", "CG", "CD1",
          new String[] { "HD1", "HD2", "HE1", "HE2", "HZ", "CD1", "CD2", "CZ", "CE1", "CE2" },
          new String[] { "HD1", "HD2", "HE1", "HE2", "HZ", "CD1", "CD2", "CZ", "CE1", "CE2" },
          new String[] { "CA", "HB2", "HB3", "O", "H", "N", "C", "HA" })
  };

  public static final DihedralAngle[] glycine = new DihedralAngle[] {};

  public static final DihedralAngle[] histidine = new DihedralAngle[] {
      new DihedralAngle("N", "CA", "CB", "CG",
          new String[] { "CG", "ND1", "HD1", "CE1", "HE1", "CD2", "HD2", "NE2", "HB2", "HB3" },
          new String[] { "HB2", "HB3", "CG" },
          new String[] { "N", "H", "C", "O", "HA" }),
      new DihedralAngle("CA", "CB", "CG", "ND1",
          new String[] { "HD2", "ND1", "HE1", "HE2", "NE2", "CD2", "CE1" },
          new String[] { "HD2", "ND1", "HE1", "HE2", "NE2", "CD2", "CE1" },
          new String[] { "CA", "HB2", "HB3", "O", "H", "N", "C", "HA" })
  };

  public static final DihedralAngle[] isoleucine = new DihedralAngle[] {
      new DihedralAngle("N", "CA", "CB", "CG1",
          new String[] { "CG1", "CG2", "HB", "HG12", "HG13", "CD1", "HD11", "HD12", "HD13", "HG21", "HG22", "HG23" },
          new String[] { "CG1", "CG2", "HB" },
          new String[] { "N", "C", "HA", "O", "H" }),
      new DihedralAngle("CA", "CB", "CG1", "CD1",
          new String[] { "CD1", "HG12", "HG13", "HD11", "HD12", "HD13" },
          new String[] { "CD1", "HG12", "HG13" },
          new String[] { "CG2", "HB", "CA", "O", "H", "N", "C", "HA" }),
      new DihedralAngle("CB", "CG1", "CD1", "HD11",
          new String[] { "HD11", "HD12", "HD13" },
          new String[] { "HD11", "HD12", "HD13" },
          new String[] { "CB", "HG12", "HG13", "CG2", "HB", "O", "H", "N", "C", "HA", "CA" }),
      new DihedralAngle("CA", "CB", "CG2", "HG21",
          new String[] { "HG21", "HG22", "HG23" },
          new String[] { "HG21", "HG22", "HG23" },
          new String[] { "CA", "CG1", "HB", "HD11", "HD12", "HD13", "HG12", "HG13", "O", "H", "N", "C", "HA", "CD1" })
  };

  public static final DihedralAngle[] lysine = new DihedralAngle[] {
      new DihedralAngle("N", "CA", "CB", "CG",
          new String[] { "CG", "HB2", "HB3", "CD", "HG2", "HG3", "CE", "HD2", "HD3", "NZ", "HE2", "HE3", "HZ1", "HZ2",
              "HZ3" },
          new String[] { "CG", "HB2", "HB3" },
          new String[] { "N", "C", "HA", "O", "H" }),
      new DihedralAngle("CA", "CB", "CG", "CD",
          new String[] { "CD", "HG2", "HG3", "CE", "HD2", "HD3", "NZ", "HE2", "HE3", "HZ1", "HZ2", "HZ3" },
          new String[] { "CD", "HG2", "HG3" },
          new String[] { "CA", "HB2", "HB3", "O", "H", "N", "C", "HA" }),
      new DihedralAngle("CB", "CG", "CD", "CE",
          new String[] { "CE", "HD2", "HD3", "NZ", "HE2", "HE3", "HZ1", "HZ2", "HZ3" },
          new String[] { "CE", "HD2", "HD3" },
          new String[] { "CB", "HG2", "HG3", "HB2", "HB3", "O", "H", "N", "C", "HA", "CA" }),
      new DihedralAngle("CG", "CD", "CE", "NZ",
          new String[] { "NZ", "HE2", "HE3", "HZ1", "HZ2", "HZ3" },
          new String[] { "NZ", "HE2", "HE3" },
          new String[] { "CG", "HD2", "HD3", "HG2", "HG3", "HB2", "HB3", "O", "H", "N", "C", "HA", "CB", "CA" }),
      new DihedralAngle("CD", "CE", "NZ", "HZ1",
          new String[] { "HZ1", "HZ2", "HZ3" },
          new String[] { "HZ1", "HZ2", "HZ3" },
          new String[] { "CD", "HE2", "HE3", "HD2", "HD3", "HG2", "HG3", "HB2", "HB3", "O", "H", "N", "C", "HA", "CG",
              "CB", "CA" }),
  };

  public static final DihedralAngle[] leucine = new DihedralAngle[] {
      new DihedralAngle("N", "CA", "CB", "CG",
          new String[] { "CG", "HB2", "HB3", "CD1", "CD2", "HG", "HD11", "HD12", "HD13", "HD21", "HD22", "HD23" },
          new String[] { "CG", "HB2", "HB3" },
          new String[] { "N", "C", "HA", "O", "H" }),
      new DihedralAngle("CA", "CB", "CG", "CD1",
          new String[] { "CD1", "CD2", "HG", "HD11", "HD12", "HD13", "HD21", "HD22", "HD23" },
          new String[] { "CD1", "CD2", "HG" },
          new String[] { "CA", "HB2", "HB3", "O", "H", "N", "C", "HA" }),
      new DihedralAngle("CB", "CG", "CD1", "HD11",
          new String[] { "HD11", "HD12", "HD13" },
          new String[] { "HD11", "HD12", "HD13" },
          new String[] { "CD2", "CB", "HG", "HB2", "HB3", "O", "H", "N", "C", "HA", "CA" }),
      new DihedralAngle("CB", "CG", "CD2", "HD21",
          new String[] { "HD21", "HD22", "HD23" },
          new String[] { "HD21", "HD22", "HD23" },
          new String[] { "CD1", "CB", "HG", "HD11", "HD12", "HD13", "HB2", "HB3", "O", "H", "N", "C", "HA", "CA" })
  };

  public static final DihedralAngle[] methionine = new DihedralAngle[] {
      new DihedralAngle("N", "CA", "CB", "CG",
          new String[] { "CG", "HB2", "HB3", "SD", "HG2", "HG3", "CE", "HE1", "HE2", "HE3" },
          new String[] { "CG", "HB2", "HB3" },
          new String[] { "N", "C", "HA", "O", "H" }),
      new DihedralAngle("CA", "CB", "CG", "SD",
          new String[] { "SD", "HG2", "HG3", "CE", "HE1", "HE2", "HE3" },
          new String[] { "SD", "HG2", "HG3" },
          new String[] { "CA", "HB2", "HB3", "O", "H", "N", "C", "HA" }),
      new DihedralAngle("CB", "CG", "SD", "CE",
          new String[] { "CE", "HE1", "HE2", "HE3" },
          new String[] { "CE" },
          new String[] { "CB", "HG2", "HG3", "HB2", "HB3", "O", "H", "N", "C", "HA", "CA" }),
      new DihedralAngle("CG", "SD", "CE", "HE1",
          new String[] { "HE1", "HE2", "HE3" },
          new String[] { "HE1", "HE2", "HE3" },
          new String[] { "CG", "HG2", "HG3", "HB2", "HB3", "O", "H", "N", "C", "HA", "CB", "CA" })
  };

  public static final DihedralAngle[] asparagine = new DihedralAngle[] {
      new DihedralAngle("N", "CA", "CB", "CG",
          new String[] { "CG", "HB2", "HB3", "OD1", "ND2", "HD21", "HD22" },
          new String[] { "CG", "HB2", "HB3" },
          new String[] { "N", "C", "HA", "O", "H" }),
      new DihedralAngle("CA", "CB", "CG", "OD1",
          new String[] { "OD1", "ND2", "HD21", "HD22" },
          new String[] { "OD1", "ND2", "HD21", "HD22" },
          new String[] { "CA", "HB2", "HB3", "O", "H", "N", "C", "HA" })
  };

  public static final DihedralAngle[] proline = new DihedralAngle[] {};

  public static final DihedralAngle[] glutamine = new DihedralAngle[] {
      new DihedralAngle("N", "CA", "CB", "CG",
          new String[] { "CG", "HB2", "HB3", "CD", "HG2", "HG3", "OE1", "NE2", "HE21", "HE22" },
          new String[] { "CG", "HB2", "HB3" },
          new String[] { "N", "C", "HA", "O", "H" }),
      new DihedralAngle("CA", "CB", "CG", "CD",
          new String[] { "CD", "HG2", "HG3", "OE1", "NE2", "HE21", "HE22" },
          new String[] { "CD", "HG2", "HG3" },
          new String[] { "CA", "HB2", "HB3", "O", "H", "N", "C", "HA" }),
      new DihedralAngle("CB", "CG", "CD", "OE1",
          new String[] { "OE1", "NE2", "HE21", "HE22" },
          new String[] { "OE1", "NE2", "HE21", "HE22" },
          new String[] { "CB", "HG2", "HG3", "HB2", "HB3", "O", "H", "N", "C", "HA", "CA" })
  };

  public static final DihedralAngle[] arginine = new DihedralAngle[] {
      new DihedralAngle("N", "CA", "CB", "CG",
          new String[] { "CG", "HB2", "HB3", "CD", "HG2", "HG3", "NE", "HD2", "HD3", "CZ", "HE", "NH1", "NH2", "HH11",
              "HH12", "HH21", "HH22" },
          new String[] { "CG", "HB2", "HB3" },
          new String[] { "N", "C", "HA", "O", "H" }),
      new DihedralAngle("CA", "CB", "CG", "CD",
          new String[] { "CD", "HG2", "HG3", "NE", "HD2", "HD3", "CZ", "HE", "NH1", "NH2", "HH11", "HH12", "HH21",
              "HH22" },
          new String[] { "CD", "HG2", "HG3" },
          new String[] { "CA", "HB2", "HB3", "O", "H", "N", "C", "HA" }),
      new DihedralAngle("CB", "CG", "CD", "NE",
          new String[] { "NE", "HD2", "HD3", "CZ", "HE", "NH1", "NH2", "HH11", "HH12", "HH21", "HH22" },
          new String[] { "NE", "HD2", "HD3" },
          new String[] { "CB", "HG2", "HG3", "HB2", "HB3", "O", "H", "N", "C", "HA", "CA" }),
      new DihedralAngle("CG", "CD", "NE", "CZ",
          new String[] { "CZ", "HE", "NH1", "NH2", "HH11", "HH12", "HH21", "HH22" },
          new String[] { "CZ", "HE", "NH1", "NH2", "HH11", "HH12", "HH21", "HH22" },
          new String[] { "CG", "HD2", "HD3", "HG2", "HG3", "HB2", "HB3", "O", "H", "N", "C", "HA", "CB", "CA" })
  };

  public static final DihedralAngle[] serine = new DihedralAngle[] {
      new DihedralAngle("N", "CA", "CB", "OG",
          new String[] { "OG", "HB2", "HB3", "HG" },
          new String[] { "OG", "HB2", "HB3" },
          new String[] { "N", "C", "HA", "O", "H" }),
      new DihedralAngle("CA", "CB", "OG", "HG",
          new String[] { "HG" },
          new String[] { "HG" },
          new String[] { "CA", "HB2", "HB3", "O", "H", "N", "C", "HA" })
  };

  public static final DihedralAngle[] threonine = new DihedralAngle[] {
      new DihedralAngle("N", "CA", "CB", "OG1",
          new String[] { "OG1", "CG2", "HB", "HG1", "HG21", "HG22", "HG23" },
          new String[] { "OG1", "CG2", "HB" },
          new String[] { "N", "C", "HA", "O", "H" }),
      new DihedralAngle("CA", "CB", "OG1", "HG1",
          new String[] { "HG1" },
          new String[] { "HG1" },
          new String[] { "CA", "CG2", "HB", "O", "H", "N", "C", "HA" }),
      new DihedralAngle("CA", "CB", "CG2", "HG21",
          new String[] { "HG21", "HG22", "HG23" },
          new String[] { "HG21", "HG22", "HG23" },
          new String[] { "OG1", "CA", "HB", "HG1", "O", "H", "N", "C", "HA" })
  };

  public static final DihedralAngle[] valine = new DihedralAngle[] {
      new DihedralAngle("N", "CA", "CB", "CG1",
          new String[] { "CG1", "CG2", "HB", "HG11", "HG12", "HG13", "HG21", "HG22", "HG23" },
          new String[] { "CG1", "CG2", "HB" },
          new String[] { "N", "C", "HA", "O", "H" }),
      new DihedralAngle("CA", "CB", "CG1", "HG11",
          new String[] { "HG11", "HG12", "HG13" },
          new String[] { "HG11", "HG12", "HG13" },
          new String[] { "CA", "CG2", "HB", "O", "H", "N", "C", "HA" }),
      new DihedralAngle("CA", "CB", "CG2", "HG21",
          new String[] { "HG21", "HG22", "HG23" },
          new String[] { "HG21", "HG22", "HG23" },
          new String[] { "CA", "CG1", "HB", "HG11", "HG12", "HG13", "O", "H", "N", "C", "HA" })
  };

  public static final DihedralAngle[] tryptophan = new DihedralAngle[] {
      new DihedralAngle("N", "CA", "CB", "CG",
          new String[] { "CG", "HB2", "HB3", "CD1", "CD2", "HD1", "HE1", "HE3", "HZ2", "HZ3", "HH2", "NE1", "CE2",
              "CE3", "CZ2", "CZ3", "CH2" },
          new String[] { "HB2", "HB3", "CG" },
          new String[] { "N", "C", "HA", "O", "H" }),
      new DihedralAngle("CA", "CB", "CG", "CD1",
          new String[] { "CD1", "CD2", "HD1", "HE1", "HE3", "HZ2", "HZ3", "HH2", "NE1", "CE2", "CE3", "CZ2", "CZ3",
              "CH2" },
          new String[] { "CD1", "CD2", "HD1", "HE1", "HE3", "HZ2", "HZ3", "HH2", "NE1", "CE2", "CE3", "CZ2", "CZ3",
              "CH2" },
          new String[] { "CA", "HB2", "HB3", "O", "H", "N", "C", "HA" })
  };

  public static final DihedralAngle[] tyrosine = new DihedralAngle[] {
      new DihedralAngle("N", "CA", "CB", "CG",
          new String[] { "CG", "HB2", "HB3", "CD1", "CD2", "HD1", "HD2", "HE1", "HE2", "OH", "CE1", "CE2", "CZ", "HH" },
          new String[] { "CG", "HB2", "HB3" },
          new String[] { "N", "C", "HA", "O", "H" }),
      new DihedralAngle("CA", "CB", "CG", "CD1",
          new String[] { "CD1", "CD2", "HD1", "HD2", "HE1", "HE2", "OH", "CE1", "CE2", "CZ", "HH" },
          new String[] { "CD1", "CD2", "HD1", "HD2", "HE1", "HE2", "OH", "CE1", "CE2", "CZ" },
          new String[] { "CA", "HB2", "HB3", "O", "H", "N", "C", "HA" }),
      new DihedralAngle("CE1", "CZ", "OH", "HH",
          new String[] { "HH" },
          new String[] { "HH" },
          new String[] { "CE1", "CE2", "HE1", "HE2", "HD1", "HD2", "HB2", "HB3", "O", "H", "N", "C", "HA", "CD1", "CD2",
              "CG", "CB", "CA" })
  };
}

/**
 * Defines all dihedral angles of the 20 amino acids.
 * Each record also contains the atoms that will be rotated around each of its dihedral angles,
 * and the atoms with which these rotated atoms might clash (physically overlap) with.
 * <p>
 * All atoms are identified through their PDB Atom Names.
 * </p>
 *
 * @param a1 the first rotatable atom on the dihedral bond
 * @param a2 the first atom which defines the dihedral axis
 * @param a3 the second atom which defines the dihedral axis
 * @param a4 the second rotatable atom on the dihedral bond
 * @param rotations the atoms that will be rotated around the dihedral axis
 * @param mightClash the atoms that have just been rotated and could now be clashing with other atoms
 * @param clashesWith the atoms that make up the backbone and anything that has already been rotated that the recently rotated atoms could now be clashing with
 */
public record DihedralAngle(
    String a1, String a2, String a3, String a4,
    String[] rotations,
    String[] mightClash,
    String[] clashesWith) {

  /**
   * Get all dihedral angle information about a given amino acid based on its 3-letter code.
   *
   * @param residue the 3-letter code of the amino acid
   * @return a record of all dihedral angle information for the amino acid
   */
  public static DihedralAngle[] getDihedrals(String residue) {
    switch (residue.toLowerCase()) {
      case "ala":
        return Constants.alanine;
      case "cys":
        return Constants.cysteine;
      case "asp":
        return Constants.asparticAcid;
      case "glu":
        return Constants.glutamicAcid;
      case "phe":
        return Constants.phenylalanine;
      case "gly":
        return Constants.glycine;
      case "his":
        return Constants.histidine;
      case "ile":
        return Constants.isoleucine;
      case "lys":
        return Constants.lysine;
      case "leu":
        return Constants.leucine;
      case "met":
        return Constants.methionine;
      case "asn":
        return Constants.asparagine;
      case "pro":
        return Constants.proline;
      case "gln":
        return Constants.glutamine;
      case "arg":
        return Constants.arginine;
      case "ser":
        return Constants.serine;
      case "thr":
        return Constants.threonine;
      case "val":
        return Constants.valine;
      case "trp":
        return Constants.tryptophan;
      case "tyr":
        return Constants.tyrosine;
      default:
        return null;
    }
  }

  /**
   * Calculates the dihedral angle between four atoms,
	 * with 0 degrees being the state where all four atoms are perfectly aligned.
	 *
   * @param a The first point which is free to rotate.
   * @param b The first point on the dihedral axis.
   * @param c The second point on the dihedral axis.
   * @param d The second point which is free to rotate.
   * @return The angle between `a` and `d` across the axis `b-c`.
   */
  public static double calcAngle(Atom a, Atom b, Atom c, Atom d) {
    // Calculate angle with dot product of the plane normals
    Atom abc = Calc.vectorProduct(Calc.subtract(a, b), Calc.subtract(c, b));
    abc = Calc.scale(abc, 1 / Calc.amount(abc));
    Atom bcd = Calc.vectorProduct(Calc.subtract(b, c), Calc.subtract(d, c));
    bcd = Calc.scale(bcd, 1 / Calc.amount(bcd));
    double angle = Calc.angle(abc, bcd);
    // Check if d is "under" or "over" the abc place, if under then we use -angle
    Atom plane = Calc.subtract(d, a);
    plane.setX(plane.getX() * abc.getX());
    plane.setY(plane.getY() * abc.getY());
    plane.setZ(plane.getZ() * abc.getZ());
    if (plane.getX() + plane.getY() + plane.getZ() < 0)
      return angle;
    else
      return 360 - angle;
  }
}
