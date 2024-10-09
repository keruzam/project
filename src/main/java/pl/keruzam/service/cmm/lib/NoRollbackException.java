package pl.keruzam.service.cmm.lib;

/**
 * Klasa wyjątku pomijanego w transakcjach
 *
 * @author Cezary Szczesny
 */

public class NoRollbackException extends BusinessException {

	public NoRollbackException() {
		super();
	}

	public NoRollbackException(final String message, final Long idMessageMethodType) {
		super(message, idMessageMethodType);
	}

	public NoRollbackException(final String message) {
		super(message);
	}

}
