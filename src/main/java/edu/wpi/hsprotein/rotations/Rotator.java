// Get rotated idiot
package edu.wpi.hsprotein.rotations;

import java.util.ArrayList;

import org.biojava.nbio.structure.Atom;
import org.biojava.nbio.structure.Calc;
import org.biojava.nbio.structure.Group;
import org.biojava.nbio.structure.align.util.RotationAxis;

/**
 * A class containing all rotational operations. Will be greatly expanded in the
 * future.
 */
public class Rotator {
  /**
   * Takes a group and dihedral angle and calculates the energy of that rotation.
   * This should probably be moved to CalcEnergy.
   * @param group The group that is being rotated.
   * @param angle The dihedral which was just rotated.
   * @returns The energy of the rotation.
   */
  public static double getRotationEnergy(Group group, DihedralAngle angle) {
    double energy = 0;
    for (String atom : angle.mightClash())
      energy += CalcEnergy.calculateEnergy(atom, angle.clashesWith(), group, group.getChain());
    return energy;
  }

  /**
   * Rotates an amino acid residue and calculates the minimum energy. Recurses
   * over all dihedrals for the residue. A proper implementation would
   * accumulate the optimal angles and have a better energy threshold to stop
   * recursing, this one doesn't.
   * @param group The group to rotate.
   * @param index The index of the dihedral angle to rotate around for the sake of recursion.
   */
  public static double rotateAroundDihedral(Group group, int index) {
    DihedralAngle[] dihedrals = DihedralAngle.getDihedrals(group.getPDBName());
    DihedralAngle dihedral = dihedrals[index];
    Atom a1 = group.getAtom(dihedral.a1());
    Atom a2 = group.getAtom(dihedral.a2());

    RotationAxis axis = new RotationAxis(Calc.subtract(a1, a2), a2, 0);
    ArrayList<Atom> rotateList = new ArrayList<>(dihedral.rotations().length);
    for (String name : dihedral.rotations())
      if (group.getAtom(name) != null)
        rotateList.add(group.getAtom(name));
    rotateList.trimToSize();
    Atom[] rotateAtoms = rotateList.toArray(new Atom[] {});

    double minEnergy = Double.MAX_VALUE;
    for (int i = 0; i < 72; ++i) {
      axis.rotate(rotateAtoms, Math.toRadians(5));
      double energy = getRotationEnergy(group, dihedral);
      if (energy < minEnergy && index < dihedrals.length - 1)
        energy += rotateAroundDihedral(group, index + 1);
      System.out.println(index + "\t" + (i * 5 + 5) + "\t" + energy);
      if (energy < minEnergy)
        minEnergy = energy;
    }
    System.out.println("minEnergy: " + minEnergy);
    return minEnergy;
  }

  /**
   * Rotates a single residue 5° repeatedly.
   * @param group the group of atoms to rotate
   */
  public static void runSingleResidue(Group group) {
    rotateAroundDihedral(group, 0);
  }
}
