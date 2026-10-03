package edu.wpi.hsprotein.filemanager;

import edu.wpi.hsprotein.helpers.InputValidation;

import org.biojava.nbio.structure.Structure;

/**
 * Public interface that handles file loading, structuring, and outputting independent of file type.
 * All file-type-specific actions are handled by package-private implementing classes.
 */
public interface ProteinManager {

	/**
	 * Loads a ProteinManager instance based on the file extension.
	 *
	 * @param input_filepath the path / name of the file to load
	 * @param output_directory the directory with which to output the file to
	 * @return A concrete ProteinManager (like {@link PDBManager} or {@link CIFManager}), or {@code null} if the file path / name is invalid
	 * @throws IllegalArgumentException if the file extension is not supported
	 */
	static ProteinManager load(String input_filepath, String output_directory) {
		// Validate inputted file path and file name
		try {
			String path = (input_filepath.lastIndexOf('/') >= 0) ? input_filepath.substring(0, input_filepath.lastIndexOf('/')) : "";
			String file = (input_filepath.lastIndexOf('/') >= 0) ? input_filepath.substring(input_filepath.lastIndexOf('/') + 1) : input_filepath;
			InputValidation.validateFile(path, file);
		} catch (IllegalArgumentException e) {
			return null;
		}

		String fileType = input_filepath.substring(input_filepath.lastIndexOf('.') + 1);
		return switch (fileType) {
			case "pdb" -> new PDBManager(input_filepath, output_directory);
			case "cif" -> new CIFManager(input_filepath, output_directory);
			default -> throw new IllegalArgumentException("Unknown filetype: " + fileType);
		};
	}

	/**
	 * Returns the BioJava Structure object generated from the loaded file path.
	 *
	 * @return The {@link Structure} object, or {@code null} if the file cannot be read or parsed successfully
	 */
	public Structure getStructure();

	/**
	 * Exports the modified structure data to the provided output directory.
	 *
	 * @param residueName the name of the rotated residue
	 */
	public void export(String residueName);
}
