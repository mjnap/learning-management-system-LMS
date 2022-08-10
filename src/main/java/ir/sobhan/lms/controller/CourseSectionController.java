package ir.sobhan.lms.controller;

import ir.sobhan.lms.model.dto.inputdto.CourseSectionInputDTO;
import ir.sobhan.lms.model.dto.outputdto.CourseSectionOutputDTO;
import ir.sobhan.lms.model.entity.Course;
import ir.sobhan.lms.model.entity.CourseSection;
import ir.sobhan.lms.model.entity.Instructor;
import ir.sobhan.lms.model.entity.Term;
import ir.sobhan.lms.service.CourseSectionService;
import ir.sobhan.lms.service.CourseService;
import ir.sobhan.lms.service.SecurityService;
import ir.sobhan.lms.service.TermService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/course-sections")
public class CourseSectionController {

    private final CourseSectionService courseSectionService;
    private final SecurityService securityService;
    private final CourseService courseService;
    private final TermService termService;

    @GetMapping()
    public List<CourseSectionOutputDTO> all(@RequestParam Long termId,
                                            @RequestParam @Nullable String instructorName,
                                            @RequestParam @Nullable String courseTitle,
                                            @RequestParam int size,
                                            @RequestParam int page) {

        return courseSectionService.getAll(termId, instructorName, courseTitle, size, page);
    }

    @GetMapping("/{id}")
    public CourseSectionOutputDTO one(@PathVariable Long id) {
        return courseSectionService.getOne(id).toDTO();
    }

    @PostMapping("/create")
    public ResponseEntity<?> newCourseSection(@RequestBody CourseSectionInputDTO courseSectionInputDTO,
                                              Authentication authentication) {

        Instructor instructor = securityService.getInstructor(authentication);
        Course course = courseService.getOne(courseSectionInputDTO.getCourseTitle());
        Term term = termService.getOne(courseSectionInputDTO.getTermId());

        if (!term.isOpen())
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("The term is not open");

        CourseSection newCourseSection = new CourseSection(instructor, course, term);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(courseSectionService.save(newCourseSection).toDTO());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCourseSection(@PathVariable Long id,
                                                 @RequestBody CourseSectionInputDTO courseSectionInputDTO,
                                                 Authentication authentication) {

        CourseSection courseSection = courseSectionService.getOne(id);

        if (securityService.isInstructorOrAdmin(courseSection, authentication)) {
            courseSectionService.update(courseSection, courseSectionInputDTO);
            return ResponseEntity
                    .ok(courseSection.toDTO());
        }

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("You are not the instructor of this group");
    }

    @DeleteMapping("/delete/{courseSectionId}")
    @Transactional
    public ResponseEntity<?> deleteCourseSection(@PathVariable Long courseSectionId,
                                                 Authentication authentication) {

        CourseSection courseSection = courseSectionService.getOne(courseSectionId);

        if (!securityService.isInstructorOrAdmin(courseSection, authentication))
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("You are not the instructor of this course section");

        if (!courseSection.getCourseSectionRegistrationList().isEmpty())
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("You can not delete this course section");

        courseSectionService.delete(courseSectionId);
        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/get-students/{courseSectionId}")
    public ResponseEntity<?> getStudentsOfCourseSection(@PathVariable Long courseSectionId,
                                                        Authentication authentication) {

        CourseSection courseSection = courseSectionService.getOne(courseSectionId);

        if (!securityService.isInstructorOrAdmin(courseSection, authentication))
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("You are not the instructor of this course section");

        return ResponseEntity
                .ok(courseSectionService.getStudentsList(courseSection));
    }
}
