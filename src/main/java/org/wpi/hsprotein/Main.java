package org.wpi.hsprotein;
import org.wpi.hsprotein.filemanager.ProteinManager;
import org.wpi.hsprotein.rotations.Rotator;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.biojava.nbio.structure.Chain;
import org.biojava.nbio.structure.Group;
import org.biojava.nbio.structure.Atom;
import org.biojava.nbio.structure.BondImpl;

class Main {
	/** 
	 * Alerts the user of improper command line arguments in a consistent, formatted way.
	 * <b>Exits the program.<b>
	 * @param errorMessage the error message to display to the user
	 */
	private static void handleImproperUserInput(String errorMessage) {
		System.out.println("\n\033[31m" + "Error: " + errorMessage + "\033[0m");
		System.out.println("See README for example arguments and proper usage.\n");
		System.exit(1);
	}

	public static void main(String[] args) {
		// Assign command line args to named variables for readability
		String pathToInputFileDirectory = args[0];
		String inputFileName = args[1];
		String filepath = pathToInputFileDirectory + "/" + inputFileName;
		
		// Check if input directory exists
		if (!Files.exists(Paths.get(args[0])) || !Files.isDirectory(Paths.get(args[0]))) {
			handleImproperUserInput("Invalid input directory path");
		}
		// Check if input file exists
		if (!(new File(filepath).isFile())) {
			handleImproperUserInput("Invalid input file");
		}
		// Check if Residue ID is a valid number
		int residueID;
		try {
			residueID = Integer.parseInt(args[2]);
		}
		catch (NumberFormatException e) {
			handleImproperUserInput("Invalid ResidueID (not a number)");
		}
		// Check if output directory exists
		String pathToOutputFileDirectory = args[3];
		if (!Files.exists(Paths.get(args[3])) || !Files.isDirectory(Paths.get(args[3]))) {
			handleImproperUserInput("Invalid output directory path");
		}
		// Check if only-dipeptide tag is a valid boolean (1 / 0)
		Boolean dipeptideOnlyTag;
		try {
			int tempTag = Integer.parseInt(args[4]);
			if (tempTag != 0 && tempTag != 1) {
				handleImproperUserInput("Invalid dipeptideOnlyTag (must be 1 or 0)");
			}
			dipeptideOnlyTag = (tempTag == 1);
		}
		catch (NumberFormatException e) {
			handleImproperUserInput("Invalid dipeptideOnlyTag (not a number)");
		}



		// Avoid missing "PDB Directory" system property warning
		System.setProperty("PDB_DIR", pathToInputFileDirectory);



		// Load input file into Protein File Manager
		ProteinManager pManager = null;
		try {
			pManager = ProteinManager.load(filepath);
		}
		catch (Exception e) {
			handleImproperUserInput("Invalid input file type");
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
