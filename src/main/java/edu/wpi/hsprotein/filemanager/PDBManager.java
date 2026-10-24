package edu.wpi.hsprotein.filemanager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.biojava.nbio.structure.io.FileParsingParameters;
import org.biojava.nbio.structure.io.PDBFileReader;
import org.biojava.nbio.structure.Structure;

/**
 * Public interface that handles file loading, structuring, and outputting independent of file type.
 * All file-type-specific actions are handled by package-private implementing classes.
 * Handles {@code .pdb} files.
 */
class PDBManager implements ProteinManager {
	String input_filepath = "";
	String output_directory = "";
	PDBFileReader pdbReader = new PDBFileReader();
	FileParsingParameters fpp = new FileParsingParameters();
	Structure struct;

	PDBManager(String input_filepath, String output_directory) {
		this.input_filepath = input_filepath;
		this.output_directory = output_directory;
		fpp.setCreateAtomBonds(true);
		pdbReader.setFileParsingParameters(fpp);
	}

	@Override
	public Structure getStructure() {
		try {
			struct = pdbReader.getStructure(input_filepath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return struct;
	}

	@Override
	public void export(String residueName) {
		// Find filename that doesn't exist in outputDirectory
		int num = 1;
		String structName = "rotated_" + (residueName != "" ? residueName : (struct.getName().equals("") ? "PDB_Protein" : struct.getName()));
		String filename = structName + ".pdb";
		File file = new File(output_directory, filename);
		while (file.exists()) {
			filename = structName + "_" + (num++) + ".pdb";
			file = new File(output_directory, filename);
		}

		// Write to PDB file
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(struct.toPDB());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
