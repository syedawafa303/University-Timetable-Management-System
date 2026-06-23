package service;

import utility.DbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TimetableService {

    public String validateSchedule(String day, String timeSlot, int teacherId, int roomId, String section) {
        // 1. Teacher Conflict check
        if (checkTeacherConflict(day, timeSlot, teacherId)) {
            return "Teacher conflict! This teacher is already scheduled for another class at this time.";
        }

        // 2. Room Conflict check
        if (checkRoomConflict(day, timeSlot, roomId)) {
            return "Room conflict! This room is already occupied at this time.";
        }

        // 3. Section/Class Conflict check
        if (checkSectionConflict(day, timeSlot, section)) {
            return "Section conflict! Section '" + section + "' already has a class scheduled at this time.";
        }

        return null; // No conflicts found
    }

    private boolean checkTeacherConflict(String day, String timeSlot, int teacherId) {
        String sql = "SELECT COUNT(*) FROM timetables WHERE day = ? AND time_slot = ? AND teacher_id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, day);
            pstmt.setString(2, timeSlot);
            pstmt.setInt(3, teacherId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkRoomConflict(String day, String timeSlot, int roomId) {
        String sql = "SELECT COUNT(*) FROM timetables WHERE day = ? AND time_slot = ? AND room_id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, day);
            pstmt.setString(2, timeSlot);
            pstmt.setInt(3, roomId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkSectionConflict(String day, String timeSlot, String section) {
        String sql = "SELECT COUNT(*) FROM timetables WHERE day = ? AND time_slot = ? AND section = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, day);
            pstmt.setString(2, timeSlot);
            pstmt.setString(3, section);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
