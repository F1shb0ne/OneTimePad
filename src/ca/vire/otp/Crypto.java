package ca.vire.otp;

import java.io.EOFException;
import ca.vire.otp.exception.PadNotDefined;
import ca.vire.otp.exception.InsufChars;

public class Crypto {

	public static void encode(Pad padfile, SourceFile source, LogFile log) throws PadNotDefined, InsufChars {
		char inChar = 0, padChar = 0, outChar = 0;
		boolean run = true;
		
		do {
			try {
				// Get a char from the source file
				inChar = source.getChar();
			} catch (EOFException e) {
				System.out.println("Source EOF Reached.");
			} 
			System.out.println("encode(): source: " + inChar);
				
			try {
				// Get a char from the pad file
				padChar = padfile.getChar();
			} catch (PadNotDefined e) {				
				throw e;
			} catch (EOFException e) {
				System.out.println("Crypto.encode(): pad file has run out of characters.");
				throw new InsufChars();
			}
			
			System.out.println("encode(): pad " + padChar);
			
		} while (run);			
	}
	
	public static void decode(Pad padfile, SourceFile source, LogFile log) {
		
	}
	
}
