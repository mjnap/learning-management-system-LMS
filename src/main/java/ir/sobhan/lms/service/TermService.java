package ir.sobhan.lms.service;

import ir.sobhan.lms.business.exceptions.TermNotFoundException;
import ir.sobhan.lms.dao.CourseSectionRegistrationRepository;
import ir.sobhan.lms.dao.TermRepository;
import ir.sobhan.lms.model.dto.inputdto.TermInputDTO;
import ir.sobhan.lms.model.dto.other.ListSemesterOutputDTO;
import ir.sobhan.lms.model.dto.outputdto.TermOutputDTO;
import ir.sobhan.lms.model.dto.outputdto.TermOutputSummaryDTO;
import ir.sobhan.lms.model.entity.CourseSectionRegistration;
import ir.sobhan.lms.model.entity.Term;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TermService {

    private final TermRepository termRepository;
    private final CourseSectionRegistrationRepository courseSectionRegistrationRepository;

    public List<TermOutputDTO> getAll(Pageable pageable) {
        return termRepository.findAll(pageable).stream()
                .map(Term::toDTO)
                .collect(Collectors.toList());
    }

    public Term getOne(Long id) {
        return termRepository.findById(id)
                .orElseThrow(() -> new TermNotFoundException(id));
    }

    public void checkExist(Long termId) {
        if (!termRepository.existsById(termId))
            throw new TermNotFoundException(termId);
    }

    public List<ListSemesterOutputDTO> createListInfo(List<CourseSectionRegistration> courseList) {
        return courseList.stream()
                .map(course ->
                     ListSemesterOutputDTO.builder()
                            .CourseSectionId(course.getCourseSection().getId())
                            .course(course.getCourseSection().getCourse().getTitle())
                            .units(course.getCourseSection().getCourse().getUnits())
                            .instructorName(course.getCourseSection().getInstructor().getUser().getName())
                            .score(course.getScore())
                            .build()
                )
                .collect(Collectors.toList());
    }

    public List<TermOutputSummaryDTO> createTermList(Authentication authentication) {
        List<CourseSectionRegistration> courseList = courseSectionRegistrationRepository
                .findAllByStudent_User_UserName(authentication.getName());

        return courseList.stream()
                .map(courseSectionRegistration -> courseSectionRegistration.getCourseSection().getTerm().getId())
                .distinct()
                .map(termId -> TermOutputSummaryDTO.builder()
                        .termId(termId)
                        .termTitle(termRepository.findById(termId)
                                .orElseThrow(() -> new TermNotFoundException(termId))
                                .getTitle())
                        .termAverage(average(courseList.stream()
                                .filter(course -> course.getCourseSection().getTerm().getId().equals(termId))
                                .collect(Collectors.toList())))
                        .build())
                .collect(Collectors.toList());
    }

    public Term save(Term term) {
        return termRepository.save(term);
    }

    public TermOutputDTO update(Long termId, TermInputDTO termInputDTO) {
        return termRepository.findById(termId)
                .map(term -> {
                    term.setTitle(termInputDTO.getTitle());
                    term.setOpen(termInputDTO.isOpen());
                    return termRepository.save(term);
                })
                .orElseThrow(() -> new TermNotFoundException(termId))
                .toDTO();
    }

    public void delete(Long id) {
        termRepository.deleteById(id);
    }

    public Double average(List<CourseSectionRegistration> courseList) {
        String avg = String.format("%.2f", courseList.stream()
                .map(CourseSectionRegistration::getScore)
                .reduce(Double::sum)
                .orElseGet(() -> Double.NaN) / courseList.size());

        if (avg.equals("NaN"))
            return 0D;

        return Double.valueOf(avg);
    }
}
