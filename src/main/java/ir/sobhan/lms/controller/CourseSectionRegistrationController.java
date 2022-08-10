package ir.sobhan.lms.controller;

import ir.sobhan.lms.model.dto.other.GradingInputDTO;
import ir.sobhan.lms.model.entity.CourseSection;
import ir.sobhan.lms.model.entity.CourseSectionRegistration;
import ir.sobhan.lms.model.entity.Student;
import ir.sobhan.lms.service.CourseSectionRegistrationService;
import ir.sobhan.lms.service.CourseSectionService;
import ir.sobhan.lms.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course-section-registrations")
@RequiredArgsConstructor
public class CourseSectionRegistrationController {

    private final CourseSectionService courseSectionService;
    private final SecurityService securityService;
    private final CourseSectionRegistrationService courseSectionRegistrationService;

    @PostMapping("/register-course")
    public ResponseEntity<?> registerCourse(@RequestParam Long courseSectionId, Authentication authentication) {
        CourseSection courseSection = courseSectionService.getOne(courseSectionId);
        Student student = securityService.getStudent(authentication);

        courseSectionRegistrationService.save(new CourseSectionRegistration(courseSection, student));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Registration is done");
    }

    @PutMapping("/grading")
    public ResponseEntity<?> grading(@RequestBody GradingInputDTO gradingInputDTO,
                                     Authentication authentication) {
        CourseSection courseSection = courseSectionService.getOne(gradingInputDTO.getCourseSectionId());

        if (!securityService.isInstructorOrAdmin(courseSection, authentication))
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("You are not the instructor of this course section");

        gradingInputDTO.getStudentsScore()
                .forEach(studentInfoDTO -> courseSectionRegistrationService.addScore(courseSection.getId(), studentInfoDTO));

        return ResponseEntity
                .ok("Scores were recorded");
    }
}
