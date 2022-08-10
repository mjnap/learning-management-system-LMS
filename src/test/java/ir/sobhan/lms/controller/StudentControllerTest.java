package ir.sobhan.lms.controller;

import ir.sobhan.lms.dao.StudentRepository;
import ir.sobhan.lms.model.dto.other.SemesterGradesOutputDTO;
import ir.sobhan.lms.model.entity.*;
import ir.sobhan.lms.service.CourseSectionRegistrationService;
import ir.sobhan.lms.service.StudentService;
import ir.sobhan.lms.service.TermService;
import ir.sobhan.lms.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = {StudentController.class, StudentService.class})
class StudentControllerTest {

    final Long termId = 2L;
    final String userName = "ali";

    @MockBean
    Authentication authentication;
    @MockBean
    UserService userService;
    @MockBean
    TermService termService;
    @MockBean
    CourseSectionRegistrationService courseSectionRegistrationService;
    @MockBean
    StudentRepository studentRepository;

    @Autowired
    StudentService studentService;
    @Autowired
    StudentController studentController;


    @BeforeEach
    void setUp() {
        when(authentication.getName()).thenReturn(userName);

        Instructor instructor = new Instructor();
        User user = new User();
        user.setName("Ali");
        instructor.setUser(user);

        Course course = new Course();
        course.setTitle("Math");
        course.setUnits(4);

        CourseSection courseSection = new CourseSection();
        courseSection.setId(1L);
        courseSection.setInstructor(instructor);
        courseSection.setCourse(course);

        CourseSectionRegistration courseSectionRegistration1 = new CourseSectionRegistration();
        courseSectionRegistration1.setCourseSection(courseSection);
        courseSectionRegistration1.setScore(16.26D);

        CourseSectionRegistration courseSectionRegistration2 = new CourseSectionRegistration();
        courseSectionRegistration2.setCourseSection(courseSection);
        courseSectionRegistration2.setScore(18.95D);

        when(courseSectionRegistrationService.findByTermIdAndUserName(termId, userName))
                .thenReturn(Arrays.asList(courseSectionRegistration1, courseSectionRegistration2));

        doNothing().when(termService).checkExist(termId);
    }

    @Test
    void semesterGrades() {
        ResponseEntity<?> responseEntity = studentController.semesterGrades(termId, authentication);
        SemesterGradesOutputDTO semesterGradesOutputDTO = (SemesterGradesOutputDTO) responseEntity.getBody();

        assert semesterGradesOutputDTO != null;
        assertEquals(17.61, semesterGradesOutputDTO.getAverage());

        when(courseSectionRegistrationService.findByTermIdAndUserName(termId, userName))
                .thenReturn(new ArrayList<>());
        responseEntity = studentController.semesterGrades(termId, authentication);
        semesterGradesOutputDTO = (SemesterGradesOutputDTO) responseEntity.getBody();

        assert semesterGradesOutputDTO != null;
        assertEquals(0, semesterGradesOutputDTO.getAverage());
    }
}