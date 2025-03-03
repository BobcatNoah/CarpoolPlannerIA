import javax.swing.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        // Create database if missing
        new File("database" + File.separatorChar + "calendars").mkdirs();
        new File("database" + File.separatorChar + "users").mkdirs();
        new File("database" + File.separatorChar + "groups").mkdirs();

        // https://www.jetbrains.com/help/idea/design-gui-using-swing.html
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginScreen login = new LoginScreen();
                login.setVisible(true);
            }
        });


    }
}
