package ir.sobhan.lms.controller;

import ir.sobhan.lms.business.assembler.CourseSectionModelAssembler;
import ir.sobhan.lms.business.assembler.InstructorModelAssembler;
import ir.sobhan.lms.business.exceptions.CourseNotFoundException;
import ir.sobhan.lms.business.exceptions.CourseSectionNotFoundException;
import ir.sobhan.lms.business.exceptions.InstructorNotFoundException;
import ir.sobhan.lms.business.exceptions.TermNotFoundException;
import ir.sobhan.lms.dao.*;
import ir.sobhan.lms.model.dto.inputdto.CourseSectionInputDTO;
import ir.sobhan.lms.model.dto.other.GradingInputDTO;
import ir.sobhan.lms.model.dto.other.StudentCourseSectionDTO;
import ir.sobhan.lms.model.entity.*;
import ir.sobhan.lms.model.dto.outputdto.InstructorOutputDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


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
    private final CourseSectionModelAssembler courseSectionAssembler;
    private final CourseSectionRepository courseSectionRepository;
    private final CourseSectionRegistrationRepository courseSectionRegistrationRepository;

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

    @PostMapping("/newCourseSection")
    public ResponseEntity<?> newCourseSection(@RequestBody CourseSectionInputDTO courseSectionInputDTO,
                                              Authentication authentication){

        Instructor instructor = instructorRepository.findByUser_UserName(authentication.getName());//todo must be Optional

        Course course = courseRepository.findByTitle(courseSectionInputDTO.getCourseTitle());//todo must be Optional

        Term term = termRepository.findById(courseSectionInputDTO.getTermId())
                .orElseThrow(() -> new TermNotFoundException(courseSectionInputDTO.getTermId()));

        CourseSection newCourseSection = new CourseSection(instructor,course,term);

        EntityModel<CourseSection> entityModel = courseSectionAssembler.toModel(
                courseSectionRepository.save(newCourseSection));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(entityModel);
    }

    @PutMapping("/updateCourseSection")
    public ResponseEntity<?> updateCourseSection(){
        return null;
    }//todo

    @DeleteMapping("/deleteCourseSection/{courseSectionId}")
    public ResponseEntity<?> deleteCourseSection(@PathVariable Long courseSectionId){

        CourseSection courseSection = courseSectionRepository.findById(courseSectionId)
                .orElseThrow(() -> new CourseNotFoundException(courseSectionId));

        if(courseSection.getCourseSectionRegistrationList().isEmpty())
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("There are no students");

        courseSectionRepository.delete(courseSection);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getStudentsOfCourseSection/{courseSectionId}")
    public List<StudentCourseSectionDTO> getStudentsOfCourseSection(@PathVariable Long courseSectionId){

        CourseSection courseSection = courseSectionRepository.findById(courseSectionId)
                .orElseThrow(() -> new CourseSectionNotFoundException(courseSectionId));

        List<StudentCourseSectionDTO> studentList = new ArrayList<>();

        courseSection.getCourseSectionRegistrationList()
                .forEach(courseSectionRegistration -> studentList.add(StudentCourseSectionDTO.toDTO(courseSectionRegistration)));

        return studentList;
    }

    @PutMapping("/grading")
    public ResponseEntity<?> grading(@RequestBody GradingInputDTO gradingInputDTO){

        gradingInputDTO.getStudentsScore().forEach(studentInfoDTO -> {

            CourseSectionRegistration updateCourseSectionRegistration = courseSectionRegistrationRepository
                    .findByCourseSection_IdAndStudent_Id(gradingInputDTO.getCourseSectionId() , studentInfoDTO.getStudentId());

            updateCourseSectionRegistration.setScore(studentInfoDTO.getScore());

            courseSectionRegistrationRepository.save(updateCourseSectionRegistration);
        });

        return ResponseEntity.ok("Scores were recorded");
    }
}
