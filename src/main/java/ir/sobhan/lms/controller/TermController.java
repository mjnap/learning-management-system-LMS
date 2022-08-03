package ir.sobhan.lms.controller;

import ir.sobhan.lms.business.assembler.TermModelAssembler;
import ir.sobhan.lms.business.exceptions.TermNotFoundException;
import ir.sobhan.lms.dao.TermRepository;
import ir.sobhan.lms.model.dto.outputdto.TermOutputDTO;
import ir.sobhan.lms.model.entity.Term;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/terms")
@RequiredArgsConstructor
public class TermController {

    private final TermRepository termRepository;
    private final TermModelAssembler termAssembler;

    @GetMapping()
    public CollectionModel<EntityModel<TermOutputDTO>> all(){

        List<EntityModel<TermOutputDTO>> modelList = termRepository.findAll().stream()
                .map(termAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(modelList,
                linkTo(methodOn(TermController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<TermOutputDTO> one(@PathVariable Long id){

        Term term = termRepository.findById(id).orElseThrow(() -> new TermNotFoundException(id));

        return termAssembler.toModel(term);
    }
}
