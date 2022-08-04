package ir.sobhan.lms.model.entity;

import ir.sobhan.lms.model.dto.MapperOutput;
import ir.sobhan.lms.model.dto.outputdto.UserOutputDTO;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "User_Table" , indexes = @Index(columnList = "userName"))
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Setter
public class User implements MapperOutput<UserOutputDTO> {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    @Column(unique = true, nullable = false)
    private String userName;

    @NonNull
    @Column(nullable = false)
    private String password;

    @NonNull
    @Column(nullable = false)
    private String name;

    @NonNull
    @Column(unique = true, nullable = false)
    private String phone;

    @NonNull
    @Column(unique = true, nullable = false)
    private String nationalId;

    @NonNull
    private boolean admin;

    @NonNull
    private boolean active;

    private String roles;

    @Override
    public UserOutputDTO toDTO() {
        return UserOutputDTO.builder()
                .userName(userName)
                .name(name)
                .phone(phone)
                .nationalId(nationalId)
                .build();
    }
}
