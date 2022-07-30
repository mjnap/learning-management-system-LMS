package ir.sobhan.lms.dao;

import ir.sobhan.lms.model.entity.CourseSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseSectionRepository extends JpaRepository<CourseSection, Long> {
    List<CourseSection> findAllByTerm_Id(Long id);
}
