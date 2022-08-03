package ir.sobhan.lms.model.dto.outputdto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class CourseOutputDTO {
    private Long id;
    private String title;
    private int units;
}
