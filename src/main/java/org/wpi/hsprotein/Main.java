package org.wpi.hsprotein;

import org.wpi.hsprotein.filemanager.ProteinManager;
import org.wpi.hsprotein.helpers.InputValidation; 
import org.wpi.hsprotein.rotations.Rotator;

import java.util.List;
import java.util.stream.Collectors;

import org.biojava.nbio.structure.Atom;
import org.biojava.nbio.structure.Chain;
import org.biojava.nbio.structure.Group;

class Main {
	public static void main(String[] args) {
		// Check if input directory exists
		String pathToInputFileDirectory = InputValidation.validateDirectory(args[0], "Invalid input directory path");
		// Check if input file exists
		String inputFileName = InputValidation.validateFile(pathToInputFileDirectory, args[1], "Invalid input file");
		// Combine into a proper file path
		String filepath = pathToInputFileDirectory + "/" + inputFileName;
		// Check if Residue ID is a valid number
		int residueID = InputValidation.validateInteger(args[2], "ResidueID");
		// Check if output directory exists
		String pathToOutputFileDirectory = InputValidation.validateDirectory(args[3], "Invalid output directory path");
		// Check if only-dipeptide tag is a valid boolean (1 / 0)
		Boolean dipeptideOnlyTag = InputValidation.validateNumericalBoolean(args[4], "dipeptideOnlyTag");


		// Avoid missing "PDB Directory" system property warning
		System.setProperty("PDB_DIR", pathToInputFileDirectory);


		// Load input file into Protein File Manager
		ProteinManager pManager = null;
		try {
			pManager = ProteinManager.load(filepath);
		}
		catch (Exception e) {
			InputValidation.handleImproperUserInput("Invalid input file type");
		}

		// Extract structure from loaded file
		for (Chain chain : pManager.getStructure().getChains()) {
			for (Group group : chain.getAtomGroups()) {
				List<Atom> atoms = group.getAtoms();

				// Hardcode bonds for Dummy PDB file
				if (args[1].equals("DummyCarbons.pdb")) {
					new BondImpl(atoms.get(0), atoms.get(1),1);
					new BondImpl(atoms.get(1), atoms.get(2),1);
					new BondImpl(atoms.get(2), atoms.get(3),1);
				}

				System.out.println("There are " + atoms.size() + " atoms in " + group.getPDBName());
				atoms.forEach(a -> System.out.println(a.getName() + " - [ " + a.getBonds() + " ]"));
				System.out.println();

				Rotator.runSingleResidue(group);
			}
		}

		// Output rotated file
		pManager.export(pathToOutputFileDirectory);
	}
}
