import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class HomeScreen extends JFrame {
    private JPanel contentPane;
    private JPanel jMenuBar;
    private JComboBox groupOptions;
    private JPanel jButtonContainer;
    private JButton addGroupButton;
    private JButton settingsButton;
    private JLabel dateLabel;
    private JButton addEventButton;
    private JPanel calendarContentPane;
    private JPanel calendarContainer;
    private JPanel weekPanel;

    private User user;
    private CarPoolCalendar calendar;

    public HomeScreen(User user) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(contentPane);
        pack();
        setSize(1200, 600);

        this.user = user;
        this.calendar = DBM.loadCalendar(user.getCalendarId());

        initCalendar(LocalDate.now().getYear(), LocalDate.now().getMonthValue());
        initButtons();

        setVisible(true);
    }

    private void initButtons() {
        addEventButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Event Menu");
                EditEventScreen createEventScreen = new EditEventScreen(user, calendar);
            }
        });
    }

    private void initCalendar(int year, int month) {
        calendarContainer.setLayout(new GridLayout(0, 7));
        weekPanel.setLayout(new GridLayout(0, 7));

        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : daysOfWeek) {
            JLabel dayLabel = new JLabel(day, SwingConstants.CENTER);
            dayLabel.setFont(new Font("Arial", Font.BOLD, 14));
            weekPanel.add(dayLabel);
        }

        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        int firstDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue() % 7; //
        int daysInMonth = firstDayOfMonth.lengthOfMonth();

        // Fill in blank space before start of first week
        for (int i = 1; i <= firstDayOfWeek; i++) {
            calendarContainer.add(new JLabel(""));
        }

        for (int day = 1; day <= daysInMonth; day++) {
            JPanel dayPanel = new JPanel();
            dayPanel.setLayout(new BorderLayout());

            // Fill day with events/values
            JLabel dayNum = new JLabel("" + day);
            dayNum.setFont(new Font("Arial", Font.BOLD, 14));
            dayNum.setBorder(new EmptyBorder(10, 10, 10, 10));
            dayPanel.add(dayNum, BorderLayout.PAGE_START);
            dayPanel.add(dayNum, BorderLayout.CENTER);

            dayPanel.setBorder(BorderFactory.createLineBorder(Color.lightGray));
            calendarContainer.add(dayPanel);
        }

        calendarContainer.revalidate();
        calendarContainer.repaint();
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
        contentPane.setLayout(new BorderLayout(0, 0));
        contentPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        jMenuBar = new JPanel();
        jMenuBar.setLayout(new BorderLayout(0, 0));
        contentPane.add(jMenuBar, BorderLayout.NORTH);
        groupOptions = new JComboBox();
        jMenuBar.add(groupOptions, BorderLayout.WEST);
        jButtonContainer = new JPanel();
        jButtonContainer.setLayout(new BorderLayout(0, 0));
        jMenuBar.add(jButtonContainer, BorderLayout.EAST);
        addGroupButton = new JButton();
        addGroupButton.setText("Add Group");
        jButtonContainer.add(addGroupButton, BorderLayout.WEST);
        settingsButton = new JButton();
        settingsButton.setEnabled(true);
        settingsButton.setText("Settings");
        jButtonContainer.add(settingsButton, BorderLayout.EAST);
        addEventButton = new JButton();
        addEventButton.setText("Add Event");
        jButtonContainer.add(addEventButton, BorderLayout.CENTER);
        dateLabel = new JLabel();
        dateLabel.setHorizontalAlignment(0);
        dateLabel.setText("Label");
        jMenuBar.add(dateLabel, BorderLayout.CENTER);
        calendarContentPane = new JPanel();
        calendarContentPane.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(calendarContentPane, BorderLayout.CENTER);
        weekPanel = new JPanel();
        weekPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        calendarContentPane.add(weekPanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        calendarContainer = new JPanel();
        calendarContainer.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        calendarContentPane.add(calendarContainer, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
