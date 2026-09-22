package utils;

public class ValidationUtils {

    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String trimmed = email.trim();
        if (trimmed.equals("@gmail.com") || trimmed.equals("@ssism.org")) {
            return false;
        }
        return trimmed.endsWith("@gmail.com") || trimmed.endsWith("@ssism.org");
    }
}
