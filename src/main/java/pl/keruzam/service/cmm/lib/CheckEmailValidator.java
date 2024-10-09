package pl.keruzam.service.cmm.lib;

import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.keruzam.service.cmm.StringUtil;

/**
 * Walidator do emaila
 *
 * @author Mirek Szajowski
 *
 */
public class CheckEmailValidator implements ConstraintValidator<Email, String> {

	@Override
	public void initialize(final Email constraintAnnotation) {
	}

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {
		return isValid(value);
	}

	public static boolean isValid(final String value) {
		if (value == null || value.isEmpty()) {
			return true; // to nie jest blad
		}
		String emailsWithoutWhiteSpaces = value.replaceAll(" ", "");
		String[] emailList = emailsWithoutWhiteSpaces.split(";");
		return checkAllEmails(emailList);
	}

	private static boolean checkAllEmails(final String[] emailList) {
		for (String email : emailList) {
			if (!validEmail(email)) {
				return false;
			}
		}
		return true;
	}

	private static boolean validEmail(final String value) {
		if (isContainsForbiddenChars(value)) {
			return false;
		}
		if (!value.contains("@")) {
			return false;
		}
		if (StringUtil.getCharacterCount(value, "@") > 1) {
			return false;
		}
		String[] split = value.split("@");
		if (split.length <= 1) {
			return false;
		}
		String name = split[0];
		String subDomain = split[1];
		if (StringUtil.isEmpty(name, subDomain)) {
			return false;
		}
		if (!subDomain.contains(".")) {
			return false;
		}

		boolean firstOrLastCharForbiddenInName = checkIsFirstOrLastCharForbidden(name);
		boolean firstOrLastCharForbiddenInSubDomin = checkIsFirstOrLastCharForbidden(subDomain);
		if (firstOrLastCharForbiddenInName || firstOrLastCharForbiddenInSubDomin) {
			return false;
		}

		String[] subDomainSplit = subDomain.split("\\.");
		if (subDomainSplit.length <= 1) {
			return false;
		}
		if (StringUtil.isEmpty(subDomainSplit[0], subDomainSplit[1])) {
			return false;
		}
		return true;
	}

	private static boolean checkIsFirstOrLastCharForbidden(final String value) {
		int lastCharIdx = value.length() - 1;
		return value.charAt(lastCharIdx) == '.' || value.charAt(0) == '.';
	}

	private static boolean isContainsForbiddenChars(final String value) {
		List<String> forbiddenChars = StringUtil.getEmailForbiddenCharsList();
		for (String forbiddenChar : forbiddenChars) {
			if (value.contains(forbiddenChar)) {
				return true;
			}
		}
		return false;
	}
}
