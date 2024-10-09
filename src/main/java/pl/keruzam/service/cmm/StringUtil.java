package pl.keruzam.service.cmm;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import javax.swing.text.MaskFormatter;

import jakarta.mail.internet.InternetAddress;
import org.apache.hc.core5.net.InetAddressUtils;

/**
 * Narzedziowka do stringow
 *
 * @author Mirek Szajowski
 */
public class StringUtil {

	public static final String EMPTY_STRING = "";
	public static final String SPACE = " ";
	public static final String COMMA_WITH_SPACE = ", ";

	public static final int SHORTEN_LENGTH = 15;
	public static final int MEDIUM_LENGTH = 50;
	private static final Locale PL = new Locale("PL", "pl");
	private static final List<Character> polishChars = new ArrayList<Character>(
			Arrays.asList('ą', 'ć', 'ę', 'ł', 'ń', 'ó', 'ś', 'ź', 'ż', 'Ą', 'Ć', 'Ę', 'Ł', 'Ń', 'Ó', 'Ś', 'Ź', 'Ż'));

	/**
	 * Zwraca pusty string zamiast null-a
	 */
	public static String getValue(final String value) {
		return value == null ? "" : value;
	}

	public static String replacePolishChars(final String fileName) {
		if (fileName == null) {
			return null;
		}

		char[] chars = fileName.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			switch (chars[i]) {
				case 'ą':
					chars[i] = 'a';
					break;
				case 'ć':
					chars[i] = 'c';
					break;
				case 'ę':
					chars[i] = 'e';
					break;
				case 'ł':
					chars[i] = 'l';
					break;
				case 'ń':
					chars[i] = 'n';
					break;
				case 'ó':
					chars[i] = 'o';
					break;
				case 'ś':
					chars[i] = 's';
					break;
				case 'ź':
				case 'ż':
					chars[i] = 'z';
					break;
				case 'Ą':
					chars[i] = 'A';
					break;
				case 'Ć':
					chars[i] = 'C';
					break;
				case 'Ę':
					chars[i] = 'E';
					break;
				case 'Ł':
					chars[i] = 'L';
					break;
				case 'Ń':
					chars[i] = 'N';
					break;
				case 'Ó':
					chars[i] = 'O';
					break;
				case 'Ś':
					chars[i] = 'S';
					break;
				case 'Ź':
				case 'Ż':
					chars[i] = 'Z';
					break;
			}
		}
		return new String(chars);
	}

	public static boolean isEmpty(final String value, final String... values) {
		if (value == null) {
			return true;
		}
		for (String val : values) {
			boolean emptyValue = isEmptyValue(val);
			if (emptyValue) {
				return true;
			}
		}
		return isEmptyValue(value);
	}

	public static String shorten(final String value) {
		return makeShort(value, SHORTEN_LENGTH);
	}

	public static String medium(final String value) {
		return makeShort(value, MEDIUM_LENGTH);
	}

	private static String makeShort(final String value, final int length) {
		return value != null ? (value.length() >= length ? value.substring(0, length) + "..." : value) : null;
	}

	private static boolean isEmptyValue(final String value) {
		return value == null || value.isEmpty();
	}

	public static String getFirstWordAndUpperCaseIt(final String value) {
		if (value != null) {
			return getFirstWord(value).toUpperCase();
		}
		return value;
	}

	public static String getFirstWord(final String value) {
		if (value != null) {
			return value.trim().split(" ")[0];
		}
		return value;
	}

	public static String createStreetAddress(final String locality, final String post, final String street, final String houseNumber, final String flatNumber) {
		if (isEmpty(locality)) {
			return "";
		} else {
			String numbers = isEmpty(houseNumber) ? "" : " " + houseNumber + (isEmpty(flatNumber) ? "" : "/" + flatNumber);
			if (isEmpty(street)) {
				return locality + numbers;
			} else if (!locality.equalsIgnoreCase(post)) {
				return street + numbers + ", " + locality;
			} else {
				return street + numbers;
			}
		}
	}

	public static String createPostAddress(final String post, final String postCode) {
		String code = isEmpty(postCode) ? "" : " " + postCode;
		return code + post;
	}

	public static Boolean isNumeric(final String str) {
		if (isEmpty(str)) {
			return Boolean.FALSE;
		}
		NumberFormat formatter = NumberFormat.getInstance(PL);
		ParsePosition pos = new ParsePosition(0);
		formatter.parse(str, pos);
		return str.length() == pos.getIndex();
	}

	public static String generateString() {
		Random random = new Random();
		int length = getLength(random);
		String characters = "qwertyuiopasdfghjklzxcvbnm";
		char[] text = new char[length];
		for (int i = 0; i < length; i++) {
			text[i] = characters.charAt(random.nextInt(characters.length()));
		}
		return new String(text);
	}

	private static int getLength(final Random random) {
		int length = random.nextInt(9);
		if (length < 3) {
			length = length + 3;
		}
		return length;
	}

	public static String encodeUrl(final String text) {
		try {
			return URLEncoder.encode(text, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String decodeUrl(final String text) {
		try {
			return URLDecoder.decode(text, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String firstLetterLower(final String input) {
		return input.substring(0, 1).toLowerCase() + input.substring(1);
	}

	public static List<String> getEmailForbiddenCharsList() {
		List<String> forbiddenChars = new ArrayList<String>();
		forbiddenChars.add("<");
		forbiddenChars.add(">");
		forbiddenChars.add("(");
		forbiddenChars.add(")");
		forbiddenChars.add("[");
		forbiddenChars.add("]");
		forbiddenChars.add("/");
		forbiddenChars.add("\\");
		forbiddenChars.add("ą");
		forbiddenChars.add("ć");
		forbiddenChars.add("ę");
		forbiddenChars.add("ł");
		forbiddenChars.add("ń");
		forbiddenChars.add("ó");
		forbiddenChars.add("ś");
		forbiddenChars.add("ź");
		forbiddenChars.add("ż");
		forbiddenChars.add("Ą");
		forbiddenChars.add("Ć");
		forbiddenChars.add("Ę");
		forbiddenChars.add("Ł");
		forbiddenChars.add("Ń");
		forbiddenChars.add("Ó");
		forbiddenChars.add("Ś");
		forbiddenChars.add("Ź");
		forbiddenChars.add("Ż");
		forbiddenChars.add(":");
		forbiddenChars.add("	"); // tabulator
		forbiddenChars.add(" "); // spacja ctrl+shift+space z worda
		forbiddenChars.add(",");
		return forbiddenChars;
	}

	private static List<String> getPasswordSpecialCharsList() {
		List<String> specialChars = new ArrayList<String>();
		specialChars.add("<");
		specialChars.add(">");
		specialChars.add("(");
		specialChars.add(")");
		specialChars.add("[");
		specialChars.add("]");
		specialChars.add("/");
		specialChars.add("\\");
		specialChars.add(":");
		specialChars.add(",");
		specialChars.add(".");
		specialChars.add("!");
		specialChars.add("@");
		specialChars.add("#");
		specialChars.add("$");
		specialChars.add("%");
		specialChars.add("^");
		specialChars.add("&");
		specialChars.add("*");
		specialChars.add("=");
		specialChars.add("+");
		specialChars.add("-");
		specialChars.add("_");
		specialChars.add("|");
		specialChars.add("'");
		specialChars.add("\"");
		specialChars.add(";");
		specialChars.add("?");
		specialChars.add("`");
		specialChars.add("~");
		return specialChars;
	}

	public static String substringTo(final String value, final int length) {
		if (!isEmpty(value)) {
			return value.substring(0, length);
		}
		return "";
	}

	public static String substringIfTooLong(final String value, final int length) {
		if (!isEmpty(value)) {
			if (value.length() > length) {
				return value.substring(0, length);
			}
			return value;
		}
		return "";
	}

	public static Boolean ipV4AddressValidator(final String ipAddress) {
		if (isEmpty(ipAddress)) {
			return false;
		}
		return InetAddressUtils.isIPv4Address(ipAddress);
	}

	public static String firstLetterUpper(final String input) {
		return input.substring(0, 1).toUpperCase() + input.substring(1);
	}

	public static String correctEmailAddress(final String email) {
		if (email == null || email.equals("")) {
			return "";
		}
		String inputEmails = email.replaceAll(" ", ";").replaceAll(",", ";").replaceAll("\"", "").replaceAll("\'", "");
		String correctedEmails = inputEmails.replaceAll(";{2,}", ";");
		return correctedEmails.endsWith(";") ? correctedEmails : correctedEmails + ";";
	}

	public static String addWordBeforeLastOccurrenceValue(final String text, final String wordToAdd, final String value) {
		int pos = text.lastIndexOf(value);
		if (pos >= 0) {
			return text.substring(0, pos) + wordToAdd + text.substring(pos);
		}
		return text;
	}

	public static String getFormatedPostCode(final String postCode) {
		String formatedPostCode = "";
		if (!isEmpty(postCode)) {
			formatedPostCode += postCode.substring(0, 2);
			formatedPostCode += "-";
			formatedPostCode += postCode.substring(2);
		}
		return formatedPostCode;
	}

	public static Map<String, String> createParamsMap(final String queryString) {
		Map<String, String> params = new HashMap<String, String>();
		String decoded = "";
		try {
			decoded = URLDecoder.decode(queryString, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String[] pares = decoded.split("&");
		for (String pare : pares) {
			String[] nameAndValue = pare.split("=");
			params.put(nameAndValue[0], nameAndValue[1]);
		}

		return params;
	}

	public static String shortenWIthoutDots(final String value, final int max) {
		if (value != null && !isEmptyValue(value)) {
			return value.length() > max ? value.substring(0, max) : value;
		} else {
			return value;
		}
	}

	public static boolean isNotEmpty(final String value) {
		return value != null && value.length() != 0;
	}

	public static boolean isNotEmpty(final String... values) {
		for (String val : values) {
			boolean notEmptyValue = !isEmptyValue(val);
			if (notEmptyValue) {
				return true;
			}
		}
		return false;
	}

	/**
	 * usuwa biale znaki z lewej czesci tekstu
	 */
	public static String leftTrim(final String s) {
		if (s == null) {
			return s;
		}
		int i = 0;
		while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
			i++;
		}
		return i == 0 ? s : s.substring(i);
	}

	/**
	 * usuwa biale znaki z prawej czesci tekstu
	 */
	public static String rightTrim(final String s) {
		if (s == null) {
			return s;
		}
		int lastIndex = s.length() - 1;
		int i = lastIndex;
		while (i >= 0 && Character.isWhitespace(s.charAt(i))) {
			i--;
		}
		return i == lastIndex ? s : s.substring(0, i + 1);
	}

	/**
	 * usuwa biale znaki z lewej i prawej czesci tekstu
	 */
	public static String trim(final String s) {
		return rightTrim(leftTrim(s));
	}

	/**
	 * Przytnij tekst jeslij jest za dlugi i dodaj na koniec "...", a jesli jest ok zwraza orginalny tekst
	 *
	 * @param text
	 * 		tekst
	 * @param maxTextLength
	 * 		maksymalna długość
	 * @return
	 */
	public static String cutStringIfNeed(final String text, final int maxTextLength) {
		String result = text;
		if (text != null && text.length() > maxTextLength) {
			if (maxTextLength < 3) {
				result = multiplyString(".", maxTextLength);
			} else {
				result = text.substring(0, maxTextLength - 3) + "...";
			}
		}
		return result;
	}

	public static String multiplyString(final String text, final int times) {
		StringBuilder result = new StringBuilder();
		for (int i = 1; i <= times; i++) {
			result.append(text);
		}
		return result.toString();
	}

	/**
	 * Metoda pozwalajaca usunac z podanego tekstu wskazany ciag. Usuwane sa wszystkie wystapienia. W przypadku gdy tekst oryginalny lub tekst ktory ma wystapic
	 * jest nullem lub pusty to wtedy zwracany jest oryginalny tekst.
	 *
	 * @param text
	 * @param textToRemove
	 * @return
	 */
	public static String removeAll(final String text, final String textToRemove) {
		if (isEmpty(text) || isEmpty(textToRemove)) {
			// nie ma co lub z czego usuwac
			return text;
		}
		String removeAll = text.replaceAll(textToRemove, EMPTY_STRING);
		return removeAll;
	}

	/**
	 * Metoda zwraca z listy jeden string, oddzielony wskazanym separatorem.
	 *
	 * @param stringList
	 * @param separator
	 * @return
	 */
	public static String convertListToString(final List<String> stringList, final String separator) {
		String result = EMPTY_STRING;
		if (stringList != null && stringList.size() > 0) {
			StringBuilder sBuilder = new StringBuilder();
			for (String s : stringList) {
				// gdy wieksze o 1 to znaczy ze cos juz dodane - dodaje separator
				if (sBuilder.length() > 0) {
					sBuilder.append(separator);
				}
				sBuilder.append(s);
			}
			result = sBuilder.toString();
		}
		return result;
	}

	public static String convertToRoman(final int value) {
		String result = "";
		int num = value;
		while (num > 0) {
			if (num >= 1000) {
				result += "M";
				num -= 1000;
			} else if (num >= 900) {
				result += "CM";
				num -= 900;
			} else if (num >= 500) {
				result += "D";
				num -= 500;
			} else if (num >= 400) {
				result += "CD";
				num -= 400;
			} else if (num >= 100) {
				result += "C";
				num -= 100;
			} else if (num >= 90) {
				result += "XC";
				num -= 90;
			} else if (num >= 50) {
				result += "L";
				num -= 50;
			} else if (num >= 40) {
				result += "XL";
				num -= 40;
			} else if (num >= 10) {
				result += "X";
				num -= 10;
			} else if (num >= 9) {
				result += "IX";
				num -= 9;
			} else if (num >= 5) {
				result += "V";
				num -= 5;
			} else if (num >= 4) {
				result += "IV";
				num -= 4;
			} else if (num >= 1) {
				result += "I";
				num -= 1;
			} else {
				break;
			}
		}

		return result;
	}

	public static String convertToHtml(final String value) {
		if (!isEmpty(value)) {
			return value.replaceAll("\n", "<br>");
		}
		return "";
	}

	public static boolean isContainsSpecialChar(final String value) {
		if (value == null || value.isEmpty()) {
			return false;
		}
		List<String> specialChars = getPasswordSpecialCharsList();
		for (String specChar : specialChars) {
			if (value.contains(specChar)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isContainsNumber(final String value) {
		if (value == null || value.isEmpty()) {
			return false;
		} else {
			return value.matches(".*\\d+.*");
		}
	}

	public static String getBankAccountNumberMask(final String value) {
		try {
			String trimValue = value == null ? "" : value.replaceAll(" ", "");
			MaskFormatter mf = new MaskFormatter("## #### #### #### #### #### ####");
			mf.setValueContainsLiteralCharacters(false);
			return mf.valueToString(trimValue);
		} catch (ParseException e) {
			return value;
		}
	}

	/**
	 * Zwraca listę email oddzieloną ";" weryfikujac pojedynczy adres email;
	 */
	public static String getValidEmailAdress(final String email) {
		if (isEmpty(email)) {
			return "";
		}
		String value = replacePolishChars(email).replace(" ", ";");
		for (String forbidden : getEmailForbiddenCharsList()) {
			value = value.replace(forbidden, ";");
		}
		List<String> validEmail = new ArrayList<String>();
		String[] emails = value.split(";");
		for (String searchEmail : emails) {
			try {
				InternetAddress addres = new InternetAddress(searchEmail, true);
				addres.validate();
				if (addres.getAddress().contains(".") && addres.getAddress().contains("@")) {
					validEmail.add(addres.getAddress());
				}
			} catch (Exception e) {
				// System.out.println("EMAIL ERR " + searchEmail); FIXME dodaj do email_error
			}
		}
		StringBuilder result = new StringBuilder();
		for (String emailAddress : validEmail) {
			result.append(emailAddress);
			result.append(",");
		}
		if (isEmpty(result.toString())) {
			return "";
		} else {
			return result.toString().substring(0, result.toString().length() - 1);
		}
	}

	public static String getNotEmptyString(final String value) {
		if (StringUtil.isEmpty(value)) {
			return null;
		}
		return value;
	}

	public static int getCharacterCount(final String value, final String character) {
		if (isNotEmpty(value, character)) {
			int beginLen = value.length();
			String repalcedString = value.replaceAll(character, "");
			int endLen = repalcedString.length();
			return beginLen - endLen;
		}
		return 0;
	}

	public static String deletePolishChars(String value) {
		char[] chars = value.toCharArray();
		StringBuilder result = new StringBuilder();
		for (char character : chars) {
			if (!polishChars.contains(character)) {
				result.append(character);
			}
		}
		return result.toString();
	}

}
