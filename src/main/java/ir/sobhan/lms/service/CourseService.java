package ir.sobhan.lms.service;

import ir.sobhan.lms.business.exceptions.CourseNotFoundException;
import ir.sobhan.lms.dao.CourseRepository;
import ir.sobhan.lms.model.dto.inputdto.CourseInputDTO;
import ir.sobhan.lms.model.dto.outputdto.CourseOutputDTO;
import ir.sobhan.lms.model.entity.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public List<CourseOutputDTO> getAll(Pageable pageable) {
        return courseRepository.findAll(pageable).stream()
                .map(Course::toDTO)
                .collect(Collectors.toList());
    }

    public Course getOne(Long id) {
        return courseRepository.findById(id)
                    .orElseThrow(() -> new CourseNotFoundException(id));
    }

    public Course getOne(String title){
        return courseRepository.findByTitle(title)
                .orElseThrow(() -> new CourseNotFoundException(title));
    }

    public Course save(Course course) {
        return courseRepository.save(course);
    }

    public void delete(Long id){
        courseRepository.deleteById(id);
    }

    public CourseOutputDTO update(Long courseId, CourseInputDTO courseInputDTO) {
        return courseRepository.findById(courseId)
                .map(course -> {
                    course.setTitle(courseInputDTO.getTitle());
                    course.setUnits(courseInputDTO.getUnits());
                    return courseRepository.save(course);
                })
                .orElseThrow(() -> new CourseNotFoundException(courseId))
                .toDTO();
    }
}
