import utils.ValidationUtils;

public class TestValidation {
    public static void main(String[] args) {
        String[] valid = {
            "om.tamrakar@gmail.com",
            "om123@gmail.com",
            "om.tamrakar123@gmail.com"
        };
        
        String[] invalid = {
            "om.tamrakar@gmail",
            "om.tamrakar",
            "om tamrakar@gmail.com",
            "om@tamrakar@gmail.com",
            "om.tamrakar@yahoo.com",
            "om.tamrakar@outlook.com",
            "@gmail.com",
            "om..tamrakar@gmail.com",
            ".om@gmail.com",
            "om.@gmail.com",
            "",
            " "
        };
        
        System.out.println("Valid Tests:");
        for (String email : valid) {
            boolean isValid = utils.ValidationUtils.isValidEmail(email);
            System.out.println(email + " -> " + isValid);
        }

        System.out.println("\nInvalid Tests:");
        for (String email : invalid) {
            boolean isValid = utils.ValidationUtils.isValidEmail(email);
            System.out.println(email + " -> " + isValid);
        }
    }
}
