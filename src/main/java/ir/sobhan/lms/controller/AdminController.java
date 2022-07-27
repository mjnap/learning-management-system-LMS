package ir.sobhan.lms.controller;

import ir.sobhan.lms.business.assembler.AdminModelAssembler;
import ir.sobhan.lms.business.assembler.InstructorModelAssembler;
import ir.sobhan.lms.business.assembler.StudentModelAssembler;
import ir.sobhan.lms.business.assembler.TermModelAssembler;
import ir.sobhan.lms.dao.InstructorRepository;
import ir.sobhan.lms.dao.StudentRepository;
import ir.sobhan.lms.dao.TermRepository;
import ir.sobhan.lms.dao.UserRepository;
import ir.sobhan.lms.model.*;
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

    @PostMapping("/newInstructor/{userName}")
    public ResponseEntity<?> newInstructor(@PathVariable String userName){

        User user = userRepository.findByUserName(userName);
        user.setActive(true);

        Instructor instructor = new Instructor(user, Rank.ASSISTANT);//todo رنک آن کاربر و یوزرنیم او باید به صورت رکویست بادی گرفته شود

        EntityModel<Instructor> instructorModel = instructorAssembler.toModel(instructorRepository.save(instructor));

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

    @PostMapping("/newStudent/{userName}")
    public ResponseEntity<?> newStudent(@PathVariable String userName){

        User user = userRepository.findByUserName(userName);
        user.setActive(true);

        Student student = new Student(user,"40021160040", Degree.BS, new Date());

        EntityModel<Student> studentModel = studentAssembler.toModel(studentRepository.save(student));

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

    @PostMapping("/newTerm/{title}")
    public ResponseEntity<?> newTerm(@PathVariable String title){

        Term newTerm = new Term(title, true);

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

    // Course
}

