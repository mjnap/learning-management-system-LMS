package ir.sobhan.lms.model.dto.outputdto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class CourseSectionOutputDTO {
    private Long CourseSectionId;
    private String course;
    private int units;
    private String instructorName;
    private Double score;
}
