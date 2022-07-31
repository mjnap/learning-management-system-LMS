package ir.sobhan.lms.model.dto.other;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class ListSemesterOutputDTO {
    private Long CourseSectionId;
    private String course;
    private int units;
    private String instructorName;
    private Double score;
}
