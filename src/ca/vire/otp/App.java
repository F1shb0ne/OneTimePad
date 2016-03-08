package ca.vire.otp;

import ca.vire.otp.exception.*;

public class App {

	public static void main(String[] args) {
		boolean isEncoding = false, isDecoding = false;
		int argc = args.length;
		String encodePadFilename = null, decodePadFilename = null;
		String plainTextFilename = null, cipherTextFilename = null;
		
		SourceFile src;
		LogFile log;
		
		DotFile df;
		
		// Load in existing dot file if there is one, or create new one
		df = new DotFile();

		// Process command line arguments
		if (argc > 0 ) {
			for (int i = 0; i < argc; ++i) {
				if (args[i].equalsIgnoreCase("-pe")) {
					// Set encode pad file
					encodePadFilename = args[++i];
					System.out.println("Using " + encodePadFilename + " as encode pad file.");
					df.setEncodeFile(encodePadFilename);
					continue;
				}
				if (args[i].equalsIgnoreCase("-pd")) {
					// Set decode pad file
					decodePadFilename = args[++i];
					System.out.println("Using " + decodePadFilename + " as decode pad file.");
					df.setEncodeFile(decodePadFilename);
					continue;					
				}
				if (args[i].equals("-e")) {
					// indicate source file to encode
					plainTextFilename = args[++i];
					isEncoding = true;
					continue;
				}
				if (args[i].equals("-d")) {
					// indicate source file to decode
					cipherTextFilename = args[++i];
					isDecoding = true;
					continue;
				}
			}
		}
		
		if ((isEncoding | isDecoding) == false) {
			System.out.println("Nothing to encode or decode; use -e or -d");
		} else if (isEncoding || isDecoding) {
			if (isEncoding) {
				src = new SourceFile(plainTextFilename);
				log = new LogFile(df.getCipherTextLogFile());
				try {
					Crypto.encode(df.getEncodePad(), src, log);
				} catch (PadNotDefined e) {
					System.out.println("App.main(): pad file for encoding was not defined.");
				} catch (InsufChars e) {
					System.out.println("App.main(): pad file ran out of characters.");
				}
				src.close();
				log.close();
			}			
			if (isDecoding) {
				src = new SourceFile(cipherTextFilename);
				log = new LogFile(df.getClearTextLogFile());
				
				src.close();
				log.close();
			}
		}
				
		System.out.println("Terminating...");
		df.close();
	}
}
