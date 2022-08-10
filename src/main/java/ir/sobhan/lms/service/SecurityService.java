package ir.sobhan.lms.service;

import ir.sobhan.lms.business.exceptions.InstructorNotFoundException;
import ir.sobhan.lms.business.exceptions.StudentNotFoundException;
import ir.sobhan.lms.dao.InstructorRepository;
import ir.sobhan.lms.dao.StudentRepository;
import ir.sobhan.lms.model.entity.CourseSection;
import ir.sobhan.lms.model.entity.Instructor;
import ir.sobhan.lms.model.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final InstructorRepository instructorRepository;
    private final StudentRepository studentRepository;

    public Instructor getInstructor(Authentication authentication) {
        return instructorRepository.findByUser_UserName(authentication.getName())
                .orElseThrow(() -> new InstructorNotFoundException(authentication.getName()));
    }

    public Student getStudent(Authentication authentication) {
        return studentRepository.findByUser_UserName(authentication.getName())
                .orElseThrow(() -> new StudentNotFoundException(authentication.getName()));
    }

    public boolean isInstructorOrAdmin(CourseSection courseSection, Authentication authentication) {
        if (courseSection.getInstructor().getUser().getUserName().equals(authentication.getName()) ||
                authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))
            return true;
        return false;
    }
}
