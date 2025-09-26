package overlook_hotel.overlook_hotel.util;

import java.security.SecureRandom;
import java.util.Base64;

public class RandomizeSalt {
    public static String generateSalt(int lengthBytes) {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[lengthBytes];
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }
}
