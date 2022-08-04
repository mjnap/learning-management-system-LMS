package ir.sobhan.lms.controller;

import ir.sobhan.lms.business.assembler.*;
import ir.sobhan.lms.business.exceptions.*;
import ir.sobhan.lms.dao.*;
import ir.sobhan.lms.model.dto.inputdto.*;
import ir.sobhan.lms.model.dto.outputdto.CourseOutputDTO;
import ir.sobhan.lms.model.dto.outputdto.InstructorOutputDTO;
import ir.sobhan.lms.model.dto.outputdto.StudentOutputDTO;
import ir.sobhan.lms.model.dto.outputdto.TermOutputDTO;
import ir.sobhan.lms.model.entity.*;
import ir.sobhan.lms.security.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Date;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;

    // Instructor ---------------------------------------------
    private final InstructorModelAssembler instructorAssembler;
    private final InstructorRepository instructorRepository;

    @PostMapping("/new-instructor")
    public ResponseEntity<?> newInstructor(@RequestBody InstructorInputDTO instructorInputDTO){

        User user = userRepository.findByUserName(instructorInputDTO.getUserName())
                .orElseThrow(() -> new UserNotFoundException(instructorInputDTO.getUserName()));
        user.setActive(true);
        String roles = user.getRoles();
        roles += " "+Role.INSTRUCTOR.name();
        user.setRoles(roles);

        Instructor instructor = new Instructor(user,
                Rank.valueOf(instructorInputDTO.getRank().toUpperCase()));

        EntityModel<InstructorOutputDTO> instructorModel = instructorAssembler.toModel(instructorRepository.save(instructor));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(instructorModel);
    }

    @PutMapping("/update-instructor/{instructorId}")
    public ResponseEntity<?> updateInstructor(@PathVariable Long instructorId,
                                              @RequestBody InstructorUpdateInputDTO instructorUpdateInputDTO){

        Instructor updateInstructor = instructorRepository.findById(instructorId)
                .map(instructor -> {
                    User user = instructor.getUser();
                    user.setName(instructorUpdateInputDTO.getName());
                    instructor.setUser(user);
                    instructor.setLevel(Rank.valueOf(instructorUpdateInputDTO.getRank().toUpperCase()));
                    return instructorRepository.save(instructor);
                })
                .orElseThrow(() -> new InstructorNotFoundException(instructorId));

        return ResponseEntity
                .ok(instructorAssembler.toModel(updateInstructor));
    }

    @DeleteMapping("/delete-instructor/{userName}")
    @Transactional
    public ResponseEntity<?> deleteInstructor(@PathVariable String userName){

        instructorRepository.deleteByUser_UserName(userName);

        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException(userName));

        String[] roles = user.getRoles().split(" ");
        String newRoles = "";
        for(String role:roles)
            if(!role.equals(Role.INSTRUCTOR.name()))
                newRoles += role;

        user.setRoles(newRoles);
        user.setActive(false);
        userRepository.save(user);

        return ResponseEntity
                .noContent()
                .build();
    }

    // Student -----------------------------------------
    private final StudentModelAssembler studentAssembler;
    private final StudentRepository studentRepository;

    @PostMapping("/new-student")
    public ResponseEntity<?> newStudent(@RequestBody StudentInputDTO studentInputDTO){

        User user = userRepository.findByUserName(studentInputDTO.getUserName())
                .orElseThrow(() -> new UserNotFoundException(studentInputDTO.getUserName()));
        user.setActive(true);
        String roles = user.getRoles();
        roles += " "+Role.STUDENT.name();
        user.setRoles(roles);

        Student student = new Student(user,
                studentInputDTO.getStudentId(),
                Degree.valueOf(studentInputDTO.getDegree().toUpperCase()),
                new Date());

        EntityModel<StudentOutputDTO> studentModel = studentAssembler.toModel(studentRepository.save(student));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(studentModel);
    }

    @PutMapping("/update-student/{studentId}")
    public ResponseEntity<?> updateStudent(@PathVariable Long studentId,
                                           @RequestBody StudentUpdateInputDTO studentUpdateInputDTO){

        Student updateStudent = studentRepository.findById(studentId)
                .map(student -> {
                    User user = student.getUser();
                    user.setName(studentUpdateInputDTO.getName());
                    student.setUser(user);
                    student.setDegree(Degree.valueOf(studentUpdateInputDTO.getDegree()));
                    return studentRepository.save(student);
                })
                .orElseThrow(() -> new StudentNotFoundException(studentId));

        return ResponseEntity
                .ok(studentAssembler.toModel(updateStudent));
    }

    @DeleteMapping("/delete-student/{userName}")
    @Transactional
    public ResponseEntity<?> deleteStudent(@PathVariable String userName){
        studentRepository.deleteByUser_UserName(userName);

        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException(userName));

        String[] roles = user.getRoles().split(" ");
        String newRoles = "";
        for(String role:roles)
            if(!role.equals(Role.STUDENT.name()))
                newRoles += role;

        user.setRoles(newRoles);
        user.setActive(false);
        userRepository.save(user);

        return ResponseEntity
                .noContent()
                .build();
    }

    // Term ---------------------------------------
    private final TermModelAssembler termAssembler;
    private final TermRepository termRepository;

    @PostMapping("/new-term")
    public ResponseEntity<?> newTerm(@RequestBody TermInputDTO termInputDTO){

        Term newTerm = termInputDTO.toEntity();

        EntityModel<TermOutputDTO> termModel = termAssembler.toModel(termRepository.save(newTerm));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(termModel);
    }

    @PutMapping("/update-term/{termId}")
    public ResponseEntity<?> updateTerm(@PathVariable Long termId,
                                        @RequestBody TermInputDTO termInputDTO){

        Term updateTerm = termRepository.findById(termId)
                .map(term -> {
                    term.setTitle(termInputDTO.getTitle());
                    term.setOpen(termInputDTO.isOpen());
                    return termRepository.save(term);
                })
                .orElseThrow(() -> new TermNotFoundException(termId));

        return ResponseEntity
                .ok(updateTerm.toDTO());
    }

    @DeleteMapping("/delete-term/{id}")
    @Transactional
    public ResponseEntity<?> deleteTerm(@PathVariable Long id){
        termRepository.deleteById(id);
        return ResponseEntity
                .noContent()
                .build();
    }

    // Course -----------------------------------------
    private final CourseModelAssembler courseAssembler;
    private final CourseRepository courseRepository;

    @PostMapping("/new-course")
    public ResponseEntity<?> newCourse(@RequestBody CourseInputDTO courseInputDTO){

        Course newCourse = courseInputDTO.toEntity();

        EntityModel<CourseOutputDTO> courseModel = courseAssembler.toModel(courseRepository.save(newCourse));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(courseModel);
    }

    @PutMapping("/update-course/{courseId}")
    public ResponseEntity<?> updateCourse(@PathVariable Long courseId,
                                          @RequestBody CourseInputDTO courseInputDTO){

        Course updateCourse = courseRepository.findById(courseId)
                .map(course -> {
                    course.setTitle(courseInputDTO.getTitle());
                    course.setUnits(courseInputDTO.getUnits());
                    return courseRepository.save(course);
                })
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        return ResponseEntity
                .ok(updateCourse.toDTO());
    }

    @DeleteMapping("/delete-course/{id}")
    @Transactional
    public ResponseEntity<?> deleteCurse(@PathVariable Long id){
        courseRepository.deleteById(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}

