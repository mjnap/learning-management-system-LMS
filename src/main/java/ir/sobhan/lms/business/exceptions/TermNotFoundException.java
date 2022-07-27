package ir.sobhan.lms.business.exceptions;

public class TermNotFoundException extends RuntimeException{
    public TermNotFoundException(Long id) {
        super("Could not find term " + id);
    }
}
