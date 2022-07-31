package ir.sobhan.lms.service;

import ir.sobhan.lms.model.entity.CourseSectionRegistration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceTest {

    List<CourseSectionRegistration> courseSectionList = new ArrayList<>();
    final StudentService studentService = new StudentService();

    @BeforeEach
    void setUp() {
        CourseSectionRegistration courseSectionRegistration1 = new CourseSectionRegistration();
        courseSectionRegistration1.setScore(19.5);

        CourseSectionRegistration courseSectionRegistration2 = new CourseSectionRegistration();
        courseSectionRegistration2.setScore(18.25);

        CourseSectionRegistration courseSectionRegistration3 = new CourseSectionRegistration();
        courseSectionRegistration3.setScore(17d);

        courseSectionList.addAll(Arrays.asList(courseSectionRegistration1,courseSectionRegistration2,courseSectionRegistration3));
    }

    @Test
    void average() {
        assertEquals(18.25 , studentService.average(courseSectionList));

        courseSectionList.clear();

        assertThrows(NoSuchElementException.class, () -> studentService.average(courseSectionList));
    }
}