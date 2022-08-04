package ir.sobhan.lms.model.entity;

import ir.sobhan.lms.model.dto.MapperOutput;
import ir.sobhan.lms.model.dto.outputdto.InstructorOutputDTO;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(indexes = @Index(columnList = "user_id"))
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
    @Cascade(value = CascadeType.DELETE)
    private List<CourseSection> courseSectionList;

    @Override
    public InstructorOutputDTO toDTO() {
        return InstructorOutputDTO.builder()
                .userInfo(user.toDTO())
                .rank(level)
                .build();
    }
}
