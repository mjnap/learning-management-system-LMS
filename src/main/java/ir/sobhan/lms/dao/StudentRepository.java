package ir.sobhan.lms.dao;

import ir.sobhan.lms.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUser_UserName(String userName);
    void deleteByUser_UserName(String userName);
}
