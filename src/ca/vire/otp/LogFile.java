package ca.vire.otp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import ca.vire.otp.utils.DateTime;

/*
 * Log files keep a record of all transactions.
 * Usable for both cipher-text decodings and plain-text encodings.
 * Each transaction will begin with an empty line and a time stamp.
 * Instantiation begins with a filename to create or append to, and decoded characters are written one by one.
 */

public final class LogFile {

	private FileWriter writer = null;

	public LogFile(String filename) throws IOException {
		File log;

		// Check if the log file already exists
		log = new File(filename);

		if (log.exists()) {
			// Open for append
			System.out.println("LogFile(): " + filename + " exists; appending");
			try {
				writer = new FileWriter(new File(filename), true);
			} catch (IOException e) {
				System.out.println("LogFile(): couldnt append to " + filename + ": " + e.getMessage());
				e.printStackTrace();
				throw e;
			}
		} else {
			// or create new file
			System.out.println("LogFile(): creating " + filename);
			try {
				writer = new FileWriter(new File(filename));
			} catch (IOException e) {
				System.out.println("LogFile(): couldnt open " + filename + ": " + e.getMessage());
				e.printStackTrace();
				throw e;
			}
		}
		writeHeader();
	}

	public void writeChar(char c) throws IOException {
		try {
			writer.write(c);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void close() {
		if (writer != null) {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void writeHeader() throws IOException {
		String timestamp = DateTime.getTimeStamp();

		try {
			writer.write("\n" + timestamp + "\n");
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

}
