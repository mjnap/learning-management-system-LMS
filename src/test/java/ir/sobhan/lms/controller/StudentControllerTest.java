package ir.sobhan.lms.controller;

import ir.sobhan.lms.business.assembler.StudentModelAssembler;
import ir.sobhan.lms.dao.CourseSectionRegistrationRepository;
import ir.sobhan.lms.dao.CourseSectionRepository;
import ir.sobhan.lms.dao.StudentRepository;
import ir.sobhan.lms.dao.TermRepository;
import ir.sobhan.lms.model.dto.other.SemesterGradesOutputDTO;
import ir.sobhan.lms.model.entity.*;
import ir.sobhan.lms.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = {StudentController.class , StudentService.class})
class StudentControllerTest {

    final Long termId = 2L;

    @MockBean
    Authentication authentication;
    @MockBean
    CourseSectionRegistrationRepository courseSectionRegistrationRepository;
    @MockBean
    StudentRepository studentRepository;
    @MockBean
    StudentModelAssembler studentAssembler;
    @MockBean
    CourseSectionRepository courseSectionRepository;
    @MockBean
    TermRepository termRepository;

    @Autowired
    StudentController studentController;
    @Autowired
    StudentService studentService;


    @BeforeEach
    void setUp() {
        when(authentication.getName()).thenReturn("ali");

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

        when(courseSectionRegistrationRepository.findAllByCourseSection_Term_IdAndStudent_User_UserName(termId,"ali"))
                .thenReturn(Arrays.asList(courseSectionRegistration1, courseSectionRegistration2));

        when(termRepository.existsById(termId)).thenReturn(true);
    }

    @Test
    void semesterGrades() {
        ResponseEntity<?> responseEntity = studentController.semesterGrades(termId,authentication);
        SemesterGradesOutputDTO semesterGradesOutputDTO = (SemesterGradesOutputDTO) responseEntity.getBody();

        assertEquals(17.61,semesterGradesOutputDTO.getAverage());

        when(courseSectionRegistrationRepository.findAllByCourseSection_Term_IdAndStudent_User_UserName(termId,"ali"))
                .thenReturn(new ArrayList<>());
        responseEntity = studentController.semesterGrades(termId,authentication);
        semesterGradesOutputDTO = (SemesterGradesOutputDTO) responseEntity.getBody();

        assertEquals(0, semesterGradesOutputDTO.getAverage());
    }
}