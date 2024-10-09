package pl.keruzam.service.cmm;

import java.io.Serializable;

/**
 * 
 * @author Mirek Szajowski
 * 
 */
public class FieldValidationInfo implements Serializable {
	String fieldName;
	String message;
	AbstractDto dto;
	String htmlFieldId;

	public FieldValidationInfo(final String fieldName, final String message) {
		super();
		this.fieldName = fieldName;
		this.message = message;
		this.dto = null;
		this.htmlFieldId = null;
	}

	public FieldValidationInfo(final String fieldName, final String message, final AbstractDto dto) {
		super();
		this.fieldName = fieldName;
		this.message = message;
		this.dto = dto;
		this.htmlFieldId = null;
	}

	public FieldValidationInfo(final String fieldName, final String message, final AbstractDto dto, final String htmlFieldId) {
		super();
		this.fieldName = fieldName;
		this.message = message;
		this.dto = dto;
		this.htmlFieldId = htmlFieldId;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getMessage() {
		return message;
	}

	public AbstractDto getDto() {
		return dto;
	}

	public String getHtmlFieldId() {
		return htmlFieldId;
	}

}
