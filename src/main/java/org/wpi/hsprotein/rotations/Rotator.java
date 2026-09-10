// Get rotated idiot
package org.wpi.hsprotein.rotations;

import org.biojava.nbio.structure.Calc;
import org.biojava.nbio.structure.Atom;
import org.biojava.nbio.structure.Group;
import org.biojava.nbio.structure.align.util.RotationAxis;

public class Rotator {
	static String[][] hardCodedDihedralAtoms = {{"C1", "C2", "C3", "O3"}};
	static String[][] rotatedAtoms = {{"O3"}};

	public static void runSingleResidue(Group group) {
		for (String[] dihedralAngleCarbons : hardCodedDihedralAtoms) {
			Atom atom1 = group.getAtom(dihedralAngleCarbons[0]);
			Atom atom2 = group.getAtom(dihedralAngleCarbons[1]);
			Atom atom3 = group.getAtom(dihedralAngleCarbons[2]);
			Atom atom4 = group.getAtom(dihedralAngleCarbons[3]);

			RotationAxis dihedralAxis = new RotationAxis(Calc.subtract(atom2, atom3), atom3, Math.toRadians(Calc.torsionAngle(atom1, atom2, atom3, atom4)));
			dihedralAxis.rotate(new Atom[] {group.getAtom("O3")}, Math.toRadians(90));
		}
	}
}