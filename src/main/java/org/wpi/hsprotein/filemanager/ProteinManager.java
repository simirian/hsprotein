package org.wpi.hsprotein.filemanager;

import org.wpi.hsprotein.helpers.InputValidation;

import org.biojava.nbio.structure.Structure;

public interface ProteinManager {
	static ProteinManager load(String filepath) {
		// Validate inputted file path and file name
		try {
			String path = (filepath.lastIndexOf('/') >= 0) ? filepath.substring(0, filepath.lastIndexOf('/')) : "";
			String file = (filepath.lastIndexOf('/') >= 0) ? filepath.substring(filepath.lastIndexOf('/') + 1) : filepath;
			InputValidation.validateFile(path, file);
		} catch (IllegalArgumentException e) {
			return null;
		}

		String fileType = filepath.substring(filepath.lastIndexOf('.') + 1);
		return switch (fileType) {
			case "pdb" -> new PDBManager(filepath);
			case "cif" -> new CIFManager(filepath);
			default    -> throw new IllegalArgumentException("Unknown filetype: " + fileType);
		};
	}

	public Structure getStructure();
	public void export(String outputDirectory);
}
