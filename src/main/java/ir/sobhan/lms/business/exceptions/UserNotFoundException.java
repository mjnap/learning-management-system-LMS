package ir.sobhan.lms.business.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Object id) {
        super("Could not find user " + id);
    }
}
