package ca.vire.otp.exception;

public class PadNotDefined  extends Exception {

	// Needed as super class Throwable implements Serializable 
	private static final long serialVersionUID = -2883415609802901387L;
	private String message = null;
	
	public PadNotDefined() {
		super();
	}

	public PadNotDefined(String message) {
		super(message);
		this.message = message;
	}
	
	public PadNotDefined(Throwable cause) {
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
