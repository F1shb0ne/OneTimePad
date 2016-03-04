package ca.vire.otp;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.EOFException;
import java.io.IOException;

/*
 * A source file is the input file containing either the cleartext or cyphertext to work with.
 * Characters are loaded one by one until EOFException is thrown.
 */

public class SourceFile {

	private BufferedReader reader;
	private int charsRead;
	private long fileSize;
	private File srcFile;

	public SourceFile(String filename) {
		charsRead = 0;

		try {
			srcFile = new File(filename);
			fileSize = srcFile.length();
			@SuppressWarnings({ "unused", "resource" })
			BufferedReader reader = new BufferedReader(new FileReader(srcFile));
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

		try {
			c = (char)reader.read();
		} catch (EOFException e) {
			this.close();
			throw e;			
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		return c;
	}

	public void close() {
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
