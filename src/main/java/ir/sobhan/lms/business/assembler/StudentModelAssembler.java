package ir.sobhan.lms.business.assembler;

import ir.sobhan.lms.controller.StudentController;
import ir.sobhan.lms.model.Student;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class StudentModelAssembler implements RepresentationModelAssembler<Student, EntityModel<Student>> {
    @Override
    public EntityModel<Student> toModel(Student entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(StudentController.class).one(entity.getUser().getUserName())).withSelfRel(),
                linkTo(methodOn(StudentController.class).all()).withRel("students"));
    }
}
