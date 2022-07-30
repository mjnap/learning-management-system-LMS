package ir.sobhan.lms.controller;

import ir.sobhan.lms.business.assembler.StudentModelAssembler;
import ir.sobhan.lms.business.exceptions.CourseSectionNotFoundException;
import ir.sobhan.lms.business.exceptions.StudentNotFoundException;
import ir.sobhan.lms.business.exceptions.TermNotFoundException;
import ir.sobhan.lms.dao.CourseSectionRegistrationRepository;
import ir.sobhan.lms.dao.CourseSectionRepository;
import ir.sobhan.lms.dao.StudentRepository;
import ir.sobhan.lms.dao.TermRepository;
import ir.sobhan.lms.model.dto.inputdto.CourseSectionRegistrationInputDTO;
import ir.sobhan.lms.model.dto.other.SemesterGradesOutputDTO;
import ir.sobhan.lms.model.dto.outputdto.CourseSectionOutputDTO;
import ir.sobhan.lms.model.entity.CourseSectionRegistration;
import ir.sobhan.lms.model.entity.Student;
import ir.sobhan.lms.model.dto.outputdto.StudentOutputDTO;
import ir.sobhan.lms.model.entity.Term;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository studentRepository;
    private final StudentModelAssembler studentAssembler;
    private final CourseSectionRepository courseSectionRepository;
    private final CourseSectionRegistrationRepository courseSectionRegistrationRepository;
    private final TermRepository termRepository;

    @GetMapping()
    public CollectionModel<EntityModel<StudentOutputDTO>> all(){

        List<EntityModel<StudentOutputDTO>> modelList = studentRepository.findAll().stream()
                .map(studentAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(modelList,
                linkTo(methodOn(StudentController.class).all()).withSelfRel());
    }

    @GetMapping("/{userName}")
    public EntityModel<StudentOutputDTO> one(@PathVariable String userName){

        Student student = studentRepository.findByUser_UserName(userName).orElseThrow(() -> new StudentNotFoundException(userName));

        return studentAssembler.toModel(student);
    }

    @PostMapping("/registerCourse")
    public ResponseEntity<?> registerCourse(@RequestBody CourseSectionRegistrationInputDTO courseSectionRegistrationInputDTO){

        CourseSectionRegistration courseSectionRegistration = new CourseSectionRegistration(
                courseSectionRepository.findById(courseSectionRegistrationInputDTO.getCourseSectionId())
                        .orElseThrow(() -> new CourseSectionNotFoundException(courseSectionRegistrationInputDTO.getCourseSectionId())),
                studentRepository.findByUser_UserName(courseSectionRegistrationInputDTO.getUserName())
                        .orElseThrow(() -> new StudentNotFoundException(courseSectionRegistrationInputDTO.getUserName())));

        courseSectionRegistrationRepository.save(courseSectionRegistration);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(courseSectionRegistration);
    }

    @GetMapping("/semesterGrades/{termId}")
    public SemesterGradesOutputDTO semesterGrades(@PathVariable Long termId){//todo test it

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        List<CourseSectionRegistration> courseList = courseSectionRegistrationRepository
                .findAllByCourseSection_Term_IdAndStudent_User_UserName(termId, userName);

        Double avg = courseList.stream()
                .map(CourseSectionRegistration::getScore)
                .reduce(Double::sum)
                .get() / courseList.size();

        List<CourseSectionOutputDTO> sectionOutputDTOList = new ArrayList<>();
        courseList.forEach(course -> {
            sectionOutputDTOList.add(CourseSectionOutputDTO.builder()
                    .CourseSectionId(course.getCourseSection().getId())
                    .course(course.getCourseSection().getCourse().getTitle())
                    .units(course.getCourseSection().getCourse().getUnits())
                    .instructorName(course.getCourseSection().getInstructor().getUser().getName())
                    .score(course.getScore())
                    .build());
        });

        return SemesterGradesOutputDTO.builder()
                .average(avg)
                .courseSectionList(sectionOutputDTOList)
                .build();
    }

}
