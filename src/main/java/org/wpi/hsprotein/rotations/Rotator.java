// Get rotated idiot
package org.wpi.hsprotein.rotations;

import org.biojava.nbio.structure.Calc;
import org.biojava.nbio.structure.Atom;
import org.biojava.nbio.structure.Group;
import org.biojava.nbio.structure.align.util.RotationAxis;

public class Rotator {
	static String[][] hardCodedDihedralAtoms = {{"C1", "C2", "C3", "C4"}};
	static String[][] rotatedAtoms = {{"C4"}};

	public static void runSingleResidue(Group group) {
		for (String[] dihedralAngleCarbons : hardCodedDihedralAtoms) {
			Atom atom1 = group.getAtom(dihedralAngleCarbons[1]);
			Atom atom2 = group.getAtom(dihedralAngleCarbons[2]);

			RotationAxis dihedralAxis = new RotationAxis(Calc.subtract(atom1, atom2), atom2, 0);
			dihedralAxis.rotate(new Atom[] {group.getAtom("C4")}, Math.toRadians(90));
		}
	}
}