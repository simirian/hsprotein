package edu.wpi.hsprotein;

import java.util.List;

import org.biojava.nbio.structure.Chain;
import org.biojava.nbio.structure.Group;

import edu.wpi.hsprotein.filemanager.ProteinManager;
import edu.wpi.hsprotein.helpers.InputValidation;
import edu.wpi.hsprotein.rotations.Rotator;

class Main {
  public static void main(String[] args) {
    String file = args[0];
    String residue = args[1];

    // Avoid missing "PDB Directory" system property warning
    if (System.getProperty("PDB_DIR") == null) {
      System.setProperty("PDB_DIR", "~/Downloads/PDB");
    }

    // Load input file into Protein File Manager
    ProteinManager pManager = null;
    try {
      pManager = ProteinManager.load(file);
    } catch (Exception e) {
      InputValidation.handleImproperUserInput("Invalid input file type");
    }

    int resid = Integer.parseInt(residue);

    // Extract structure from loaded file
    List<Chain> chains = pManager.getStructure().getChains();
    if (chains.size() < 1) {
      System.out.println("Found no chains!");
      return;
    }
    Group group = chains.get(0).getSeqResGroup(resid);
    Rotator.runSingleResidue(group);
  }
}
