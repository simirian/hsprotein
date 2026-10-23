// Get rotated idiot
package edu.wpi.hsprotein.rotations;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import org.biojava.nbio.structure.Atom;
import org.biojava.nbio.structure.Calc;
import org.biojava.nbio.structure.Chain;
import org.biojava.nbio.structure.Group;
import org.biojava.nbio.structure.align.util.RotationAxis;

/**
 * InnerRotator
 */
final record DihedralAtomCache(
		Atom a1, Atom a2, Atom a3, Atom a4,
		Atom[] rotations,
		Atom[] mightClash,
		Atom[] clashesWith) {
}

/**
 * A class containing all rotational operations. Will be greatly expanded in the future.
 */
public class Rotator {
	Group group;
	Chain chain;
	String resName;
	Boolean shouldSaveEnergy;
	DihedralAngle[] dihAngles;

	double lowestTotalEnergy;
	ArrayList<int[]> lowestEnergyAngles;
	double[] allEnergy;

	Atom[] nearAtoms;
	DihedralAtomCache[] atomCaches;

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
		resName = group.getPDBName();
		shouldSaveEnergy = saveEnergy;
		dihAngles = DihedralAngle.getDihedrals(resName);

		lowestTotalEnergy = Integer.MAX_VALUE;
		lowestEnergyAngles = new ArrayList<>();

		// Only save energy if necessary due to storage size
		// if (saveEnergy) {
		// 	if (dihAngles.length > 5) {
		// 		System.out.println("Cannot save all energy outputs for each dihedral angle due to storage capacity issues.");
		// 	} else {
		// 		System.out.println("going to blow the heap with " + dihAngles.length + " angles");
		// 		allEnergy = new double[(int) Math.pow(360 / 5, dihAngles.length)];
		// 	}
		// }

		// Get the nearby atoms to generate the atom cache
		Atom center = group.getAtom("CB");
		if (center == null)
			throw new RuntimeException("Residue missing CB!");
		double radius = DihedralAngle.getRadius(resName);
		nearAtoms = CalcEnergy.getNearAtoms(center, radius, chain);

		// Generate the atom cache for this residue
		LinkedList<DihedralAtomCache> cacheList = new LinkedList<DihedralAtomCache>();
		for (DihedralAngle dih : dihAngles) {
			LinkedList<Atom> clashesWith = new LinkedList<Atom>();
			// We can clash with only nearby atoms from other residues, or ones from this residue which are in the clashesWith list
			for (Atom atom : nearAtoms)
				if (atom.getGroup().getResidueNumber() != group.getResidueNumber()
						|| Arrays.stream(dih.clashesWith()).anyMatch(atom.getName()::equals))
					clashesWith.add(atom);
			DihedralAtomCache cache = new DihedralAtomCache(
					group.getAtom(dih.a1()), group.getAtom(dih.a2()), group.getAtom(dih.a3()), group.getAtom(dih.a4()),
					Arrays.stream(dih.rotations()).map(e -> group.getAtom(e)).filter(e -> e != null).toArray(Atom[]::new),
					Arrays.stream(dih.mightClash()).map(e -> group.getAtom(e)).filter(e -> e != null).toArray(Atom[]::new),
					clashesWith.toArray(Atom[]::new));
			cacheList.add(cache);
		}
		atomCaches = cacheList.toArray(DihedralAtomCache[]::new);

		// Zero out the dihedral angles of the amino acid
		zeroOutAminoAcid();
	}

	/**
	 * Calculates the current output of the amino acid from its "true zero" position
	 * and resets it to that value.
	 */
	private void zeroOutAminoAcid() {
		for (DihedralAtomCache cache : atomCaches) {
			RotationAxis dihedralAxis = new RotationAxis(Calc.subtract(cache.a2(), cache.a3()), cache.a3(), 0);
			dihedralAxis.rotate(cache.rotations(),
					Math.toRadians(DihedralAngle.calcAngle(cache.a1(), cache.a2(), cache.a3(), cache.a4())));
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
		if (atomCaches.length != angles.length) {
			throw new IllegalArgumentException("There are " + atomCaches.length + " dihedral angles, but only " + angles.length
					+ " rotational angles were provided");
		}

		for (int i = 0; i < atomCaches.length; ++i) {
			DihedralAtomCache cache = atomCaches[i];
			RotationAxis dihedralAxis = new RotationAxis(Calc.subtract(cache.a2(), cache.a3()), cache.a3(), 0);
			dihedralAxis.rotate(cache.rotations(), Math.toRadians(angles[i]));
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
		Atom atom1 = atomCaches[currentIndex].a2();
		Atom atom2 = atomCaches[currentIndex].a3();
		RotationAxis dihedralAxis = new RotationAxis(Calc.subtract(atom1, atom2), atom2, 0);

		for (int i = 0; i < 360; i += 5) {
			// Rotate all connected atoms
			if (i != 0)
				dihedralAxis.rotate(atomCaches[currentIndex].rotations(), Math.toRadians(5));

			// Calculate energy of the current dihedral angle
			double totalEnergy = currentEnergy;
			for (Atom clashAtom : atomCaches[currentIndex].mightClash())
				totalEnergy += CalcEnergy.calculateEnergy(clashAtom, atomCaches[currentIndex].clashesWith());

			// If not on the last dihedral angle, continue to rotate the later angles
			if (currentIndex < atomCaches.length - 1) {
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
		Atom atom1 = atomCaches[currentIndex].a2();
		Atom atom2 = atomCaches[currentIndex].a3();
		RotationAxis dihedralAxis = new RotationAxis(Calc.subtract(atom1, atom2), atom2, 0);

		for (int i = 0; i < 360; i += 5) {
			// Rotate all connected atoms
			if (i != 0)
				dihedralAxis.rotate(atomCaches[currentIndex].rotations(), Math.toRadians(5));

			// Calculate energy of the current dihedral angle
			double totalEnergy = currentEnergy;
			for (Atom clashAtom : atomCaches[currentIndex].mightClash())
				totalEnergy += CalcEnergy.calculateEnergy(clashAtom, atomCaches[currentIndex].clashesWith());

			// Don't recurse if we're over the energy threshold
			if (totalEnergy > lowestTotalEnergy)
				continue;

			// If not on the last dihedral angle, continue to rotate the later angles
			if (currentIndex < atomCaches.length - 1) {
				currentAngles[currentIndex] = i;
				rotateFast(currentIndex + 1, currentAngles, totalEnergy);
			}

			// If on the last dihedral angle, calculate the energy of the entire side chain
			else {
				// Save the lowest energy and the angle of rotation from the initial orientation
				if (totalEnergy < lowestTotalEnergy) {
					lowestTotalEnergy = totalEnergy;
					lowestEnergyAngles.clear();
				}
				// Record the current angles as competitors for the lowest energy
				// (the check above makes sure we aren't over the lowest when we get here)
				currentAngles[currentIndex] = i;
				lowestEnergyAngles.add(currentAngles.clone());
			}
		}
	}

	/**
	 * Rotates a single residue in 5° increments.
	 * Calculates the energy for each rotation and finds the angle combination(s) that result in the lowest total system energy.
	 * Then, sets the structure of the amino acid to the first of those angle combinations.
	 */
	public void rotateToLowestEnergy() {
		// Rotate the amino acid and find the lowest energy angle
		rotateFast(0, (int[]) Array.newInstance(int.class, atomCaches.length), 0);

		// Report the best angles of rotation and their total energy
		for (int i = 0; i < lowestEnergyAngles.size(); i++) {
			System.out.print("The best angles of rotation were -> [");
			for (int j = 0; j < atomCaches.length; j++) {
				System.out.print(lowestEnergyAngles.get(i)[j] + "°" + (j == atomCaches.length - 1 ? "" : ", "));
			}
			System.out.println("]");
		}
		System.out.println("\nThe total energy of " + (atomCaches.length > 1 ? "these rotations" : "this rotation") + " was "
				+ lowestTotalEnergy);

		// Reset the dihedral angle to its initial orientation
		int[] resetArray = new int[atomCaches.length];
		Arrays.fill(resetArray, 5);
		rotateSingleResidue(resetArray);

		// Rotate the side chain to the lowest energy angle
		rotateSingleResidue(lowestEnergyAngles.get(0));
	}
}
