package ir.sobhan.lms.business.assembler;

import ir.sobhan.lms.controller.InstructorController;
import ir.sobhan.lms.model.Instructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@Component
public class InstructorModelAssembler implements RepresentationModelAssembler<Instructor, EntityModel<Instructor>> {
    @Override
    public EntityModel<Instructor> toModel(Instructor entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(InstructorController.class).one(entity.getUser().getId())).withSelfRel(),
                linkTo(methodOn(InstructorController.class).all()).withRel("instructors"));
    }
}
