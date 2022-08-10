package ir.sobhan.lms.business.exceptions;

public class CourseSectionNotFoundException extends RuntimeException {
    public CourseSectionNotFoundException(Long id) {
        super("Could not find course section " + id);
    }
}
