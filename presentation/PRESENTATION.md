# UNIVERSITY TIMETABLE MANAGEMENT SYSTEM
## Semester Project Presentation Slides Outline

---

### **Slide 1: Title Slide**
* **Project Title:** University Timetable Management System
* **Course:** Software Construction & Development (CS-3001)
* **Presenter:** Syeda Wafa Nadir
* **Roll Number:** l1f23bsse0425
* **Institution:** National University of Computer and Emerging Sciences

---

### **Slide 2: The Problem Statement**
* **Manual Bottleneck:** Compiling a university-wide schedule manually using spreadsheets takes days.
* **Scheduling Clashes:** 
  * Faculty members assigned to multiple locations at once.
  * Classroom double bookings.
  * Overlapping class slots for the same student section.
* **Lack of Data Validation:** Invalid capacity configurations (e.g. large section in a small room).
* **Isolation of Data:** No centralized storage for classrooms, teachers, and subjects.

---

### **Slide 3: Project Idea & Core Solution**
* **Goal:** Create a modern desktop application that handles database-driven timetable planning with built-in clash detection.
* **Key Mechanisms:**
  * Embedded SQLite database storing scheduling entities.
  * Automated conflict-checking logic running on schedule creation.
  * Administrative dashboard to easily view, edit, search, and delete records.

---

### **Slide 4: Key Features**
* **Admin Login & Dashboard Overview:** Secure access control with live metrics counter.
* **Faculty & Subject Management:** Full CRUD actions and subject allocation mapping.
* **Classroom Resource Allocation:** Capacity boundaries to prevent seating overloads.
* **Timetable Scheduler Engine:** Real-time checking for:
  * Teacher Conflicts
  * Room Conflicts
  * Section Conflicts
* **Dynamic Search Viewer & Export:** Filter class times by section, teacher, or room. Print schedule directly.

---

### **Slide 5: Technology Stack**
* **Language:** Java SE (Core JDK)
* **GUI Framework:** Java Swing + FlatLaf Flat Light Look & Feel
* **Database Management:** SQLite 3 (Lightweight embedded DB)
* **Database Driver:** JDBC (SQLite-JDBC Driver Jar)
* **Logging System:** SLF4J API + Simple Logger implementation
* **Build / Run Script:** Custom Windows Batch Command launcher (`run.bat`) for automated JDK/driver downloads and compilation.

---

### **Slide 6: System Design & Object-Oriented Principles**
* **Classes & Objects:** Model objects created dynamically (`Teacher`, `Room`, `Subject`, `Timetable`).
* **Encapsulation:** Class variables set to `private` with validation logic inside setters.
* **Inheritance:** Extends Swing `JFrame` to build standard UI windows.
* **Polymorphism:** Method overriding for interface repaints; Constructor overloading to handle DB row vs. runtime instantiations.
* **Abstraction:** SQLite operations are isolated in `DatabaseService` class using JDBC interface methods.

---

### **Slide 7: Database Relational Schema**
* **Admins:** `admin_id` (PK), `username`, `password`
* **Teachers:** `teacher_id` (PK), `name`, `department`, `email`
* **Subjects:** `subject_id` (PK), `name`, `credit_hours`, `assigned_teacher_id` (FK)
* **Rooms:** `room_id` (PK), `room_number`, `capacity`
* **Timetables:** `timetable_id` (PK), `day`, `time_slot`, `subject_id` (FK), `teacher_id` (FK), `room_id` (FK), `section`

---

### **Slide 8: Conflict Checking Algorithm Demo**
* **Teacher Check:** 
  `SELECT COUNT(*) FROM timetables WHERE day = ? AND time_slot = ? AND teacher_id = ?`
* **Room Check:** 
  `SELECT COUNT(*) FROM timetables WHERE day = ? AND time_slot = ? AND room_id = ?`
* **Section Check:** 
  `SELECT COUNT(*) FROM timetables WHERE day = ? AND time_slot = ? AND section = ?`
* **Behavior:** If any count > 0, throw a customized `Exception` and alert the user with a descriptive dialog box instead of crashing.

---

### **Slide 9: Project Screenshots Demo**
* Show screen layout and FlatLaf design:
  1. *Admin Login Screen*
  2. *Live Dashboard Counters*
  3. *Timetable Scheduler Panel*
  4. *Dynamic Schedule Filter & Search*

---

### **Slide 10: Challenges Faced & Solutions**
1. **Challenge:** Java Swing look-and-feel is outdated by default.
   * *Solution:* Implemented FlatLaf Look-and-Feel for a sleek, modern UI.
2. **Challenge:** Hard to compile and run across different systems without IDE setups.
   * *Solution:* Developed a self-contained `run.bat` script that auto-downloads the JDK and drivers and compiles files on the fly.
3. **Challenge:** Keeping data synchronized when records are deleted (e.g. deleting a teacher who has scheduled classes).
   * *Solution:* Utilized SQLite Foreign Key constraints (`ON DELETE CASCADE` and `ON DELETE SET NULL`).

---

### **Slide 11: Conclusion & Future Enhancements**
* **Conclusion:** The application meets all Phase 2 criteria, providing a fully functional, conflict-free administrative tool.
* **Future Steps:**
  1. Integrating a Genetic Algorithm for automated scheduling optimization.
  2. Adding Student/Teacher read-only portal view apps.
  3. Allowing Excel import of teacher registries.
