package pl.keruzam.service.cmm.lib;

/**
 * Blad autoryzacji
 *
 * @author Mirek Szajowski
 */

public class AuthenticationException extends BusinessException {

	public AuthenticationException(final String email) {
		super(email);
	}

}
