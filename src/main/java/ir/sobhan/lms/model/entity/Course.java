package ir.sobhan.lms.model.entity;

import ir.sobhan.lms.model.dto.MapperOutput;
import ir.sobhan.lms.model.dto.outputdto.CourseOutputDTO;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(indexes = @Index(columnList = "title"))
@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class Course implements MapperOutput<CourseOutputDTO> {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    @Column(unique = true, nullable = false)
    private String title;

    @NonNull
    @Column(nullable = false)
    private int units;

    @OneToMany(mappedBy = "course")
    @Cascade(value = CascadeType.DELETE)
    private List<CourseSection> courseSectionList;

    @Override
    public CourseOutputDTO toDTO() {
        return CourseOutputDTO.builder()
                .id(id)
                .title(title)
                .units(units)
                .build();
    }
}
