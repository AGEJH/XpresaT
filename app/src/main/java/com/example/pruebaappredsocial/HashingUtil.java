package com.example.pruebaappredsocial;

import java.security.MessageDigest;
import java.security.SecureRandom;

public class HashingUtil {

    // Generate a random salt
    public static byte[] generateSalt() throws Exception {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    // Hash a password with the given salt
    public static String hashPassword(String password, byte[] salt) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes());
        return HexUtil.bytesToHex(hashedPassword);
    }
}
