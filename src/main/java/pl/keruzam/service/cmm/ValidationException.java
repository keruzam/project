package pl.keruzam.service.cmm;

/**
 * Blad validacji
 *
 * @author Mirek Szajowski
 */

public abstract class ValidationException extends RuntimeException {

	public ValidationException() {
		super();
	}

	public ValidationException(final String message) {
		super(message);
	}

}
