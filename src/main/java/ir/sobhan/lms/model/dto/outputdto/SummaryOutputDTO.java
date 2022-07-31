package ir.sobhan.lms.model.dto.outputdto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Setter
@Getter
public class SummaryOutputDTO {
    private Double totalAverage;
    private List<TermOutputSummaryDTO> termList;
}
