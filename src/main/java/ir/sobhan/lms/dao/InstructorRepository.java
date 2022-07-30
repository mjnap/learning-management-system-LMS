package ir.sobhan.lms.dao;

import ir.sobhan.lms.model.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    Instructor findByUser_UserName(String userName);
    void deleteByUser_UserName(String userName);
}
