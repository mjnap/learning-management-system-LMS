package ir.sobhan.lms.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class Term {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    @Column(nullable = false)
    private String title;

    @NonNull
    private boolean open;

    @OneToMany(mappedBy = "term")
    private List<CourseSection> courseSectionList;
}
