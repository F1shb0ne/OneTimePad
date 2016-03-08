package ca.vire.otp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import ca.vire.otp.utils.DateTime;

/*
 * Log files keep a record of all transactions.
 * Usable for both cipher-text decodings and plain-text encodings.
 * Each transaction will begin with an empty line and a time stamp.
 * Instantiation begins with a filename to create or append to, and decoded characters are written one by one.
 */

public final class LogFile {

	private FileWriter writer;

	public LogFile(String filename) {
		try {
			writer = new FileWriter(new File(filename), true);
			writeHeader();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeChar(char c) {
		try {
			writer.write(c);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeHeader() {
		//String timestamp = ca.vire.otp.utils.DateTime();
		String timestamp = "";

		try {
			writer.write("\n" + timestamp + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
