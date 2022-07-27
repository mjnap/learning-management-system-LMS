package ir.sobhan.lms.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class Student{

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
    private List<CourseSectionRegistration> courseSectionRegistrationList;
}
