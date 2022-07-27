package ir.sobhan.lms.model.dto.outputdto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserOutputDTO {
    private String userName;
    private String name;
    private String phone;
    private String nationalId;
}
