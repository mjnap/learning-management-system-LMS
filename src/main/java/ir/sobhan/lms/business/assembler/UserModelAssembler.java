package ir.sobhan.lms.business.assembler;

import ir.sobhan.lms.controller.UserController;
import ir.sobhan.lms.model.entity.User;
import ir.sobhan.lms.model.dto.outputdto.UserOutputDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.stereotype.Component;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<UserOutputDTO>> {
    @Override
    public EntityModel<UserOutputDTO> toModel(User entity) {
        UserOutputDTO userDto = entity.toDTO();
        return EntityModel.of(userDto,
                linkTo(methodOn(UserController.class).one(entity.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).all()).withRel("users"));
    }
}
