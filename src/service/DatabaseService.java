package service;

import model.*;
import utility.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {

    // --- Admin Operations ---
    public boolean validateAdmin(String username, String password) {
        String sql = "SELECT * FROM admins WHERE username = ? AND password = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- Teacher CRUD Operations ---
    public boolean addTeacher(Teacher teacher) {
        String sql = "INSERT INTO teachers (name, department, email) VALUES (?, ?, ?)";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, teacher.getTeacherName());
            pstmt.setString(2, teacher.getDepartment());
            pstmt.setString(3, teacher.getEmail());
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateTeacher(Teacher teacher) {
        String sql = "UPDATE teachers SET name = ?, department = ?, email = ? WHERE teacher_id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, teacher.getTeacherName());
            pstmt.setString(2, teacher.getDepartment());
            pstmt.setString(3, teacher.getEmail());
            pstmt.setInt(4, teacher.getTeacherId());
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteTeacher(int teacherId) {
        String sql = "DELETE FROM teachers WHERE teacher_id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, teacherId);
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Teacher> getAllTeachers() {
        List<Teacher> list = new ArrayList<>();
        String sql = "SELECT * FROM teachers ORDER BY name";
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Teacher(
                        rs.getInt("teacher_id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getString("email")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // --- Subject CRUD Operations ---
    public boolean addSubject(Subject subject) {
        String sql = "INSERT INTO subjects (name, credit_hours, assigned_teacher_id) VALUES (?, ?, ?)";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, subject.getSubjectName());
            pstmt.setInt(2, subject.getCreditHours());
            if (subject.getAssignedTeacherId() > 0) {
                pstmt.setInt(3, subject.getAssignedTeacherId());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateSubject(Subject subject) {
        String sql = "UPDATE subjects SET name = ?, credit_hours = ?, assigned_teacher_id = ? WHERE subject_id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, subject.getSubjectName());
            pstmt.setInt(2, subject.getCreditHours());
            if (subject.getAssignedTeacherId() > 0) {
                pstmt.setInt(3, subject.getAssignedTeacherId());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            pstmt.setInt(4, subject.getSubjectId());
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSubject(int subjectId) {
        String sql = "DELETE FROM subjects WHERE subject_id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, subjectId);
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Subject> getAllSubjects() {
        List<Subject> list = new ArrayList<>();
        String sql = "SELECT * FROM subjects ORDER BY name";
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Subject(
                        rs.getInt("subject_id"),
                        rs.getString("name"),
                        rs.getInt("credit_hours"),
                        rs.getInt("assigned_teacher_id")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // --- Room CRUD Operations ---
    public boolean addRoom(Room room) {
        String sql = "INSERT INTO rooms (room_number, capacity) VALUES (?, ?)";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, room.getRoomNumber());
            pstmt.setInt(2, room.getCapacity());
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateRoom(Room room) {
        String sql = "UPDATE rooms SET room_number = ?, capacity = ? WHERE room_id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, room.getRoomNumber());
            pstmt.setInt(2, room.getCapacity());
            pstmt.setInt(3, room.getRoomId());
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteRoom(int roomId) {
        String sql = "DELETE FROM rooms WHERE room_id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, roomId);
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Room> getAllRooms() {
        List<Room> list = new ArrayList<>();
        String sql = "SELECT * FROM rooms ORDER BY room_number";
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Room(
                        rs.getInt("room_id"),
                        rs.getString("room_number"),
                        rs.getInt("capacity")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // --- Timetable Generation / View Operations ---
    public boolean addTimetable(Timetable t) {
        String sql = "INSERT INTO timetables (day, time_slot, subject_id, teacher_id, room_id, section) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, t.getDay());
            pstmt.setString(2, t.getTimeSlot());
            pstmt.setInt(3, t.getSubjectId());
            pstmt.setInt(4, t.getTeacherId());
            pstmt.setInt(5, t.getRoomId());
            pstmt.setString(6, t.getSection());
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteTimetable(int timetableId) {
        String sql = "DELETE FROM timetables WHERE timetable_id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, timetableId);
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Timetable> getAllTimetableEntries() {
        List<Timetable> list = new ArrayList<>();
        String sql = "SELECT t.*, s.name AS subject_name, tc.name AS teacher_name, r.room_number " +
                "FROM timetables t " +
                "JOIN subjects s ON t.subject_id = s.subject_id " +
                "JOIN teachers tc ON t.teacher_id = tc.teacher_id " +
                "JOIN rooms r ON t.room_id = r.room_id " +
                "ORDER BY t.day, t.time_slot";
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Timetable entry = new Timetable(
                        rs.getInt("timetable_id"),
                        rs.getString("day"),
                        rs.getString("time_slot"),
                        rs.getInt("subject_id"),
                        rs.getInt("teacher_id"),
                        rs.getInt("room_id"),
                        rs.getString("section")
                );
                entry.setSubjectName(rs.getString("subject_name"));
                entry.setTeacherName(rs.getString("teacher_name"));
                entry.setRoomNumber(rs.getString("room_number"));
                list.add(entry);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Dynamic Filter
    public List<Timetable> getTimetableFiltered(String filterType, int idValue, String section) {
        List<Timetable> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT t.*, s.name AS subject_name, tc.name AS teacher_name, r.room_number " +
                "FROM timetables t " +
                "JOIN subjects s ON t.subject_id = s.subject_id " +
                "JOIN teachers tc ON t.teacher_id = tc.teacher_id " +
                "JOIN rooms r ON t.room_id = r.room_id "
        );

        boolean hasWhere = false;
        if ("Teacher".equalsIgnoreCase(filterType) && idValue > 0) {
            sql.append("WHERE t.teacher_id = ? ");
            hasWhere = true;
        } else if ("Room".equalsIgnoreCase(filterType) && idValue > 0) {
            sql.append("WHERE t.room_id = ? ");
            hasWhere = true;
        }

        if (section != null && !section.trim().isEmpty() && !"All".equalsIgnoreCase(section)) {
            if (hasWhere) {
                sql.append("AND t.section = ? ");
            } else {
                sql.append("WHERE t.section = ? ");
            }
        }

        sql.append("ORDER BY t.day, t.time_slot");

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (("Teacher".equalsIgnoreCase(filterType) || "Room".equalsIgnoreCase(filterType)) && idValue > 0) {
                pstmt.setInt(paramIndex++, idValue);
            }
            if (section != null && !section.trim().isEmpty() && !"All".equalsIgnoreCase(section)) {
                pstmt.setString(paramIndex, section);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Timetable entry = new Timetable(
                            rs.getInt("timetable_id"),
                            rs.getString("day"),
                            rs.getString("time_slot"),
                            rs.getInt("subject_id"),
                            rs.getInt("teacher_id"),
                            rs.getInt("room_id"),
                            rs.getString("section")
                    );
                    entry.setSubjectName(rs.getString("subject_name"));
                    entry.setTeacherName(rs.getString("teacher_name"));
                    entry.setRoomNumber(rs.getString("room_number"));
                    list.add(entry);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // --- Dashboard Stats ---
    public int getCount(String tableName) {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
