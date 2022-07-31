package ir.sobhan.lms.model.dto.inputdto;

import ir.sobhan.lms.model.entity.User;
import ir.sobhan.lms.model.dto.MapperInput;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserInputDTO implements MapperInput<User> {
    private String userName;
    private String password;
    private String name;
    private String phone;
    private String nationalId;

    @Override
    public User toEntity() {
        return new User(userName,password,name,phone,nationalId,false,false);
    }
}
