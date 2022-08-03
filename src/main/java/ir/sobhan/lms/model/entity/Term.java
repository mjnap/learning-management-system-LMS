package ir.sobhan.lms.model.entity;

import ir.sobhan.lms.model.dto.MapperOutput;
import ir.sobhan.lms.model.dto.outputdto.TermOutputDTO;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class Term implements MapperOutput<TermOutputDTO> {

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

    @Override
    public TermOutputDTO toDTO() {
        return TermOutputDTO.builder()
                .id(id)
                .title(title)
                .open(open)
                .build();
    }
}
