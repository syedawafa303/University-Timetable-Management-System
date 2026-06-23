package model;

public class Teacher {
    private int teacherId;
    private String teacherName;
    private String department;
    private String email;

    public Teacher() {}

    public Teacher(int teacherId, String teacherName, String department, String email) {
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.department = department;
        this.email = email;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return teacherName;
    }
}
