package ir.sobhan.lms.dao;

import ir.sobhan.lms.model.Instructor;
import ir.sobhan.lms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    void deleteByUser_UserName(String userName);
}
