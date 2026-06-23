# UNIVERSITY TIMETABLE MANAGEMENT SYSTEM
## Semester Project Documentation Report

---

### **1. Cover Page**

* **Project Title:** University Timetable Management System
* **Course Title:** Software Construction & Development
* **Course Code:** CS-3001 / SE-302
* **Semester:** Spring 2026
* **Submitted By:** Syeda Wafa Nadir
* **Roll Number:** l1f23bsse0425
* **Department:** Department of Software Engineering
* **Institution:** National University of Computer and Emerging Sciences
* **Submitted To:** Project Course Instructor / Evaluation Committee

---

### **2. Introduction**
The **University Timetable Management System** is a robust desktop application built using Java Swing (incorporating the modern FlatLaf Flat Light Look & Feel) and SQLite. 

University course scheduling is inherently complicated due to the multi-dimensional constraints of rooms, instructors, student sections, and time slots. This project delivers an elegant graphical user interface (GUI) that empowers administrators to manage teachers, subjects, classroom resources, and sections. 

It provides an automated conflict-checking scheduling engine that ensures error-free, double-booking-proof timetables.

---

### **3. Objectives**
The primary objectives of developing this system are:
1. **Automate Scheduling:** Replace error-prone spreadsheet scheduling with an automated, computerized relational model.
2. **Conflict Prevention:** Detect and block scheduling conflicts (room clashes, teacher clashes, section clashes) in real-time.
3. **Information Integrity:** Maintain consistent relational integrity between Teachers, Subjects, Rooms, and Timetable slots using SQLite databases.
4. **Intuitive UX:** Create an administrative control dashboard using high-quality rendering, sidebar navigation, and search/filter components.
5. **Report Generation:** Offer dynamic view filters and export/print schedules to facilitate distribution.

---

### **4. Problem Statement**
In universities, manual timetable compilation takes days of administrative overhead. The major challenges of manual systems are:
* **Overlapping Schedules:** Assigning a teacher to two different classes at the same time.
* **Double Booking:** Booking the same classroom for two different sections simultaneously.
* **Capacity Overloading:** Assigning a class section with 80 students to a lab space holding only 40.
* **Data Discrepancy:** Lack of a centralized database leading to outdated schedule sheets.

This application solves these problems by running dynamic relational validations before committing any schedule entry to the database.

---

### **5. System Architecture & OOP Implementation**
The system is designed with clean architectural division of concerns, using Object-Oriented Programming (OOP) paradigms:

```
[Main Entry Point] -> [UI Frames] -> [Service Controllers] -> [Model Data Objects] -> [Utility SQLite DB]
```

#### **Object-Oriented Concepts Used:**
1. **Encapsulation:** 
   Model classes (`Teacher`, `Subject`, `Room`, `Timetable`) isolate data using `private` access modifiers and expose data through public getter and setter methods. Input validation is performed within setters/validators.
2. **Inheritance:** 
   GUI classes (`LoginFrame` and `DashboardFrame`) inherit from Java Swing's `javax.swing.JFrame` to leverage framework window properties while extending custom panels.
3. **Abstraction:** 
   The `DatabaseService` class abstracts the database querying layer. The UI components are unaware of SQL queries or connection drivers; they interact solely with Java collections.
4. **Polymorphism:** 
   * **Method Overriding:** Custom rendering components override `paintComponent()` or `paint()` methods for custom designs.
   * **Method Overloading:** Constructors inside Model classes are overloaded to initialize objects differently depending on whether they are new (no ID) or retrieved from the database (with ID).

#### **Relational Database Schema:**
* **Admins:** `admin_id` (PK), `username`, `password`
* **Teachers:** `teacher_id` (PK), `name`, `department`, `email`
* **Subjects:** `subject_id` (PK), `name`, `credit_hours`, `assigned_teacher_id` (FK -> Teachers)
* **Rooms:** `room_id` (PK), `room_number`, `capacity`
* **Timetables:** `timetable_id` (PK), `day`, `time_slot`, `subject_id` (FK), `teacher_id` (FK), `room_id` (FK), `section`

---

### **6. Screenshots**

Here is the flow of the administrative interface:

#### **A. Admin Security Login Portal**
Requires secure authentication. Prevents unauthorized database tampering.
![Login Screen](../screenshots/login.png)

#### **B. Dashboard Metrics Console**
Displays statistics of teachers, courses, rooms, and schedule entries in real-time.
![Dashboard Screen](../screenshots/dashboard_dashboard.png)

#### **C. Faculty / Teacher CRUD Panel**
Manage academic professors with active email/department integrity.
![Faculty Screen](../screenshots/dashboard_teachers.png)

#### **D. Subject Management & Teacher Assignment**
Assign academic subjects to specific teachers.
![Course Screen](../screenshots/dashboard_subjects.png)

#### **E. Classroom Allocator**
Manage seating capacity limits and room locations.
![Rooms Screen](../screenshots/dashboard_rooms.png)

#### **F. Conflict-Checking Timetable Planner**
The central scheduler module. Applies conflict detection before booking.
![Planner Screen](../screenshots/dashboard_timetable_planner.png)

#### **G. Dynamic Schedule Viewer**
Filter schedules instantly by teacher name, section, or room, with a Print functionality.
![View Screen](../screenshots/dashboard_view_schedule.png)

---

### **7. Testing & Conflict Validation**

The conflict detection engine is verified through rigorous test cases:

| Test Case ID | Scenario | Input / Action | Expected Result | Actual Result |
| :---: | :--- | :--- | :--- | :--- |
| **TC-01** | Empty Fields Login | Leave fields blank and submit | Alert popup: "Please enter username and password" | **Pass** |
| **TC-02** | Invalid Credentials | Username: `admin`, Password: `wrong` | Alert: "Invalid username or password" | **Pass** |
| **TC-03** | Faculty Email Validation | Add teacher with email `not_an_email` | Block add, highlight email error | **Pass** |
| **TC-04** | Room Conflict | Book `Lecture Hall A` for two sections at Monday 08:30 AM | Alert: "Room is already booked for Monday at 08:30 AM" | **Pass** |
| **TC-05** | Teacher Conflict | Schedule `Dr. Syeda Wafa` to two classes at Monday 08:30 AM | Alert: "Teacher is already scheduled for another class at this time" | **Pass** |
| **TC-06** | Section Conflict | Schedule `BSSE-4A` to two classes at Tuesday 10:00 AM | Alert: "Section BSSE-4A is already scheduled for a class at this time" | **Pass** |

---

### **8. Conclusion**
The **University Timetable Management System** successfully automates scheduling and resolves resource conflicts using a robust, database-driven Java application. The integration of SQLite JDBC handles data persistence seamlessly, and the FlatLaf UI delivers an elegant user experience. By implementing OOP concepts and defensive programming principles (validations/exceptions), the software is maintainable, secure, and ready for deployment.

---

### **9. Future Enhancements**
To expand the software's capabilities, future work will include:
1. **AI Auto-Scheduler:** Implement a genetic scheduling algorithm to auto-generate conflict-free timetables based on constraint priorities.
2. **Student/Teacher Web Portal:** Migrate the system to a web application or client-server model where students can view their personalized class timetables.
3. **Data Import/Export:** Add CSV/Excel upload parsers to seed faculty rosters and room lists instantly.
