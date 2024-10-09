package pl.keruzam.service.cmm.lib;

public class AccountLimitException extends RuntimeException {

	public AccountLimitException(String string) {
		super(string);
	}

	public AccountLimitException() {
	}

}
