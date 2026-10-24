package edu.wpi.hsprotein;

import edu.wpi.hsprotein.filemanager.ProteinManager;
import edu.wpi.hsprotein.helpers.InputValidation;
import edu.wpi.hsprotein.rotations.Rotator;

import java.util.stream.Collectors;

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
		// Check if Residue ID is a valid number and fix off-by-one errors (base it on the CIF 1-based indexing)
		int residueID = InputValidation.validateInteger(args[2], "ResidueID") - 1;
		// Check if output directory exists
		String pathToOutputFileDirectory = InputValidation.validateDirectory(args[3], "Invalid output directory path");
		// Check if get energy tag is a valid boolean (1 / 0)
		Boolean getEnergyTag = InputValidation.validateNumericalBoolean(args[4], "getEnergyTag");

		// Avoid missing "PDB Directory" system property warning
		if (System.getProperty("PDB_DIR") == null) {
			System.setProperty("PDB_DIR", pathToInputFileDirectory);
		}

		// Load input file into Protein File Manager
		ProteinManager pManager = null;
		try {
			pManager = ProteinManager.load(filepath, pathToOutputFileDirectory);
		} catch (Exception e) {
			InputValidation.handleImproperUserInput("Invalid input file type");
		}

		// Check for valid protein structure
		if (pManager.getStructure().getChains().size() < 1) {
      InputValidation.handleImproperUserInput("No valid chains");
    }

		// Extract structure from loaded file
		Chain proteinChain = pManager.getStructure().getChains().get(0);
		Group aminoAcid = proteinChain.getSeqResGroup(residueID);
		
		System.out.println("\n" + aminoAcid.getPDBName() + ": " + aminoAcid.getAtoms().stream().map(a -> a.getName()).collect(Collectors.joining(", ")) + "\n");

		// Rotate the provided residue to its lowest energy position
		Rotator rotation = new Rotator(aminoAcid, proteinChain, getEnergyTag);
		rotation.rotateToLowestEnergy();

		// Output rotated file
		pManager.export(aminoAcid.getPDBName() + (residueID + 1));
	}
}
