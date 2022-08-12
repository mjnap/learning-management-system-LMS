package ir.sobhan.lms.model.dto.outputdto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class TermOutputSummaryDTO {
    private Long termId;
    private String termTitle;
    private Double termAverage;
}
