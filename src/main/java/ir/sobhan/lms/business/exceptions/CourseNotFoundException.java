package ir.sobhan.lms.business.exceptions;

public class CourseNotFoundException extends RuntimeException{
    public CourseNotFoundException(Long id) {
        super("Could not find course " + id);
    }
}
