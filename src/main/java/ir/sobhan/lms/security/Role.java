package ir.sobhan.lms.security;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public enum Role {
    @Enumerated(value = EnumType.STRING)
    ADMIN,
    @Enumerated(value = EnumType.STRING)
    STUDENT,
    @Enumerated(value = EnumType.STRING)
    INSTRUCTOR,
    @Enumerated(value = EnumType.STRING)
    USER;

    public static String[] getAllRoles() {
        return new String[]{
                ADMIN.name(),
                STUDENT.name(),
                INSTRUCTOR.name(),
                USER.name()};
    }
}
