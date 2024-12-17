import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

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
    private JButton addSharedEvent;

    private User user;
    private CarPoolCalendar calendar;

    public HomeScreen(User user) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(contentPane);
        pack();
        //setSize(1200, 600);

        this.user = user;
        this.calendar = DBM.loadCalendar(user.getCalendarId());

        initCalendar(LocalDate.now().getYear(), LocalDate.now().getMonthValue());
        initButtons();

        pack();
        setVisible(true);
    }

    private void initButtons() {
        addEventButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initCalendar(LocalDate.now().getYear(), LocalDate.now().getMonthValue());
                addEvent();
            }
        });

        addSharedEvent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog(null,
                        "Please enter the Event ID (UUID):",
                        "Enter Event ID",
                        JOptionPane.QUESTION_MESSAGE);

                try {
                    UUID id = UUID.fromString(input);
                    JOptionPane.showMessageDialog(null,
                            "The Event ID is valid! Event added to Calendar.",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (IllegalArgumentException error) {
                    System.out.println("Incorrect Event ID format");
                    JOptionPane.showMessageDialog(null,
                            "Invalid UUID format. Please try again.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                UUID id = UUID.fromString(input);
                Event event = DBM.getEventById(id);
                if (event != null) {
                    // Add the event to user calendar if it isn't already saved
                    if (!calendar.getSharedEventIds().contains(id)) {
                        calendar.getSharedEventIds().add(id);
                        DBM.saveCalendar(calendar);
                    }
                }
                initCalendar(LocalDate.now().getYear(), LocalDate.now().getMonthValue());
            }
        });
    }

    private void initCalendar(int year, int month) {
        // Simply clearing components so that there aren't duplicates
        // when calendar is reloaded
        calendarContainer.removeAll();
        weekPanel.removeAll();

        // Reload user and calendar data
        user = DBM.loadUser(user.getUsername());
        calendar = DBM.loadCalendar(calendar.getCalendarId());

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
            // dayPanel contains the date and list of events for that day
            JPanel dayPanel = new JPanel();
            dayPanel.setLayout(new BorderLayout());

            // Fill day with events/values
            JLabel dayNum = new JLabel("" + day);
            dayNum.setFont(new Font("Arial", Font.BOLD, 14));
            dayNum.setBorder(new EmptyBorder(10, 10, 10, 10));
            dayPanel.add(dayNum, BorderLayout.PAGE_START);
            // eventsOfDay is necessary because you can only have one item in the center of borderlayout
            JPanel eventsOfDayPanel = new JPanel(new GridLayout(0, 1));
            ArrayList<Event> eventsOfDay = calendar.getEventsByDate(LocalDate.of(year, month, day));
            for (Event event : eventsOfDay) {
                JButton eventButton = new JButton(event.getName());
                eventButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addEvent(event);
                    }
                });
                eventsOfDayPanel.add(eventButton);
            }
            dayPanel.add(eventsOfDayPanel, BorderLayout.CENTER);


            dayPanel.setBorder(BorderFactory.createLineBorder(Color.lightGray));
            calendarContainer.add(dayPanel);
        }

        // Set date label
        dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));

        pack();
        revalidate();
        repaint();
    }

    private void addEvent(Event event) {
        // Similar to the function below
        // But, I'm passing in the event to be edited
        // This shows a slightly different menu than creating an event
        System.out.println("Event Menu");
        EditEventScreen createEventScreen = new EditEventScreen(user, calendar, event, () -> {
            initCalendar(LocalDate.now().getYear(), LocalDate.now().getMonthValue());
        });
    }

    private void addEvent() {
        // Open event creator screen when user click the add event button
        System.out.println("Event Menu");
        EditEventScreen createEventScreen = new EditEventScreen(user, calendar, () -> {
            initCalendar(LocalDate.now().getYear(), LocalDate.now().getMonthValue());
        });
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
        addSharedEvent = new JButton();
        addSharedEvent.setText("Add Shared Event");
        jButtonContainer.add(addSharedEvent, BorderLayout.NORTH);
        dateLabel = new JLabel();
        Font dateLabelFont = this.$$$getFont$$$(null, -1, 16, dateLabel.getFont());
        if (dateLabelFont != null) dateLabel.setFont(dateLabelFont);
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
