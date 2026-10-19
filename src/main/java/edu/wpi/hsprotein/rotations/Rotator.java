// Get rotated idiot
package edu.wpi.hsprotein.rotations;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import org.biojava.nbio.structure.Atom;
import org.biojava.nbio.structure.Calc;
import org.biojava.nbio.structure.Chain;
import org.biojava.nbio.structure.Group;
import org.biojava.nbio.structure.align.util.RotationAxis;

/**
 * A class containing all rotational operations. Will be greatly expanded in the future.
 */
public class Rotator {
	Group group;
	Chain chain;
	Boolean shouldSaveEnergy;
	DihedralAngle[] dihAngles;

	double lowestTotalEnergy;
	ArrayList<int[]> lowestEnergyAngles;
	double[] allEnergy;

	/**
	 * An object representing the rotation of the provided amino acid group.
	 * 
	 * @param chain the protein chain containing {@code group}
	 * @param group the group of atoms to rotate
	 * @param saveEnergy whether to save all calculated energy outputs or not
	 */
	public Rotator(Group group, Chain chain, boolean saveEnergy) {
		this.group = group;
		this.chain = chain;
		shouldSaveEnergy = saveEnergy;
		dihAngles = DihedralAngle.getDihedrals(group.getPDBName());
		lowestTotalEnergy = Integer.MAX_VALUE;
		lowestEnergyAngles = new ArrayList<>();

		// Zero out the dihedral angles of the amino acid
		zeroOutAminoAcid();

		// Only save energy if necessary due to storage size
		if (saveEnergy) {
			if (dihAngles.length > 5) {
				System.out.println("Cannot save all energy outputs for each dihedral angle due to storage capacity issues.");
			} else {
				allEnergy = new double[(int) Math.pow(360 / 5, dihAngles.length)];
			}
		}
	}

	/**
	 * Calculates the current output of the amino acid from its "true zero" position and resets it to that value.
	 */
	private void zeroOutAminoAcid() {
		for (int i = 0; i < dihAngles.length; i++) {
			Atom atom1 = group.getAtom(dihAngles[i].a1());
			Atom atom2 = group.getAtom(dihAngles[i].a2());
			Atom atom3 = group.getAtom(dihAngles[i].a3());
			Atom atom4 = group.getAtom(dihAngles[i].a4());
			RotationAxis dihedralAxis = new RotationAxis(Calc.subtract(atom2, atom3), atom3, 0);
			Atom[] rotatingAtoms = Arrays.stream(dihAngles[i].rotations()).map(a -> group.getAtom(a)).toArray(Atom[]::new);

			// Rotate all connected atoms
			dihedralAxis.rotate(rotatingAtoms, Math.toRadians(DihedralAngle.calcAngle(atom1, atom2, atom3, atom4)));
		}
	}

	/**
	 * Rotates each dihedral angle in a single residue by {@code angle[i]}°.
	 *
	 * @param group the group of atoms to rotate
	 * @param angles the angles to rotate each dihedral angle by, in order
	 * @throws IllegalArgumentException if the length of angles is not the same as the number of dihedral angles in the group
	 */
	public void rotateSingleResidue(int[] angles) throws IllegalArgumentException {
		if (dihAngles.length != angles.length) {
			throw new IllegalArgumentException("There are " + dihAngles.length + " dihedral angles, but only " + angles.length
					+ " rotational angles were provided");
		}

		for (int i = 0; i < angles.length; i++) {
			Atom atom1 = group.getAtom(dihAngles[i].a2());
			Atom atom2 = group.getAtom(dihAngles[i].a3());
			RotationAxis dihedralAxis = new RotationAxis(Calc.subtract(atom1, atom2), atom2, 0);

			// Define all atoms
			Atom[] rotatingAtoms = Arrays.stream(dihAngles[i].rotations()).map(a -> group.getAtom(a)).toArray(Atom[]::new);

			// Rotate all connected atoms
			dihedralAxis.rotate(rotatingAtoms, Math.toRadians(angles[i]));
		}
	}

	/**
	 * The slower rotation function that calculates all energy states and the final rotation.
	 *
	 * @param currentIndex the current index of the dihedral angle
	 * @param currentAngles the angles of the previously rotated dihedral angles
	 * @param currentEnergy the calculated energy of the previously rotated dihedral angles
	 */
	private void rotateFindEnergy(int currentIndex, int[] currentAngles, double currentEnergy) {
		Atom atom1 = group.getAtom(dihAngles[currentIndex].a2());
		Atom atom2 = group.getAtom(dihAngles[currentIndex].a3());
		RotationAxis dihedralAxis = new RotationAxis(Calc.subtract(atom1, atom2), atom2, 0);

		// Define all atoms
		Atom[] rotatingAtoms = Arrays.stream(dihAngles[currentIndex].rotations()).map(a -> group.getAtom(a)).toArray(Atom[]::new);

		for (int i = 0; i < 360; i += 5) {
			// Rotate all connected atoms
			if (i != 0)
				dihedralAxis.rotate(rotatingAtoms, Math.toRadians(5));

			// Calculate energy of the current dihedral angle
			double totalEnergy = currentEnergy;
			for (String clashAtom : dihAngles[currentIndex].mightClash())
				totalEnergy += CalcEnergy.calculateEnergy(clashAtom, dihAngles[currentIndex].clashesWith(), group, chain);

			// If not on the last dihedral angle, continue to rotate the later angles
			if (currentIndex < dihAngles.length - 1) {
				currentAngles[currentIndex] = i;
				rotateFindEnergy(currentIndex + 1, currentAngles, totalEnergy);
			}

			// If on the last dihedral angle, calculate the energy of the entire side chain
			else {
				// Save the lowest energy and the angle of rotation from the initial orientation
				if (totalEnergy < lowestTotalEnergy) {
					lowestTotalEnergy = totalEnergy;
					lowestEnergyAngles.clear();
				}
				// record the current angles as competitors for the lowest energy
				if (totalEnergy <= lowestTotalEnergy) {
					currentAngles[currentIndex] = i;
					lowestEnergyAngles.add(currentAngles.clone());
				}
			}
		}
	}

	/**
	 * The optimized rotation function that only returns the lowest energy and final rotation.
	 * Will require further optimizations as right now it is very minimal.
	 * This is simply a place to develop for future iterations of the project.
	 *
	 * @param dih the index of the dihedral angle currently being worked on
	 */
	private void rotateFast(int currentIndex, int[] currentAngles, double currentEnergy) {
		Atom atom1 = group.getAtom(dihAngles[currentIndex].a2());
		Atom atom2 = group.getAtom(dihAngles[currentIndex].a3());
		RotationAxis dihedralAxis = new RotationAxis(Calc.subtract(atom1, atom2), atom2, 0);

		// Define all atoms
		Atom[] rotatingAtoms = Arrays.stream(dihAngles[currentIndex].rotations()).map(a -> group.getAtom(a)).toArray(Atom[]::new);

		for (int i = 0; i < 360; i += 5) {
			// Rotate all connected atoms
			if (i != 0)
				dihedralAxis.rotate(rotatingAtoms, Math.toRadians(5));

			// Calculate energy of the current dihedral angle
			double totalEnergy = currentEnergy;
			for (String clashAtom : dihAngles[currentIndex].mightClash())
				totalEnergy += CalcEnergy.calculateEnergy(clashAtom, dihAngles[currentIndex].clashesWith(), group, chain);

			// Don't recurse if we're over the energy threshold
			if (totalEnergy > lowestTotalEnergy)
				continue;

			// If not on the last dihedral angle, continue to rotate the later angles
			if (currentIndex < dihAngles.length - 1) {
				currentAngles[currentIndex] = i;
				rotateFindEnergy(currentIndex + 1, currentAngles, totalEnergy);
			}

			// If on the last dihedral angle, calculate the energy of the entire side chain
			else {
				// Save the lowest energy and the angle of rotation from the initial orientation
				if (totalEnergy < lowestTotalEnergy) {
					lowestTotalEnergy = totalEnergy;
					lowestEnergyAngles.clear();
				}
				// record the current angles as competitors for the lowest energy
				if (totalEnergy <= lowestTotalEnergy) {
					currentAngles[currentIndex] = i;
					lowestEnergyAngles.add(currentAngles.clone());
				}
			}
		}
	}

	/**
	 * Rotates a single residue in 5° increments.
	 * Calculates the energy for each rotation and finds the angle combination(s) that result in the lowest total system energy.
	 * Then, sets the structure of the amino acid to the first of those angle combinations.
	 */
	public void rotateToLowestEnergy() {
		DihedralAngle[] dihAngles = DihedralAngle.getDihedrals(group.getPDBName());

		// Rotate the amino acid and find the lowest energy angle
		// rotateFindEnergy(0, (int[]) Array.newInstance(int.class, dihAngles.length), 0);
		rotateFast(0, (int[]) Array.newInstance(int.class, dihAngles.length), 0);

		// Report the best angles of rotation and their total energy
		for (int i = 0; i < lowestEnergyAngles.size(); i++) {
			System.out.print("The best angles of rotation were -> [");
			for (int j = 0; j < dihAngles.length; j++) {
				System.out.print(lowestEnergyAngles.get(i)[j] + "°" + (j == dihAngles.length - 1 ? "" : ", "));
			}
			System.out.println("]");
		}
		System.out.println("\nThe total energy of " + (dihAngles.length > 1 ? "these rotations" : "this rotation") + " was "
				+ lowestTotalEnergy);

		// Reset the dihedral angle to its initial orientation
		int[] resetArray = new int[dihAngles.length];
		Arrays.fill(resetArray, 5);
		rotateSingleResidue(resetArray);

		// Rotate the side chain to the lowest energy angle
		rotateSingleResidue(lowestEnergyAngles.get(0));
	}
}
