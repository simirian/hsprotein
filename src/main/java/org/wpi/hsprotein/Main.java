package org.wpi.hsprotein;
import org.wpi.hsprotein.filemanager.ProteinManager;
import org.wpi.hsprotein.rotations.Rotator;

import java.util.List;

import org.biojava.nbio.structure.Chain;
import org.biojava.nbio.structure.Group;
import org.biojava.nbio.structure.Atom;
// import org.biojava.nbio.structure.BondImpl;

class Main {
	public static void main(String[] args) {
		// Avoid missing system PDB Directory property warning
		System.setProperty("PDB_DIR", args[0]);

		// Input Validation
		String filepath = args[0] + "/" + args[1];

		ProteinManager pManager = ProteinManager.load(filepath);

		for (Chain chain : pManager.getStructure().getChains()) {
			for (Group group : chain.getAtomGroups()) {
				List<Atom> atoms = group.getAtoms();

				// Hardcode bonds for Dummy PDB file
				// new BondImpl(atoms.get(0), atoms.get(1),1);
				// new BondImpl(atoms.get(1), atoms.get(2),1);
				// new BondImpl(atoms.get(2), atoms.get(3),1);

				System.out.println("There are " + atoms.size() + " atoms in " + group.getPDBName());
				atoms.forEach(a -> System.out.println(a.getName() + " - [ " + a.getBonds() + " ]"));
				System.out.println();

				Rotator.runSingleResidue(group);
			}
		}

		// Convert back to PDB file for testing
		pManager.export(args[3]);
	}
}
