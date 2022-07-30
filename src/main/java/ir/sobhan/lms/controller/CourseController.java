package ir.sobhan.lms.controller;

import ir.sobhan.lms.business.assembler.CourseModelAssembler;
import ir.sobhan.lms.business.exceptions.CourseNotFoundException;
import ir.sobhan.lms.dao.CourseRepository;
import ir.sobhan.lms.model.entity.Course;
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
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseRepository courseRepository;
    private final CourseModelAssembler courseAssembler;

    @GetMapping()
    public CollectionModel<EntityModel<Course>> all(){

        List<EntityModel<Course>> modelList = courseRepository.findAll().stream()
                .map(courseAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(modelList,
                linkTo(methodOn(CourseController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Course> one(@PathVariable Long id){

        Course course = courseRepository.findById(id).orElseThrow(() -> new CourseNotFoundException(id));

        return courseAssembler.toModel(course);
    }
}
