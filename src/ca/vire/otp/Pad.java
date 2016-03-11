package ca.vire.otp;

import java.io.File;
import java.io.FileReader;
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
    private String padFileURI = null;
    private String tempFileURI = null;
    private RandomAccessFile padReader = null;
    private FileReader tempReader = null;
    private long fileLength = 0;
    private long tempFileLength = 0;
    private long padIndex;
    private long tempIndex;
    private boolean isDefined = false;
    private boolean isTempReady = false;

    /*
     * Open the pad file and move seek position to where it last was.
     */
    public Pad(String padfilename, long index) throws FileNotFoundException {
        padFileURI = padfilename;
        tempFileURI = padFileURI + ".tmp";
        padIndex = index;

        try {
            padReader = new RandomAccessFile(new File(padFileURI), "r");
            padReader.seek(padIndex);
            fileLength = padReader.length();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        isDefined = true;
    }

    /*
     * Request a given number of characters and write them to a temporary file.
     * If the request fails, throw InsufChars exception and reset pad index.
     */
    public void reqLength(long lengthNeeded) throws InsufChars, PadNotDefined {
        long i = 0;
        long originalPadIndex = padIndex;
        char c = 0;
        FileWriter writer = null;

        System.out.println("Pad.reqLength(): was requested " + Long.toString(lengthNeeded) + " characters");

        // Open the temporary file
        System.out.println("Pad.reqLength(): opening temp file: " + tempFileURI);
        try {
            writer = new FileWriter(new File(tempFileURI));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // write the temporary file
        do {
            // read pad until a letter is found
            do {
                try {
                    c = rawGetChar();
                    System.out.println("Pad.reqLength(): Read: " + Character.toString(c));
                } catch (EOFException e) {
                    padIndex = originalPadIndex;
                    throw new InsufChars();
                }
            } while (!isLetter(c));

            // write it to the temporary file
            try {
                writer.write(toUCase(c));
                System.out.println("Pad.reqLength(): wrote: " + c);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        } while (++i < lengthNeeded);
        System.out.println("Pad.reqLength(): Got enough; closing temp file.");

        // close the temp file
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // update the temp file length
        tempFileLength = lengthNeeded;
        isTempReady = true;
    }

    public boolean isDefined() {
        return isDefined;
    }

    private boolean isLetter(int c) {
        if ((c >= 65 && c <= 90) || (c >= 97 && c <= 122))
            return true;

        return false;
    }

    // Assumes given value already passes isLetter()
    private char toUCase(char c) {
        if (c >= 97)
            return (char)(c - 32);

        return c;
    }

    // Assumes given value already passes isLetter()
    private boolean isUCase(int c) {
        if (c <= 90)
            return true;

        return false;
    }

    /*
     * Read characters back from the temp file, one at a time.
     */
    public char getChar() throws EOFException {
        char c = 0;

        // should not be here if isTempReady is false
        if (!isTempReady) {
            System.out.println("Pad.getChar(): temp file not defined!");
            throw new EOFException();
        }

        // open the temp file if it hasn't been already
        if (isTempReady && tempReader == null) {
            try {
                System.out.println("Pad.getChar(): opening temp file.");
                tempReader = new FileReader(new File(tempFileURI));
            } catch (FileNotFoundException e) {
                // This should never happen
                e.printStackTrace();
                System.exit(1);
            }
        }

        // are there more characters to read?
        if (tempIndex < tempFileLength) {
            try {
                c = (char) tempReader.read();
                ++tempIndex;
                System.out.println("Pad.getChar(): read: " + Character.toString(c));
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        } else {
            // reached end of temp file, close up
            try {
                tempReader.close();
                System.out.println("Pad.getChar(): closing temp file.");
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            isTempReady = false;
            throw new EOFException();
        }

        return c;
    }

    /*
     * Read one character from the pad file.
     */
    public char rawGetChar() throws EOFException, PadNotDefined {
        char c = 0;

        // Bail immediately if nothing can be read
        if (!isDefined) {
            System.out.println("Pad.rawGetChar(): pad file not defined!");
            throw new PadNotDefined();
        }

        if (padIndex < fileLength) {
            try {
                c = (char) padReader.read();
                ++padIndex;
                System.out.println("Pad.rawGetChar(): read : " + Character.toString(c));
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        } else {
            System.out.println("Pad.rawGetChar(): reached end of file.");
            throw new EOFException();
        }

        return c;
    }

}
