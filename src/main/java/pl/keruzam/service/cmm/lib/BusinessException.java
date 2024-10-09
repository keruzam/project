package pl.keruzam.service.cmm.lib;

/**
 * Blad przetwarzania biznesu
 *
 * @author Mirek Szajowski
 */

public class BusinessException extends RuntimeException {

	private Long idMessageMethodType = ConstExceptionMessageType.DEFAULT;

	public BusinessException() {
		super();
	}

	public BusinessException(final String message) {
		super(message);
	}

	public BusinessException(final String message, final Long idMessageMethodType) {
		super(message);
		this.idMessageMethodType = idMessageMethodType;
	}

	public Long getIdMessageMethodType() {
		return idMessageMethodType;
	}

}
