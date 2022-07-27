package ir.sobhan.lms.model;

import ir.sobhan.lms.model.dto.MapperOutput;
import ir.sobhan.lms.model.dto.outputdto.UserOutputDTO;
import lombok.*;

import javax.persistence.*;

@Entity
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

    @Override
    public UserOutputDTO toDTO() {
        return UserOutputDTO.builder()
                .userName(this.userName)
                .name(this.name)
                .phone(this.phone)
                .nationalId(this.nationalId).build();
    }
}
