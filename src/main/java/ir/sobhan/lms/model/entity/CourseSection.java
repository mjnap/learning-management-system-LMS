package ir.sobhan.lms.model.entity;

import ir.sobhan.lms.model.dto.MapperOutput;
import ir.sobhan.lms.model.dto.outputdto.CourseSectionOutputDTO;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(indexes = {@Index(columnList = "instructor_id"),
                  @Index(columnList = "course_id"),
                  @Index(columnList = "term_id")})
@NoArgsConstructor
@Setter
@Getter
@RequiredArgsConstructor
public class CourseSection implements MapperOutput<CourseSectionOutputDTO> {

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
    @Cascade(value = CascadeType.DELETE)
    private List<CourseSectionRegistration> courseSectionRegistrationList = new ArrayList<>();

    @Override
    public CourseSectionOutputDTO toDTO() {
        return CourseSectionOutputDTO.builder()
                .id(id)
                .instructorName(instructor.getUser().getName())
                .courseTitle(course.getTitle())
                .termId(term.getId())
                .studentCount(courseSectionRegistrationList.size())
                .build();
    }
}
