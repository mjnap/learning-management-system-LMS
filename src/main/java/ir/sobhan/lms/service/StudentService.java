package ir.sobhan.lms.service;

import ir.sobhan.lms.business.exceptions.StudentNotFoundException;
import ir.sobhan.lms.dao.StudentRepository;
import ir.sobhan.lms.model.dto.inputdto.StudentUpdateInputDTO;
import ir.sobhan.lms.model.dto.other.SemesterGradesOutputDTO;
import ir.sobhan.lms.model.dto.outputdto.StudentOutputDTO;
import ir.sobhan.lms.model.dto.outputdto.SummaryOutputDTO;
import ir.sobhan.lms.model.entity.*;
import ir.sobhan.lms.security.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserService userService;
    private final TermService termService;
    private final CourseSectionRegistrationService courseSectionRegistrationService;

    public List<StudentOutputDTO> getAll(Pageable pageable) {
        return studentRepository.findAll(pageable).stream()
                .map(Student::toDTO)
                .collect(Collectors.toList());
    }

    public Student getOne(String userName) {
        return studentRepository.findByUser_UserName(userName)
                .orElseThrow(() -> new StudentNotFoundException(userName));
    }

    public Student save(Student student) {
        return studentRepository.save(student);
    }

    public StudentOutputDTO update(Long studentId, StudentUpdateInputDTO studentUpdateInputDTO) {
        return studentRepository.findById(studentId)
                .map(student -> {
                    User user = student.getUser();
                    user.setName(studentUpdateInputDTO.getName());
                    student.setUser(user);
                    student.setDegree(Degree.valueOf(studentUpdateInputDTO.getDegree()));
                    return studentRepository.save(student);
                })
                .orElseThrow(() -> new StudentNotFoundException(studentId))
                .toDTO();
    }

    public void delete(String userName) {
        studentRepository.deleteByUser_UserName(userName);

        User user = userService.getOne(userName);
        userService.removeRoleFromUser(user, Role.STUDENT);
        userService.save(user);
    }

    public SemesterGradesOutputDTO showSemester(List<CourseSectionRegistration> courseList) {
        return SemesterGradesOutputDTO.builder()
                .average(average(courseList))
                .courseSectionList(termService.createListInfo(courseList))
                .build();
    }

    public SummaryOutputDTO showSummery(List<Term> termList, Authentication authentication) {
        return SummaryOutputDTO.builder()
                .totalAverage(courseSectionRegistrationService.totalAvg(authentication))
                .termList(termService.createTermList(termList, authentication))
                .build();
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
