package overlook_hotel.overlook_hotel.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Arrays;

public class DatabaseEnumUtils {
    /**
     * Parses a MySQL enum column definition string and returns the list of values.
     * Example input: "enum('A','B','C')"
     */
    public static List<String> parseEnumValues(String columnType) {
        Matcher m = Pattern.compile("enum\\((.*)\\)").matcher(columnType);
        if (m.find()) {
            String enums = m.group(1);
            String[] values = enums.split(",");
            return Arrays.stream(values)
                .map(s -> s.replace("'", "").trim())
                .toList();
        }
        return List.of();
    }
}
