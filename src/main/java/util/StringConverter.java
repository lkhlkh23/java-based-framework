package util;

public class StringConverter {

    public static String extractSetterName(String str) {
        return str.substring(3, 4).toLowerCase() + str.substring(4);
    }
}
