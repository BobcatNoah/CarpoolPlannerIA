import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

public class LoginScreen extends JFrame {
    private JPanel contentPane;
    private JLabel title;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton signIn;
    private JLabel username;
    private JLabel password;
    private JPanel JContainer;

    public LoginScreen() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(contentPane);
        pack();

        signIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usernameInput = usernameField.getText();
                String passwordInput = new String(passwordField.getPassword());

                if (!usernameInput.isEmpty() && DBM.userExists(usernameInput)) {
                    System.out.println("User: " + usernameInput + " exists");
                    User user = DBM.loadUser(usernameInput);

                    if (user.getPassword().equals(passwordInput)) {
                        System.out.println("Sign in Successful");

                        // Delete the sign in menu screen
                        dispose();

                        HomeScreen homeScreen = new HomeScreen(user);
                    } else {
                        System.out.println("Incorrect password");
                        JOptionPane.showMessageDialog(null,
                                "Incorrect password. Please try again",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                                passwordField.setText("");
                    }
                } else {
                    System.out.println("Account doesn't exist");
                    int option = JOptionPane.showOptionDialog(null,
                            "This account does not exist. Would you like to create an account?",
                            "Account Not Found",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            new String[]{"Create Account", "Try Again"},
                            "Create Account");

                    if (option == JOptionPane.YES_OPTION) {
                        if (passwordInput != null && !passwordInput.isEmpty()) {
                            CarPoolCalendar cal = new CarPoolCalendar();
                            User user = new User(usernameInput, passwordInput);
                            user.setCalendarId(cal.getCalendarId());
                            DBM.saveUser(user);
                            DBM.saveCalendar(cal);

                            System.out.println("Account Created Successfully");
                            JOptionPane.showMessageDialog(null,
                                    "Account created successfully. Please sign in",
                                    "Success",
                                    JOptionPane.INFORMATION_MESSAGE);

                        } else {
                            System.out.println("Failed to create account: Password field blank");
                            JOptionPane.showMessageDialog(null,
                                    "Failed to create account. Please input a password",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        // If user selects try again. Erase input
                        passwordField.setText("");
                        usernameField.setText("");
                    }
                }

            }
        });

        setVisible(true);
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 2, new Insets(20, 20, 20, 20), -1, -1));
        title = new JLabel();
        Font titleFont = this.$$$getFont$$$(null, Font.BOLD, 36, title.getFont());
        if (titleFont != null) title.setFont(titleFont);
        title.setText("Carpool");
        contentPane.add(title, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        JContainer = new JPanel();
        JContainer.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 2, new Insets(0, 20, 20, 20), -1, -1));
        contentPane.add(JContainer, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        username = new JLabel();
        username.setText("Username:");
        JContainer.add(username, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        usernameField = new JTextField();
        JContainer.add(usernameField, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        password = new JLabel();
        password.setText("Password:");
        JContainer.add(password, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        passwordField = new JPasswordField();
        JContainer.add(passwordField, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        signIn = new JButton();
        signIn.setText("Sign In / Create Account");
        JContainer.add(signIn, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
