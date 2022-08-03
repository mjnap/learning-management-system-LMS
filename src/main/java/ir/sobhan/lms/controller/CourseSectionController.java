package ir.sobhan.lms.controller;

import ir.sobhan.lms.business.exceptions.CourseSectionNotFoundException;
import ir.sobhan.lms.dao.CourseSectionRepository;
import ir.sobhan.lms.model.dto.outputdto.CourseSectionOutputDTO;
import ir.sobhan.lms.model.entity.CourseSection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/course-sections")
public class CourseSectionController {

    private final CourseSectionRepository courseSectionRepository;

    @GetMapping()
    public List<CourseSectionOutputDTO> all(@RequestParam Long termId,
                                            @RequestParam @Nullable String instructorName,
                                            @RequestParam @Nullable String courseTitle,
                                            @RequestParam int size,
                                            @RequestParam int page){

        List<CourseSectionOutputDTO> outputList;

        if(instructorName != null && courseTitle != null){
            outputList = courseSectionRepository.findAllByTerm_IdAndInstructor_User_NameAndCourse_Title(termId,instructorName,courseTitle,PageRequest.of(page, size)).stream()
                    .map(CourseSection::toDTO)
                    .collect(Collectors.toList());
        }
        else if(instructorName != null){
            outputList = courseSectionRepository.findAllByTerm_IdAndInstructor_User_Name(termId, instructorName, PageRequest.of(page, size)).stream()
                    .map(CourseSection::toDTO)
                    .collect(Collectors.toList());
        }
        else if(courseTitle != null){
            outputList = courseSectionRepository.findAllByTerm_IdAndCourse_Title(termId, courseTitle, PageRequest.of(page, size)).stream()
                    .map(CourseSection::toDTO)
                    .collect(Collectors.toList());
        }
        else {
            outputList = courseSectionRepository.findAllByTerm_Id(termId, PageRequest.of(page, size)).stream()
                    .map(CourseSection::toDTO)
                    .collect(Collectors.toList());
        }

        return outputList;
    }

    @GetMapping("/{id}")
    public CourseSectionOutputDTO one(@PathVariable Long id){

        CourseSection courseSection = courseSectionRepository.findById(id).orElseThrow(() -> new CourseSectionNotFoundException(id));
        
        return courseSection.toDTO();
    }
}
