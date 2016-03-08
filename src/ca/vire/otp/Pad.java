package ca.vire.otp;

import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.EOFException;

import ca.vire.otp.exception.InsufChars;
import ca.vire.otp.exception.PadNotDefined;

/*
 * A Pad object contains the filename of a pad file.
 * It's purpose is to return characters in the pad file and keep track
 * of where it's pulling them from.
 */
public final class Pad {	
	private String pwd = System.getProperty("user.dir");
	private String padFilename = null;
	private String padFileURI = null;
	private RandomAccessFile raf;
	private long charsRead = 0;
	
	public long fileLength;
	public long padIndex;
	public boolean isDefined = false;
	
	/*
	 * Open the pad file and move seek position to where it last was.
	 */
	public Pad(String padfilename, long index) {
		padFilename = padfilename;
		//padFileURI = pwd + "/" + padFilename;
		padFileURI = padfilename;
		padIndex = index;
		
		try {
			raf = new RandomAccessFile(new File(padFileURI), "r");
			fileLength = raf.length();
			isDefined = true;
			raf.seek(padIndex);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();			
			System.exit(1);
		}
	}
	
	/*
	 * create a temporary file containing all the characters needed specified by
	 * the lengthNeeded argument. This is done because otp only operates on the
	 * letters a-z: all other characters are ignored. 
	 */
	public void prepChars(long lengthNeeded) throws InsufChars, PadNotDefined {
		long i = 0;
		int c = 0;
		FileWriter writer = null;
		
		String tempFile = padFileURI + ".tmp";
		
		// Open the temporary file
		try {
			writer = new FileWriter(new File(tempFile));
		} catch (IOException e) {			
			e.printStackTrace();
			System.exit(1);
		}

		// 
		do {
			try {
				c = getChar();
			} catch (PadNotDefined e) {
				throw e;
			} catch (EOFException e) {
				throw new InsufChars();
			}

			try {
				if (isLetter(c)) {
					writer.write(toUCase(c));
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			
		} while (++i < lengthNeeded);
		
		try {
			writer.close();
		} catch (IOException e) {
			
		}
		
		
	}
	
	private boolean isLetter(int c) {
		if ((c >= 65 && c <= 90) || (c >= 97 && c <= 122))
			return true;
		
		return false;
	}
	
	// Assumes given value already passes isLetter()
	private int toUCase(int c) {
		if (c >= 97)
			return c - 32;
		
		return c;
	}
	
	// Assumes given value already passes isLetter()
	private boolean isUCase(int c) {
		if (c <= 90)
			return true;
		
		return false;
	}
	
	/*
	 * Read one character from the pad file.
	 * Does not touch the padIndex
	 */
	public char getChar() throws EOFException, PadNotDefined {
		long charsRead = 0;
		char c = 0;

		// Bail immediately if nothing can be read
		if (!isDefined)
			throw new PadNotDefined();
		
		try {
			c = raf.readChar();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return c;
	}

}
