package org.wpi.hsprotein.rotations;

final class Constants {
  public static final DihedralAngle[] alanine = new DihedralAngle[] {
      new DihedralAngle("CA", "CB",
          new String[] { "HB1", "HB2", "HB3" },
          new String[] { "HB1", "HB2", "HB3" },
          new String[] { "N", "H", "C", "O", "HA" }),
  };

  public static final DihedralAngle[] cysteine = new DihedralAngle[] {
      new DihedralAngle("CA", "CB",
          new String[] { "HB2", "HB3", "SG" },
          new String[] { "HB2", "HB3", "SG" },
          new String[] { "N", "H", "C", "O", "HA" }),
  };

  public static final DihedralAngle[] asparticAcid = new DihedralAngle[] {
      new DihedralAngle("CA", "CB",
          new String[] { "HB2", "HB3", "CG",  "OD1", "OD2" },
          new String[] { "HB2", "HB3", "CG" },
          new String[] { "N", "H", "C", "O", "HA" }),
      new DihedralAngle("CB", "CG",
          new String[] { "OD1", "OD2" },
          new String[] { "OD1", "OD2" },
          new String[] { "N", "H", "C", "O", "HA", "HB2", "HB3", "CA" }),
  };

  public static final DihedralAngle[] glutamicAcid = new DihedralAngle[] {
      new DihedralAngle("CA", "CB",
          new String[] { "HB2", "HB3", "CG", "CD", "OE1", "OE2", "HG2", "HG3", },
          new String[] { "HB2", "HB3", "CG" },
          new String[] { "N", "H", "C", "O", "HA" }),
      new DihedralAngle("CB", "CG",
          new String[] { "HG2", "HG3", "CD", "OE1", "OE2", },
          new String[] { "HG2", "HG3", "CD" },
          new String[] { "N", "H", "C", "O", "HA", "HB2", "HB3", "CA" }),
      new DihedralAngle("CG", "CD",
          new String[] { "OE1", "OE2" },
          new String[] { "OE1", "OE2" },
          new String[] { "N", "H", "C", "O", "HA", "HB2", "HB3", "HG2", "HG3", "CB", "CA" }),
  };

  public static final DihedralAngle[] phenylalanine = new DihedralAngle[] {
      new DihedralAngle("CA", "CB",
          new String[] { "CG", "CD1", "HD1", "CE1", "HE1", "CZ", "HZ", "CD2", "HD2", "CE2", "HE2", "HB2", "HB3" },
          new String[] { "HB2", "HB3", "CG" },
          new String[] { "N", "H", "C", "O", "HA" }),
      new DihedralAngle("CB", "CG",
          new String[] { "HD1", "HE1", "HZ", "HD2", "HE2", "HZ", "CD1", "CE1", "CZ", "CD2", "CE2", "CZ" },
          new String[] { "HD1", "HE1", "HZ", "HD2", "HE2", "HZ", "CD1", "CE1", "CZ", "CD2", "CE2", "CZ" },
          new String[] { "N", "H", "C", "O", "HA", "HB2", "HB3", "CA" }),
  };

  public static final DihedralAngle[] glycine = new DihedralAngle[] {};

  public static final DihedralAngle[] histidine = new DihedralAngle[] {
      new DihedralAngle("CA", "CB",
          new String[] { "CG", "ND1", "HD1", "CE1", "HE1", "CD2", "HD2", "NE2", "HB2", "HB3" },
          new String[] { "HB2", "HB3", "CG" },
          new String[] { "N", "H", "C", "O", "HA" }),
      new DihedralAngle("CB", "CG",
          new String[] { "ND1", "HD1", "HE1", "HD2", "NE2", "CE1", "CD2" },
          new String[] { "ND1", "HD1", "HE1", "HD2", "NE2", "CE1", "CD2" },
          new String[] { "N", "H", "C", "O", "HA", "HB2", "HB3", "CA" }),
  };

  public static final DihedralAngle[] isoleucine = new DihedralAngle[] {
      new DihedralAngle("CA", "CB",
          new String[] { "HB", "CG1", "CD1", "HD11", "HD12", "HD13", "HG12", "HG13", "CG2", "HG21", "HG22", "HG23" },
          new String[] { "HB", "CG1", "CG2" },
          new String[] { "N", "H", "C", "O", "HA" }),
      new DihedralAngle("CB", "CG1",
          new String[] { "CD1", "HD11", "HD12", "HD13", "HG12", "HG13" },
          new String[] { "HG12", "HG13", "CD1" },
          new String[] { "N", "H", "C", "O", "HA", "HB", "CG2", "CA" }),
      new DihedralAngle("CG1", "CD1",
          new String[] { "HD11", "HD12", "HD13" },
          new String[] { "HD11", "HD12", "HD13" },
          new String[] { "N", "H", "C", "O", "HA", "HB", "HG12", "HG13", "CA", "CG2", "CB" }),
      new DihedralAngle("CB", "CG2",
          new String[] { "HG21", "HG22", "HG23" },
          new String[] { "HG21", "HG22", "HG23" },
          new String[] { "N", "H", "C", "O", "HA", "HB", "HG12", "HG13", "HD11", "HD12", "HD13", "CD1", "CA", "CB",
              "CG1" }),
  };

  public static final DihedralAngle[] lysine = new DihedralAngle[] {
      new DihedralAngle("CA", "CB",
          new String[] { "CG", "CD", "CE", "NZ", "HZ1", "HZ2", "HZ3", "HE2", "HE3", "HD2", "HE3", "HD2", "HD3", "HG2",
              "HG3", "HB2", "HB3" },
          new String[] { "HB2", "HB3", "CG" },
          new String[] { "N", "H", "C", "O", "HA" }),
      new DihedralAngle("CB", "CG",
          new String[] { "NZ", "HZ1", "HZ2", "HZ3", "HE2", "HE3", "HD2", "HE3", "HD2", "HD3", "HG2", "HG3", "CD",
              "CE" },
          new String[] { "CD", "HG2", "HG3" },
          new String[] { "N", "H", "C", "O", "HA", "HB2", "HB3", "CA" }),
      new DihedralAngle("CG", "CD",
          new String[] { "NZ", "HZ1", "HZ2", "HZ3", "HE2", "HE3", "HD2", "HE3", "HD2", "HD3", "CE" },
          new String[] { "HD2", "HD3", "CE" },
          new String[] { "N", "H", "C", "O", "HA", "HB2", "HB3", "HG2", "HG3", "CA", "CB" }),
      new DihedralAngle("CD", "CE",
          new String[] { "NZ", "HZ1", "HZ2", "HZ3", "HE2", "HE3" },
          new String[] { "HE2", "HE3", "NZ" },
          new String[] { "N", "H", "C", "O", "HA", "HB2", "HB3", "HG2", "HG3", "HD2", "HD3", "CA", "CB", "CG" }),
      new DihedralAngle("CE", "NZ",
          new String[] { "HZ1", "HZ2", "HZ3" },
          new String[] { "HZ1", "HZ2", "HZ3" },
          new String[] { "N", "H", "C", "O", "HA", "HB2", "HB3", "HG2", "HG3", "HD2", "HD3", "HE2", "HE3", "CA",
              "CB", "CG", "CD" }),
  };

  public static final DihedralAngle[] leucine = new DihedralAngle[] {
      new DihedralAngle("CA", "CB",
          new String[] { "CG", "HG", "CD1", "HD11", "HD12", "HD13", "CD2", "HD21", "HD22", "HD23", "HB2", "HB3" },
          new String[] { "CG", "HB2", "HB3" },
          new String[] { "N", "H", "C", "O", "HA" }),
      new DihedralAngle("CB", "CG",
          new String[] { "HG", "CD1", "HD11", "HD12", "HD13", "CD2", "HD21", "HD22", "HD23" },
          new String[] { "HG", "CD1", "CD2" },
          new String[] { "N", "H", "C", "O", "HA", "HB2", "HB3", "CA" }),
      new DihedralAngle("CG", "CD1",
          new String[] { "HD11", "HD12", "HD13" },
          new String[] { "HD11", "HD12", "HD13" },
          new String[] { "N", "H", "C", "O", "HA", "HB2", "HB3", "HG", "CD2", "CA", "CB" }),
      new DihedralAngle("CG", "CD2",
          new String[] { "HD21", "HD22", "HD23" },
          new String[] { "HD21", "HD22", "HD23" },
          new String[] { "N", "H", "C", "O", "HA", "HB2", "HB3", "HG", "HD11", "HD12", "HD13", "CA", "CB", "CD1" }),
  };

  public static final DihedralAngle[] methionine = new DihedralAngle[] {
      new DihedralAngle("CA", "CB",
          new String[] { "CG", "SD", "CE", "HE1", "HE2", "HE3", "HG2", "HG3", "HB2", "HB3" },
          new String[] { "HB2", "HB3", "CG" },
          new String[] { "N", "H", "C", "O", "HA" }),
      new DihedralAngle("CB", "CG",
          new String[] { "SD", "CE", "HE1", "HE2", "HE3", "HG2", "HG3" },
          new String[] { "SD", "HG2", "HG3" },
          new String[] { "N", "H", "C", "O", "HA", "HB2", "HB3", "CA" }),
      new DihedralAngle("CG", "SD",
          new String[] { "CE", "HE1", "HE2", "HE3" },
          new String[] { "CE" },
          new String[] { "N", "H", "C", "O", "HA", "HB2", "HB3", "HG2", "HG3", "CA", "CB" }),
      new DihedralAngle("SD", "CE",
          new String[] { "HE1", "HE2", "HE3" },
          new String[] { "HE1", "HE2", "HE3" },
          new String[] { "N", "H", "C", "O", "HA", "HB2", "HB3", "HG2", "HG3", "CA", "CB", "CG" }),
  };

  public static final DihedralAngle[] asparagine = new DihedralAngle[] {
      new DihedralAngle("CA", "CB",
          new String[] { "CG", "OD1", "ND2", "HD21", "HD22", "HB2", "HB3" },
          new String[] { "CG", "HB2", "HB3" },
          new String[] { "N", "H", "C", "O", "HA" }),
      new DihedralAngle("CB", "CG",
          new String[] { "OD1", "ND2", "HD21", "HD22" },
          new String[] { "OD1", "ND2", "HD21", "HD22" },
          new String[] { "N", "H", "C", "O", "HA", "HB2", "HB3", "CA" }),
  };

  public static final DihedralAngle[] proline = new DihedralAngle[] {};

  public static final DihedralAngle[] glutamine = new DihedralAngle[] {
      new DihedralAngle("CA", "CB",
          new String[] { "CD", "CE", "OE1", "NE2", "HE21", "HE22", "HG2", "HG3", "HB2", "HB3" },
          new String[] { "HB2", "HB3", "CG" },
          new String[] { "N", "H", "C", "O", "HA" }),
      new DihedralAngle("CB", "CG",
          new String[] { "CD", "OE1", "NE2", "HE21", "HE22", "HG2", "HG3" },
          new String[] { "HG2", "HG3", "CD" },
          new String[] { "N", "H", "C", "O", "HA", "HB2", "HB3", "CA" }),
      new DihedralAngle("CG", "CD",
          new String[] { "OE1", "NE2", "HE21", "HE22" },
          new String[] { "OE1", "NE2", "HE21", "HE22" },
          new String[] { "N", "H", "C", "O", "HA", "HB2", "HB3", "HG2", "HG3", "CA", "CB" }),
  };

  public static final DihedralAngle[] arginine = new DihedralAngle[] {
      new DihedralAngle("CA", "CB",
          new String[] { "CG", "CD", "NE", "CZ", "NH1", "HH11", "HH12", "NH2", "HH21", "HH22", "HE", "HD2", "HD3",
              "HG2", "HG3", "HB2", "HB3" },
          new String[] { "HB2", "HB3", "CG" },
          new String[] { "N", "H", "C", "O", "HA" }),
      new DihedralAngle("CB", "CG",
          new String[] { "CD", "NE", "CZ", "NH1", "HH11", "HH12", "NH2", "HH21", "HH22", "HE", "HD2", "HD3", "HG2",
              "HG3" },
          new String[] { "HG2", "HG3", "CD" },
          new String[] { "N", "H", "C", "O", "HA", "HB2", "HB3", "CA" }),
      new DihedralAngle("CG", "CD",
          new String[] { "NE", "CZ", "NH1", "HH11", "HH12", "NH2", "HH21", "HH22", "HE", "HD2", "HD3" },
          new String[] { "HD2", "HD3", "NE" },
          new String[] { "N", "H", "C", "O", "HA", "HB2", "HB3", "HG2", "HG3", "CA", "CB" }),
      new DihedralAngle("CD", "NE",
          new String[] { "HH11", "HH12", "HH21", "HH22", "HE", "NH1", "NH2", "CZ" },
          new String[] { "HH11", "HH12", "HH21", "HH22", "HE", "NH1", "NH2", "CZ" },
          new String[] { "N", "H", "C", "O", "HA", "HB2", "HB3", "HG2", "HG3", "HD2", "HD3", "CA", "CB", "CG" }),
  };

  public static final DihedralAngle[] serine = new DihedralAngle[] {
      new DihedralAngle("CA", "CB",
          new String[] { "OG", "HG", "HB2", "HB3" },
          new String[] { "OG", "HB2", "HB3" },
          new String[] { "N", "H", "C", "O", "HA" }),
      new DihedralAngle("CB", "OG",
          new String[] { "HG" },
          new String[] { "HG" },
          new String[] { "N", "H", "C", "O", "HA", "CA", "HB2", "HB3" }),
  };

  public static final DihedralAngle[] threonine = new DihedralAngle[] {
      new DihedralAngle("CA", "CB",
          new String[] { "HB", "OG1", "HG1", "CG2", "HG21", "HG22", "HG23" },
          new String[] { "HB", "OG1", "CG2" },
          new String[] { "N", "H", "C", "O", "HA" }),
      new DihedralAngle("CB", "OG1",
          new String[] { "HG1" },
          new String[] { "HG1" },
          new String[] { "N", "H", "C", "O", "HA", "HB", "CA", "CG2" }),
      new DihedralAngle("CB", "CG2",
          new String[] { "HG21", "HG22", "HG23" },
          new String[] { "HG21", "HG22", "HG23" },
          new String[] { "N", "H", "C", "O", "HA", "HB", "OG1", "HG1", "CA" }),
  };

  public static final DihedralAngle[] valine = new DihedralAngle[] {
      new DihedralAngle("CA", "CB",
          new String[] { "HB", "CG1", "HG11", "HG12", "HG13", "CG2", "HG21", "HG22", "HG23" },
          new String[] { "HB", "CG1", "CG2" },
          new String[] { "N", "H", "C", "O", "HA" }),
      new DihedralAngle("CB", "CG1",
          new String[] { "HG11", "HG12", "HG13" },
          new String[] { "HG11", "HG12", "HG13" },
          new String[] { "N", "H", "C", "O", "HA", "HB", "CA", "CG2" }),
      new DihedralAngle("CB", "CG2",
          new String[] { "HG21", "HG22", "HG23" },
          new String[] { "HG21", "HG22", "HG23" },
          new String[] { "N", "H", "C", "O", "HA", "HB", "HG11", "HG12", "HG13", "CA", "CG1" }),
  };

  public static final DihedralAngle[] tryptophan = new DihedralAngle[] {
      new DihedralAngle("CA", "CB",
          new String[] { "CG", "CD1", "HD1", "NE1", "HE1", "CD2", "CE2", "CZ2", "HZ2", "CH2", "HH2", "CE3", "HE3",
              "CZ3", "HZ3", "HB2", "HB3" },
          new String[] { "HB2", "HB3", "CG" },
          new String[] { "N", "H", "C", "O", "HA" }),
      new DihedralAngle("CB", "CG",
          new String[] { "CD1", "HD1", "NE1", "HE1", "CD2", "CE2", "CZ2", "HZ2", "CH2", "HH2", "CE3", "HE3", "CZ3",
              "HZ3" },
          new String[] { "HD1", "HE1", "HZ2", "HH2", "HE3", "HZ3", "CD1", "CD2", "NE1", "CE2", "CZ2", "CH2", "CE3",
              "CZ3" },
          new String[] { "N", "H", "C", "O", "HA", "HB2", "HB3", "CA" }),
  };

  public static final DihedralAngle[] tyrosine = new DihedralAngle[] {
      new DihedralAngle("CA", "CB",
          new String[] { "CG", "CD1", "HD1", "CE1", "HE1", "CZ", "OH", "HH", "CD2", "HD2", "CE2", "HE2", "HB2", "HB3" },
          new String[] { "HB2", "HB3", "CG" },
          new String[] { "N", "H", "C", "O", "HA" }),
      new DihedralAngle("CB", "CG",
          new String[] { "CD1", "HD1", "CE1", "HE1", "CZ", "OH", "HH", "CD2", "HD2", "CE2", "HE2" },
          new String[] { "HD1", "HE1", "OH", "HD2", "HE2", "CD1", "CD2", "CE1", "CE2", "CZ" },
          new String[] { "N", "H", "C", "O", "HA", "HB2", "HB3", "CA" }),
      new DihedralAngle("CZ", "OH",
          new String[] { "HH" },
          new String[] { "HH" },
          new String[] { "N", "H", "C", "O", "HA", "HB2", "HB3", "HD1", "HE1", "HD2", "HE2", "CA", "CB", "CG", "CD1",
              "CD2", "CE1", "CE2" }),
  };
}

/**
 * Defines all dihedral angles of the 20 amino acids.
 * Each record also contains the atoms that will be rotated around each of its dihedral angles,
 * and the atoms with which these rotated atoms might clash (physically overlap) with.
 * <p>All atoms are identified through their PDB Atom Names.</p>
 *
 * @param a1 the first atom that makes up the axis of the dihedral bond
 * @param a2 the second atom that makes up the axis of the dihedral bond
 * @param rotations the atoms that will be rotated around the dihedral axis
 * @param mightClash the atoms that have just been rotated and could now be clashing with other atoms
 * @param clashesWith the atoms that make up the backbone and anything that has already been rotated that the recently rotated atoms could now be clashing with
 */
public record DihedralAngle(
    String a1, String a2,
    String[] rotations,
    String[] mightClash,
    String[] clashesWith
	) {

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
}
