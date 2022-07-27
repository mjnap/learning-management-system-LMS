package ir.sobhan.lms.controller;

import ir.sobhan.lms.business.assembler.StudentModelAssembler;
import ir.sobhan.lms.business.exceptions.StudentNotFoundException;
import ir.sobhan.lms.dao.StudentRepository;
import ir.sobhan.lms.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository studentRepository;
    private final StudentModelAssembler studentAssembler;

    @GetMapping()
    public CollectionModel<EntityModel<Student>> all(){

        List<EntityModel<Student>> modelList = studentRepository.findAll().stream()
                .map(studentAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(modelList,
                linkTo(methodOn(StudentController.class).all()).withSelfRel());
    }

    @GetMapping("/{userName}")
    public EntityModel<Student> one(@PathVariable String userName){

        Student student = studentRepository.findByUser_UserName(userName).orElseThrow(() -> new StudentNotFoundException(userName));

        return studentAssembler.toModel(student);
    }
}
