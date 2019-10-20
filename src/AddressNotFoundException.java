/**
 * Creates an exception when address of a symbol is not found.
 * @author Ethan Huynh
 *
 */
public class AddressNotFoundException extends Exception {
	/**
	 * Inherits the Exception's ctor
	 * @param message
	 */
	public AddressNotFoundException(String message){
		super(message);
	}
}
