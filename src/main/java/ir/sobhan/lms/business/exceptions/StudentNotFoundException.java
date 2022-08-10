package ir.sobhan.lms.business.exceptions;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(Object userName) {
        super("Could not found student " + userName);
    }
}
