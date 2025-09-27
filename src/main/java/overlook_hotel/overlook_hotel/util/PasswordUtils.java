package overlook_hotel.overlook_hotel.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {
    public static class PasswordResult {
        public final String error;
        public final String hashedPassword;
        public final String salt;
        public PasswordResult(String error, String hashedPassword, String salt) {
            this.error = error;
            this.hashedPassword = hashedPassword;
            this.salt = salt;
        }
    }

    public static PasswordResult validateAndHash(String password, String confirmPassword) {
        if (password == null || password.length() < 6) {
            return new PasswordResult("Mot de passe invalide (min 6 caractères).", null, null);
        }
        if (!password.equals(confirmPassword)) {
            return new PasswordResult("Les mots de passe ne correspondent pas.", null, null);
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(password);
        String salt = RandomizeSalt.generateSalt(16);
        return new PasswordResult(null, hashedPassword, salt);
    }
}
