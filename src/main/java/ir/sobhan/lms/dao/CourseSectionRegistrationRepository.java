package ir.sobhan.lms.dao;

import ir.sobhan.lms.model.entity.CourseSectionRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sun.rmi.server.LoaderHandler;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseSectionRegistrationRepository extends JpaRepository<CourseSectionRegistration, Long> {
    CourseSectionRegistration findByCourseSection_IdAndStudent_Id(Long courseSectionId, Long studentId);
    List<CourseSectionRegistration> findAllByCourseSection_Term_IdAndStudent_User_UserName(Long termId, String userName);
    List<CourseSectionRegistration> findAllByStudent_User_UserName(String UserName);
}
