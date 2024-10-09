package pl.keruzam.service.cmm.lib;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.crypto.password.PasswordEncoder;

public final class FirminoPasswordEncoder implements PasswordEncoder {

	private String algorithm = "SHA-256";

	public FirminoPasswordEncoder() {
		this.algorithm = "SHA-256";
	}

	public FirminoPasswordEncoder(String algorithm) {
		this.algorithm = algorithm;
	}

	@Override
	public String encode(CharSequence rawPassword) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			byte[] encodedHash = messageDigest.digest(rawPassword.toString().getBytes());
			return bytesToHex(encodedHash);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Error occurred while encoding password", e);
		}
	}

	private String bytesToHex(byte[] bytes) {
		StringBuilder hexString = new StringBuilder(2 * bytes.length);
		for (byte b : bytes) {
			String hex = Integer.toHexString(0xff & b);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return encode(rawPassword).equals(encodedPassword);
	}
}


