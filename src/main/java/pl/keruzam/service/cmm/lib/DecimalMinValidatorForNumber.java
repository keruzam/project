package pl.keruzam.service.cmm.lib;

import java.math.BigDecimal;
import java.math.BigInteger;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


/**
 * @author marceli.matuszak
 */
public class DecimalMinValidatorForNumber implements ConstraintValidator<DecimalMin, Number> {

	private BigDecimal minValue;

	private boolean nullable;

	@Override
	public void initialize(final DecimalMin minValue) {
		nullable = minValue.nullable();
		try {
			this.minValue = new BigDecimal(minValue.value());
		} catch (NumberFormatException nfe) {
			//throw LoggerFactory.make(null).getInvalidBigDecimalFormatException(minValue.value(), nfe);
		}
	}

	@Override
	public boolean isValid(final Number value, final ConstraintValidatorContext constraintValidatorContext) {

		if (value == null) {
			return nullable;
		}

		if (value instanceof BigDecimal) {
			return ((BigDecimal) value).compareTo(minValue) != -1;
		} else if (value instanceof BigInteger) {
			return (new BigDecimal((BigInteger) value)).compareTo(minValue) != -1;
		}
		if (value instanceof Long) {
			return (BigDecimal.valueOf(value.longValue()).compareTo(minValue)) != -1;
		} else {
			return (BigDecimal.valueOf(value.doubleValue()).compareTo(minValue)) != -1;
		}
	}
}