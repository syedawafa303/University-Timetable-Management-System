package utility;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;

public class DbConnection {
    private static final String DB_FILE = "timetable.db";
    private static final String CONNECTION_URL = "jdbc:sqlite:" + DB_FILE;

    static {
        try {
            // Load SQLite JDBC Driver
            Class.forName("org.sqlite.JDBC");
            initializeDatabase();
        } catch (Exception e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(CONNECTION_URL);
    }

    private static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Create Admin table
            stmt.execute("CREATE TABLE IF NOT EXISTS admins (" +
                    "admin_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT NOT NULL UNIQUE," +
                    "password TEXT NOT NULL" +
                    ");");

            // Create Teacher table
            stmt.execute("CREATE TABLE IF NOT EXISTS teachers (" +
                    "teacher_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "department TEXT NOT NULL," +
                    "email TEXT NOT NULL UNIQUE" +
                    ");");

            // Create Subject table
            stmt.execute("CREATE TABLE IF NOT EXISTS subjects (" +
                    "subject_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL UNIQUE," +
                    "credit_hours INTEGER NOT NULL," +
                    "assigned_teacher_id INTEGER," +
                    "FOREIGN KEY (assigned_teacher_id) REFERENCES teachers(teacher_id) ON DELETE SET NULL" +
                    ");");

            // Create Room table
            stmt.execute("CREATE TABLE IF NOT EXISTS rooms (" +
                    "room_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "room_number TEXT NOT NULL UNIQUE," +
                    "capacity INTEGER NOT NULL" +
                    ");");

            // Create Timetable table
            stmt.execute("CREATE TABLE IF NOT EXISTS timetables (" +
                    "timetable_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "day TEXT NOT NULL," +
                    "time_slot TEXT NOT NULL," +
                    "subject_id INTEGER NOT NULL," +
                    "teacher_id INTEGER NOT NULL," +
                    "room_id INTEGER NOT NULL," +
                    "section TEXT NOT NULL," +
                    "FOREIGN KEY (subject_id) REFERENCES subjects(subject_id) ON DELETE CASCADE," +
                    "FOREIGN KEY (teacher_id) REFERENCES teachers(teacher_id) ON DELETE CASCADE," +
                    "FOREIGN KEY (room_id) REFERENCES rooms(room_id) ON DELETE CASCADE" +
                    ");");

            // Insert default admin if table is empty
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM admins")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    stmt.execute("INSERT INTO admins (username, password) VALUES ('admin', 'admin123')");
                    System.out.println("Default admin created.");
                }
            }

            // Seed Teachers
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM teachers")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    stmt.execute("INSERT INTO teachers (name, department, email) VALUES " +
                            "('Dr. Syeda Wafa', 'Software Engineering', 'wafa.nadir@university.edu'), " +
                            "('Dr. Asim Jamil', 'Computer Science', 'asim.jamil@university.edu'), " +
                            "('Prof. Sarah Khan', 'Information Technology', 'sarah.khan@university.edu'), " +
                            "('Dr. John Doe', 'Data Science', 'john.doe@university.edu')");
                }
            }

            // Seed Rooms
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM rooms")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    stmt.execute("INSERT INTO rooms (room_number, capacity) VALUES " +
                            "('Lecture Hall A', 120), " +
                            "('CS Lab 3', 45), " +
                            "('Room 102', 60), " +
                            "('Room 204', 50)");
                }
            }

            // Seed Subjects
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM subjects")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    // Assign to teachers (id 1 to 4)
                    stmt.execute("INSERT INTO subjects (name, credit_hours, assigned_teacher_id) VALUES " +
                            "('Software Construction & Development', 3, 1), " +
                            "('Object-Oriented Programming', 4, 2), " +
                            "('Database Systems', 3, 3), " +
                            "('Artificial Intelligence', 3, 4)");
                }
            }

            // Seed Timetables
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM timetables")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    stmt.execute("INSERT INTO timetables (day, time_slot, subject_id, teacher_id, room_id, section) VALUES " +
                            "('Monday', '08:30 AM - 10:00 AM', 1, 1, 1, 'BSSE-4A'), " +
                            "('Tuesday', '10:00 AM - 11:30 AM', 2, 2, 2, 'BSCS-2B'), " +
                            "('Wednesday', '11:30 AM - 01:00 PM', 3, 3, 3, 'BSIT-6A'), " +
                            "('Thursday', '01:30 PM - 03:00 PM', 4, 4, 4, 'BSDS-8A')");
                }
            }

        } catch (Exception e) {
            System.err.println("Error initializing database tables: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
