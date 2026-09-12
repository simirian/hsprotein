package org.wpi.hsprotein;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.gui.BiojavaJmol;
import org.biojava.nbio.structure.io.CifFileReader;
import org.wpi.hsprotein.filemanager.ProteinManager;
import org.wpi.hsprotein.rotations.DihedralAngle;

class Main {
  public static void main(String[] args) {
    if (args.length < 3) {
      System.out.println("Needs more args!\ncmd FILE RESIDUE NANGLE");
      return;
    }
    String filepath = args[0];

    // Load input file into Protein File Manager
    ProteinManager pManager = null;
    try {
      pManager = ProteinManager.load(filepath);
    }
    catch (Exception e) {
      handleImproperUserInput("Invalid input file type");
    }

    Structure s;
    try {
      s = new CifFileReader().getStructure(filepath);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.out.println("Failed to load; aborting.");
      return;
    }

    DihedralAngle[] angles = DihedralAngle.getDihedrals(args[1]);
    int idx = Integer.parseInt(args[2]);
    if (idx >= angles.length) {
      System.out.println("Can't use angle " + idx + " in residue " + args[1] + "; it doesn't exist.");
      return;
    }
    DihedralAngle angle = angles[idx];

    BiojavaJmol jmol = new BiojavaJmol();
    jmol.setStructure(s);
    jmol.evalString("hide !" + args[1]);
    jmol.evalString("select *." + angle.a1() + " or *." + angle.a2() + "; color [x00ffff];");
    for (String atom : angle.rotations())
      jmol.evalString("select *." + atom + "; color [x44ff44];");
    for (String atom : angle.clashesWith())
      jmol.evalString("select *." + atom + "; color [xff4444];");
    for (String atom : angle.mightClash())
      jmol.evalString("select *." + atom + "; color [xffff00];");
  }

  /** 
   * Alerts the user of improper command line arguments in a consistent, formatted way.
   * <b>Exits the program.<b>
   * @param errorMessage the error message to display to the user
   */
  private static void handleImproperUserInput(String errorMessage) {
    System.out.println("\n\033[31m" + "Error: " + errorMessage + "\033[0m");
    System.out.println("See README for example arguments and proper usage.\n");
    System.exit(1);
  }
}
