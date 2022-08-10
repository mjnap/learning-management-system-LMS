package ir.sobhan.lms.business.exceptions;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(Object id) {
        super("Could not find course " + id);
    }
}
