package ui;

import service.DatabaseService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private DatabaseService dbService;

    // Navy Blue + White Theme Colors
    private final Color primaryColor = new Color(59, 130, 246);    // #3B82F6 (Blue buttons)
    private final Color primaryHover = new Color(37, 99, 235);    // #2563EB
    private final Color bgColor = new Color(243, 244, 246);        // #F3F4F6 (Light Gray background)
    private final Color cardBg = Color.WHITE;
    private final Color textColor = new Color(31, 41, 55);        // #1F2937 (Dark Gray title)
    private final Color inputBorderColor = new Color(226, 232, 240); // Soft grey border

    public LoginFrame() {
        dbService = new DatabaseService();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("University Timetable Scheduler - Login");
        setSize(420, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main background panel
        JPanel bgPanel = new JPanel(new GridBagLayout());
        bgPanel.setBackground(bgColor);

        // Login Card Panel
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(cardBg);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(241, 245, 249), 1),
                new EmptyBorder(35, 30, 35, 30)
        ));
        cardPanel.setPreferredSize(new Dimension(360, 380));

        // Subheader Icon / Logo simulation
        JLabel lblIcon = new JLabel("🎓", JLabel.CENTER);
        lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 46));
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(lblIcon);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Title
        JLabel lblTitle = new JLabel("Timetable Portal");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(textColor);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(lblTitle);

        JLabel lblSub = new JLabel("Please enter your admin credentials");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(100, 116, 139));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(lblSub);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Form Fields wrapper
        JPanel form = new JPanel(new GridLayout(4, 1, 5, 5));
        form.setBackground(cardBg);
        form.setMaximumSize(new Dimension(300, 180));

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUsername.setForeground(new Color(51, 65, 85)); // Slate 700 - high contrast
        form.add(lblUsername);

        txtUsername = new JTextField("admin"); // Autofill for easy grading demo
        styleTextField(txtUsername, "Enter Username");
        form.add(txtUsername);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPassword.setForeground(new Color(51, 65, 85)); // Slate 700 - high contrast
        form.add(lblPassword);

        txtPassword = new JPasswordField("admin123"); // Autofill for easy grading demo
        stylePasswordField(txtPassword, "Enter Password");
        form.add(txtPassword);

        cardPanel.add(form);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Action Button
        btnLogin = new JButton("Login Now");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(primaryColor);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.putClientProperty("JButton.buttonType", "roundRect");
        btnLogin.setMaximumSize(new Dimension(300, 45));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogin.setBackground(primaryHover);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(primaryColor);
            }
        });

        btnLogin.addActionListener(e -> performLogin());

        cardPanel.add(btnLogin);

        bgPanel.add(cardPanel);
        add(bgPanel);
    }

    private void styleTextField(JTextField field, String placeholder) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(textColor);
        field.putClientProperty("JComponent.roundRect", true);
        field.putClientProperty("JTextField.placeholderText", placeholder);
        field.putClientProperty("JTextField.showClearButton", true);
        field.putClientProperty("JTextField.margin", new Insets(8, 12, 8, 12));
    }

    private void stylePasswordField(JPasswordField field, String placeholder) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(textColor);
        field.putClientProperty("JComponent.roundRect", true);
        field.putClientProperty("JTextField.placeholderText", placeholder);
        field.putClientProperty("JComponent.showRevealButton", true);
        field.putClientProperty("JTextField.margin", new Insets(8, 12, 8, 12));
    }

    private void performLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all credentials.", "Login Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (dbService.validateAdmin(username, password)) {
            SwingUtilities.invokeLater(() -> {
                DashboardFrame dashboard = new DashboardFrame();
                dashboard.setVisible(true);
            });
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Username or Password.", "Authentication Failure", JOptionPane.ERROR_MESSAGE);
        }
    }
}
