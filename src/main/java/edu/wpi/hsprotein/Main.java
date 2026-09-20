package edu.wpi.hsprotein;

import java.io.FileWriter;

import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.gui.BiojavaJmol;
import org.biojava.nbio.structure.io.CifFileReader;

import edu.wpi.hsprotein.rotations.DihedralAngle;

class Main {
  public static void main(String[] args) {
    if (args.length == 1) {
      String[] residues = { "ala", "cys", "asp", "glu", "phe", "gly", "his", "ile", "lys", "leu", "met", "asn", "pro",
          "gln", "arg", "ser", "thr", "val", "trp", "tyr", };
      for (String residue : residues) {
        DihedralAngle[] dihedrals = DihedralAngle.getDihedrals(residue);
        for (int i = 0; i < dihedrals.length; ++i) {
          DihedralAngle dihedral = dihedrals[i];
          String fname = residue + i + ".cxc";
          String command = "hide cartoon; show; color #888888; hide ~:" + residue + "; color @" + dihedral.a1() + " | @"
              + dihedral.a2() + " #00ffff\n";
          for (String atom : dihedral.rotations())
            command += "; color @" + atom + " #44ff44";
          for (String atom : dihedral.clashesWith())
            command += "; color @" + atom + " #ff4444";
          for (String atom : dihedral.mightClash())
            command += "; color @" + atom + " #ffff00";
          try {
            FileWriter writer = new FileWriter(args[0] + "/" + fname);
            writer.write(command);
            writer.close();
          } catch (Exception e) {
            System.out.println(e);
          }
        }
      }
    } else if (args.length == 3) {
      String filepath = args[0];
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
      jmol.evalString("hide !" + args[1] + "; color [x888888];");
      jmol.evalString("select *." + angle.a1() + " or *." + angle.a2() + "; color [x00ffff];");
      for (String atom : angle.rotations())
        jmol.evalString("select *." + atom + "; color [x44ff44];");
      for (String atom : angle.clashesWith())
        jmol.evalString("select *." + atom + "; color [xff4444];");
      for (String atom : angle.mightClash())
        jmol.evalString("select *." + atom + "; color [xffff00];");
    } else {
      System.out.println("Invalid command form! Try one of these:\ncmd FILE RESIDUE NANGLE\ncmd OUTDIR");
    }
  }
}
