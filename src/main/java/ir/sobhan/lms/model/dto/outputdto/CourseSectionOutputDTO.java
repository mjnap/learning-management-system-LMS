package ir.sobhan.lms.model.dto.outputdto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class CourseSectionOutputDTO {
    private Long id;
    private String instructorName;
    private String courseTitle;
    private Long termId;
    private Integer studentCount;
}
