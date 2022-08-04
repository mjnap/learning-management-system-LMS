package ir.sobhan.lms.model.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(indexes = {@Index(columnList = "course_section_id") ,
                  @Index(columnList = "student_id")})
@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class CourseSectionRegistration {

    @Id
    @GeneratedValue
    private Long id;

    private Double score = 0D;

    @ManyToOne
    @NonNull
    private CourseSection courseSection;

    @ManyToOne
    @NonNull
    private Student student;
}
