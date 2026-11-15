package edu.wpi.hsprotein;

import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashMap;

import org.biojava.nbio.structure.Chain;
import org.biojava.nbio.structure.Group;
import org.biojava.nbio.structure.ResidueNumber;

import edu.wpi.hsprotein.filemanager.ProteinManager;
import edu.wpi.hsprotein.helpers.InputValidation;
import edu.wpi.hsprotein.rotations.DihedralAngle;
import edu.wpi.hsprotein.rotations.Rotator;

class Main {
	public static int[] runResidue(Chain chain, Group residue, String chainName, String outDir) {

		// Get the residue and check that it's one we care about
		String resName = residue.getPDBName();
		System.out.println("\u001B[ARunning residue " + chainName + " " + resName + residue.getResidueNumber());
		if (DihedralAngle.getDihedrals(resName) == null)
			return null;

		// Do the actual rotations
		Rotator rotation = null;
		long start, end;
		try {
			start = System.nanoTime();
			rotation = new Rotator(residue, chain, true);
			rotation.rotateToLowestEnergy();
			end = System.nanoTime();
		} catch (RuntimeException e) {
			System.err.println("Failed to run rotations:\n" + e + "\n");
			if (rotation != null)
				return rotation.getOriginalAngles();
			return null;
		}
		rotation.restoreRotation();

		// Write data
		String statsFile = outDir + "/" + resName + ".csv";
		try (FileWriter writer = new FileWriter(statsFile, true)) {
			for (int[] result : rotation.getAngles()) {
				// chain,resnum,time,energy,angles
				String line = chainName + ","
						+ residue.getResidueNumber().toString() + ","
						+ (end - start) + ","
						+ String.valueOf(rotation.getEnergy()) + ","
						+ String.join(",", Arrays.stream(result).mapToObj(String::valueOf).toArray(String[]::new)) + "\n";
				writer.write(line);
			}
			writer.close();
		} catch (Exception e) {
			System.err.println("Failed to write statistics:\n" + e);
		}

		return rotation.getAngles().get(0);
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
		HashMap<ResidueNumber, int[]> angles = new HashMap<ResidueNumber, int[]>();
		System.out.println(""); // this is for the sake of the CUP in `runResidue()` so it doesn't overwrite anything important
		for (Group residue : proteinChain.getSeqResGroups())
			angles.put(residue.getResidueNumber(), Main.runResidue(proteinChain, residue, fileName, outDir));

		for (Group residue : proteinChain.getSeqResGroups()) {
			if (angles.get(residue.getResidueNumber()) == null)
				continue;
			try {
				Rotator rotator = new Rotator(residue, proteinChain, true);
				rotator.rotateToAngles(angles.get(residue.getResidueNumber()));
			} catch (Exception e) {
				System.out.println(
						"Failed to adjust angle for residue " + fileName + " " + residue.getPDBName() + residue.getResidueNumber()
								+ "\n" + e);
			}
		}

		// Export the structure
		pManager.export(fileName);
	}
}
