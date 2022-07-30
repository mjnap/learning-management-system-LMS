package ir.sobhan.lms.model.entity;

import lombok.*;

import javax.persistence.*;
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
    @Column(unique = true , nullable = false)
    private String title;

    @NonNull
    @Column(nullable = false)
    private int units;

    @OneToMany(mappedBy = "course")
    private List<CourseSection> courseSectionList;
}
