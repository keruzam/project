package pl.keruzam.service.cmm.lib;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Walidator do tablicy obiektów
 *
 * @author Bartłomiej Jasik
 *
 */
public class CheckNotEmptyValidator implements ConstraintValidator<NotEmpty, Object[]> {

	@Override
	public void initialize(final NotEmpty constraintAnnotation) {
	}

	@Override
	public boolean isValid(final Object[] array, final ConstraintValidatorContext context) {
		if (array == null || array.length == 0) {
			return false;
		}
		return true;
	}
}
