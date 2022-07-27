package ir.sobhan.lms.business.exceptions;

public class StudentNotFoundException extends RuntimeException{
    public StudentNotFoundException(String userName) {
        super("Could not found student " + userName);
    }
}
