package com.dayou.postory.global.encrytion;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import org.springframework.stereotype.Component;

@Component
public class SHA256EncryptionService {

	public String encryptPassword(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] passBytes = password.getBytes(StandardCharsets.UTF_8);
			md.reset();
			byte[] digested = md.digest(passBytes);
			StringBuilder sb = new StringBuilder();
			for (byte b : digested) {
				sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
			}
			return sb.toString();
		} catch (Exception e) {
			throw new IllegalArgumentException("encrypt password error");
		}
	}
}
