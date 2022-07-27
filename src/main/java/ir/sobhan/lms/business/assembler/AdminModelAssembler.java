package ir.sobhan.lms.business.assembler;

import ir.sobhan.lms.controller.InstructorController;
import ir.sobhan.lms.controller.UserController;
import ir.sobhan.lms.model.User;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class AdminModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {

    @Override
    public EntityModel<User> toModel(User entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(UserController.class).one(entity.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).all()).withRel("users"),
                linkTo(methodOn(InstructorController.class).all()).withRel("instructors"));//todo add student
    }
}
