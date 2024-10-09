package pl.keruzam.service.cmm.lib;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Walidator poprawnosci tablicy obiektów
 *
 * @author Bartłomiej Jasik
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckNotEmptyValidator.class)
@Documented
public @interface NotEmpty {
	public static final String ID = "{pl.com.stream.constraints.notEmpty}";

	String message() default ID;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
