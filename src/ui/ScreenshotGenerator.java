package ui;

import com.formdev.flatlaf.FlatLightLaf;
import service.DatabaseService;
import model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

public class ScreenshotGenerator {
    public static void main(String[] args) {
        // Set rendering properties
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        try {
            FlatLightLaf.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Ensure screenshots directory exists
        File dir = new File("screenshots");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 1. Seed realistic mock data if the database is empty
        seedDatabase();

        // 2. Generate Login Screen Screenshot
        generateLoginScreenshot();

        // 3. Generate Dashboard Tab Screenshots
        generateDashboardScreenshots();

        System.out.println("ALL SCREENSHOTS GENERATED SUCCESSFULLY IN 'screenshots/' DIRECTORY!");
        System.exit(0);
    }

    private static void seedDatabase() {
        DatabaseService db = new DatabaseService();
        
        // Seed Teachers if empty
        if (db.getAllTeachers().isEmpty()) {
            System.out.println("Seeding database with realistic mock data...");
            db.addTeacher(new Teacher(0, "Dr. John Smith", "Computer Science", "john.smith@university.edu"));
            db.addTeacher(new Teacher(0, "Dr. Sarah Jenkins", "Software Engineering", "sarah.j@university.edu"));
            db.addTeacher(new Teacher(0, "Prof. Robert Downey", "Information Technology", "robert.d@university.edu"));
            db.addTeacher(new Teacher(0, "Dr. Emma Watson", "Data Science", "emma.w@university.edu"));
        }

        // Seed Subjects if empty
        if (db.getAllSubjects().isEmpty()) {
            List<Teacher> teachers = db.getAllTeachers();
            int t1 = teachers.get(0).getTeacherId();
            int t2 = teachers.get(1).getTeacherId();
            int t3 = teachers.get(2).getTeacherId();
            int t4 = teachers.get(3).getTeacherId();

            db.addSubject(new Subject(0, "Object Oriented Programming", 3, t1));
            db.addSubject(new Subject(0, "Software Construction & Dev", 4, t2));
            db.addSubject(new Subject(0, "Database Management Systems", 3, t3));
            db.addSubject(new Subject(0, "Introduction to Data Science", 3, t4));
        }

        // Seed Rooms if empty
        if (db.getAllRooms().isEmpty()) {
            db.addRoom(new Room(0, "CS-101 (Lab)", 45));
            db.addRoom(new Room(0, "SE-202 (Lecture)", 60));
            db.addRoom(new Room(0, "IT-305 (Seminar)", 80));
            db.addRoom(new Room(0, "Main Auditorium", 120));
        }

        // Seed Timetables if empty
        if (db.getAllTimetableEntries().isEmpty()) {
            List<Subject> subjects = db.getAllSubjects();
            List<Room> rooms = db.getAllRooms();

            db.addTimetable(new Timetable(0, "Monday", "08:30 AM - 10:00 AM", 
                subjects.get(0).getSubjectId(), subjects.get(0).getAssignedTeacherId(), 
                rooms.get(0).getRoomId(), "BSSE-4A"));

            db.addTimetable(new Timetable(0, "Tuesday", "10:00 AM - 11:30 AM", 
                subjects.get(1).getSubjectId(), subjects.get(1).getAssignedTeacherId(), 
                rooms.get(1).getRoomId(), "BSSE-4B"));

            db.addTimetable(new Timetable(0, "Wednesday", "11:30 AM - 01:00 PM", 
                subjects.get(2).getSubjectId(), subjects.get(2).getAssignedTeacherId(), 
                rooms.get(2).getRoomId(), "BSCS-3A"));

            db.addTimetable(new Timetable(0, "Thursday", "01:30 PM - 03:00 PM", 
                subjects.get(3).getSubjectId(), subjects.get(3).getAssignedTeacherId(), 
                rooms.get(3).getRoomId(), "BSDS-5A"));
        }
    }

    private static void generateLoginScreenshot() {
        try {
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
            Thread.sleep(600); // Allow window to render fully
            
            BufferedImage img = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = img.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            frame.paint(g);
            g.dispose();

            ImageIO.write(img, "PNG", new File("screenshots/login.png"));
            System.out.println("Generated: screenshots/login.png");
            frame.dispose();
        } catch (Exception e) {
            System.err.println("Error generating login screenshot: " + e.getMessage());
        }
    }

    private static void generateDashboardScreenshots() {
        try {
            DashboardFrame frame = new DashboardFrame();
            frame.setVisible(true);
            Thread.sleep(800); // Allow dashboard to render fully

            // Extract sidebar using reflection
            Field sidebarField = DashboardFrame.class.getDeclaredField("sidebarPanel");
            sidebarField.setAccessible(true);
            JPanel sidebar = (JPanel) sidebarField.get(frame);

            // Iterate buttons in sidebar
            for (Component comp : sidebar.getComponents()) {
                if (comp instanceof JButton) {
                    JButton btn = (JButton) comp;
                    String btnText = btn.getText();
                    
                    // Skip logout button
                    if (btnText.equals("Logout")) {
                        continue;
                    }

                    // Extract actual card name
                    String cardName = "";
                    if (btnText.contains("Dashboard")) cardName = "Dashboard";
                    else if (btnText.contains("Faculty")) cardName = "Teachers";
                    else if (btnText.contains("Catalog")) cardName = "Subjects";
                    else if (btnText.contains("Allocator")) cardName = "Rooms";
                    else if (btnText.contains("Planner")) cardName = "Timetable Planner";
                    else if (btnText.contains("Search")) cardName = "View Schedule";

                    if (cardName.isEmpty()) continue;

                    // Programmatically trigger button click to switch card
                    btn.doClick();
                    frame.validate();
                    frame.repaint();

                    // Sleep a small duration to allow tables to render and repaint
                    Thread.sleep(600);

                    // Capture frame
                    BufferedImage img = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
                    Graphics2D g = img.createGraphics();
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    frame.paint(g);
                    g.dispose();

                    String filename = "dashboard_" + cardName.toLowerCase().replace(" ", "_") + ".png";
                    ImageIO.write(img, "PNG", new File("screenshots/" + filename));
                    System.out.println("Generated: screenshots/" + filename);
                }
            }
            frame.dispose();
        } catch (Exception e) {
            System.err.println("Error generating dashboard screenshots: " + e.getMessage());
        }
    }
}
