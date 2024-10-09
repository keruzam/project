package pl.keruzam.service.cmm.stereotypes;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.keruzam.service.cmm.lib.NoRollbackException;

/**
 * Adtotacja dla wszystkich serwisow aplikacyjnych
 *
 * @author Mirek Szajowski
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, noRollbackFor = NoRollbackException.class)
public @interface ApplicationService {

	String value() default "";

}
