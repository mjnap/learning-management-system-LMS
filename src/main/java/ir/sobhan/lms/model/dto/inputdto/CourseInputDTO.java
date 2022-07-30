package ir.sobhan.lms.model.dto.inputdto;

import ir.sobhan.lms.model.entity.Course;
import ir.sobhan.lms.model.dto.MapperInput;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseInputDTO implements MapperInput<Course> {
    private String title;
    private int units;

    @Override
    public Course toEntity() {
        return new Course(title,units);
    }
}
