package ir.sobhan.lms.business.assembler;

import ir.sobhan.lms.controller.TermController;
import ir.sobhan.lms.model.Term;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@Component
public class TermModelAssembler implements RepresentationModelAssembler<Term, EntityModel<Term>> {
    @Override
    public EntityModel<Term> toModel(Term entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(TermController.class).one(entity.getId())).withSelfRel(),
                linkTo(methodOn(TermController.class).all()).withRel("terms"));
    }
}
