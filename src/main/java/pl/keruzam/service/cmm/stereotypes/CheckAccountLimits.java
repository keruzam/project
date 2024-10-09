package pl.keruzam.service.cmm.stereotypes;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import pl.keruzam.service.cmm.lib.AccountLimits;

/**
 * Sprawdza limity dla konta
 * 
 * @author Mirek Szajowski
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckAccountLimits {
	Class<? extends AccountLimits> value();

	String operation() default "";
}
