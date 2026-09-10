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

		// Convert back to PDB file for testing
		pManager.export(args[3]);
	}
}
