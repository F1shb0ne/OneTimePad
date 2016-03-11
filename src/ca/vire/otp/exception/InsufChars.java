package ca.vire.otp.exception;

public class InsufChars extends Exception {

    // Needed as super class Throwable implements Serializable 
    private static final long serialVersionUID = -2401716239650695167L;    
    
    private String message = null;
    
    public InsufChars() {
        super();
    }

    public InsufChars(String message) {
        super(message);
        this.message = message;
    }
    
    public InsufChars(Throwable cause) {
        super(cause);        
    }
    
    @Override
    public String toString() {
        return message;
    }
    
    @Override
    public String getMessage() {
        return message;
    }
}
