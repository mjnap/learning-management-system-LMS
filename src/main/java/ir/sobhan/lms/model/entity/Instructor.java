package ir.sobhan.lms.model.entity;

import ir.sobhan.lms.model.dto.MapperOutput;
import ir.sobhan.lms.model.dto.outputdto.InstructorOutputDTO;
import ir.sobhan.lms.security.Role;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Instructor implements MapperOutput<InstructorOutputDTO> {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    @OneToOne
    private User user;

    @NonNull
    private Rank level;

    @OneToMany(mappedBy = "instructor")
    private List<CourseSection> courseSectionList;

    @Override
    public InstructorOutputDTO toDTO() {
        return InstructorOutputDTO.builder()
                .userInfo(user.toDTO())
                .rank(level)
                .build();
    }
}
