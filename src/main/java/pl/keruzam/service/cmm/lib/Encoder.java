package pl.keruzam.service.cmm.lib;


/**
 * Klasa szyfrująca
 *
 * @author Tomasz Mazurek
 */
public class Encoder {

	private static final FirminoPasswordEncoder SHA1 = new FirminoPasswordEncoder("SHA-1");

	private static final FirminoPasswordEncoder SHA256 = new FirminoPasswordEncoder("SHA-256");

	public static String encode(final String rawPass, final String salt) {
		return SHA256.encode(rawPass + createSalt(salt));
	}

	private static String createSalt(String salt) {
		return "{" + salt + "}";
	}

	public static String encode(final String rawPass) {
		return SHA256.encode(rawPass);
	}

	public static String encodeWithSHA1(final String rawPass, final String salt) {
		return SHA1.encode(rawPass + createSalt(salt));
	}

	public static String encodeWithSHA1(final String text) {
		return SHA1.encode(text);
	}

}
