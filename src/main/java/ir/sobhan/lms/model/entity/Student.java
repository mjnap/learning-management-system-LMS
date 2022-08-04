package ir.sobhan.lms.model.entity;

import ir.sobhan.lms.model.dto.MapperOutput;
import ir.sobhan.lms.model.dto.outputdto.StudentOutputDTO;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(indexes = @Index(columnList = "user_id"))
@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class Student implements MapperOutput<StudentOutputDTO> {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    @OneToOne
    private User user;

    @NonNull
    @Column(unique = true , nullable = false)
    private String studentId;

    @NonNull
    private Degree degree;

    @NonNull
    private Date startDate;

    @OneToMany(mappedBy = "student")
    @Cascade(value = CascadeType.DELETE)
    private List<CourseSectionRegistration> courseSectionRegistrationList;

    @Override
    public StudentOutputDTO toDTO() {
        return StudentOutputDTO.builder()
                .userInfo(user.toDTO())
                .studentId(studentId)
                .degree(degree)
                .startDate(startDate)
                .build();
    }
}
