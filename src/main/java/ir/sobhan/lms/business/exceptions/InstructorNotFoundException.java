package ir.sobhan.lms.business.exceptions;

public class InstructorNotFoundException extends RuntimeException{
    public InstructorNotFoundException(Object id) {
        super("Could not find instructor " + id);
    }
}
