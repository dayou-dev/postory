package com.dayou.postory.global.encrytion;

import java.security.MessageDigest;

import org.springframework.stereotype.Component;

@Component
public class SHA256EncryptionService {

	public String encryptPassword(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] passBytes = password.getBytes();
			md.reset();
			byte[] digested = md.digest(passBytes);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < digested.length; i++) {
				sb.append(Integer.toString((digested[i] & 0xff) + 0x100, 16).substring(1));
			}
			return sb.toString();
		} catch (Exception e) {
			return password;
		}
	}
}
