package ir.sobhan.lms.model.dto.inputdto;

import ir.sobhan.lms.model.dto.MapperInput;
import ir.sobhan.lms.model.entity.Term;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TermInputDTO implements MapperInput<Term> {
    private String title;
    private boolean open;

    @Override
    public Term toEntity() {
        return new Term(title, open);
    }
}
