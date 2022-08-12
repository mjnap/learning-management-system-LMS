package ir.sobhan.lms.dao;

import ir.sobhan.lms.model.entity.CourseSectionRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseSectionRegistrationRepository extends JpaRepository<CourseSectionRegistration, Long> {
    CourseSectionRegistration findByCourseSection_IdAndStudent_Id(Long courseSectionId, Long studentId);

    List<CourseSectionRegistration> findAllByStudent_User_UserName(String userName);

    List<CourseSectionRegistration> findAllByCourseSection_Term_IdAndStudent_User_UserName(Long termId, String userName);

    @Query(nativeQuery = true, value = "SELECT AVG(score) FROM course_section_registration WHERE student_id=?")
    Double totalAverage(Long studentId);
}
