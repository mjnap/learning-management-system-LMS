package ir.sobhan.lms.model.dto.outputdto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class TermOutputDTO {
    private Long id;
    private String title;
    private boolean open;
}
