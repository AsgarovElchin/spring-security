package elchinasgarov.plantly_backend.util;

import java.util.Random;

public class OtpUtil {

    public static String generate6DigitOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}