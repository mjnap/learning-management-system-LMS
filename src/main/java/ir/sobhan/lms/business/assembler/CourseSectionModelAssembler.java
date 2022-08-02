package ir.sobhan.lms.business.assembler;

import ir.sobhan.lms.controller.CourseSectionController;
import ir.sobhan.lms.model.dto.outputdto.CourseSectionOutputDTO;
import ir.sobhan.lms.model.entity.CourseSection;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CourseSectionModelAssembler implements RepresentationModelAssembler<CourseSection, EntityModel<CourseSectionOutputDTO>> {
    @Override
    public EntityModel<CourseSectionOutputDTO> toModel(CourseSection entity) {
        CourseSectionOutputDTO dto = entity.toDTO();
        return EntityModel.of(dto,
                linkTo(methodOn(CourseSectionController.class).one(entity.getId())).withSelfRel(),
                linkTo(methodOn(CourseSectionController.class).all(entity.getTerm().getId(),null,null)).withRel("courseSections"));
    }
}
