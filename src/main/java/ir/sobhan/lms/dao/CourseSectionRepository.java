package ir.sobhan.lms.dao;

import ir.sobhan.lms.model.entity.CourseSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseSectionRepository extends JpaRepository<CourseSection, Long> {
    List<CourseSection> findAllByTerm_Id(Long id);
    List<CourseSection> findAllByTerm_IdAndInstructor_User_NameAndCourse_Title(Long termId, String instructorName, String courseTitle);
    List<CourseSection> findAllByTerm_IdAndInstructor_User_Name(Long termId, String instructorName);
    List<CourseSection> findAllByTerm_IdAndCourse_Title(Long termId, String courseTitle);
}
