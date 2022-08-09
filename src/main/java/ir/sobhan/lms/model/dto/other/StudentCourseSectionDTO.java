package ir.sobhan.lms.model.dto.other;

import ir.sobhan.lms.model.entity.CourseSectionRegistration;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class StudentCourseSectionDTO {
    private Long userId;
    private String name;
    private String studentId;
    private Double score;

    public static StudentCourseSectionDTO toDTO(CourseSectionRegistration courseSectionRegistration){
        return StudentCourseSectionDTO.builder()
                .userId(courseSectionRegistration.getStudent().getId())
                .name(courseSectionRegistration.getStudent().getUser().getName())
                .studentId(courseSectionRegistration.getStudent().getStudentId())
                .score(courseSectionRegistration.getScore())
                .build();
    }
}
