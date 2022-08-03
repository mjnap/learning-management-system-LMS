package ir.sobhan.lms.model.entity;

import ir.sobhan.lms.model.dto.MapperOutput;
import ir.sobhan.lms.model.dto.outputdto.CourseOutputDTO;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class Course implements MapperOutput<CourseOutputDTO> {

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

    @Override
    public CourseOutputDTO toDTO() {
        return CourseOutputDTO.builder()
                .id(id)
                .title(title)
                .units(units)
                .build();
    }
}
