package ca.vire.otp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DotFile {

	private String pwd = System.getProperty("user.dir");
	private String dotFilename = pwd + "/.otp";
	private String clearTextLogFile = "cleartext.log"; // Where decoded material is written by default
	private String cipherTextLogFile = "ciphertext.log"; // same for encoded
	private String encodePadFilename;
	private String decodePadFilename;
	private long encodePadIndex;
	private long decodePadIndex;
	private Pad encodePad = null;
	private Pad decodePad = null;
	
	public boolean isEncodeSet = false;
	public boolean isDecodeSet = false;
	
	public DotFile() {
		// check to see if dotfile already exists
		readDotFile();
	}
	
	public Pad getEncodePad() {
		if (encodePad == null && encodePadFilename != null) {			
			return new Pad(encodePadFilename, encodePadIndex);			
		} else if (encodePad != null) {
			return encodePad;			
		}
		
		return null;
	}
	
	public Pad getDecodePad() {
		if (decodePad == null && decodePadFilename != null) {			
			return new Pad(decodePadFilename, decodePadIndex);			
		} else if (decodePad != null) {
			return decodePad;			
		}
		
		return null;
	}
	
	public void setEncodeIndex(long index) {
		if (isEncodeSet) {
			encodePadIndex = index;
		}
	}
	
	public void setDecodeIndex(long index) {
		if (isDecodeSet) {
			decodePadIndex = index;
		}
	}
	
	public void setEncodeFile(String encodeFilename) {
		encodePadFilename = encodeFilename;
		encodePadIndex = 0;
		isEncodeSet = true;
	}
	
	public void setDecodeFile(String decodeFilename) {
		decodePadFilename = decodeFilename;
		decodePadIndex = 0;
		isDecodeSet = true;
	}
	
	public String getClearTextLogFile() {
		return clearTextLogFile;
	}
	
	public String getCipherTextLogFile() {
		return cipherTextLogFile;
	}
	
	public void close() {
		writeDotFile();
	}
	
	private void writeDotFile() {
		BufferedWriter dotfile;
		
		if (encodePadFilename != null || decodePadFilename != null) {
			try {
				dotfile = new BufferedWriter(new FileWriter(new File(dotFilename)));
				
				if (encodePadFilename != null) {
					dotfile.write("encode " + encodePadFilename + " " + String.valueOf(encodePadIndex) + "\n");
				}
				if (decodePadFilename != null) {
					dotfile.write("decode " + decodePadFilename + " " + String.valueOf(decodePadIndex) + "\n");
				}
				
				dotfile.write("cleartextlog " + clearTextLogFile + "\n");
				dotfile.write("ciphertextlog " +cipherTextLogFile + "\n");

				dotfile.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}			
		} else {
			System.out.println("Pad file not configured; specify with -pe and -pd");
		}
	}
	
	private boolean readDotFile() {
		boolean result = false;
		BufferedReader DotFile;
		String line;
		String[] element;
		
		try {
			DotFile = new BufferedReader(new FileReader(dotFilename));
			
			while ((line = DotFile.readLine()) != null) {
				if (line.length() > 2) {
					element = line.split(" ");
					if (element == null) {
						continue;
					} else {
						if (element.length == 2) {
							if (element[0].equalsIgnoreCase("cleartextlog")) {
								clearTextLogFile = element[1];
								continue;
							}
							if (element[0].equalsIgnoreCase("ciphertextlog")) {
								cipherTextLogFile = element[1];
								continue;
							}
						}
						if (element.length == 3) {
							if (element[0].equalsIgnoreCase("encode")) {
								encodePadFilename = element[1];
								encodePadIndex = new Long(element[2]);
								isEncodeSet = true;
								result = true;
								System.out.println("Using " + encodePadFilename + " for encoding.");								
								continue;
							}
							if (element[0].equalsIgnoreCase("decode")) {
								decodePadFilename = element[1];
								decodePadIndex = new Long(element[2]);
								isDecodeSet = true;
								result = true;
								System.out.println("Using " + decodePadFilename + " for decoding.");
								continue;
							}
						}
					}
				}
			}
			DotFile.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	
		return result;
	}
	
}
