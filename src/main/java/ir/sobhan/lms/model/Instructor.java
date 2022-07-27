package ir.sobhan.lms.model;

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
public class Instructor{

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
}
