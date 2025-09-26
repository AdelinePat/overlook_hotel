package overlook_hotel.overlook_hotel.util;

import java.util.regex.Pattern;

public class InputValidator {
    private static final Pattern WORD_PATTERN = Pattern.compile("^[A-Za-zÀ-ÖØ-öø-ÿ\\-\\s']{1,50}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9\\-\\+\\s]{6,20}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^.{6,100}$"); // At least 6 chars

    public static boolean isValidWord(String input) {
        return input != null && WORD_PATTERN.matcher(input.trim()).matches();
    }

    public static boolean isValidEmail(String input) {
        return input != null && EMAIL_PATTERN.matcher(input.trim()).matches();
    }

    public static boolean isValidPhone(String input) {
        return input != null && PHONE_PATTERN.matcher(input.trim()).matches();
    }

    public static boolean isValidPassword(String input) {
        return input != null && PASSWORD_PATTERN.matcher(input).matches();
    }
}
