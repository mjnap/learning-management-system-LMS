package ir.sobhan.lms.business.assembler;

import ir.sobhan.lms.controller.InstructorController;
import ir.sobhan.lms.model.entity.Instructor;
import ir.sobhan.lms.model.dto.outputdto.InstructorOutputDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@Component
public class InstructorModelAssembler implements RepresentationModelAssembler<Instructor, EntityModel<InstructorOutputDTO>> {
    @Override
    public EntityModel<InstructorOutputDTO> toModel(Instructor entity) {
        InstructorOutputDTO dto = entity.toDTO();
        return EntityModel.of(dto,
                linkTo(methodOn(InstructorController.class).one(entity.getUser().getId())).withSelfRel(),
                linkTo(methodOn(InstructorController.class).all()).withRel("instructors"));
    }
}
