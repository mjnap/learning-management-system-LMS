package ir.sobhan.lms.dao;

import ir.sobhan.lms.model.CourseSectionRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseSectionRegistrationRepository extends JpaRepository<CourseSectionRegistration, Long> {
}
