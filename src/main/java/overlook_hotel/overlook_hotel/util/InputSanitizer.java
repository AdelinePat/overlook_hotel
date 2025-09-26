package overlook_hotel.overlook_hotel.util;

public class InputSanitizer {
    /**
     * Basic input sanitization: trims, removes HTML tags, and escapes dangerous characters.
     */
    public static String sanitize(String input) {
        if (input == null) return null;
        // Remove leading/trailing whitespace
        input = input.trim();
        // Remove HTML tags (basic)
        input = input.replaceAll("<[^>]*>", "");
        // Escape quotes and other special chars
        input = input.replace("\"", "");
        input = input.replace("'", "");
        return input;
    }
}
