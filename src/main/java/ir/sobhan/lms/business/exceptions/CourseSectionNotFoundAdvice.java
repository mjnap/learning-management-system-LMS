package ir.sobhan.lms.business.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CourseSectionNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(CourseSectionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String CourseSectionNotFoundHandler(CourseSectionNotFoundException e) {
        return e.getMessage();
    }
}
