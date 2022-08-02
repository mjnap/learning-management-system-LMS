package ir.sobhan.lms.controller;

import ir.sobhan.lms.business.assembler.CourseSectionModelAssembler;
import ir.sobhan.lms.business.exceptions.CourseSectionNotFoundException;
import ir.sobhan.lms.dao.CourseSectionRepository;
import ir.sobhan.lms.model.dto.outputdto.CourseSectionOutputDTO;
import ir.sobhan.lms.model.entity.CourseSection;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courseSections")
public class CourseSectionController {

    private final CourseSectionModelAssembler courseSectionAssembler;
    private final CourseSectionRepository courseSectionRepository;

    @GetMapping("/{termId}")
    public CollectionModel<EntityModel<CourseSectionOutputDTO>> all(@PathVariable Long termId,
                                                                    @RequestParam @Nullable String instructorName,
                                                                    @RequestParam @Nullable String courseTitle){

        List<EntityModel<CourseSectionOutputDTO>> modelList;

        if(instructorName != null && courseTitle != null){
            modelList = courseSectionRepository.findAllByTerm_IdAndInstructor_User_NameAndCourse_Title(termId,instructorName,courseTitle).stream()
                    .map(courseSectionAssembler::toModel)
                    .collect(Collectors.toList());
        }
        else if(instructorName != null){
            modelList = courseSectionRepository.findAllByTerm_IdAndInstructor_User_Name(termId, instructorName).stream()
                    .map(courseSectionAssembler::toModel)
                    .collect(Collectors.toList());
        }
        else if(courseTitle != null){
            modelList = courseSectionRepository.findAllByTerm_IdAndCourse_Title(termId, courseTitle).stream()
                    .map(courseSectionAssembler::toModel)
                    .collect(Collectors.toList());
        }
        else {
            modelList = courseSectionRepository.findAllByTerm_Id(termId).stream()
                    .map(courseSectionAssembler::toModel)
                    .collect(Collectors.toList());
        }

        return CollectionModel.of(modelList,
                linkTo(methodOn(CourseSectionController.class).all(termId,null,null)).withSelfRel());
    }

    @GetMapping()
    public EntityModel<CourseSectionOutputDTO> one(@RequestParam Long id){

        CourseSection courseSection = courseSectionRepository.findById(id).orElseThrow(() -> new CourseSectionNotFoundException(id));
        
        return courseSectionAssembler.toModel(courseSection);
    }
}
