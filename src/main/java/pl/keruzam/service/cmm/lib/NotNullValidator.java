package pl.keruzam.service.cmm.lib;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotNullValidator implements ConstraintValidator<NotNull, Object> {

	@Override
	public void initialize(final NotNull constraintAnnotation) {
	}

	@Override
	public boolean isValid(final Object value, final ConstraintValidatorContext context) {
		if (value instanceof String) {
			if (value != null && ((String) value).length() > 0) {
				return true;
			}
			return false;
		}
		return value != null;
	}
}
