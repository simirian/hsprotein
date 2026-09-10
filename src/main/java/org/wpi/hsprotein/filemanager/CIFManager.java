package org.wpi.hsprotein.filemanager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.biojava.nbio.structure.io.CifFileReader;
import org.biojava.nbio.structure.Structure;

class CIFManager implements ProteinManager {
	String filepath = "";
	CifFileReader cifReader = new CifFileReader();
	Structure struct;

	CIFManager(String filepath) {
		this.filepath = filepath;
	}

	@Override public Structure getStructure() {
		try {
			struct = cifReader.getStructure(filepath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return struct;
	}

	@Override public void export(String outputDirectory) {
		// Find filename that doesn't exist in outputDirectory
		int num = 0;
		String structName = struct.getName().equals("") ? "rotatedProtein" : struct.getName();
		String filename = structName + ".cif";
		File file = new File(outputDirectory, filename);
		while(file.exists()) {
			filename = structName + (num++) + ".cif";
			file = new File(outputDirectory, filename);
		}

		// Write to CIF file
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(struct.toMMCIF());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
