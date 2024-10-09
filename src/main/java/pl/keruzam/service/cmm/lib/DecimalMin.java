package pl.keruzam.service.cmm.lib;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * {@link jakarta.validation.constraints.DecimalMin}
 *
 * @author marceli.matuszak
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { DecimalMinValidatorForNumber.class })
public @interface DecimalMin {
	String message() default "{jakarta.validation.constraints.DecimalMin.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * The <code>String</code> representation of the min value according to the
	 * <code>BigDecimal</code> string representation
	 *
	 * @return value the element must be higher or equal to
	 */
	String value();

	String htmlFieldId() default "";

	boolean nullable() default true;

	/**
	 * Defines several <code>@DecimalMin</code> annotations on the same element
	 *
	 * @author Emmanuel Bernard
	 * @see pl.com.stream.topfirma.blc.pub.lib.DecimalMin
	 */
	@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
	@Retention(RUNTIME)
	@Documented
	@interface List {
		DecimalMin[] value();
	}
}
