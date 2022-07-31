package ir.sobhan.lms.model.entity;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public enum Degree {
    @Enumerated(value = EnumType.STRING)
    BS ,
    @Enumerated(value = EnumType.STRING)
    MS ,
    @Enumerated(value = EnumType.STRING)
    PHD
}
