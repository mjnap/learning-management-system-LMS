package ir.sobhan.lms.model.dto.outputdto;

import ir.sobhan.lms.model.entity.Degree;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
public class StudentOutputDTO {
    private UserOutputDTO userDTO;
    private String studentId;
    private Degree degree;
    private Date startDate;
}
