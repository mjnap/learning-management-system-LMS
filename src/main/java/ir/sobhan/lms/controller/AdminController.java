package ir.sobhan.lms.controller;

import ir.sobhan.lms.business.assembler.*;
import ir.sobhan.lms.dao.*;
import ir.sobhan.lms.model.dto.inputdto.CourseInputDTO;
import ir.sobhan.lms.model.dto.inputdto.InstructorInputDTO;
import ir.sobhan.lms.model.dto.inputdto.StudentInputDTO;
import ir.sobhan.lms.model.dto.inputdto.TermInputDTO;
import ir.sobhan.lms.model.dto.outputdto.InstructorOutputDTO;
import ir.sobhan.lms.model.dto.outputdto.StudentOutputDTO;
import ir.sobhan.lms.model.entity.*;
import ir.sobhan.lms.security.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Date;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminModelAssembler adminAssembler;
    private final UserRepository userRepository;

    // Instructor ---------------------------------------------
    private final InstructorModelAssembler instructorAssembler;
    private final InstructorRepository instructorRepository;

    @PostMapping("/newInstructor")
    public ResponseEntity<?> newInstructor(@RequestBody InstructorInputDTO instructorInputDTO){

        User user = userRepository.findByUserName(instructorInputDTO.getUserName());
        user.setActive(true);
        user.setRole(Role.INSTRUCTOR);

        Instructor instructor = new Instructor(user,
                Rank.valueOf(instructorInputDTO.getRank().toUpperCase()));

        EntityModel<InstructorOutputDTO> instructorModel = instructorAssembler.toModel(instructorRepository.save(instructor));

        return ResponseEntity
                .created(instructorModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(instructorModel);
    }

    @PutMapping("/updateInstructor")
    public ResponseEntity<?> updateInstructor(){//todo
        return null;
    }//todo

    @DeleteMapping("/deleteInstructor/{userName}")
    @Transactional
    public ResponseEntity<?> deleteInstructor(@PathVariable String userName){
        instructorRepository.deleteByUser_UserName(userName);
        return ResponseEntity.noContent().build();
    }

    // Student -----------------------------------------
    private final StudentModelAssembler studentAssembler;
    private final StudentRepository studentRepository;

    @PostMapping("/newStudent")
    public ResponseEntity<?> newStudent(@RequestBody StudentInputDTO studentInputDTO){

        User user = userRepository.findByUserName(studentInputDTO.getUserName());
        user.setActive(true);
        user.setRole(Role.STUDENT);

        Student student = new Student(user,
                studentInputDTO.getStudentId(),
                Degree.valueOf(studentInputDTO.getDegree().toUpperCase()),
                new Date());

        EntityModel<StudentOutputDTO> studentModel = studentAssembler.toModel(studentRepository.save(student));

        return ResponseEntity
                .created(studentModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(studentModel);
    }

    @PutMapping("/updateStudent")
    public ResponseEntity<?> updateStudent(){//todo
        return null;
    }//todo

    @DeleteMapping("/deleteStudent/{userName}")
    @Transactional
    public ResponseEntity<?> deleteStudent(@PathVariable String userName){
        studentRepository.deleteByUser_UserName(userName);
        return ResponseEntity.noContent().build();
    }

    // Term ---------------------------------------
    private final TermModelAssembler termAssembler;
    private final TermRepository termRepository;

    @PostMapping("/newTerm")
    public ResponseEntity<?> newTerm(@RequestBody TermInputDTO termInputDTO){

        Term newTerm = termInputDTO.toEntity();

        EntityModel<Term> termModel = termAssembler.toModel(termRepository.save(newTerm));

        return ResponseEntity
                .created(termModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(termModel);
    }

    @PutMapping("/updateTerm")
    public ResponseEntity<?> updateTerm(){return null;}//todo

    @DeleteMapping("/deleteTerm/{id}")
    @Transactional
    public ResponseEntity<?> deleteTerm(@PathVariable Long id){
        termRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Course -----------------------------------------
    private final CourseModelAssembler courseAssembler;
    private final CourseRepository courseRepository;

    @PostMapping("/newCourse")
    public ResponseEntity<?> newCourse(@RequestBody CourseInputDTO courseInputDTO){

        Course newCourse = courseInputDTO.toEntity();

        EntityModel<Course> courseModel = courseAssembler.toModel(courseRepository.save(newCourse));

        return ResponseEntity
                .created(courseModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(courseModel);
    }

    @PutMapping("/updateCourse")
    public ResponseEntity<?> updateCourse(){return null;}//todo

    @DeleteMapping("/deleteCourse/{id}")
    @Transactional
    public ResponseEntity<?> deleteCurse(@PathVariable Long id){
        courseRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

