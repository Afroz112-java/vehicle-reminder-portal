package net.konic.vehicle.utils;
import net.konic.vehicle.execption.InvalidInputException;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
@Component
public class CsvValidationUtils {

    public static String required(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidInputException(fieldName + " is missing.");
        }
        return value.trim();
    }

    public static String validateEmail(String email) {
        email = required(email, "Email");

        if (!email.contains("@")) {
            throw new InvalidInputException("Invalid email: " + email);
        }
        return email;
    }

    public static String validatePhone(String phone) {
        phone = required(phone, "Phone number");

        phone = phone.trim();

        if (!phone.matches("\\d+")) {
            throw new InvalidInputException("Phone number must contain only digits.");
        }

        if (phone.length() != 10) {
            throw new InvalidInputException("Phone number must be 10 digits.");
        }

        if (phone.matches("0+")) {
            throw new InvalidInputException("Phone number cannot be all zeros.");
        }

        return phone;
    }

    public static LocalDate validateDate(String dateString, String fieldName, DateTimeFormatter formatter) {
        dateString = required(dateString, fieldName);

        try {
            return LocalDate.parse(dateString.trim(), formatter);
        } catch (Exception e) {
            throw new InvalidInputException("Invalid " + fieldName + ": " + dateString);
        }
    }
}
