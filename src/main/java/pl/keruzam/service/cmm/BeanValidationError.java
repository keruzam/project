package pl.keruzam.service.cmm;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import pl.keruzam.service.cmm.lib.DecimalMin;
import pl.keruzam.service.cmm.lib.NotNull;

/**
 * Blad validacji
 *
 * @author Mirek Szajowski
 */

public class BeanValidationError extends ValidationException {
	Set<FieldValidationInfo> validations = new HashSet<FieldValidationInfo>();

	public BeanValidationError() {
		super();
	}

	public BeanValidationError(final String message) {
		super(message);
	}

	public Set<FieldValidationInfo> getValidations() {
		return validations;
	}

	public void setValidations(final Iterator validate) {
		while (validate.hasNext()) {
			ConstraintViolation validation = (ConstraintViolation) validate.next();
			Annotation annotation = validation.getConstraintDescriptor().getAnnotation();
			if (annotation instanceof NotNull) {
				NotNull notNullAnnotation = (NotNull) annotation;
				String htmlFieldId = notNullAnnotation.htmlFieldId();
				if (!StringUtil.isEmpty(htmlFieldId)) {
					this.validations.add(
							new FieldValidationInfo(validation.getPropertyPath().toString(), validation.getMessage(), (AbstractDto) validation.getRootBean(),
									htmlFieldId));
				} else {
					this.validations.add(
							new FieldValidationInfo(validation.getPropertyPath().toString(), validation.getMessage(), (AbstractDto) validation.getRootBean()));
				}
			} else if (annotation instanceof DecimalMin) {
				DecimalMin decimalMinAnnotation = (DecimalMin) annotation;
				String htmlFieldId = decimalMinAnnotation.htmlFieldId();
				if (!StringUtil.isEmpty(htmlFieldId)) {
					this.validations.add(
							new FieldValidationInfo(validation.getPropertyPath().toString(), validation.getMessage(), (AbstractDto) validation.getRootBean(),
									htmlFieldId));
				} else {
					this.validations.add(
							new FieldValidationInfo(validation.getPropertyPath().toString(), validation.getMessage(), (AbstractDto) validation.getRootBean()));
				}
			} else {
				this.validations.add(
						new FieldValidationInfo(validation.getPropertyPath().toString(), validation.getMessage(), (AbstractDto) validation.getRootBean()));
			}
		}
	}

}
