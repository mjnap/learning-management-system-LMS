package ir.sobhan.lms.controller;

import ir.sobhan.lms.model.dto.inputdto.StudentInputDTO;
import ir.sobhan.lms.model.dto.inputdto.StudentUpdateInputDTO;
import ir.sobhan.lms.model.dto.outputdto.StudentOutputDTO;
import ir.sobhan.lms.model.entity.Degree;
import ir.sobhan.lms.model.entity.Student;
import ir.sobhan.lms.model.entity.User;
import ir.sobhan.lms.security.Role;
import ir.sobhan.lms.service.CourseSectionRegistrationService;
import ir.sobhan.lms.service.StudentService;
import ir.sobhan.lms.service.TermService;
import ir.sobhan.lms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final UserService userService;
    private final TermService termService;
    private final CourseSectionRegistrationService courseSectionRegistrationService;

    @GetMapping()
    public List<StudentOutputDTO> all(@RequestParam int size, @RequestParam int page) {
        return studentService.getAll(PageRequest.of(page, size));
    }

    @GetMapping("/{userName}")
    public StudentOutputDTO one(@PathVariable String userName) {
        return studentService.getOne(userName).toDTO();
    }

    @PostMapping("/new-student")
    public ResponseEntity<?> newStudent(@RequestBody StudentInputDTO studentInputDTO) {

        User user = userService.getByUserName(studentInputDTO.getUserName());
        userService.addRoleToUser(user, Role.STUDENT);

        Student student = new Student(user,
                studentInputDTO.getStudentId(),
                Degree.valueOf(studentInputDTO.getDegree().toUpperCase()),
                new Date());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(studentService.save(student).toDTO());
    }

    @PutMapping("/update-student/{studentId}")
    public ResponseEntity<?> updateStudent(@PathVariable Long studentId,
                                           @RequestBody StudentUpdateInputDTO studentUpdateInputDTO) {
        return ResponseEntity
                .ok(studentService.update(studentId, studentUpdateInputDTO));
    }

    @DeleteMapping("/delete-student/{userName}")
    @Transactional
    public ResponseEntity<?> deleteStudent(@PathVariable String userName) {
        studentService.delete(userName);
        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/semester-grades/{termId}")
    public ResponseEntity<?> semesterGrades(@PathVariable Long termId,
                                            Authentication authentication) {
        termService.checkExist(termId);

        return ResponseEntity
                .ok(studentService.showSemester(courseSectionRegistrationService.findByTermIdAndUserName(termId, authentication.getName())));
    }

    @GetMapping("/summary")
    public ResponseEntity<?> summary(Authentication authentication) {
        return ResponseEntity
                .ok(studentService.showSummery(termService.getAll(), authentication));
    }
}
