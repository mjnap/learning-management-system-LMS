package ir.sobhan.lms.controller;

import ir.sobhan.lms.model.dto.inputdto.InstructorInputDTO;
import ir.sobhan.lms.model.dto.inputdto.InstructorUpdateInputDTO;
import ir.sobhan.lms.model.dto.outputdto.InstructorOutputDTO;
import ir.sobhan.lms.model.entity.Instructor;
import ir.sobhan.lms.model.entity.Rank;
import ir.sobhan.lms.model.entity.User;
import ir.sobhan.lms.security.Role;
import ir.sobhan.lms.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/instructors")
@RequiredArgsConstructor
public class InstructorController {

    private final InstructorService instructorService;
    private final UserService userService;

    @GetMapping()
    public List<InstructorOutputDTO> all(@RequestParam int size, @RequestParam int page) {
        return instructorService.getAll(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public InstructorOutputDTO one(@PathVariable Long id) {
        return instructorService.getOne(id).toDTO();
    }

    @PostMapping("/new-instructor")
    public ResponseEntity<?> newInstructor(@RequestBody InstructorInputDTO instructorInputDTO) {

        User user = userService.getOne(instructorInputDTO.getUserName());
        userService.addRoleToUser(user, Role.INSTRUCTOR);

        Instructor instructor = new Instructor(user, Rank.valueOf(instructorInputDTO.getRank().toUpperCase()));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(instructorService.save(instructor).toDTO());
    }

    @PutMapping("/update-instructor/{instructorId}")
    public ResponseEntity<?> updateInstructor(@PathVariable Long instructorId,
                                              @RequestBody InstructorUpdateInputDTO instructorUpdateInputDTO) {
        return ResponseEntity
                .ok(instructorService.update(instructorId, instructorUpdateInputDTO));
    }

    @DeleteMapping("/delete-instructor/{userName}")
    @Transactional
    public ResponseEntity<?> deleteInstructor(@PathVariable String userName) {
        instructorService.delete(userName);
        return ResponseEntity
                .noContent()
                .build();
    }


}
