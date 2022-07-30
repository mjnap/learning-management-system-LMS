package ir.sobhan.lms.model.dto.outputdto;

import ir.sobhan.lms.model.entity.Rank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class InstructorOutputDTO {
    private UserOutputDTO userDTO;
    private Rank rank;
}
