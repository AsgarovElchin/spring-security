package elchinasgarov.plantly_backend.util;


import java.security.SecureRandom;
import java.util.Base64;

public class SecretKeyGenerator {
    public static void main(String[] args) {
        System.out.println("ACCESS_SECRET_KEY: " + generateSecretKey());
        System.out.println("REFRESH_SECRET_KEY: " + generateSecretKey());
    }

    private static String generateSecretKey() {
        byte[] key = new byte[32];
        new SecureRandom().nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }
}
