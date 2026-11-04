package edu.wpi.hsprotein;

import java.io.FileWriter;
import java.util.Arrays;

import org.biojava.nbio.structure.Atom;
import org.biojava.nbio.structure.Chain;
import org.biojava.nbio.structure.Group;

import edu.wpi.hsprotein.filemanager.ProteinManager;
import edu.wpi.hsprotein.helpers.InputValidation;
import edu.wpi.hsprotein.rotations.DihedralAngle;
import edu.wpi.hsprotein.rotations.Rotator;

class Main {
	public static void runResidue(Chain chain, int id, String chainName, String outDir) {

		// Get the residue and check that it's one we care about
		Group residue = chain.getSeqResGroup(id);
		String resName = residue.getPDBName();
		System.out.println("Running residue " + chainName + " " + resName + residue.getResidueNumber());
		if (DihedralAngle.getDihedrals(resName) == null)
			return;

		// Do the actual rotations
		Rotator rotation;
		long start, end;
		try {
			start = System.nanoTime();
			rotation = new Rotator(residue, chain);
			rotation.rotateToLowestEnergy();
			end = System.nanoTime();
		} catch (RuntimeException e) {
			System.err.println("Failed to run rotations:\n" + e);
			return;
		}

		// Write data
		String statsFile = outDir + "/" + resName + ".csv";
		FileWriter writer;
		try {
			writer = new FileWriter(statsFile, true);
		} catch (Exception e) {
			System.err.println("Failed to write statistics:\n" + e);
			return;
		}

		for (int[] result : rotation.getAngles()) {
			// chain,resnum,time,energy,angles
			String line = chainName + ","
					+ String.valueOf(id) + ","
					+ (end - start) + ","
					+ String.valueOf(rotation.getEnergy()) + ","
					+ String.join(",", Arrays.stream(result).mapToObj(String::valueOf).toArray(String[]::new)) + "\n";

			try {
				writer.write(line);
			} catch (Exception e) {
				System.err.println("Failed to write statistics:\n" + e);
				return;
			}
		}

		try {
			writer.close();
		} catch (Exception e) {
			System.err.println("Failed to close writer...?\n" + e);
			return;
		}
	}

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Requires two arguments: a structure file and an output directory.");
			return;
		}
		// Check file
		String file = args[0];
		int i = file.lastIndexOf('/');
		String fileDirectory = i == -1 ? "./" : file.substring(0, i);
		String fileName = i == 1 ? file : file.substring(file.lastIndexOf("/") + 1);
		InputValidation.validateFile(fileDirectory, fileName, "Invalid input file");
		// Check if output directory exists
		String outDir = InputValidation.validateDirectory(args[1], "Invalid output directory path");

		// Avoid missing "PDB Directory" system property warning
		if (System.getProperty("PDB_DIR") == null)
			System.setProperty("PDB_DIR", fileDirectory);

		// Load input file into Protein File Manager
		ProteinManager pManager = null;
		try {
			pManager = ProteinManager.load(file, outDir);
		} catch (Exception e) {
			InputValidation.handleImproperUserInput("Invalid input file type");
		}

		// Check for valid protein structure
		if (pManager.getStructure().getChains().size() < 1)
			InputValidation.handleImproperUserInput("No valid chains");

		// Rotate every residue in the structure
		Chain proteinChain = pManager.getStructure().getChains().get(0);
		for (int j = 0; j < proteinChain.getSeqResLength(); ++j)
			Main.runResidue(proteinChain, j, fileName, outDir);

		// Export the structure
		pManager.export(fileName);
	}
}
