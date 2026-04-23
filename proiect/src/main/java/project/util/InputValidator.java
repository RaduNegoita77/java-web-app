package project.util;

import project.exception.InvalidInputException;

public class InputValidator {

    public static String validateString(String input) throws InvalidInputException {
        if (input == null) throw new InvalidInputException("Input cannot be null.");
        String trimmed = input.trim();
        if (trimmed.isEmpty()) throw new InvalidInputException("Input cannot be empty.");
        return trimmed;
    }

    public static int validateInteger(String input) throws InvalidInputException {
        if (input == null) throw new InvalidInputException("Number input cannot be null.");
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Invalid integer: " + input);
        }
    }

    public static <T> T requireNotNull(T obj, String message) throws InvalidInputException {
        if (obj == null) throw new InvalidInputException(message);
        return obj;
    }
}
