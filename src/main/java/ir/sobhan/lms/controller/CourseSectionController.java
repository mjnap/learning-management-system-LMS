package ir.sobhan.lms.controller;

import ir.sobhan.lms.business.assembler.CourseSectionModelAssembler;
import ir.sobhan.lms.business.exceptions.CourseSectionNotFoundException;
import ir.sobhan.lms.dao.CourseSectionRepository;
import ir.sobhan.lms.model.entity.CourseSection;
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
@RequestMapping("/courseSections")
public class CourseSectionController {

    private final CourseSectionModelAssembler courseSectionAssembler;
    private final CourseSectionRepository courseSectionRepository;

    @GetMapping("/{termId}")
    public CollectionModel<EntityModel<CourseSection>> all(@PathVariable Long termId){

        List<EntityModel<CourseSection>> modelList = courseSectionRepository.findAllByTerm_Id(termId).stream()
                .map(courseSectionAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(modelList,
                linkTo(methodOn(CourseSectionController.class).all(termId)).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<CourseSection> one(@PathVariable Long id){

        CourseSection courseSection = courseSectionRepository.findById(id).orElseThrow(() -> new CourseSectionNotFoundException(id));

        return courseSectionAssembler.toModel(courseSection);
    }
}
