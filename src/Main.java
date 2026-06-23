import ui.LoginFrame;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Enable high-quality anti-aliasing and text rendering
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        // Set FlatLaf Light Look & Feel
        try {
            FlatLightLaf.setup();
        } catch (Exception e) {
            System.err.println("Failed to initialize FlatLaf: " + e.getMessage());
        }

        // Run Login Frame
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
