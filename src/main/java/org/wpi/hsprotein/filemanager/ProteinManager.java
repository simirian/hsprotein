package org.wpi.hsprotein.filemanager;

import org.biojava.nbio.structure.Structure;

public interface ProteinManager {
	static ProteinManager load(String filename) {
		if (filename == null) return null;

		String fileType = filename.substring(filename.lastIndexOf('.') + 1);
		return switch (fileType) {
			case "pdb" -> new PDBManager(filename);
			case "cif" -> new CIFManager(filename);
			default    -> throw new IllegalArgumentException("Unknown filetype: " + fileType);
		};
	}

	public Structure getStructure();
	public void export(String outputDirectory);
}
