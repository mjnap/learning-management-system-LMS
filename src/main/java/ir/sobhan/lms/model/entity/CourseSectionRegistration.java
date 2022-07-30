package ir.sobhan.lms.model.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class CourseSectionRegistration {

    @Id
    @GeneratedValue
    private Long id;

    private Double score;

    @ManyToOne
    @NonNull
    private CourseSection courseSection;

    @ManyToOne
    @NonNull
    private Student student;
}
