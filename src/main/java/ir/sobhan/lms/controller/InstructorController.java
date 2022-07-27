package ir.sobhan.lms.controller;

import ir.sobhan.lms.business.assembler.InstructorModelAssembler;
import ir.sobhan.lms.business.exceptions.InstructorNotFoundException;
import ir.sobhan.lms.dao.InstructorRepository;
import ir.sobhan.lms.model.Instructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/instructors")
@RequiredArgsConstructor
@Slf4j
public class InstructorController {

    private final InstructorRepository instructorRepository;
    private final InstructorModelAssembler instructorAssembler;

    @GetMapping()
    public CollectionModel<EntityModel<Instructor>> all(){

        List<EntityModel<Instructor>> modelList = instructorRepository.findAll().stream()
                .map(instructorAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(modelList,
                linkTo(methodOn(InstructorController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Instructor> one(@PathVariable Long id){

        Instructor instructor = instructorRepository.findById(id).orElseThrow(() -> new InstructorNotFoundException(id));

        return instructorAssembler.toModel(instructor);
    }
}
