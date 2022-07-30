package ir.sobhan.lms.model.dto.other;

import ir.sobhan.lms.model.dto.outputdto.CourseSectionOutputDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Setter
@Getter
public class SemesterGradesOutputDTO {
    private Double average;
    private List<CourseSectionOutputDTO> courseSectionList;
}
