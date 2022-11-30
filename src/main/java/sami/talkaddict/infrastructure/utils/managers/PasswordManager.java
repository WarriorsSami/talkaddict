package sami.talkaddict.infrastructure.utils.managers;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordManager {
    private static final BCrypt.Hasher hasher = BCrypt.withDefaults();
    private static final BCrypt.Verifyer verifyer = BCrypt.verifyer();

    public static String encode(String password) {
        return hasher.hashToString(12, password.toCharArray());
    }

    public static Boolean verify(String password, String hash) {
        return verifyer.verify(password.toCharArray(), hash).verified;
    }
}
