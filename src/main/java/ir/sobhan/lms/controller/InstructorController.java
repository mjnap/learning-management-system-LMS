package ir.sobhan.lms.controller;

import ir.sobhan.lms.business.assembler.InstructorModelAssembler;
import ir.sobhan.lms.business.exceptions.*;
import ir.sobhan.lms.dao.*;
import ir.sobhan.lms.model.dto.inputdto.CourseSectionInputDTO;
import ir.sobhan.lms.model.dto.other.GradingInputDTO;
import ir.sobhan.lms.model.dto.other.StudentCourseSectionDTO;
import ir.sobhan.lms.model.dto.outputdto.CourseSectionOutputDTO;
import ir.sobhan.lms.model.entity.*;
import ir.sobhan.lms.model.dto.outputdto.InstructorOutputDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;


import javax.transaction.Transactional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/instructors")
@RequiredArgsConstructor
@Slf4j
public class InstructorController {

    private final InstructorRepository instructorRepository;
    private final InstructorModelAssembler instructorAssembler;
    private final CourseRepository courseRepository;
    private final TermRepository termRepository;
    private final CourseSectionRepository courseSectionRepository;
    private final CourseSectionRegistrationRepository courseSectionRegistrationRepository;
    private final UserRepository userRepository;

    @GetMapping()
    public CollectionModel<EntityModel<InstructorOutputDTO>> all(){

        List<EntityModel<InstructorOutputDTO>> modelList = instructorRepository.findAll().stream()
                .map(instructorAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(modelList,
                linkTo(methodOn(InstructorController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<InstructorOutputDTO> one(@PathVariable Long id){

        Instructor instructor = instructorRepository.findById(id).orElseThrow(() -> new InstructorNotFoundException(id));

        return instructorAssembler.toModel(instructor);
    }

    @PostMapping("/new-course-section")
    public ResponseEntity<?> newCourseSection(@RequestBody CourseSectionInputDTO courseSectionInputDTO,
                                              Authentication authentication){

        Instructor instructor = instructorRepository.findByUser_UserName(authentication.getName())
                .orElseThrow(() -> new InstructorNotFoundException(authentication.getName()));

        Course course = courseRepository.findByTitle(courseSectionInputDTO.getCourseTitle())
                .orElseThrow(() -> new CourseNotFoundException(courseSectionInputDTO.getCourseTitle()));

        Term term = termRepository.findById(courseSectionInputDTO.getTermId())
                .orElseThrow(() -> new TermNotFoundException(courseSectionInputDTO.getTermId()));

        CourseSection newCourseSection = new CourseSection(instructor,course,term);

        CourseSectionOutputDTO outputDTO = courseSectionRepository.save(newCourseSection).toDTO();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(outputDTO);
    }

    @PutMapping("/update-course-section/{id}")
    public ResponseEntity<?> updateCourseSection(@PathVariable Long id,
                                                 @RequestBody CourseSectionInputDTO courseSectionInputDTO,
                                                 Authentication authentication){

        CourseSection courseSection = courseSectionRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException(id));

        if(courseSection.getInstructor().getUser().getUserName().equals(authentication.getName()) ||
            userRepository.findByUserName(authentication.getName())
                    .orElseThrow(() -> new UserNotFoundException(authentication.getName()))
                    .getRoles().equals("ADMIN")){

            courseSection.setCourse(courseRepository.findByTitle(courseSectionInputDTO.getCourseTitle())
                    .orElseThrow(() -> new CourseNotFoundException(courseSectionInputDTO.getCourseTitle())));

            courseSection.setTerm(termRepository.findById(courseSectionInputDTO.getTermId())
                    .orElseThrow(() -> new TermNotFoundException(courseSectionInputDTO.getTermId())));

            return ResponseEntity
                    .ok(courseSection.toDTO());
        }

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("You are not the instructor of this group");
    }

    @DeleteMapping("/delete-course-section/{courseSectionId}")
    @Transactional
    public ResponseEntity<?> deleteCourseSection(@PathVariable Long courseSectionId,
                                                 Authentication authentication){

        CourseSection courseSection = courseSectionRepository.findById(courseSectionId)
                .orElseThrow(() -> new CourseNotFoundException(courseSectionId));

        if(!courseSection.getInstructor().getUser().getUserName().equals(authentication.getName()) &&
            !authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("You are not the instructor of this course section");

        if(!courseSection.getCourseSectionRegistrationList().isEmpty())
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("You can not delete this course section");

        courseSectionRepository.delete(courseSection);
        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/get-students-of-course-section/{courseSectionId}")
    public ResponseEntity<?> getStudentsOfCourseSection(@PathVariable Long courseSectionId,
                                                        Authentication authentication){

        CourseSection courseSection = courseSectionRepository.findById(courseSectionId)
                .orElseThrow(() -> new CourseNotFoundException(courseSectionId));

        if(!courseSection.getInstructor().getUser().getUserName().equals(authentication.getName()))
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("You are not the instructor of this course section");


        List<StudentCourseSectionDTO> studentList = new ArrayList<>();

        courseSection.getCourseSectionRegistrationList()
                .forEach(courseSectionRegistration -> studentList.add(StudentCourseSectionDTO.toDTO(courseSectionRegistration)));

        return ResponseEntity
                .ok(studentList);
    }

    @PutMapping("/grading")
    public ResponseEntity<?> grading(@RequestBody GradingInputDTO gradingInputDTO,
                                     Authentication authentication){

        CourseSection courseSection = courseSectionRepository.findById(gradingInputDTO.getCourseSectionId())
                .orElseThrow(() -> new CourseNotFoundException(gradingInputDTO.getCourseSectionId()));

        if(!courseSection.getInstructor().getUser().getUserName().equals(authentication.getName()))
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("You are not the instructor of this course section");

        gradingInputDTO.getStudentsScore().forEach(studentInfoDTO -> {

            CourseSectionRegistration updateCourseSectionRegistration = courseSectionRegistrationRepository
                    .findByCourseSection_IdAndStudent_Id(gradingInputDTO.getCourseSectionId() , studentInfoDTO.getStudentId());

            updateCourseSectionRegistration.setScore(studentInfoDTO.getScore());

            courseSectionRegistrationRepository.save(updateCourseSectionRegistration);
        });

        return ResponseEntity
                .ok("Scores were recorded");
    }
}
