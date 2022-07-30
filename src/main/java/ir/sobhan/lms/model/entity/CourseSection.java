package ir.sobhan.lms.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Setter
@Getter
@RequiredArgsConstructor
public class CourseSection {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @NonNull
    private Instructor instructor;

    @ManyToOne
    @NonNull
    private Course course;

    @ManyToOne
    @NonNull
    private Term term;

    @OneToMany(mappedBy = "courseSection")
    private List<CourseSectionRegistration> courseSectionRegistrationList;
}
