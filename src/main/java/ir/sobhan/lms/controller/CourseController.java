package ir.sobhan.lms.controller;

import ir.sobhan.lms.model.dto.inputdto.CourseInputDTO;
import ir.sobhan.lms.model.dto.outputdto.CourseOutputDTO;
import ir.sobhan.lms.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
@Slf4j
public class CourseController {

    private final CourseService courseService;

    @GetMapping()
    public List<CourseOutputDTO> all(@RequestParam int size, @RequestParam int page) {
        return courseService.getAll(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public CourseOutputDTO one(@PathVariable Long id) {
        return courseService.getOne(id).toDTO();
    }

    @PostMapping("/new-course")
    public ResponseEntity<?> newCourse(@RequestBody CourseInputDTO courseInputDTO) {
        log.info("Course added : " + courseInputDTO.toEntity());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(courseService.save(courseInputDTO.toEntity()));
    }

    @PutMapping("/update-course/{courseId}")
    public ResponseEntity<?> updateCourse(@PathVariable Long courseId,
                                          @RequestBody CourseInputDTO courseInputDTO) {
        return ResponseEntity
                .ok(courseService.update(courseId, courseInputDTO));
    }

    @DeleteMapping("/delete-course/{id}")
    @Transactional
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        courseService.delete(id);
        log.info("course deleted : " + id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
