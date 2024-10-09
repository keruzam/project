package pl.keruzam.service.cmm.lib;

import java.nio.charset.Charset;

import org.apache.commons.codec.binary.Base64;

/**
 * Klasa kodująca i odkodowująca hasła
 *
 * hasło zapisane od tyłu + pomiędzy hasło wplątany jest salt + Base64
 *
 * @author Tomasz Mazurek
 */
public class PasswordEncoder {

	private static final String SALT = "fqV6Uik7eQerTYo5.3DfEghOp";
	private static final int SALT_LENGTH = 25;
	private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");

	public static String encode(final String pass) {
		byte[] securePass = securePassword(pass);
		return Base64.encodeBase64String(securePass);
	}

	public static String decode(final String hash) {
		byte[] securePass = Base64.decodeBase64(hash);
		return unsecurePass(securePass);
	}

	private static byte[] securePassword(final String pass) {
		byte[] passBytes = pass.getBytes(UTF8_CHARSET);
		byte[] saltBytes = SALT.getBytes(UTF8_CHARSET);
		int passLength = passBytes.length;
		int iterations = passLength - 1;
		byte[] securePass = new byte[passLength * 2];
		int saltIdx = 0;
		int passIdx = 0;
		for (int i = 0; i <= iterations; i++) {
			securePass[passIdx] = passBytes[iterations - i];
			if (saltIdx >= SALT_LENGTH - 1) {
				saltIdx = 0;
			}
			securePass[passIdx + 1] = saltBytes[saltIdx];
			passIdx += 2;
			saltIdx++;
		}
		return securePass;
	}

	private static String unsecurePass(final byte[] securePass) {
		String securePassword = new String(securePass, UTF8_CHARSET);
		int length = securePassword.length();
		byte[] unsecurePass = new byte[length / 2];
		int passIdx = 0;
		for (int i = 2; i <= length; i += 2) {
			unsecurePass[passIdx] = securePass[length - i];
			passIdx++;
		}
		return new String(unsecurePass, UTF8_CHARSET);
	}
}
