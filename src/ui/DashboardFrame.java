package ui;

import model.*;
import service.DatabaseService;
import service.TimetableService;
import utility.Validator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class DashboardFrame extends JFrame {
    private DatabaseService dbService;
    private TimetableService timetableService;

    // Content Panels
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    // Active Card Indicator
    private String currentCard = "Dashboard";

    // Theme Color Palette: Dark slate & Royal blue
    private final Color primaryColor = new Color(37, 99, 235);    // #2563EB (Brighter blue)
    private final Color primaryHover = new Color(29, 78, 216);    // #1D4ED8 (Darker hover blue)
    private final Color sidebarBg = new Color(15, 23, 42);        // #0F172A (Deep Slate Sidebar)
    private final Color sidebarHover = new Color(30, 41, 59);      // #1E293B (Slate Hover)
    private final Color bgLight = new Color(248, 250, 252);       // #F8FAFC (Clean Off-White Background)
    private final Color cardBorder = new Color(226, 232, 240);    // #E2E8F0 (Subtle Slate Border)
    private final Color textDark = new Color(15, 23, 42);          // #0F172A (Deep Slate text)
    private final Color textMuted = new Color(71, 85, 105);        // #475569 (Slate Gray text for improved contrast)

    // Fonts (Larger sizes for high-DPI readability)
    private final Font fontTitle = new Font("Segoe UI", Font.BOLD, 24);
    private final Font fontSubTitle = new Font("Segoe UI", Font.BOLD, 16);
    private final Font fontNormal = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font fontBold = new Font("Segoe UI", Font.BOLD, 14);

    // Stats Labels
    private JLabel lblTotalTeachersVal;
    private JLabel lblTotalSubjectsVal;
    private JLabel lblTotalRoomsVal;
    private JLabel lblTotalSchedulesVal;

    public DashboardFrame() {
        dbService = new DatabaseService();
        timetableService = new TimetableService();
        initializeUI();
        refreshStats();
        // Load initial records immediately on start
        refreshTabContent("Dashboard");
    }

    private void initializeUI() {
        setTitle("University Timetable Management System");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Panel splits into Sidebar (West) and Card Canvas (Center)
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(bgLight);

        createSidebar();
        createContentArea();

        container.add(sidebarPanel, BorderLayout.WEST);
        container.add(contentPanel, BorderLayout.CENTER);

        add(container);
    }

    private void createSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setPreferredSize(new Dimension(250, 750));
        sidebarPanel.setBackground(sidebarBg);
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));

        // System Brand Icon and Name
        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 20));
        brandPanel.setOpaque(false);
        brandPanel.setMaximumSize(new Dimension(250, 80));

        JLabel lblBrandIcon = new JLabel("🏫");
        lblBrandIcon.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        brandPanel.add(lblBrandIcon);

        JLabel lblBrandName = new JLabel("Scheduler Pro");
        lblBrandName.setForeground(Color.WHITE);
        lblBrandName.setFont(new Font("Segoe UI", Font.BOLD, 18));
        brandPanel.add(lblBrandName);

        sidebarPanel.add(brandPanel);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Navigation Items
        String[][] menuItems = {
                {"Dashboard", "📊   Dashboard"},
                {"Teachers", "👨‍🏫   Faculty Members"},
                {"Subjects", "📚   Course Catalog"},
                {"Rooms", "🏫   Room Allocator"},
                {"Timetable Planner", "📅   Schedule Planner"},
                {"View Schedule", "🔍   Search & Print"}
        };

        for (String[] item : menuItems) {
            JButton btn = new JButton(item[1]);
            btn.setMaximumSize(new Dimension(250, 50));
            btn.setFont(fontBold);
            btn.setForeground(Color.WHITE); // Always Solid White for visibility
            btn.setBackground(sidebarBg);
            btn.setFocusPainted(false);
            btn.putClientProperty("JButton.buttonType", "toolBarButton");
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Default Highlight for Dashboard
            if (item[0].equals("Dashboard")) {
                btn.setBackground(primaryColor);
                btn.setOpaque(true);
                btn.setContentAreaFilled(true);
                btn.setForeground(Color.WHITE);
                btn.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, new Color(96, 165, 250)));
            } else {
                btn.setOpaque(false);
                btn.setContentAreaFilled(false);
                btn.setBorder(BorderFactory.createEmptyBorder(14, 25, 14, 25));
            }

            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent evt) {
                    if (!currentCard.equals(item[0])) {
                        btn.setBackground(sidebarHover);
                        btn.setOpaque(true);
                        btn.setContentAreaFilled(true);
                        btn.setForeground(Color.WHITE);
                    }
                }
                public void mouseExited(MouseEvent evt) {
                    if (!currentCard.equals(item[0])) {
                        btn.setBackground(sidebarBg);
                        btn.setOpaque(false);
                        btn.setContentAreaFilled(false);
                        btn.setForeground(Color.WHITE);
                    }
                }
            });

            btn.addActionListener(e -> {
                currentCard = item[0];
                cardLayout.show(contentPanel, item[0]);
                refreshTabContent(item[0]);

                // Highlight active button
                for (Component c : sidebarPanel.getComponents()) {
                    if (c instanceof JButton && !((JButton) c).getText().equals("Logout")) {
                        JButton b = (JButton) c;
                        // Match button by display label
                        if (b.getText().contains(item[0])) {
                            b.setBackground(primaryColor);
                            b.setOpaque(true);
                            b.setContentAreaFilled(true);
                            b.setForeground(Color.WHITE);
                            b.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, new Color(96, 165, 250)));
                        } else {
                            b.setBackground(sidebarBg);
                            b.setOpaque(false);
                            b.setContentAreaFilled(false);
                            b.setForeground(Color.WHITE);
                            b.setBorder(BorderFactory.createEmptyBorder(14, 25, 14, 25));
                        }
                    }
                }
            });

            sidebarPanel.add(btn);
        }

        sidebarPanel.add(Box.createVerticalGlue());

        // Logout Session Button
        JButton btnLogout = new JButton("Logout");
        btnLogout.setMaximumSize(new Dimension(250, 55));
        btnLogout.setFont(fontBold);
        btnLogout.setForeground(new Color(239, 68, 68)); // Red color
        btnLogout.setBackground(sidebarBg);
        btnLogout.setFocusPainted(false);
        btnLogout.putClientProperty("JButton.buttonType", "toolBarButton");
        btnLogout.setOpaque(false);
        btnLogout.setContentAreaFilled(false);
        btnLogout.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        btnLogout.setHorizontalAlignment(SwingConstants.LEFT);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to end session?", "Logout Request", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new LoginFrame().setVisible(true);
                this.dispose();
            }
        });
        sidebarPanel.add(btnLogout);
    }

    private void createContentArea() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(bgLight);

        // Add modular panels
        contentPanel.add(createDashboardPanel(), "Dashboard");
        contentPanel.add(createTeachersPanel(), "Teachers");
        contentPanel.add(createSubjectsPanel(), "Subjects");
        contentPanel.add(createRoomsPanel(), "Rooms");
        contentPanel.add(createPlannerPanel(), "Timetable Planner");
        contentPanel.add(createViewerPanel(), "View Schedule");
    }

    // Custom utility to style JTables to look modern
    private void styleTable(JTable table) {
        table.setBackground(Color.WHITE);
        table.setRowHeight(36);
        table.setFont(fontNormal);
        table.setForeground(textDark);
        table.setGridColor(new Color(226, 232, 240));
        table.setSelectionBackground(new Color(219, 234, 254)); // Soft blue selection
        table.setSelectionForeground(sidebarBg);
        table.setShowGrid(true);
        table.setFillsViewportHeight(true);

        // Header Styling - Navy Blue Header with White Text
        JTableHeader header = table.getTableHeader();
        header.setBackground(sidebarBg);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(100, 38));

        // Zebra Striping Cell Renderer
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                if (isSelected) {
                    c.setBackground(new Color(219, 234, 254));
                    c.setForeground(sidebarBg);
                } else {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(248, 250, 252)); // Very soft grey
                    }
                    c.setForeground(textDark);
                }
                // Padding
                setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
                return c;
            }
        };
        table.setDefaultRenderer(Object.class, cellRenderer);
    }

    // Custom text field style helper
    private void styleInput(JTextField field, String placeholder) {
        field.setFont(fontNormal);
        field.setForeground(textDark);
        field.putClientProperty("JComponent.roundRect", true);
        if (placeholder != null) {
            field.putClientProperty("JTextField.placeholderText", placeholder);
            field.putClientProperty("JTextField.showClearButton", true);
        }
        field.putClientProperty("JTextField.margin", new Insets(6, 10, 6, 10));
    }

    private void styleComboBox(JComboBox<?> box) {
        box.setFont(fontNormal);
        box.setForeground(textDark);
        box.setBackground(Color.WHITE);
        box.putClientProperty("JComponent.roundRect", true);
    }

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setFont(fontBold);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.putClientProperty("JButton.buttonType", "roundRect");
        btn.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(fontBold);
        label.setForeground(new Color(51, 65, 85)); // Slate 700 - high contrast
        return label;
    }

    private JPanel createRoundedPanel(int radius, Color bg, Color borderCol) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
                if (borderCol != null) {
                    g2.setColor(borderCol);
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
                }
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBackground(bg);
        return panel;
    }

    private void refreshTabContent(String item) {
        if ("Dashboard".equals(item)) {
            refreshStats();
        } else if ("Teachers".equals(item)) {
            refreshTeachersTable();
        } else if ("Subjects".equals(item)) {
            refreshSubjectsTable();
            refreshTeachersCombos();
        } else if ("Rooms".equals(item)) {
            refreshRoomsTable();
        } else if ("Timetable Planner".equals(item)) {
            refreshPlannerCombos();
            refreshPlannerTable();
        } else if ("View Schedule".equals(item)) {
            refreshViewerCombos();
            refreshViewerTable();
        }
    }

    // ==========================================
    // 1. DASHBOARD OVERVIEW PANEL
    // ==========================================
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgLight);
        panel.setBorder(new EmptyBorder(30, 35, 30, 35));

        // Welcome Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel lblWelcome = new JLabel("Welcome to Administrator Console");
        lblWelcome.setFont(fontTitle);
        lblWelcome.setForeground(textDark);
        JLabel lblSub = new JLabel("Real-time summary of the scheduled courses, rooms, and clash constraints.");
        lblSub.setFont(fontNormal);
        lblSub.setForeground(textMuted);
        headerPanel.add(lblWelcome, BorderLayout.NORTH);
        headerPanel.add(lblSub, BorderLayout.SOUTH);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Stats Container
        JPanel statsContainer = new JPanel(new GridLayout(2, 2, 25, 25));
        statsContainer.setOpaque(false);
        statsContainer.setBorder(new EmptyBorder(35, 0, 30, 0));

        lblTotalTeachersVal = new JLabel("0", JLabel.CENTER);
        lblTotalSubjectsVal = new JLabel("0", JLabel.CENTER);
        lblTotalRoomsVal = new JLabel("0", JLabel.CENTER);
        lblTotalSchedulesVal = new JLabel("0", JLabel.CENTER);

        statsContainer.add(createStatCard("Faculty Members", lblTotalTeachersVal, new Color(59, 130, 246), "👨‍🏫"));
        statsContainer.add(createStatCard("Academic Subjects", lblTotalSubjectsVal, new Color(16, 185, 129), "📚"));
        statsContainer.add(createStatCard("Lecture Rooms", lblTotalRoomsVal, new Color(245, 158, 11), "🏫"));
        statsContainer.add(createStatCard("Scheduled Timetable Slots", lblTotalSchedulesVal, new Color(139, 92, 246), "📅"));

        panel.add(statsContainer, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStatCard(String title, JLabel valueLabel, Color accent, String icon) {
        JPanel card = createRoundedPanel(16, Color.WHITE, cardBorder);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(22, 25, 22, 25));

        // Add colored left bar accent as a small rounded component
        JPanel leftAccent = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(accent);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.dispose();
            }
        };
        leftAccent.setOpaque(false);
        leftAccent.setPreferredSize(new Dimension(6, 100));
        card.add(leftAccent, BorderLayout.WEST);

        JPanel inner = new JPanel(new BorderLayout());
        inner.setOpaque(false);
        inner.setBorder(new EmptyBorder(0, 15, 0, 0));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(fontBold);
        lblTitle.setForeground(textMuted);
        inner.add(lblTitle, BorderLayout.NORTH);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        valueLabel.setForeground(textDark);
        valueLabel.setHorizontalAlignment(SwingConstants.LEFT);
        inner.add(valueLabel, BorderLayout.CENTER);

        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 36));
        inner.add(lblIcon, BorderLayout.EAST);

        card.add(inner, BorderLayout.CENTER);
        return card;
    }

    // ==========================================
    // 2. TEACHERS MANAGEMENT
    // ==========================================
    private JTable tblTeachers;
    private DefaultTableModel teachersModel;
    private JTextField txtTeacherName, txtTeacherDept, txtTeacherEmail;
    private int selectedTeacherId = -1;

    private JPanel createTeachersPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(bgLight);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Form Card Left
        JPanel formCard = createRoundedPanel(16, Color.WHITE, cardBorder);
        formCard.setLayout(new GridBagLayout());
        formCard.setBorder(new EmptyBorder(25, 20, 25, 20));
        formCard.setPreferredSize(new Dimension(340, 600));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblFormTitle = new JLabel("Faculty Form Details");
        lblFormTitle.setFont(fontTitle);
        lblFormTitle.setForeground(textDark);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formCard.add(lblFormTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        formCard.add(createFormLabel("Full Name:"), gbc);
        txtTeacherName = new JTextField();
        styleInput(txtTeacherName, "e.g. Dr. John Doe");
        gbc.gridx = 1;
        formCard.add(txtTeacherName, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formCard.add(createFormLabel("Department:"), gbc);
        txtTeacherDept = new JTextField();
        styleInput(txtTeacherDept, "e.g. Computer Science");
        gbc.gridx = 1;
        formCard.add(txtTeacherDept, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formCard.add(createFormLabel("Email Address:"), gbc);
        txtTeacherEmail = new JTextField();
        styleInput(txtTeacherEmail, "e.g. john@university.edu");
        gbc.gridx = 1;
        formCard.add(txtTeacherEmail, gbc);

        // Buttons Panel
        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        btnPanel.setOpaque(false);
        JButton btnAdd = new JButton("Add Teacher");
        styleButton(btnAdd, primaryColor, Color.WHITE);
        JButton btnUpdate = new JButton("Update");
        styleButton(btnUpdate, new Color(16, 185, 129), Color.WHITE);
        JButton btnDelete = new JButton("Delete");
        styleButton(btnDelete, new Color(239, 68, 68), Color.WHITE);
        JButton btnClear = new JButton("Clear");
        styleButton(btnClear, new Color(107, 114, 128), Color.WHITE);

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 5, 5, 5);
        formCard.add(btnPanel, gbc);

        // Table List Right
        JPanel tableContainer = createRoundedPanel(16, Color.WHITE, cardBorder);
        tableContainer.setLayout(new BorderLayout());
        tableContainer.setBorder(new EmptyBorder(10, 10, 10, 10));

        teachersModel = new DefaultTableModel(new String[]{"ID", "Faculty Name", "Department", "Email"}, 0);
        tblTeachers = new JTable(teachersModel) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        styleTable(tblTeachers);
        tblTeachers.getSelectionModel().addListSelectionListener(e -> {
            int row = tblTeachers.getSelectedRow();
            if (row >= 0) {
                selectedTeacherId = (Integer) tblTeachers.getValueAt(row, 0);
                txtTeacherName.setText((String) tblTeachers.getValueAt(row, 1));
                txtTeacherDept.setText((String) tblTeachers.getValueAt(row, 2));
                txtTeacherEmail.setText((String) tblTeachers.getValueAt(row, 3));
            }
        });

        JScrollPane scroll = new JScrollPane(tblTeachers);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        tableContainer.add(scroll, BorderLayout.CENTER);

        // CRUD Triggers
        btnAdd.addActionListener(e -> {
            if (validateTeacherFields()) {
                Teacher t = new Teacher(0, txtTeacherName.getText().trim(), txtTeacherDept.getText().trim(), txtTeacherEmail.getText().trim());
                if (dbService.addTeacher(t)) {
                    JOptionPane.showMessageDialog(this, "Faculty member added successfully!");
                    clearTeacherForm();
                    refreshTeachersTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add. Double check unique email.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnUpdate.addActionListener(e -> {
            if (selectedTeacherId == -1) {
                JOptionPane.showMessageDialog(this, "Please select record from the list.", "Selection Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (validateTeacherFields()) {
                Teacher t = new Teacher(selectedTeacherId, txtTeacherName.getText().trim(), txtTeacherDept.getText().trim(), txtTeacherEmail.getText().trim());
                if (dbService.updateTeacher(t)) {
                    JOptionPane.showMessageDialog(this, "Faculty details updated.");
                    clearTeacherForm();
                    refreshTeachersTable();
                }
            }
        });

        btnDelete.addActionListener(e -> {
            if (selectedTeacherId == -1) {
                JOptionPane.showMessageDialog(this, "Please select record from the list.", "Selection Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Remove this faculty member? All associated schedules will clear.", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (dbService.deleteTeacher(selectedTeacherId)) {
                    clearTeacherForm();
                    refreshTeachersTable();
                }
            }
        });

        btnClear.addActionListener(e -> clearTeacherForm());

        panel.add(formCard, BorderLayout.WEST);
        panel.add(tableContainer, BorderLayout.CENTER);
        return panel;
    }

    private void refreshTeachersTable() {
        teachersModel.setRowCount(0);
        List<Teacher> list = dbService.getAllTeachers();
        for (Teacher t : list) {
            teachersModel.addRow(new Object[]{t.getTeacherId(), t.getTeacherName(), t.getDepartment(), t.getEmail()});
        }
    }

    private boolean validateTeacherFields() {
        if (Validator.isEmpty(txtTeacherName.getText())) {
            JOptionPane.showMessageDialog(this, "Full Name is required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (Validator.isEmpty(txtTeacherDept.getText())) {
            JOptionPane.showMessageDialog(this, "Department is required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!Validator.isValidEmail(txtTeacherEmail.getText())) {
            JOptionPane.showMessageDialog(this, "Valid email address required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void clearTeacherForm() {
        txtTeacherName.setText("");
        txtTeacherDept.setText("");
        txtTeacherEmail.setText("");
        selectedTeacherId = -1;
        tblTeachers.clearSelection();
    }

    // ==========================================
    // 3. SUBJECTS MANAGEMENT
    // ==========================================
    private JTable tblSubjects;
    private DefaultTableModel subjectsModel;
    private JTextField txtSubjectName, txtCreditHours;
    private JComboBox<Teacher> cmbAssignTeacher;
    private int selectedSubjectId = -1;

    private JPanel createSubjectsPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(bgLight);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Form Card Left
        JPanel formCard = createRoundedPanel(16, Color.WHITE, cardBorder);
        formCard.setLayout(new GridBagLayout());
        formCard.setBorder(new EmptyBorder(25, 20, 25, 20));
        formCard.setPreferredSize(new Dimension(340, 600));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblFormTitle = new JLabel("Subject Details");
        lblFormTitle.setFont(fontTitle);
        lblFormTitle.setForeground(textDark);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formCard.add(lblFormTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        formCard.add(createFormLabel("Subject Name:"), gbc);
        txtSubjectName = new JTextField();
        styleInput(txtSubjectName, "e.g. Object Oriented Programming");
        gbc.gridx = 1;
        formCard.add(txtSubjectName, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formCard.add(createFormLabel("Credit Hours:"), gbc);
        txtCreditHours = new JTextField();
        styleInput(txtCreditHours, "e.g. 3");
        gbc.gridx = 1;
        formCard.add(txtCreditHours, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formCard.add(createFormLabel("Assigned Faculty:"), gbc);
        cmbAssignTeacher = new JComboBox<>();
        styleComboBox(cmbAssignTeacher);
        gbc.gridx = 1;
        formCard.add(cmbAssignTeacher, gbc);

        // Buttons
        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        btnPanel.setOpaque(false);
        JButton btnAdd = new JButton("Add Subject");
        styleButton(btnAdd, primaryColor, Color.WHITE);
        JButton btnUpdate = new JButton("Update");
        styleButton(btnUpdate, new Color(16, 185, 129), Color.WHITE);
        JButton btnDelete = new JButton("Delete");
        styleButton(btnDelete, new Color(239, 68, 68), Color.WHITE);
        JButton btnClear = new JButton("Clear");
        styleButton(btnClear, new Color(107, 114, 128), Color.WHITE);

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 5, 5, 5);
        formCard.add(btnPanel, gbc);

        // Table
        JPanel tableContainer = createRoundedPanel(16, Color.WHITE, cardBorder);
        tableContainer.setLayout(new BorderLayout());
        tableContainer.setBorder(new EmptyBorder(10, 10, 10, 10));

        subjectsModel = new DefaultTableModel(new String[]{"ID", "Subject Name", "Credits", "Teacher ID", "Assigned Faculty"}, 0);
        tblSubjects = new JTable(subjectsModel) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        styleTable(tblSubjects);
        tblSubjects.getSelectionModel().addListSelectionListener(e -> {
            int row = tblSubjects.getSelectedRow();
            if (row >= 0) {
                selectedSubjectId = (Integer) tblSubjects.getValueAt(row, 0);
                txtSubjectName.setText((String) tblSubjects.getValueAt(row, 1));
                txtCreditHours.setText(String.valueOf(tblSubjects.getValueAt(row, 2)));
                
                int tId = (Integer) tblSubjects.getValueAt(row, 3);
                for (int i = 0; i < cmbAssignTeacher.getItemCount(); i++) {
                    Teacher t = cmbAssignTeacher.getItemAt(i);
                    if (t != null && t.getTeacherId() == tId) {
                        cmbAssignTeacher.setSelectedIndex(i);
                        break;
                    }
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tblSubjects);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        tableContainer.add(scroll, BorderLayout.CENTER);

        // Actions
        btnAdd.addActionListener(e -> {
            if (validateSubjectFields()) {
                Teacher t = (Teacher) cmbAssignTeacher.getSelectedItem();
                int tId = (t != null) ? t.getTeacherId() : 0;
                Subject s = new Subject(0, txtSubjectName.getText().trim(), Integer.parseInt(txtCreditHours.getText().trim()), tId);
                if (dbService.addSubject(s)) {
                    clearSubjectForm();
                    refreshSubjectsTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add Subject. Name must be unique.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnUpdate.addActionListener(e -> {
            if (selectedSubjectId == -1) {
                JOptionPane.showMessageDialog(this, "Please select record from the list.", "Selection Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (validateSubjectFields()) {
                Teacher t = (Teacher) cmbAssignTeacher.getSelectedItem();
                int tId = (t != null) ? t.getTeacherId() : 0;
                Subject s = new Subject(selectedSubjectId, txtSubjectName.getText().trim(), Integer.parseInt(txtCreditHours.getText().trim()), tId);
                if (dbService.updateSubject(s)) {
                    clearSubjectForm();
                    refreshSubjectsTable();
                }
            }
        });

        btnDelete.addActionListener(e -> {
            if (selectedSubjectId == -1) {
                JOptionPane.showMessageDialog(this, "Please select record from the list.", "Selection Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Remove subject? Active timetable slots using this course will delete.", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (dbService.deleteSubject(selectedSubjectId)) {
                    clearSubjectForm();
                    refreshSubjectsTable();
                }
            }
        });

        btnClear.addActionListener(e -> clearSubjectForm());

        panel.add(formCard, BorderLayout.WEST);
        panel.add(tableContainer, BorderLayout.CENTER);
        return panel;
    }

    private void refreshSubjectsTable() {
        subjectsModel.setRowCount(0);
        List<Subject> sList = dbService.getAllSubjects();
        List<Teacher> tList = dbService.getAllTeachers();

        for (Subject s : sList) {
            String tName = "None Assigned";
            for (Teacher t : tList) {
                if (t.getTeacherId() == s.getAssignedTeacherId()) {
                    tName = t.getTeacherName();
                    break;
                }
            }
            subjectsModel.addRow(new Object[]{s.getSubjectId(), s.getSubjectName(), s.getCreditHours(), s.getAssignedTeacherId(), tName});
        }
    }

    private void refreshTeachersCombos() {
        cmbAssignTeacher.removeAllItems();
        cmbAssignTeacher.addItem(null); // Blank option
        List<Teacher> list = dbService.getAllTeachers();
        for (Teacher t : list) {
            cmbAssignTeacher.addItem(t);
        }
    }

    private boolean validateSubjectFields() {
        if (Validator.isEmpty(txtSubjectName.getText())) {
            JOptionPane.showMessageDialog(this, "Subject name required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!Validator.isPositiveNumber(txtCreditHours.getText())) {
            JOptionPane.showMessageDialog(this, "Credit hours must be positive.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void clearSubjectForm() {
        txtSubjectName.setText("");
        txtCreditHours.setText("");
        if (cmbAssignTeacher.getItemCount() > 0) cmbAssignTeacher.setSelectedIndex(0);
        selectedSubjectId = -1;
        tblSubjects.clearSelection();
    }

    // ==========================================
    // 4. ROOMS MANAGEMENT
    // ==========================================
    private JTable tblRooms;
    private DefaultTableModel roomsModel;
    private JTextField txtRoomNum, txtRoomCapacity;
    private int selectedRoomId = -1;

    private JPanel createRoomsPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(bgLight);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Form Card Left
        JPanel formCard = createRoundedPanel(16, Color.WHITE, cardBorder);
        formCard.setLayout(new GridBagLayout());
        formCard.setBorder(new EmptyBorder(25, 20, 25, 20));
        formCard.setPreferredSize(new Dimension(340, 600));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblFormTitle = new JLabel("Classroom Setup");
        lblFormTitle.setFont(fontTitle);
        lblFormTitle.setForeground(textDark);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formCard.add(lblFormTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        formCard.add(createFormLabel("Room/Hall # :"), gbc);
        txtRoomNum = new JTextField();
        styleInput(txtRoomNum, "e.g. CS-102");
        gbc.gridx = 1;
        formCard.add(txtRoomNum, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formCard.add(createFormLabel("Student Capacity:"), gbc);
        txtRoomCapacity = new JTextField();
        styleInput(txtRoomCapacity, "e.g. 50");
        gbc.gridx = 1;
        formCard.add(txtRoomCapacity, gbc);

        // Buttons
        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        btnPanel.setOpaque(false);
        JButton btnAdd = new JButton("Add Room");
        styleButton(btnAdd, primaryColor, Color.WHITE);
        JButton btnUpdate = new JButton("Update");
        styleButton(btnUpdate, new Color(16, 185, 129), Color.WHITE);
        JButton btnDelete = new JButton("Delete");
        styleButton(btnDelete, new Color(239, 68, 68), Color.WHITE);
        JButton btnClear = new JButton("Clear");
        styleButton(btnClear, new Color(107, 114, 128), Color.WHITE);

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 5, 5, 5);
        formCard.add(btnPanel, gbc);

        // Table
        JPanel tableContainer = createRoundedPanel(16, Color.WHITE, cardBorder);
        tableContainer.setLayout(new BorderLayout());
        tableContainer.setBorder(new EmptyBorder(10, 10, 10, 10));

        roomsModel = new DefaultTableModel(new String[]{"ID", "Room Number", "Capacity Limits"}, 0);
        tblRooms = new JTable(roomsModel) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        styleTable(tblRooms);
        tblRooms.getSelectionModel().addListSelectionListener(e -> {
            int row = tblRooms.getSelectedRow();
            if (row >= 0) {
                selectedRoomId = (Integer) tblRooms.getValueAt(row, 0);
                txtRoomNum.setText((String) tblRooms.getValueAt(row, 1));
                txtRoomCapacity.setText(String.valueOf(tblRooms.getValueAt(row, 2)));
            }
        });

        JScrollPane scroll = new JScrollPane(tblRooms);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        tableContainer.add(scroll, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> {
            if (validateRoomFields()) {
                Room r = new Room(0, txtRoomNum.getText().trim(), Integer.parseInt(txtRoomCapacity.getText().trim()));
                if (dbService.addRoom(r)) {
                    clearRoomForm();
                    refreshRoomsTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Room Number must be unique.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnUpdate.addActionListener(e -> {
            if (selectedRoomId == -1) {
                JOptionPane.showMessageDialog(this, "Select record from list first.", "Selection Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (validateRoomFields()) {
                Room r = new Room(selectedRoomId, txtRoomNum.getText().trim(), Integer.parseInt(txtRoomCapacity.getText().trim()));
                if (dbService.updateRoom(r)) {
                    clearRoomForm();
                    refreshRoomsTable();
                }
            }
        });

        btnDelete.addActionListener(e -> {
            if (selectedRoomId == -1) {
                JOptionPane.showMessageDialog(this, "Select record from list first.", "Selection Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Delete Room? Clashes will clear.", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (dbService.deleteRoom(selectedRoomId)) {
                    clearRoomForm();
                    refreshRoomsTable();
                }
            }
        });

        btnClear.addActionListener(e -> clearRoomForm());

        panel.add(formCard, BorderLayout.WEST);
        panel.add(tableContainer, BorderLayout.CENTER);
        return panel;
    }

    private void refreshRoomsTable() {
        roomsModel.setRowCount(0);
        List<Room> list = dbService.getAllRooms();
        for (Room r : list) {
            roomsModel.addRow(new Object[]{r.getRoomId(), r.getRoomNumber(), r.getCapacity()});
        }
    }

    private boolean validateRoomFields() {
        if (Validator.isEmpty(txtRoomNum.getText())) {
            JOptionPane.showMessageDialog(this, "Room identification required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!Validator.isPositiveNumber(txtRoomCapacity.getText())) {
            JOptionPane.showMessageDialog(this, "Seat capacity must be positive.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void clearRoomForm() {
        txtRoomNum.setText("");
        txtRoomCapacity.setText("");
        selectedRoomId = -1;
        tblRooms.clearSelection();
    }

    // ==========================================
    // 5. TIMETABLE PLANNER / GENERATOR
    // ==========================================
    private JComboBox<Subject> cmbPlannerSubject;
    private JComboBox<Room> cmbPlannerRoom;
    private JComboBox<String> cmbPlannerDay;
    private JComboBox<String> cmbPlannerSlot;
    private JTextField txtPlannerSection;
    private JLabel lblPlannerAssignedTeacher;

    private JTable tblPlannerEntries;
    private DefaultTableModel plannerModel;

    private JPanel createPlannerPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(bgLight);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Form Card Left
        JPanel formCard = createRoundedPanel(16, Color.WHITE, cardBorder);
        formCard.setLayout(new GridBagLayout());
        formCard.setBorder(new EmptyBorder(25, 20, 25, 20));
        formCard.setPreferredSize(new Dimension(360, 600));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblFormTitle = new JLabel("Schedule Planner");
        lblFormTitle.setFont(fontTitle);
        lblFormTitle.setForeground(textDark);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formCard.add(lblFormTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        formCard.add(createFormLabel("Subject:"), gbc);
        cmbPlannerSubject = new JComboBox<>();
        styleComboBox(cmbPlannerSubject);
        gbc.gridx = 1;
        formCard.add(cmbPlannerSubject, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formCard.add(createFormLabel("Faculty:"), gbc);
        lblPlannerAssignedTeacher = new JLabel("None");
        lblPlannerAssignedTeacher.setFont(fontBold);
        lblPlannerAssignedTeacher.setForeground(primaryColor);
        gbc.gridx = 1;
        formCard.add(lblPlannerAssignedTeacher, gbc);

        cmbPlannerSubject.addActionListener(e -> {
            Subject s = (Subject) cmbPlannerSubject.getSelectedItem();
            if (s != null && s.getAssignedTeacherId() > 0) {
                List<Teacher> list = dbService.getAllTeachers();
                for (Teacher t : list) {
                    if (t.getTeacherId() == s.getAssignedTeacherId()) {
                        lblPlannerAssignedTeacher.setText(t.getTeacherName());
                        return;
                    }
                }
            }
            lblPlannerAssignedTeacher.setText("None (Unassigned)");
        });

        gbc.gridx = 0; gbc.gridy = 3;
        formCard.add(createFormLabel("Location Room:"), gbc);
        cmbPlannerRoom = new JComboBox<>();
        styleComboBox(cmbPlannerRoom);
        gbc.gridx = 1;
        formCard.add(cmbPlannerRoom, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formCard.add(createFormLabel("Section/Class:"), gbc);
        txtPlannerSection = new JTextField();
        styleInput(txtPlannerSection, "e.g. BSSE-4A");
        gbc.gridx = 1;
        formCard.add(txtPlannerSection, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        formCard.add(createFormLabel("Day:"), gbc);
        cmbPlannerDay = new JComboBox<>(new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"});
        styleComboBox(cmbPlannerDay);
        gbc.gridx = 1;
        formCard.add(cmbPlannerDay, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        formCard.add(createFormLabel("Time Slot:"), gbc);
        cmbPlannerSlot = new JComboBox<>(new String[]{
                "08:30 AM - 10:00 AM",
                "10:00 AM - 11:30 AM",
                "11:30 AM - 01:00 PM",
                "01:30 PM - 03:00 PM",
                "03:00 PM - 04:30 PM"
        });
        styleComboBox(cmbPlannerSlot);
        gbc.gridx = 1;
        formCard.add(cmbPlannerSlot, gbc);

        // Buttons
        JPanel btnPanel = new JPanel(new GridLayout(2, 1, 8, 8));
        btnPanel.setOpaque(false);
        JButton btnAddSlot = new JButton("Generate / Book Slot");
        styleButton(btnAddSlot, primaryColor, Color.WHITE);
        JButton btnDeleteSlot = new JButton("Cancel Scheduled Slot");
        styleButton(btnDeleteSlot, new Color(239, 68, 68), Color.WHITE);
        btnPanel.add(btnAddSlot);
        btnPanel.add(btnDeleteSlot);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 5, 5);
        formCard.add(btnPanel, gbc);

        // Table
        JPanel tableContainer = createRoundedPanel(16, Color.WHITE, cardBorder);
        tableContainer.setLayout(new BorderLayout());
        tableContainer.setBorder(new EmptyBorder(10, 10, 10, 10));

        plannerModel = new DefaultTableModel(new String[]{"ID", "Day", "Time Slot", "Section", "Subject Course", "Faculty Assigned", "Room Location"}, 0);
        tblPlannerEntries = new JTable(plannerModel) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        styleTable(tblPlannerEntries);
        JScrollPane scroll = new JScrollPane(tblPlannerEntries);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        tableContainer.add(scroll, BorderLayout.CENTER);

        // Actions
        btnAddSlot.addActionListener(e -> {
            Subject sub = (Subject) cmbPlannerSubject.getSelectedItem();
            Room room = (Room) cmbPlannerRoom.getSelectedItem();
            String section = txtPlannerSection.getText().trim();
            String day = (String) cmbPlannerDay.getSelectedItem();
            String slot = (String) cmbPlannerSlot.getSelectedItem();

            if (sub == null || sub.getAssignedTeacherId() <= 0) {
                JOptionPane.showMessageDialog(this, "Select a subject with assigned faculty.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (room == null || section.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Room location and section class are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Conflict Checker trigger
            String conflictError = timetableService.validateSchedule(day, slot, sub.getAssignedTeacherId(), room.getRoomId(), section);
            if (conflictError != null) {
                JOptionPane.showMessageDialog(this, conflictError, "Schedule Conflict Clashes", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Timetable entry = new Timetable(0, day, slot, sub.getSubjectId(), sub.getAssignedTeacherId(), room.getRoomId(), section);
            if (dbService.addTimetable(entry)) {
                JOptionPane.showMessageDialog(this, "Schedule successfully generated.");
                refreshPlannerTable();
                refreshStats();
            }
        });

        btnDeleteSlot.addActionListener(e -> {
            int row = tblPlannerEntries.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a scheduled slot row to delete.", "Selection Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = (Integer) tblPlannerEntries.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel this scheduled slot?", "Cancel Slot", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (dbService.deleteTimetable(id)) {
                    refreshPlannerTable();
                    refreshStats();
                }
            }
        });

        panel.add(formCard, BorderLayout.WEST);
        panel.add(tableContainer, BorderLayout.CENTER);
        return panel;
    }

    private void refreshPlannerCombos() {
        cmbPlannerSubject.removeAllItems();
        List<Subject> sList = dbService.getAllSubjects();
        for (Subject s : sList) {
            cmbPlannerSubject.addItem(s);
        }

        cmbPlannerRoom.removeAllItems();
        List<Room> rList = dbService.getAllRooms();
        for (Room r : rList) {
            cmbPlannerRoom.addItem(r);
        }
    }

    private void refreshPlannerTable() {
        plannerModel.setRowCount(0);
        List<Timetable> list = dbService.getAllTimetableEntries();
        for (Timetable t : list) {
            plannerModel.addRow(new Object[]{
                    t.getTimetableId(), t.getDay(), t.getTimeSlot(), t.getSection(),
                    t.getSubjectName(), t.getTeacherName(), t.getRoomNumber()
            });
        }
    }

    // ==========================================
    // 6. VIEWING & PRINT REPORT PANEL
    // ==========================================
    private JComboBox<String> cmbViewFilterType;
    private JComboBox<Object> cmbViewFilterVal;
    private JComboBox<String> cmbViewSection;
    private JTable tblViewerGrid;
    private DefaultTableModel viewerModel;

    private JPanel createViewerPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(bgLight);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Filters bar panel
        JPanel filterBar = createRoundedPanel(12, Color.WHITE, cardBorder);
        filterBar.setLayout(new GridBagLayout());
        filterBar.setBorder(new EmptyBorder(15, 20, 15, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Inputs
        // Col 0: Filter Type
        JPanel p0 = new JPanel(new BorderLayout(5, 5));
        p0.setOpaque(false);
        p0.add(createFormLabel("Search Filter:"), BorderLayout.NORTH);
        cmbViewFilterType = new JComboBox<>(new String[]{"All Schedules", "Teacher", "Room"});
        styleComboBox(cmbViewFilterType);
        p0.add(cmbViewFilterType, BorderLayout.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0;
        filterBar.add(p0, gbc);

        // Col 1: Entity Name
        JPanel p1 = new JPanel(new BorderLayout(5, 5));
        p1.setOpaque(false);
        p1.add(createFormLabel("Entity Name:"), BorderLayout.NORTH);
        cmbViewFilterVal = new JComboBox<>();
        styleComboBox(cmbViewFilterVal);
        p1.add(cmbViewFilterVal, BorderLayout.CENTER);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        filterBar.add(p1, gbc);

        // Col 2: Section/Class
        JPanel p2 = new JPanel(new BorderLayout(5, 5));
        p2.setOpaque(false);
        p2.add(createFormLabel("Section/Class:"), BorderLayout.NORTH);
        cmbViewSection = new JComboBox<>();
        styleComboBox(cmbViewSection);
        p2.add(cmbViewSection, BorderLayout.CENTER);
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 1.0;
        filterBar.add(p2, gbc);

        // Row 1: Buttons
        JPanel pButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pButtons.setOpaque(false);

        JButton btnSearch = new JButton("🔍   Filter Search");
        styleButton(btnSearch, primaryColor, Color.WHITE);
        pButtons.add(btnSearch);

        JButton btnPrint = new JButton("🖨️   Print / PDF Report");
        styleButton(btnPrint, new Color(16, 185, 129), Color.WHITE);
        pButtons.add(btnPrint);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
        filterBar.add(pButtons, gbc);

        // Viewer Table Grid list
        JPanel tableContainer = createRoundedPanel(16, Color.WHITE, cardBorder);
        tableContainer.setLayout(new BorderLayout());
        tableContainer.setBorder(new EmptyBorder(10, 10, 10, 10));

        viewerModel = new DefaultTableModel(new String[]{"Day", "Time Slot", "Class Section", "Subject Course Name", "Faculty Assigned", "Lecture Room Location"}, 0);
        tblViewerGrid = new JTable(viewerModel) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        styleTable(tblViewerGrid);
        JScrollPane scroll = new JScrollPane(tblViewerGrid);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        tableContainer.add(scroll, BorderLayout.CENTER);

        // Changing dropdown categories dynamically
        cmbViewFilterType.addActionListener(e -> {
            String type = (String) cmbViewFilterType.getSelectedItem();
            cmbViewFilterVal.removeAllItems();
            if ("Teacher".equalsIgnoreCase(type)) {
                List<Teacher> list = dbService.getAllTeachers();
                for (Teacher t : list) {
                    cmbViewFilterVal.addItem(t);
                }
            } else if ("Room".equalsIgnoreCase(type)) {
                List<Room> list = dbService.getAllRooms();
                for (Room r : list) {
                    cmbViewFilterVal.addItem(r);
                }
            }
        });

        btnSearch.addActionListener(e -> refreshViewerTable());

        btnPrint.addActionListener(e -> {
            try {
                boolean done = tblViewerGrid.print(JTable.PrintMode.FIT_WIDTH,
                        new java.text.MessageFormat("University Timetable Schedule Report"),
                        new java.text.MessageFormat("Page {0}"));
                if (done) {
                    JOptionPane.showMessageDialog(this, "Document printed successfully!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Printing failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(filterBar, BorderLayout.NORTH);
        panel.add(tableContainer, BorderLayout.CENTER);
        return panel;
    }

    private void refreshViewerCombos() {
        cmbViewSection.removeAllItems();
        cmbViewSection.addItem("All");
        List<Timetable> all = dbService.getAllTimetableEntries();
        java.util.Set<String> set = new java.util.HashSet<>();
        for (Timetable t : all) {
            if (t.getSection() != null) set.add(t.getSection());
        }
        for (String sec : set) {
            cmbViewSection.addItem(sec);
        }
    }

    private void refreshViewerTable() {
        viewerModel.setRowCount(0);
        String type = (String) cmbViewFilterType.getSelectedItem();
        Object val = cmbViewFilterVal.getSelectedItem();
        String sec = (String) cmbViewSection.getSelectedItem();

        int filterId = -1;
        if ("Teacher".equalsIgnoreCase(type) && val instanceof Teacher) {
            filterId = ((Teacher) val).getTeacherId();
        } else if ("Room".equalsIgnoreCase(type) && val instanceof Room) {
            filterId = ((Room) val).getRoomId();
        }

        List<Timetable> entries = dbService.getTimetableFiltered(type, filterId, sec);
        for (Timetable t : entries) {
            viewerModel.addRow(new Object[]{
                    t.getDay(), t.getTimeSlot(), t.getSection(),
                    t.getSubjectName(), t.getTeacherName(), t.getRoomNumber()
            });
        }
    }

    private void refreshStats() {
        int tCount = dbService.getCount("teachers");
        int sCount = dbService.getCount("subjects");
        int rCount = dbService.getCount("rooms");
        int schCount = dbService.getCount("timetables");

        if (lblTotalTeachersVal != null) lblTotalTeachersVal.setText(String.valueOf(tCount));
        if (lblTotalSubjectsVal != null) lblTotalSubjectsVal.setText(String.valueOf(sCount));
        if (lblTotalRoomsVal != null) lblTotalRoomsVal.setText(String.valueOf(rCount));
        if (lblTotalSchedulesVal != null) lblTotalSchedulesVal.setText(String.valueOf(schCount));
    }
}
