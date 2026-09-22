package edu.wpi.hsprotein.rotations;

import java.util.List;

import org.biojava.nbio.structure.Atom;
import org.biojava.nbio.structure.Bond;
import org.biojava.nbio.structure.Calc;
import org.biojava.nbio.structure.Chain;
import org.biojava.nbio.structure.Element;
import org.biojava.nbio.structure.Group;
import org.biojava.nbio.structure.GroupType;

public final class CalcEnergy {
  /**
   * Returns the radius of an atom based on the element and what it's bonded to.
   *
   * O: 1.4
   * N: 1.3
   * S: 1.75
   * C: 1.5
   * C-O: 1.3
   * H: 1
   * H-C: 1.1
   *
   * @param atom The atom to test.
   * @return The radius of the atom.
   */
  static double getRadius(Atom atom) {
    Element element = atom.getElement();
    if (element == Element.O)
      return 1.4;
    if (element == Element.N)
      return 1.3;
    if (element == Element.S)
      return 1.75;
    if (element == Element.H) {
      List<Bond> bonds = atom.getBonds();
      Element eA = bonds.get(0).getAtomA().getElement();
      Element eB = bonds.get(0).getAtomB().getElement();
      if (eA == Element.C || eB == Element.C)
        return 1.1;
      return 1;
    }
    if (element == Element.C) {
      List<Bond> bonds = atom.getBonds();
      for (Bond bond : bonds)
        if (bond.getAtomA().getElement() == Element.O || bond.getAtomA().getElement() == Element.O)
          return 1.3;
      return 1.5;
    }
    return 0.0;
  }

  /**
   * Gets the energy of a pair of atoms using the hard sphere model.
   * @param a1 The first atom to check.
   * @param a2 The second atom to check.
   * @return The energy of interaction.
   */
  static double getEnergy(Atom a1, Atom a2) {
    double distance = Calc.getDistance(a1, a2);
    double rsum = getRadius(a1) + getRadius(a2);
    rsum *= rsum;
    if (distance > rsum)
      return 0.0;
    return Math.pow(1 - Math.pow(distance / rsum, 3), 2);
  }

  /**
   * Calculates the energy of an atom's clashes within a single chain.
   * @param atom The name of the atom which might clash with the rest of the structure.
   * @param clashes The atoms of the same residue which the atom might clash with, supplied by a {@link DihedralAngle}.
   * @param residue The residue which the atom (and the clashes list) belongs to.
   * @param chain The rest of the chain which contains the atom and the residue.
   * @return The energy of the atom's interaction with the rest of the chain.
   */
  static double calculateEnergy(String atom, String[] clashes, Group residue, Chain chain) {
    Atom baseAtom = residue.getAtom(atom);
    double energy = 0.0;
    for (String clash : clashes) {
      Atom clashAtom = residue.getAtom(clash);
      energy += getEnergy(baseAtom, clashAtom);
    }
    for (Group group : chain.getAtomGroups(GroupType.AMINOACID)) {
      if (group == residue)
        continue;
      for (Atom clash : group.getAtoms()) {
        energy += getEnergy(baseAtom, clash);
      }
    }
    return energy;
  }
}
