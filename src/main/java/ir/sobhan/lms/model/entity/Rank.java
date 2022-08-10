package ir.sobhan.lms.model.entity;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public enum Rank {
    @Enumerated(value = EnumType.STRING)
    ASSISTANT,
    @Enumerated(value = EnumType.STRING)
    FULL
}
