package ca.vire.otp;

import java.io.EOFException;
import ca.vire.otp.exception.PadNotDefined;
import ca.vire.otp.exception.InsufChars;

public class Crypto {

	public static void encode(Pad padFile, SourceFile source, LogFile log) throws InsufChars, PadNotDefined {
		char inChar = 0, padChar = 0, outChar = 0;
		long index = 0;
		boolean caps;

		// inform the pad file how many characters we will need
		try {
			padFile.reqLength(source.getFileSize());
		} catch (PadNotDefined e) {
			throw e;
		}

		// Keep reading source until eof is thrown
		while (true) {
			// Get a char from the source file
			try {
				inChar = source.getChar();
			} catch (EOFException e) {
				System.out.println("Crypto.encode(): reached source EOF.");
				break;
			}
			System.out.println("Crypto.encode(): src: " + Character.toString(inChar));

			// Get a char from the pad file
			try {
				padChar = padFile.getChar();
			} catch (EOFException e) {
				System.out.println("Crypto.encode(): pad file has run out of characters!");
				throw new InsufChars();
			}
			System.out.println("Crypto.encode(): pad: " + Character.toString(padChar));


		}
	}

	public static void decode(Pad padfile, SourceFile source, LogFile log) {

	}

	private boolean isCaps(char c) {
		if (c >= 'a')
			return true;

		return false;
	}

}
