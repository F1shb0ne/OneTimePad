package ca.vire.otp;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.EOFException;
import java.io.IOException;

/*
 * A source file is the input file containing either the cleartext or ciphertext to work with.
 * Characters are loaded one by one until EOFException is thrown.
 */

public final class SourceFile {

	private BufferedReader reader = null;
	private int charsRead;
	private long fileSize;
	private File srcFile;

	public SourceFile(String filename) {
		charsRead = 0;

		try {
			srcFile = new File(filename);
			fileSize = srcFile.length();
			System.out.println("SourceFile(): " + filename + ": length " + fileSize);
			reader = new BufferedReader(new FileReader(srcFile));
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}

	public int getBytesRead() {
		return charsRead;
	}

	public long getFileSize() {
		return fileSize;
	}

	public char getChar() throws EOFException {
		char c = 0;

		// BufferedReader.read() method does not throw EOFException
		if (charsRead == fileSize)
			throw new EOFException();

		try {
			c = (char)reader.read();
			++charsRead;
		} catch (IOException e) {
			System.out.println("SourceFile(): Something went wrong.");
			e.printStackTrace();
			System.exit(1);
		}

		return c;
	}

	public void close() {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
}
