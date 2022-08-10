package ir.sobhan.lms.service;

import ir.sobhan.lms.business.exceptions.CourseSectionNotFoundException;
import ir.sobhan.lms.dao.CourseSectionRepository;
import ir.sobhan.lms.model.dto.inputdto.CourseSectionInputDTO;
import ir.sobhan.lms.model.dto.other.StudentCourseSectionDTO;
import ir.sobhan.lms.model.dto.outputdto.CourseSectionOutputDTO;
import ir.sobhan.lms.model.entity.CourseSection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseSectionService {

    private final CourseSectionRepository courseSectionRepository;
    private final CourseService courseService;
    private final TermService termService;

    private List<CourseSection> findAll(Long termId,
                                        String instructorName,
                                        String courseTitle,
                                        Pageable pageable) {

        if (instructorName != null && courseTitle != null)
            return courseSectionRepository.findAllByTerm_IdAndInstructor_User_NameAndCourse_Title(termId, instructorName, courseTitle, pageable);
        else if (instructorName != null)
            return courseSectionRepository.findAllByTerm_IdAndInstructor_User_Name(termId, instructorName, pageable);
        else if (courseTitle != null)
            return courseSectionRepository.findAllByTerm_IdAndCourse_Title(termId, courseTitle, pageable);
        else
            return courseSectionRepository.findAllByTerm_Id(termId, pageable);
    }

    public List<CourseSectionOutputDTO> getAll(Long termId,
                                               String instructorName,
                                               String courseTitle,
                                               int size,
                                               int page) {
        return findAll(termId, instructorName, courseTitle, PageRequest.of(page, size))
                .stream()
                .map(CourseSection::toDTO)
                .collect(Collectors.toList());
    }

    public CourseSection getOne(Long id) {
        return courseSectionRepository.findById(id)
                .orElseThrow(() -> new CourseSectionNotFoundException(id));
    }

    public CourseSection save(CourseSection courseSection) {
        return courseSectionRepository.save(courseSection);
    }

    public void update(CourseSection courseSection, CourseSectionInputDTO courseSectionInputDTO) {
        courseSection.setCourse(courseService.getOne(courseSectionInputDTO.getCourseTitle()));
        courseSection.setTerm(termService.getOne(courseSectionInputDTO.getTermId()));
    }

    public void delete(Long courseSectionId) {
        courseService.delete(courseSectionId);
    }

    public List<StudentCourseSectionDTO> getStudentsList(CourseSection courseSection) {
        return courseSection.getCourseSectionRegistrationList()
                .stream()
                .map(StudentCourseSectionDTO::toDTO)
                .collect(Collectors.toList());
    }
}
