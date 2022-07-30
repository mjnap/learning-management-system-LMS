package ir.sobhan.lms.business.assembler;

import ir.sobhan.lms.controller.CourseController;
import ir.sobhan.lms.model.entity.Course;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CourseModelAssembler implements RepresentationModelAssembler<Course, EntityModel<Course>> {
    @Override
    public EntityModel<Course> toModel(Course entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(CourseController.class).one(entity.getId())).withSelfRel(),
                linkTo(methodOn(CourseController.class).all()).withRel("courses"));
    }
}
