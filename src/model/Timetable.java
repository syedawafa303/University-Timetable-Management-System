package model;

public class Timetable {
    private int timetableId;
    private String day;        // e.g., Monday, Tuesday...
    private String timeSlot;   // e.g., 08:30 - 10:00, 10:00 - 11:30...
    private int subjectId;
    private int teacherId;
    private int roomId;
    private String section;    // e.g., BSSE-A, BSSE-B...

    // Derived display fields
    private String subjectName;
    private String teacherName;
    private String roomNumber;

    public Timetable() {}

    public Timetable(int timetableId, String day, String timeSlot, int subjectId, int teacherId, int roomId, String section) {
        this.timetableId = timetableId;
        this.day = day;
        this.timeSlot = timeSlot;
        this.subjectId = subjectId;
        this.teacherId = teacherId;
        this.roomId = roomId;
        this.section = section;
    }

    public int getTimetableId() {
        return timetableId;
    }

    public void setTimetableId(int timetableId) {
        this.timetableId = timetableId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
}
