package ir.sobhan.lms.security;

public enum Role {
    ADMIN,
    STUDENT,
    INSTRUCTOR,
    USER;

    public static String[] getAllRoles(){
        return new String[]{
                ADMIN.name(),
                STUDENT.name(),
                INSTRUCTOR.name(),
                USER.name()};
    }
}
