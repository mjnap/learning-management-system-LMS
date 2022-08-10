package ir.sobhan.lms.service;

import ir.sobhan.lms.business.exceptions.StudentNotFoundException;
import ir.sobhan.lms.dao.CourseSectionRegistrationRepository;
import ir.sobhan.lms.dao.StudentRepository;
import ir.sobhan.lms.model.dto.other.StudentInfoDTO;
import ir.sobhan.lms.model.entity.CourseSectionRegistration;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseSectionRegistrationService {

    private final CourseSectionRegistrationRepository courseSectionRegistrationRepository;
    private final StudentRepository studentRepository;

    public void addScore(Long courseSectionId, StudentInfoDTO studentInfoDTO) {

        CourseSectionRegistration updateCourseSectionRegistration = courseSectionRegistrationRepository
                .findByCourseSection_IdAndStudent_Id(courseSectionId, studentInfoDTO.getStudentId());

        updateCourseSectionRegistration.setScore(studentInfoDTO.getScore());

        courseSectionRegistrationRepository.save(updateCourseSectionRegistration);
    }

    public void save(CourseSectionRegistration courseSectionRegistration) {
        courseSectionRegistrationRepository.save(courseSectionRegistration);
    }

    public List<CourseSectionRegistration> findByTermIdAndUserName(Long termId, String userName) {
        return courseSectionRegistrationRepository
                .findAllByCourseSection_Term_IdAndStudent_User_UserName(termId, userName);
    }

    public Double totalAvg(Authentication authentication) {
        return courseSectionRegistrationRepository.totalAverage(studentRepository
                .findByUser_UserName(authentication.getName())
                .orElseThrow(() -> new StudentNotFoundException(authentication.getName()))
                .getId());
    }
}
