package org.wpi.hsprotein.helpers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Utility class for validating user command-line arguments, and directory /
 * file existence.
 */
public final class InputValidation {
	private InputValidation() {
	}

	/**
	 * Alerts the user of improper command line arguments in a consistent, formatted
	 * way.
	 *
	 * <p>
	 * <b>Critical Side Effect:</b> Terminates the program and displays a printout
	 * of the error.
	 * </p>
	 *
	 * @param errorMessage the error message to display to the user
	 */
	public static void handleImproperUserInput(String errorMessage) {
		System.out.println("\n\033[31m" + "Error: " + errorMessage + "\033[0m");
		System.out.println("See README for example arguments and proper usage.\n");
		System.exit(1);
	}

	/**
	 * Validates that the given path exists and points to an existing directory.
	 *
	 * <p>
	 * <b>Critical Side Effect:</b> If validation fails, this method halts
	 * application execution via {@link #handleImproperUserInput(String)}.
	 * </p>
	 *
	 * @param directoryPath the file system path to check
	 * @param errorMessage  the error message to display if validation fails
	 * @return The verified directory path string
	 */
	public static String validateDirectory(String directoryPath, String errorMessage) {
		if (!Files.isDirectory(Paths.get(directoryPath))) {
			handleImproperUserInput(errorMessage);
		}
		return directoryPath;
	}

	/**
	 * Validates that the given path exists and points to an existing directory.
	 *
	 * @param directoryPath the file system path to check
	 * @return The verified directory path string
	 * @throws IllegalArgumentException if the directory does not exist
	 */
	public static String validateDirectory(String directoryPath) throws IllegalArgumentException {
		if (!Files.isDirectory(Paths.get(directoryPath))) {
			throw new IllegalArgumentException("Invalid directory path (" + directoryPath + ")");
		}
		return directoryPath;
	}

	/**
	 * Validates that the given file exists within the provided directory.
	 *
	 * <p>
	 * <b>Critical Side Effect:</b> If validation fails, this method halts
	 * application execution via {@link #handleImproperUserInput(String)}.
	 * </p>
	 *
	 * @param filePath     the file system path to check
	 * @param fileName     the file to check
	 * @param errorMessage the error message to display if validation fails
	 * @return The verified file name string
	 */
	public static String validateFile(String filePath, String fileName, String errorMessage) {
		try {
			if (!(new File(validateDirectory(filePath) + "/" + fileName).isFile())) {
				handleImproperUserInput(errorMessage);
			}
		} catch (IllegalArgumentException e) {
			handleImproperUserInput(e.toString());
		}
		return fileName;
	}

	/**
	 * Validates that the given file exists within the provided directory.
	 *
	 * @param filePath the file system path to check
	 * @param fileName the file to check
	 * @return The verified file name string
	 * @throws IllegalArgumentException if the directory or the file does not exist
	 */
	public static String validateFile(String filePath, String fileName) throws IllegalArgumentException {
		try {
			if (!(new File(validateDirectory(filePath) + "/" + fileName).isFile())) {
				throw new IllegalArgumentException("Invalid file (" + fileName + ")");
			}
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(e.toString());
		}
		return fileName;
	}

	/**
	 * Validates that the given string is a valid integer, and parses it.
	 *
	 * <p>
	 * <b>Critical Side Effect:</b> If validation fails, this method halts
	 * application execution via {@link #handleImproperUserInput(String)}.
	 * </p>
	 *
	 * @param numString    the string to parse into an integer
	 * @param variableName the name of the variable that the error message should
	 *                     display if validation fails
	 * @return The parsed integer value
	 */
	public static int validateInteger(String numString, String variableName) {
		int num = -1;
		try {
			num = Integer.parseInt(numString);
		} catch (NumberFormatException e) {
			handleImproperUserInput("Invalid " + variableName + " (not a number)");
		}
		return num;
	}

	/**
	 * Validates that the given string is a valid numerical binary flag (1 for true,
	 * 0 for false), and parses it.
	 *
	 * <p>
	 * <b>Critical Side Effect:</b> If validation fails, this method halts
	 * application execution via {@link #handleImproperUserInput(String)}.
	 * </p>
	 *
	 * @param bool         the string to parse into a boolean
	 * @param variableName the name of the variable that the error message should
	 *                     display if validation fails
	 * @return The parsed boolean value
	 */
	public static Boolean validateNumericalBoolean(String bool, String variableName) {
		try {
			int tempTag = Integer.parseInt(bool);
			if (tempTag != 0 && tempTag != 1) {
				handleImproperUserInput("Invalid " + variableName + " (must be 1 or 0)");
			}
			return (tempTag == 1);
		} catch (NumberFormatException e) {
			handleImproperUserInput("Invalid " + variableName + " (not a number)");
			return false;
		}
	}
}
