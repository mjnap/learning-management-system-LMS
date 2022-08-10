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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TermService {

    private final TermRepository termRepository;
    private final CourseSectionRegistrationRepository courseSectionRegistrationRepository;

    public List<Term> getAll() {
        return termRepository.findAll();
    }

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
        List<ListSemesterOutputDTO> sectionOutputDTOList = new ArrayList<>();
        courseList.forEach(course -> {
            sectionOutputDTOList.add(ListSemesterOutputDTO.builder()
                    .CourseSectionId(course.getCourseSection().getId())
                    .course(course.getCourseSection().getCourse().getTitle())
                    .units(course.getCourseSection().getCourse().getUnits())
                    .instructorName(course.getCourseSection().getInstructor().getUser().getName())
                    .score(course.getScore())
                    .build());
        });

        return sectionOutputDTOList;
    }

    public List<TermOutputSummaryDTO> createTermList(List<Term> termList, Authentication authentication) {
        List<TermOutputSummaryDTO> termOutputSummaryDTOList = new ArrayList<>();

        termList.forEach(term -> {
            List<CourseSectionRegistration> courseList = courseSectionRegistrationRepository
                    .findAllByCourseSection_Term_IdAndStudent_User_UserName(term.getId(), authentication.getName());

            if (!courseList.isEmpty())
                termOutputSummaryDTOList.add(TermOutputSummaryDTO.builder()
                        .termId(term.getId())
                        .termTile(term.getTitle())
                        .termAverage(average(courseList))
                        .build());
        });

        return termOutputSummaryDTOList;
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
