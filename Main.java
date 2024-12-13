import javax.swing.*;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {

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
