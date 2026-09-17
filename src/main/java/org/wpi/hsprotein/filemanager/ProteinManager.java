package org.wpi.hsprotein.filemanager;

import org.wpi.hsprotein.helpers.InputValidation;

import org.biojava.nbio.structure.Structure;

/**
 * Public interface that handles file loading, structuring, and outputting
 * independent of file type.
 * All file-type-specific actions are handled by package-private implementing
 * classes.
 */
public interface ProteinManager {

	/**
	 * Loads a ProteinManager instance based on the file extension.
	 *
	 * @param filepath the path / name of the file to load
	 * @return A concrete ProteinManager (like {@link PDBManager} or
	 *         {@link CIFManager}), or {@code null} if the file path / name is
	 *         invalid
	 * @throws IllegalArgumentException if the file extension is not supported
	 */
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
			default -> throw new IllegalArgumentException("Unknown filetype: " + fileType);
		};
	}

	/**
	 * Returns the BioJava Structure object generated from the loaded file path.
	 *
	 * @return The {@link Structure} object, or {@code null} if the file cannot be
	 *         read or parsed successfully
	 */
	public Structure getStructure();

	/**
	 * Exports the modified structure data to the provided output directory.
	 *
	 * @param outputDirectory the directory with which to output the file to
	 */
	public void export(String outputDirectory);
}
