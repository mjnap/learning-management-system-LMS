package ir.sobhan.lms.business.assembler;

import ir.sobhan.lms.controller.StudentController;
import ir.sobhan.lms.model.entity.Student;
import ir.sobhan.lms.model.dto.outputdto.StudentOutputDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class StudentModelAssembler implements RepresentationModelAssembler<Student, EntityModel<StudentOutputDTO>> {
    @Override
    public EntityModel<StudentOutputDTO> toModel(Student entity) {
        StudentOutputDTO dto = entity.toDTO();
        return EntityModel.of(dto,
                linkTo(methodOn(StudentController.class).one(entity.getUser().getUserName())).withSelfRel(),
                linkTo(methodOn(StudentController.class).all()).withRel("students"));
    }
}
