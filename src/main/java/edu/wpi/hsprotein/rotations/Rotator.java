// Get rotated idiot
package edu.wpi.hsprotein.rotations;

import java.lang.reflect.Array;
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
	DihedralAngle[] dihAngles;

	double lowestTotalEnergy;
	LinkedList<int[]> lowestEnergyAngles;
	double[] allEnergy;
	int[] startAngles;
	int[] originalAngles;
	boolean cutOutEarly;

	Atom[] nearAtoms;
	DihedralAtomCache[] atomCaches;

	/**
	 * An object representing the rotation of the provided amino acid group.
	 *
	 * @param chain the protein chain containing {@code group}
	 * @param group the group of atoms to rotate
	 */
	public Rotator(Group group, Chain chain, boolean cutOutEarly) {
		this.group = group;
		this.chain = chain;
		this.cutOutEarly = cutOutEarly;
		resName = group.getPDBName();
		dihAngles = DihedralAngle.getDihedrals(resName);

		lowestTotalEnergy = Integer.MAX_VALUE;
		lowestEnergyAngles = new LinkedList<>();

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
			if (cache.a1() == null)
				throw new RuntimeException("Missing a1 " + dih.a1() + " in " + resName);
			if (cache.a2() == null)
				throw new RuntimeException("Missing a2 " + dih.a2() + " in " + resName);
			if (cache.a3() == null)
				throw new RuntimeException("Missing a3 " + dih.a3() + " in " + resName);
			if (cache.a4() == null)
				throw new RuntimeException("Missing a4 " + dih.a4() + " in " + resName);
			cacheList.add(cache);
		}
		atomCaches = cacheList.toArray(DihedralAtomCache[]::new);

		originalAngles = (int[]) Array.newInstance(int.class, atomCaches.length);
		for (int i = 0; i < atomCaches.length; ++i)
			originalAngles[i] = (int) DihedralAngle.calcAngle(atomCaches[i].a1(), atomCaches[i].a2(), atomCaches[i].a3(),
					atomCaches[i].a4());

		// Zero out the dihedral angles of the amino acid
		startAngles = (int[]) Array.newInstance(int.class, atomCaches.length);
		Arrays.fill(startAngles, 60);
		rotateToAngles(startAngles);
	}

	/**
	 * Calculates the current output of the amino acid from its "true zero" position and resets it to that value.
	 */
	public void zeroOutAminoAcid() {
		for (DihedralAtomCache cache : atomCaches) {
			RotationAxis dihedralAxis = new RotationAxis(Calc.subtract(cache.a2(), cache.a3()), cache.a3(), 0);
			dihedralAxis.rotate(cache.rotations(),
					Math.toRadians(-DihedralAngle.calcAngle(cache.a1(), cache.a2(), cache.a3(), cache.a4())));
		}
	}

	/**
	 * Rotates each dihedral angle in a single residue by {@code angle[i]}°.
	 *
	 * @param group  the group of atoms to rotate
	 * @param angles the angles to rotate each dihedral angle by, in order
	 * @throws IllegalArgumentException if the length of angles is not the same as the number of dihedral angles in the group
	 */
	public void rotateToAngles(int[] angles) throws IllegalArgumentException {
		if (atomCaches.length != angles.length) {
			throw new IllegalArgumentException(
					"There are " + atomCaches.length + " dihedral angles, but only " + angles.length
							+ " rotational angles were provided");
		}
		zeroOutAminoAcid();
		for (int i = 0; i < atomCaches.length; ++i) {
			DihedralAtomCache cache = atomCaches[i];
			RotationAxis dihedralAxis = new RotationAxis(Calc.subtract(cache.a2(), cache.a3()), cache.a3(), 0);
			dihedralAxis.rotate(cache.rotations(), Math.toRadians(angles[i]));
		}
	}

	public void restoreRotation() {
		rotateToAngles(originalAngles);
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
					int[] angles = currentAngles.clone();
					for (int j = 0; j < startAngles.length; ++j)
						angles[j] += startAngles[j];
					lowestEnergyAngles.add(angles);
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

		for (int i = 5 + startAngles[currentIndex]; i < 365 + startAngles[currentIndex]; i += 5) {
			// Rotate all connected atoms
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
				int[] angles = currentAngles.clone();
				for (int j = 0; j < angles.length; ++j)
					angles[j] = angles[j] % 360;
				lowestEnergyAngles.add(angles);
			}

			// once we get a bunch of legal angles, we assume the chain is just floppy and stop
			if (cutOutEarly && lowestEnergyAngles.size() >= 256)
				return;
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

		// Rotate the side chain to the lowest energy angle
		rotateToAngles(lowestEnergyAngles.get(0));
	}

	/**
	 * Returns the results of a rotation.
	 * 
	 * @return A {@link LinkedList} of every set of angles tied for the lowest energy.
	 */
	public LinkedList<int[]> getAngles() {
		return lowestEnergyAngles;
	}

	/**
	 * Returns the lowest energy achieved through the rotation.
	 * 
	 * @return The lowest energy.
	 */
	public double getEnergy() {
		return lowestTotalEnergy;
	}

	/**
	 * Gets the original angles before any rotation occurred.
	 * 
	 * @return The original angles.
	 */
	public int[] getOriginalAngles() {
		return originalAngles;
	}
}
