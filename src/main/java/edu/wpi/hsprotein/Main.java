package edu.wpi.hsprotein;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.biojava.nbio.structure.Chain;
import org.biojava.nbio.structure.Group;
import org.biojava.nbio.structure.ResidueNumber;
import org.biojava.nbio.structure.asa.AsaCalculator;
import org.biojava.nbio.structure.asa.GroupAsa;

import edu.wpi.hsprotein.filemanager.ProteinManager;
import edu.wpi.hsprotein.helpers.InputValidation;
import edu.wpi.hsprotein.rotations.DihedralAngle;
import edu.wpi.hsprotein.rotations.Rotator;

class Main {
	public static void saveResData(String fileName, String chainName, String resNum, long time, Rotator rotation) {
		try (FileWriter writer = new FileWriter(fileName, true)) {
			for (int[] result : rotation.getAngles()) {
				// chain,resnum,time,energy,angles
				String line = chainName + "," + resNum + "," + time + ","
						+ String.valueOf(rotation.getEnergy()) + ","
						+ String.join(",", Arrays.stream(result).mapToObj(String::valueOf).toArray(String[]::new)) + "\n";
				writer.write(line);
			}
		} catch (Exception e) {
			System.err.println("Failed to write statistics:\n" + e);
		}
	}

	public static void recordResError(String fileName, String chainName, String resName, String resNum) {
		try (FileWriter writer = new FileWriter(fileName, true)) {
			// chain,resname,resid,
			String line = chainName + "," + resName + "," + resNum + "," + "\n";
			writer.write(line);
		} catch (Exception e) {
			System.err.println("Failed to write statistics:\n" + e);
		}
	}

	public static int[] runResidue(Chain chain, Group residue, String chainName, String outDir) {
		// Get the residue and check that it's one we care about
		String resName = residue.getPDBName();
		if (residue.getResidueNumber() == null)
			return null;
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
			Main.recordResError(outDir + "/" + "missing.csv", chainName, resName.toLowerCase(), residue.getResidueNumber().toString());
			if (rotation != null)
				return rotation.getOriginalAngles();
			return null;
		}
		rotation.restoreRotation();
		Main.saveResData(outDir + "/" + resName.toLowerCase() + ".csv", chainName, residue.getResidueNumber().toString(), end - start,
				rotation);

		return rotation.getAngles().get(0);
	}

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Requires two arguments: a structure file and an output directory.");
			return;
		}
		// Check file
		String file = args[0];
		int slashIndex = file.lastIndexOf('/');
		String fileDirectory = slashIndex == -1 ? "./" : file.substring(0, slashIndex);
		String fileName = slashIndex == 1 ? file : file.substring(file.lastIndexOf("/") + 1);
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

		// Get residues to rotate
		// 960 probes is from https://boscoh.com/protein/calculating-the-solvent-accessible-surface-area-asa.html
		// 1.4 Å radius is from google, I could not find a source :(
		AsaCalculator asaCalculator = new AsaCalculator(pManager.getStructure(), 1.4, 960, 4, false);
		GroupAsa[] asas = asaCalculator.getGroupAsas();
		Chain proteinChain = pManager.getStructure().getChains().get(0);
		List<Group> groups = proteinChain.getSeqResGroups();
		ArrayList<Group> buriedGroups = new ArrayList<>(asas.length);
		for (int i = 0; i < asas.length; ++i)
			if (asas[i].getRelativeAsaU() < 0.2)
				buriedGroups.add(groups.get(i));
		buriedGroups.trimToSize();

		// Rotate every residue in the structure
		HashMap<ResidueNumber, int[]> angles = new HashMap<ResidueNumber, int[]>();
		System.out.println(""); // this is for the sake of the CUP in `runResidue()` so it doesn't overwrite anything important
		for (Group residue : buriedGroups)
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
