import javax.swing.*;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginScreen login = new LoginScreen();
                login.setVisible(true);
            }
        });


    }
}
