package ca.vire.otp;

import java.io.FileNotFoundException;
import java.io.IOException;

import ca.vire.otp.exception.*;

public class App {

	private static DotFile dotFile = null;
	private static SourceFile src = null;
	private static LogFile log = null;

	public static void main(String[] args) {
		boolean isEncoding = false, isDecoding = false;
		int argc = args.length;
		String encodePadFilename = null, decodePadFilename = null;
		String plainTextFilename = null, cipherTextFilename = null;


		// Load in existing dot file if there is one, or create new one
		try {
			dotFile = new DotFile();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
		}

		// Process command line arguments
		if (argc > 0 ) {
			for (int i = 0; i < argc; ++i) {
				if (args[i].equalsIgnoreCase("-pe")) {
					// Set encode pad file
					encodePadFilename = args[++i];
					System.out.println("Using " + encodePadFilename + " as encode pad file.");
					try {
						dotFile.setEncodePad(encodePadFilename, 0);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
						Bail();
					}
					continue;
				}
				if (args[i].equalsIgnoreCase("-pd")) {
					// Set decode pad file
					decodePadFilename = args[++i];
					System.out.println("Using " + decodePadFilename + " as decode pad file.");
					try {
						dotFile.setDecodePad(decodePadFilename, 0);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
						Bail();
					}
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
				log = dotFile.getCipherTextLog();
				try {
					Crypto.encode(dotFile.getEncodePad(), src, log);
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
				log = dotFile.getClearTextLog();

				src.close();
				log.close();
			}
		}

		Bail();
	}

	// for normal program termination or should something go wrong, clean up here.
	private static void Bail() {
		System.out.println("Terminating...");

		if (dotFile != null)
			dotFile.close();

		if (src != null)
			dotFile.close();

		if (log != null)
			log.close();
	}

}
