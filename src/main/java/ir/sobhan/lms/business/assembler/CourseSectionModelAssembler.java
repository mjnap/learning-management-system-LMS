package ir.sobhan.lms.business.assembler;

import ir.sobhan.lms.controller.CourseSectionController;
import ir.sobhan.lms.model.entity.CourseSection;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CourseSectionModelAssembler implements RepresentationModelAssembler<CourseSection, EntityModel<CourseSection>> {
    @Override
    public EntityModel<CourseSection> toModel(CourseSection entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(CourseSectionController.class).one(entity.getId())).withSelfRel(),
                linkTo(methodOn(CourseSectionController.class).all(entity.getId())).withRel("courseSections"));
    }
}