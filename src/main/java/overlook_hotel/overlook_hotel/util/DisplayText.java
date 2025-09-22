package overlook_hotel.overlook_hotel.util;

import org.springframework.stereotype.Component;

@Component("displayText")
public class DisplayText {
    public String capitalize(String input) {
        if (input == null || input.isEmpty()) return "";
        String lower = input.toLowerCase();
        return deleteUnderscore(lower.substring(0,1).toUpperCase() + lower.substring(1));
    }

    public String deleteUnderscore(String input) {
        if (input == null || input.isEmpty()) return "";
        return input.replace("_", " ");
    }
}