package ir.sobhan.lms.model.dto.other;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GradingInputDTO {
    private Long courseSectionId;
    private List<StudentInfoDTO> studentsScore;
}

