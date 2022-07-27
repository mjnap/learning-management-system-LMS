package ir.sobhan.lms.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class Course {

    @Id
    @GeneratedValue
    private Long id;
    @NonNull
    private String title;
    @NonNull
    private int units;
    @OneToMany(mappedBy = "course")
    @NonNull
    private List<CourseSection> courseSectionList;
}
