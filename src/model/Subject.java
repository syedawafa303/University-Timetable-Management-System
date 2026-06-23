package model;

public class Subject {
    private int subjectId;
    private String subjectName;
    private int creditHours;
    private int assignedTeacherId; // Reference to Teacher

    public Subject() {}

    public Subject(int subjectId, String subjectName, int creditHours, int assignedTeacherId) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.creditHours = creditHours;
        this.assignedTeacherId = assignedTeacherId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }

    public int getAssignedTeacherId() {
        return assignedTeacherId;
    }

    public void setAssignedTeacherId(int assignedTeacherId) {
        this.assignedTeacherId = assignedTeacherId;
    }

    @Override
    public String toString() {
        return subjectName;
    }
}
