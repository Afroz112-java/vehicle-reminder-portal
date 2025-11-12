package net.konic.vehicle.execption;

/**
 * Thrown when invalid or malformed input data is provided by the client.
 */
public class InvalidInputException extends RuntimeException {

    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
