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
          new String[] { "SG", "HB2", "HB3" },
          new String[] { "SG", "HB2", "HB3" },
          new String[] { "N", "H", "C", "O", "HA" }),
  };

  public static final DihedralAngle[] asparticAcid = new DihedralAngle[] {
      new DihedralAngle("CA", "CB",
          new String[] { "CG", "OD1", "OD2", "HB2", "HB3" },
          new String[] { "CG", "HB2", "HB3" },
          new String[] { "N", "H", "C", "O", "HA" }),
      new DihedralAngle("CB", "CG",
          new String[] { "OD1", "OD2" },
          new String[] { "OD1", "OD2" },
          new String[] { "N", "H", "C", "O", "HA", "CA", "HB2", "HB3" }),
  };

  public static final DihedralAngle[] glutamicAcid = new DihedralAngle[] {
      new DihedralAngle("CA", "CB",
          new String[] { "CG", "CD", "OE1", "OE2", "HG2", "HG3", "HB2", "HB3" },
          new String[] { "CG", "HB2", "HB3" },
          new String[] { "N", "H", "C", "O", "HA" }),
      new DihedralAngle("CB", "CG",
          new String[] { "CD", "OE1", "OE2", "HG2", "HG3" },
          new String[] { "CD", "HG2", "HG3" },
          new String[] { "N", "H", "C", "O", "HA", "CA", "HB2", "HB3" }),
      new DihedralAngle("CG", "CD",
          new String[] { "OE1", "OE2" },
          new String[] { "OE1", "OE2" },
          new String[] { "N", "H", "C", "O", "HA", "CA", "HB2", "HB3", "CB", "HG2", "HG3" }),
  };

  public static final DihedralAngle[] phenylalanine = new DihedralAngle[] {
      new DihedralAngle("CA", "CB",
          new String[] { "CG", "CD1", "HD1", "CE1", "HE1", "CZ", "HZ", "CD2", "HD2", "CE2", "HE2", "HB2", "HB3" },
          new String[] { "CG", "HB2", "HB3", "CZ", "HZ" },
          new String[] { "N", "H", "C", "O", "HA" }),
      new DihedralAngle("CB", "CG",
          new String[] { "CD1", "HD1", "CE1", "HE1", "CD2", "HD2", "CE2", "HE2" },
          new String[] { "CD1", "HD1", "CE1", "HE1", "CD2", "HD2", "CE2", "HE2" },
          new String[] { "N", "H", "C", "O", "HA", "CA", "HB2", "HB3" }),
  };

  public static final DihedralAngle[] glycine = new DihedralAngle[] {};

  public static final DihedralAngle[] histidine = new DihedralAngle[] {
      new DihedralAngle("CA", "CB",
          new String[] { "CG", "ND1", "HD1", "CE1", "HE1", "CD2", "HD2", "NE2", "HB2", "HB3" },
          new String[] { "CG", "HB2", "HB3" },
          new String[] { "N", "H", "C", "O", "HA" }),
      new DihedralAngle("CB", "CG",
          new String[] { "ND1", "HD1", "CE1", "HE1", "CD2", "HD2", "NE2" },
          new String[] { "ND1", "HD1", "CE1", "HE1", "CD2", "HD2", "NE2" },
          new String[] { "N", "H", "C", "O", "HA", "CA", "HB2", "HB3", "CB" }),
  };

  public static final DihedralAngle[] isoleucine = new DihedralAngle[] {
      new DihedralAngle("CA", "CB",
          new String[] { "HB", "CG1", "CD1", "HD11", "HD12", "HD13", "HG12", "HG13", "CG2", "HG21", "HG22", "HG23" },
          new String[] { "HB", "CG1", "CG2" },
          new String[] { "N", "H", "C", "O", "HA" }),
      new DihedralAngle("CB", "CG1",
          new String[] { "CG1", "CD1", "HD11", "HD12", "HD13", "HG12", "HG13" },
          new String[] { "CD1", "HG12", "HG13" },
          new String[] { "N", "H", "C", "O", "HA", "CA", "HB" }),
      new DihedralAngle("CG1", "CD1",
          new String[] { "HD11", "HD12", "HD13" },
          new String[] { "HD11", "HD12", "HD13" },
          new String[] { "N", "H", "C", "O", "HA", "CA", "HB", "CB", "HG12", "HG13" }),
      new DihedralAngle("CB", "CG2",
          new String[] { "HG21", "HG22", "HG23" },
          new String[] { "HG21", "HG22", "HG23" },
          new String[] { "N", "H", "C", "O", "HA", "CA", "HB", "CB", "HG12", "HG13", "CG1", "HD11", "HD12", "HD13",
              "CD1" }),
  };

  public static final DihedralAngle[] lysine = new DihedralAngle[] {
      new DihedralAngle("CA", "CB",
          new String[] { "CG", "CD", "CE", "NZ", "HZ1", "HZ2", "HZ3", "HE2", "HE3", "HD2", "HE3", "HD2", "HD3", "HG2",
              "HG3", "HB2", "HB3" },
          new String[] { "CG", "HB2", "HB3" },
          new String[] { "N", "H", "C", "O", "HA" }),
      new DihedralAngle("CB", "CG",
          new String[] { "CD", "CE", "NZ", "HZ1", "HZ2", "HZ3", "HE2", "HE3", "HD2", "HE3", "HD2", "HD3", "HG2",
              "HG3" },
          new String[] { "CD", "HG2", "HG3" },
          new String[] { "N", "H", "C", "O", "HA", "CA", "HB2", "HB3" }),
      new DihedralAngle("CG", "CD",
          new String[] { "CE", "NZ", "HZ1", "HZ2", "HZ3", "HE2", "HE3", "HD2", "HE3", "HD2", "HD3" },
          new String[] { "CE", "HD2", "HD3" },
          new String[] { "N", "H", "C", "O", "HA", "CA", "HB2", "HB3", "CB", "HG2", "HG3" }),
      new DihedralAngle("CD", "CE",
          new String[] { "NZ", "HZ1", "HZ2", "HZ3", "HE2", "HE3" },
          new String[] { "NZ", "HE2", "HE3" },
          new String[] { "N", "H", "C", "O", "HA", "CA", "HB2", "HB3", "CB", "HG2", "HG3", "CG", "HD2", "HD3" }),
      new DihedralAngle("CE", "NZ",
          new String[] { "HZ1", "HZ2", "HZ3" },
          new String[] { "HZ1", "HZ2", "HZ3" },
          new String[] { "N", "H", "C", "O", "HA", "CA", "HB2", "HB3", "CB", "HG2", "HG3", "CG", "HD2", "HD3", "CD",
              "HE2", "HE3" }),
  };

  public static final DihedralAngle[] leucine = new DihedralAngle[] {
      new DihedralAngle("CA", "CB",
          new String[] { "CG", "HG", "CD1", "HD11", "HD12", "HD13", "CD2", "HD21", "HD22", "HD23", "HB2", "HB3" },
          new String[] { "CG", "HB2", "HB3" },
          new String[] { "N", "H", "C", "O", "HA" }),
      new DihedralAngle("CB", "CG",
          new String[] { "HG", "CD1", "HD11", "HD12", "HD13", "CD2", "HD21", "HD22", "HD23" },
          new String[] { "HG", "CD1", "CD2" },
          new String[] { "N", "H", "C", "O", "HA", "CA", "HB2", "HB3" }),
      new DihedralAngle("CG", "CD1",
          new String[] { "HD11", "HD12", "HD13" },
          new String[] { "HD11", "HD12", "HD13" },
          new String[] { "N", "H", "C", "O", "HA", "CA", "HB2", "HB3", "HG" }),
      new DihedralAngle("CG", "CD2",
          new String[] { "HD21", "HD22", "HD23" },
          new String[] { "HD21", "HD22", "HD23" },
          new String[] { "N", "H", "C", "O", "HA", "CA", "HB2", "HB3", "HG", "CD1", "HD11", "HD12", "HD13" }),
  };

  public static final DihedralAngle[] methionine = new DihedralAngle[] {
      new DihedralAngle("CA", "CB",
          new String[] { "CG", "SD", "CE", "HE1", "HE2", "HE3", "HG2", "HG3", "HB2", "HB3" },
          new String[] { "CG", "HB2", "HB3" },
          new String[] { "N", "H", "C", "O", "HA" }),
      new DihedralAngle("CB", "CG",
          new String[] { "SD", "CE", "HE1", "HE2", "HE3", "HG2", "HG3" },
          new String[] { "SD", "HG2", "HG3" },
          new String[] { "N", "H", "C", "O", "HA", "CA", "HB2", "HB3" }),
      new DihedralAngle("CG", "SD",
          new String[] { "CE", "HE1", "HE2", "HE3" },
          new String[] { "CE" },
          new String[] { "N", "H", "C", "O", "HA", "CA", "HB2", "HB3", "CB", "HG2", "HG3" }),
      new DihedralAngle("SD", "CE",
          new String[] { "HE1", "HE2", "HE3" },
          new String[] { "HE1", "HE2", "HE3" },
          new String[] { "N", "H", "C", "O", "HA", "CA", "HB2", "HB3", "CB", "HG2", "HG3", "CG" }),
  };

  public static final DihedralAngle[] asparagine = new DihedralAngle[] {
      new DihedralAngle("CA", "CB",
          new String[] { "CG", "OD1", "ND2", "HD21", "HD22", "HB2", "HB3" },
          new String[] { "CG", "HB2", "HB3" },
          new String[] { "N", "H", "C", "O", "HA" }),
      new DihedralAngle("CB", "CG",
          new String[] { "OD1", "ND2", "HD21", "HD22" },
          new String[] { "OD1", "ND2" },
          new String[] { "N", "H", "C", "O", "HA", "CA", "HB2", "HB3" }),
      new DihedralAngle("CB", "CG",
          new String[] { "HD21", "HD22" },
          new String[] { "HD21", "HD22" },
          new String[] { "N", "H", "C", "O", "HA", "CA", "HB2", "HB3", "CB", "OD1" }),
  };

  public static final DihedralAngle[] proline = new DihedralAngle[] {};

  public static final DihedralAngle[] glutamine = new DihedralAngle[] {
      new DihedralAngle("CA", "CB",
          new String[] { "CD", "CE", "OE1", "NE2", "HE21", "HE22", "HG2", "HG3", "HB2", "HB3" },
          new String[] { "CG", "HB2", "HB3" },
          new String[] { "N", "H", "C", "O", "HA" }),
      new DihedralAngle("CB", "CG",
          new String[] { "CD", "OE1", "NE2", "HE21", "HE22", "HG2", "HG3" },
          new String[] { "CD", "HG2", "HG3" },
          new String[] { "N", "H", "C", "O", "HA", "CA", "HB2", "HB3" }),
      new DihedralAngle("CG", "CD",
          new String[] { "OE1", "NE2", "HE21", "HE22" },
          new String[] { "OE1", "NE2" },
          new String[] { "N", "H", "C", "O", "HA", "CA", "HB2", "HB3", "CB", "HG2", "HG3" }),
      new DihedralAngle("CD", "NE2",
          new String[] { "HE21", "HE22" },
          new String[] { "HE21", "HE22" },
          new String[] { "N", "H", "C", "O", "HA", "CA", "HB2", "HB3", "CB", "HG2", "HG3", "CG", "OE1" }),
  };

  public static final DihedralAngle[] arginine = new DihedralAngle[] {
      new DihedralAngle("CA", "CB",
          new String[] { "CG", "CD", "NE", "CZ", "NH1", "HH11", "HH12", "NH2", "HH21", "HH22", "HE", "HD2", "HD3",
              "HG2", "HG3", "HB2", "HB3" },
          new String[] { "CG", "HB2", "HB3" },
          new String[] { "N", "H", "C", "O", "HA" }),
      new DihedralAngle("CB", "CG",
          new String[] { "CD", "NE", "CZ", "NH1", "HH11", "HH12", "NH2", "HH21", "HH22", "HE", "HD2", "HD3", "HG2",
              "HG3" },
          new String[] { "CD", "HG2", "HG3" },
          new String[] { "N", "H", "C", "O", "HA", "CA", "HB2", "HB3" }),
      new DihedralAngle("CG", "CD",
          new String[] { "NE", "CZ", "NH1", "HH11", "HH12", "NH2", "HH21", "HH22", "HE", "HD2", "HD3" },
          new String[] { "NE", "HD2", "HD3" },
          new String[] { "N", "H", "C", "O", "HA", "CA", "HB2", "HB3", "CB", "HG2", "HG3" }),
      new DihedralAngle("CD", "NE",
          new String[] { "CZ", "NH1", "HH11", "HH12", "NH2", "HH21", "HH22", "HE" },
          new String[] { "CZ", "NH1", "HH11", "HH12", "NH2", "HH21", "HH22", "HE" },
          new String[] { "N", "H", "C", "O", "HA", "CA", "HB2", "HB3", "CB", "HG2", "HG3", "CG", "HD2", "HD3" }),
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
          new String[] { "N", "H", "C", "O", "HA", "CA", "HB", "CG2" }),
      new DihedralAngle("CB", "CG2",
          new String[] { "HG21", "HG22", "HG23" },
          new String[] { "HG21", "HG22", "HG23" },
          new String[] { "N", "H", "C", "O", "HA", "CA", "HB", "OG1", "HG1" }),
  };

  public static final DihedralAngle[] valine = new DihedralAngle[] {
      new DihedralAngle("CA", "CB",
          new String[] { "HB", "CG1", "HG11", "HG12", "HG13", "CG2", "HG21", "HG22", "HG23" },
          new String[] { "HB", "CG1", "CG2" },
          new String[] { "N", "H", "C", "O", "HA" }),
      new DihedralAngle("CB", "CG1",
          new String[] { "HG11", "HG12", "HG13" },
          new String[] { "HG11", "HG12", "HG13" },
          new String[] { "N", "H", "C", "O", "HA", "CA", "HB" }),
      new DihedralAngle("CB", "CG2",
          new String[] { "HG21", "HG22", "HG23" },
          new String[] { "HG21", "HG22", "HG23" },
          new String[] { "N", "H", "C", "O", "HA", "CA", "HB", "CG1", "HG11", "HG12", "HG13" }),
  };

  public static final DihedralAngle[] tryptophan = new DihedralAngle[] {
      new DihedralAngle("CA", "CB",
          new String[] { "CG", "CD1", "HD1", "NE1", "HE1", "CD2", "CE2", "CZ2", "HZ2", "CH2", "HH2", "CE3", "HE3",
              "CZ3", "HZ3", "HB2", "HB3" },
          new String[] { "CG", "HB2", "HB3" },
          new String[] { "N", "H", "C", "O", "HA" }),
      new DihedralAngle("CB", "CG",
          new String[] { "CD1", "HD1", "NE1", "HE1", "CD2", "CE2", "CZ2", "HZ2", "CH2", "HH2", "CE3", "HE3", "CZ3",
              "HZ3" },
          new String[] { "CD1", "HD1", "NE1", "HE1", "CD2", "CE2", "CZ2", "HZ2", "CH2", "HH2", "CE3", "HE3", "CZ3",
              "HZ3" },
          new String[] { "N", "H", "C", "O", "HA", "CA", "HB2", "HB3" }),
  };

  public static final DihedralAngle[] tyrosine = new DihedralAngle[] {
      new DihedralAngle("CA", "CB",
          new String[] { "CG", "CD1", "HD1", "CE1", "HE1", "CZ", "OH", "HH", "CD2", "HD2", "CE2", "HE2" },
          new String[] { "CG", "HB2", "HB3", "CZ", "OH" },
          new String[] { "N", "H", "C", "O", "HA" }),
      new DihedralAngle("CB", "CG",
          new String[] { "CD1", "HD1", "CE1", "HE1", "HH", "CD2", "HD2", "CE2", "HE2" },
          new String[] { "CD1", "HD1", "CE1", "HE1", "HH", "CD2", "HD2", "CE2" },
          new String[] { "N", "H", "C", "O", "HA", "CA", "HB2", "HB3" }),
      new DihedralAngle("CZ", "OH",
          new String[] { "HH" },
          new String[] { "HH" },
          new String[] { "HE1", "HE2", "CE1", "CE2" }),
  };
}

public record DihedralAngle(
    String a1,
    String a2,
    String[] rotations,
    String[] mightClash,
    String[] clashesWith) {

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
