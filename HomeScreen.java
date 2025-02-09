import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

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
    private JPanel monthPicker;
    private JButton prevMonth;
    private JButton nextMonth;

    private User user;
    private CarPoolCalendar calendar;
    // An index in the list of groupOptions
    private int selectedGroup;
    private LocalDate dateView;

    public HomeScreen(User user) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(contentPane);
        pack();
        //setSize(1200, 600);

        this.user = user;
        this.calendar = DBM.loadCalendar(user.getCalendarId());
        this.dateView = LocalDate.now();
        // Sets the selected group to the default (personal calendar), which is the last index of the group list + 1
        this.selectedGroup = user.getGroups().size();

        initCalendar(dateView.getYear(), dateView.getMonthValue());
        initButtons();

        pack();
        setVisible(true);
    }

    private void initButtons() {
        addEventButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEvent();
                // Because the event editor screen is asynchronous, initCalendar cannot be called this way
                // because it would run before the user finishes editing
                //initCalendar(dateView.getYear(), dateView.getMonthValue());
            }
        });

        prevMonth.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dateView = dateView.minusMonths(1);
                initCalendar(dateView.getYear(), dateView.getMonthValue());
            }
        });

        nextMonth.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dateView = dateView.plusMonths(1);
                initCalendar(dateView.getYear(), dateView.getMonthValue());
            }
        });

        groupOptions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox) e.getSource();
                if (cb.getSelectedIndex() > -1) {
                    selectedGroup = cb.getSelectedIndex();
                    initCalendar(dateView.getYear(), dateView.getMonthValue());
                }
            }
        });

        addGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showGroupMenu();
            }
        });

        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initCalendar(dateView.getYear(), dateView.getMonthValue());
            }
        });

    }

    private void showGroupMenu() {
        String[] options = {"Join a Group", "Create a group"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Would you like to join or create a group?",
                "Group Options",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            joinGroup();
        } else if (choice == 1) {
            createGroup();
        }
    }

    private void joinGroup() {
        String groupName = JOptionPane.showInputDialog(this, "Enter the group name:");
        if (groupName == null || groupName.trim().isEmpty()) return;

        // Check if group already exists
        if (!DBM.groupExists(groupName)) {
            JOptionPane.showMessageDialog(this, "This group does not exists. Try creating it.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String password = JOptionPane.showInputDialog(this, "Enter the group password:");
        if (password == null || password.trim().isEmpty()) return;

        Group group = DBM.loadGroup(groupName);
        if (group.getPassword().equals(password)) {
            JOptionPane.showMessageDialog(this, "Correct password for group: " + groupName);
            if (user.getGroups().contains(groupName)) {
                JOptionPane.showMessageDialog(this, "You have already joined group: " + groupName);
            } else {
                user.getGroups().add(groupName);
                group.getUsers().add(user.getUsername());
                selectedGroup = 0;
                DBM.saveGroup(group);
                if (DBM.saveUser(user)) {
                    JOptionPane.showMessageDialog(this, "Successfully joined group: " + groupName);
                    initCalendar(dateView.getYear(), dateView.getMonthValue());
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to join group. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void createGroup() {
        String groupName = JOptionPane.showInputDialog(this, "Enter a name for the new group:");
        if (groupName == null || groupName.trim().isEmpty()) return;

        if (DBM.groupExists(groupName)) {
            JOptionPane.showMessageDialog(this, "A group with this name already exists. Please choose a different name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String password = JOptionPane.showInputDialog(this, "Set a password for the group:");
        if (password == null || password.trim().isEmpty()) return;

        // Create group and add a new calendar to it
        Group group = new Group();
        CarPoolCalendar cal = new CarPoolCalendar(groupName);
        DBM.saveCalendar(cal);

        group.setName(groupName);
        group.setPassword(password);
        group.setCalendarId(cal.getCalendarId());
        group.getUsers().add(user.getUsername());
        if (DBM.saveGroup(group)) {
            JOptionPane.showMessageDialog(this, "Successfully create the group: " + groupName);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create the group. Try again.");
        }

        // Add the group to the user
        user.getGroups().add(groupName);
        DBM.saveUser(user);
        selectedGroup = 0;

        // reload homescreen
        initCalendar(dateView.getYear(), dateView.getMonthValue());
    }

    private void initCalendar(int year, int month) {
        // Simply clearing components so that there aren't duplicates
        // when calendar is reloaded
        calendarContainer.removeAll();
        weekPanel.removeAll();

        // Reload user and calendar data
        user = DBM.loadUser(user.getUsername());
        // If the default personal calendar, which is the last index of the group list + 1, is selected, load it
        if (selectedGroup == user.getGroups().size()) {
            calendar = DBM.loadCalendar(user.getCalendarId());
        } else {
            // Load calender from the selected group
            calendar = DBM.loadCalendar(DBM.loadGroup(user.getGroups().get(selectedGroup)).getCalendarId());

        }

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
        dateLabel.setText(dateView.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));

        // Update group options
        ArrayList<String> temp = new ArrayList<>(user.getGroups());
        temp.add("My Calendar");

        // TODO: This is a patch job fix for erratic behavior when updating the groupOptions. I simply remove the listener and add it back after the options have been updated
        // Apparently this a permanent fix
        ActionListener[] listeners = groupOptions.getActionListeners();
        for (ActionListener listener : listeners) {
            groupOptions.removeActionListener(listener);
        }

        // Clear existing items to prevent duplicates
        groupOptions.removeAllItems();
        for (String group : temp) {
            groupOptions.addItem(group);
        }
        groupOptions.setSelectedIndex(selectedGroup);


        for (ActionListener listener : listeners) {
            groupOptions.addActionListener(listener);
        }

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
            initCalendar(dateView.getYear(), dateView.getMonthValue());
        });
    }

    private void addEvent() {
        // Open event creator screen when user click the add event button
        System.out.println("Event Menu");
        EditEventScreen createEventScreen = new EditEventScreen(user, calendar, () -> {
            initCalendar(dateView.getYear(), dateView.getMonthValue());
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
        monthPicker = new JPanel();
        monthPicker.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        jMenuBar.add(monthPicker, BorderLayout.CENTER);
        prevMonth = new JButton();
        prevMonth.setText("<");
        monthPicker.add(prevMonth);
        dateLabel = new JLabel();
        Font dateLabelFont = this.$$$getFont$$$(null, Font.BOLD, 14, dateLabel.getFont());
        if (dateLabelFont != null) dateLabel.setFont(dateLabelFont);
        dateLabel.setHorizontalAlignment(0);
        dateLabel.setHorizontalTextPosition(0);
        dateLabel.setText("Label");
        monthPicker.add(dateLabel);
        nextMonth = new JButton();
        nextMonth.setText(">");
        monthPicker.add(nextMonth);
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
