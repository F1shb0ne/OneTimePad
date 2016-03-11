package ca.vire.otp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import ca.vire.otp.exception.PadNotDefined;

public class DotFile {
    private String dotFilename = System.getProperty("user.dir") + "/.otp";
    private String clearTextLogFilename = "cleartext.log"; // Where decoded material is written by default
    private String cipherTextLogFilename = "ciphertext.log"; // same for encoded
    private LogFile clearTextLog = null;
    private LogFile cipherTextLog = null;
    private String encodePadFilename = null;
    private String decodePadFilename = null;
    private long encodePadIndex = 0;
    private long decodePadIndex = 0;
    private Pad encodePad = null;
    private Pad decodePad = null;
    public boolean isEncodeSet = false;
    public boolean isDecodeSet = false;

    public DotFile() throws FileNotFoundException, IOException {
        // check to see if dotfile already exists
        try {
            readDotFile();
        } catch (FileNotFoundException e) {
            // this is a non-issue.
        } catch (IOException e) {
            throw e; // this isn't.
        }

        // open the encode pad file if a filename is set
        if (encodePadFilename != null)
            setEncodePad(encodePadFilename, encodePadIndex);

        // ditto for decode pad
        if (decodePadFilename != null) {
            setDecodePad(decodePadFilename, decodePadIndex);
        }

        // Get the clear text log ready
        if (clearTextLogFilename != null) {
            setClearTextLog(clearTextLogFilename);
        }

        // Get the cipher text log ready
        if (cipherTextLogFilename != null) {
            setCipherTextLog(cipherTextLogFilename);
        }
    }

    public Pad getEncodePad() throws PadNotDefined {
        Pad padFile = null;

        if (isEncodeSet) {
            padFile = encodePad;
        } else
            throw new PadNotDefined();

        return padFile;
    }

    public Pad getDecodePad() throws PadNotDefined {
        Pad padFile = null;

        if (isDecodeSet) {
            padFile = decodePad;
        } else
            throw new PadNotDefined();

        return padFile;
    }

    public void setEncodePad(String filename, long index) throws FileNotFoundException {
        File f = null;

        f = new File(filename);
        if (f.exists()) {
            encodePad = new Pad(filename, index);
            encodePadFilename = filename;
        } else {
            throw new FileNotFoundException("DotFile.setEncodePad(): " + encodePadFilename + " does not exist.");
        }
    }

    public void setDecodePad(String filename, long index) throws FileNotFoundException {
        File f = null;

        f = new File(filename);
        if (f.exists()) {
            decodePad = new Pad(filename, index);
            decodePadFilename = filename;
        } else {
            throw new FileNotFoundException("DotFile.setDecodePad(): " + decodePadFilename + " does not exist.");
        }
    }

    public void setClearTextLog(String filename) throws IOException {
        clearTextLog = new LogFile(filename);
        clearTextLogFilename = filename;
    }

    public void setCipherTextLog(String filename) throws IOException {
        cipherTextLog = new LogFile(filename);
        cipherTextLogFilename = filename;
    }

    public LogFile getClearTextLog() {
        return clearTextLog;
    }

    public LogFile getCipherTextLog() {
        return cipherTextLog;
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

                dotfile.write("cleartextlog " + clearTextLogFilename + "\n");
                dotfile.write("ciphertextlog " +cipherTextLogFilename + "\n");

                dotfile.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        } else {
            System.out.println("Pad file(s) not configured; specify with -pe and -pd");
        }
    }

    /*
     * this method will read the contents of the dot file and set fields appropriately,
     * no other files are opened.
     */
    private void readDotFile() throws FileNotFoundException, IOException {
        BufferedReader dotFile;
        File f = null;
        String line;
        String[] element;

        // check if the dotfile exists.
        f = new File(dotFilename);
        if (!f.exists())
            throw new FileNotFoundException("DotFile.readDotFile(): " + dotFilename + " does not exist.");

        // open for buffered reading
        try {
            dotFile = new BufferedReader(new FileReader(dotFilename));
        } catch (IOException e) {
            System.out.println("DotFile.readDotFile(): problem reading " + dotFilename);
            e.printStackTrace();
            throw e;
        }

        // read in each line
        while ((line = dotFile.readLine()) != null) {
            if (line.length() > 2) {
                element = line.split(" ");
                if (element == null) {
                    continue;
                } else {
                    if (element.length == 2) {
                        if (element[0].equalsIgnoreCase("cleartextlog")) {
                            clearTextLogFilename = element[1];
                            continue;
                        }
                        if (element[0].equalsIgnoreCase("ciphertextlog")) {
                            cipherTextLogFilename = element[1];
                            continue;
                        }
                    }
                    if (element.length == 3) {
                        if (element[0].equalsIgnoreCase("encode")) {
                            encodePadFilename = element[1];
                            encodePadIndex = new Long(element[2]);
                            isEncodeSet = true;
                            System.out.println("Using " + encodePadFilename + " for encoding.");
                            continue;
                        }
                        if (element[0].equalsIgnoreCase("decode")) {
                            decodePadFilename = element[1];
                            decodePadIndex = new Long(element[2]);
                            isDecodeSet = true;
                            System.out.println("Using " + decodePadFilename + " for decoding.");
                            continue;
                        }
                    }
                }
            }
        }
        dotFile.close();
    }
}
