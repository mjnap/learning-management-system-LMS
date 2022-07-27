package ir.sobhan.lms.business.exceptions;

public class InstructorNotFoundException extends RuntimeException{
    public InstructorNotFoundException(Long id) {
        super("Could not find instructor " + id);
    }
}
