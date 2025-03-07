import com.github.lgooddatepicker.components.DateTimePicker;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class EditEventScreen extends JFrame {
    private JPanel contentPane;
    private JTextField nameField;
    private JLabel title;
    private JLabel nameLabel;
    private JButton setBeginDateButton;
    private JLabel beginDateLabel;
    private JPanel beginDateLabelContainer;
    private JPanel endDateLabelContainer;
    private JButton setEndDateButton;
    private JLabel driversLabel;
    private JList<Driver> driversList;
    private JButton addNewDriverButton;
    private JLabel ridersLabel;
    private JList<Rider> ridersList;
    private JButton addNewRiderButton;
    private JButton cancelButton;
    private JButton saveButton;
    private JPanel controlButtonsContainer;
    private JLabel basicStatusLabel;
    private JLabel statusLabel;
    private JLabel sendLabel;
    private JTextField sendField;
    private JButton sendButton;
    private DateTimePicker beginDatePicker;
    private DateTimePicker endDatePicker;
    private JButton deleteEventButton;

    private CarPoolCalendar calendar;
    private User user;
    private DefaultListModel<Rider> riderModel = new DefaultListModel<>();
    private DefaultListModel<Driver> driverModel = new DefaultListModel<>();
    private Runnable onCompletionCallback;
    private Event eventToBeEdited = null;


    // Creates an event when no parameters are passed
    public EditEventScreen(User user, CarPoolCalendar cal, Runnable onCompletionCallback) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(contentPane);

        this.calendar = cal;
        this.user = user;
        this.onCompletionCallback = onCompletionCallback;


        initButtons(false);
        initInfo();
        updateStatus();

        pack();
        setVisible(true);
    }

    public EditEventScreen(User user, CarPoolCalendar cal, Event event, Runnable onCompletionCallback) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(contentPane);

        this.calendar = cal;
        this.user = user;
        this.onCompletionCallback = onCompletionCallback;
        this.eventToBeEdited = event;

        initInfoEditMode(event);
        initButtons(true);
        updateStatus();

        pack();
        setVisible(true);
    }

    private void initInfoEditMode(Event event) {
        title.setText("Edit Event");
        nameField.setText(event.getName());
        // TODO: beginDateField.setText(event.getBeginDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy:HH:mm")));
        beginDatePicker.setDateTimePermissive(event.getBeginDate());
        endDatePicker.setDateTimePermissive(event.getEndDate());

        // When editing event select the already selected riders and drivers
        event.getRiders().forEach(riderModel::addElement);
        event.getDrivers().forEach(driverModel::addElement);
        for (Rider rider : user.getRiders()) {
            if (!event.getRiders().contains(rider)) {
                riderModel.addElement(rider);

            }
        }
        for (Driver driver : user.getDrivers()) {
            if (!event.getDrivers().contains(driver)) {
                driverModel.addElement(driver);
            }
        }

        // I'm not entirely sure what a model is
        // But I do know that it contains a list of drivers and riders
        // this just sets both JLists to display the correct model
        driversList.setModel(driverModel);
        ridersList.setModel(riderModel);


        // Enable Toggle Selection Model
        driversList.setSelectionModel(new ToggleSelectionModel());
        ridersList.setSelectionModel(new ToggleSelectionModel());

        // Select all previously selected items
        for (Rider rider : event.getRiders()) {
            ridersList.setSelectedIndex(riderModel.indexOf(rider));
        }
        for (Driver driver : event.getDrivers()) {
            driversList.setSelectedIndex(driverModel.indexOf(driver));
        }

        updateStatus();
    }

    private void initInfo() {
        title.setText("Create Event");

        user.getRiders().forEach(riderModel::addElement);
        user.getDrivers().forEach(driverModel::addElement);

        // I'm not entirely sure what a model is
        // But I do know that it contains a list of drivers and riders
        // this just sets both JLists to display the correct model
        driversList.setModel(driverModel);
        ridersList.setModel(riderModel);

        // Enable Toggle Selection Model
        driversList.setSelectionModel(new ToggleSelectionModel());
        ridersList.setSelectionModel(new ToggleSelectionModel());

        updateStatus();
    }

    private void initButtons(boolean editMode) {
        // Upon pressing the delete button, delete the event and close the editor
        // If the user is creating a new event, just close the menu
        deleteEventButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editMode) {
                    calendar.getEvents().remove(eventToBeEdited);
                    DBM.saveCalendar(calendar);

                    System.out.println("Event deleted");
                    dispose();

                    // Reload home screen
                    if (onCompletionCallback != null) {
                        onCompletionCallback.run();
                    }
                } else {
                    dispose();
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setBeginDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                beginDatePicker.setDateTimePermissive(LocalDateTime.now());

            }
        });

        setEndDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endDatePicker.setDateTimePermissive(LocalDateTime.now());
            }
        });

        addNewRiderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog(null, "Please enter the name of the new rider:",
                        "Name Input", JOptionPane.QUESTION_MESSAGE);

                // Warn user if input is blank
                // TODO: Fix this
                if (name != null && !name.isBlank()) {
                    JOptionPane.showMessageDialog(null, "Rider: " + name + ", added to event",
                            "Name Confirmation", JOptionPane.INFORMATION_MESSAGE);
                    Rider rider = new Rider(name);
                    riderModel.addElement(rider);
                } else {
                    JOptionPane.showMessageDialog(null, "No name was entered.",
                            "Input Cancelled", JOptionPane.WARNING_MESSAGE);
                }
                updateStatus();
                pack();
            }
        });

        addNewDriverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditDriverScreen driverMenu = new EditDriverScreen(null);
                Driver driver = driverMenu.showDialog();
                if (driver != null) {
                    driverModel.addElement(driver);
                }
                updateStatus();
                pack();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create event from user input
                if (!nameField.getText().isBlank() && beginDatePicker.getDateTimePermissive() != null && endDatePicker.getDateTimePermissive() != null) {
                    // End date must be after begin data
                    if (beginDatePicker.getDateTimePermissive().isBefore(endDatePicker.getDateTimePermissive())) {
                        // Limit the length of the event name
                        if (nameField.getText().length() <= 50) {
                            Event event = new Event();
                            if (editMode) {
                                event = new Event(eventToBeEdited.getEventId());
                            }
                            event.setName(nameField.getText());
                            try {
                                event.setBeginDate(beginDatePicker.getDateTimePermissive());
                                event.setEndDate(endDatePicker.getDateTimePermissive());

                            } catch (DateTimeParseException parseException) {
                                System.out.println("Failed to parse event date");
                                JOptionPane.showMessageDialog(null, "Incorrect date format. Please enter correct format.",
                                        "Incorrect date format", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            if (editMode) {
                                event.setParentCalendarId(eventToBeEdited.getParentCalendarId());
                            } else {
                                event.setParentCalendarId(calendar.getCalendarId());
                            }
                            event.getRiders().addAll(ridersList.getSelectedValuesList());
                            event.getDrivers().addAll(driversList.getSelectedValuesList());

                            // Add event to calendar if not editing
                            if (editMode) {
                                calendar.editEvent(event);
                            } else {
                                calendar.addEvent(event);
                            }

                            // TODO: Explain this code and figure out what it does
                            // Checks if the event was created by yourself
                            // otherwise it's a shared event
                            // It's shared when the parent calendars don't match
                            if (event.getParentCalendarId().equals(calendar.getCalendarId())) {
                                // Saves you personal calendar, doesn't matter if it's a new or edited event
                                DBM.saveCalendar(calendar);
                            } else {
                                // If it's a shared event, then it means you must be editing it
                                // and since you aren't the owner of the event, the original calendar must be edited
                                // which the DBM editEvent function edits the event based on it's parentCalenderId
                                DBM.editEvent(event);
                            }
                            dispose();
                            System.out.println("Event created successfully");
                            if (onCompletionCallback != null) {
                                onCompletionCallback.run();
                            }
                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "Event name must be less than 50 characters.",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        driversList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateStatus();
                pack();
            }
        });

        ridersList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateStatus();
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Only let the user send the event if it's been saved
                // This means the user must be editing the event, rather than creating a new one
                if (eventToBeEdited != null) {
                    if (DBM.userExists(sendField.getText())) {
                        System.out.println("Account found");

                        User tempUser = DBM.loadUser(sendField.getText());
                        CarPoolCalendar tempCal = DBM.loadCalendar(tempUser.getCalendarId());
                        tempCal.getSharedEventIds().add(eventToBeEdited.getEventId());

                        DBM.saveCalendar(tempCal);
                        System.out.println("The username is valid! Event successfully sent");
                        JOptionPane.showMessageDialog(null,
                                "The username is valid! Event sent to " + sendField.getText() + ".",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Username doesn't exist. Please try again.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Please save the event, then send it",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

            }
        });


    }

    private void updateStatus() {
        int totalRiders = ridersList.getSelectedValuesList().size();

        int totalCapacity = 0;
        for (int i = 0; i < driversList.getSelectedValuesList().size(); i++) {
            totalCapacity += driversList.getSelectedValuesList().get(i).getCarCapacity();
        }


        int difference = totalCapacity - totalRiders;

        if (difference >= 0) {
            basicStatusLabel.setText("<html><span style='color:green;font-weight:bold;'> Enough drivers! </span></html>");
            statusLabel.setText("<html><span style='color:green;font-weight:bold;'>" +
                    "Total Capacity: " + totalCapacity +
                    ", Total Riders: " + totalRiders +
                    ", Difference: " + difference + "</span></html>");
        } else {
            basicStatusLabel.setText("<html><span style='color:red;font-weight:bold;'> Not enough drivers! </span></html>");
            statusLabel.setText("<html><span style='color:red;font-weight:bold;'>" +
                    "Total Capacity: " + totalCapacity +
                    ", Total Riders: " + totalRiders +
                    ", Short by: " + Math.abs(difference) + "</span></html>");
        }
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
        contentPane.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(8, 3, new Insets(10, 10, 10, 10), -1, -1));
        title = new JLabel();
        Font titleFont = this.$$$getFont$$$(null, Font.BOLD, 20, title.getFont());
        if (titleFont != null) title.setFont(titleFont);
        title.setText("Edit/Create Event");
        contentPane.add(title, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nameLabel = new JLabel();
        nameLabel.setText("Name:");
        contentPane.add(nameLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nameField = new JTextField();
        contentPane.add(nameField, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        setBeginDateButton = new JButton();
        setBeginDateButton.setText("Set Date Now");
        contentPane.add(setBeginDateButton, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        beginDateLabelContainer = new JPanel();
        beginDateLabelContainer.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(beginDateLabelContainer, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        beginDateLabel = new JLabel();
        beginDateLabel.setText("Start Date:");
        beginDateLabelContainer.add(beginDateLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        endDateLabelContainer = new JPanel();
        endDateLabelContainer.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(endDateLabelContainer, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("End Date:");
        endDateLabelContainer.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        driversLabel = new JLabel();
        driversLabel.setText("Drivers:");
        contentPane.add(driversLabel, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        driversList = new JList();
        contentPane.add(driversList, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        addNewDriverButton = new JButton();
        addNewDriverButton.setText("Add New Driver");
        contentPane.add(addNewDriverButton, new com.intellij.uiDesigner.core.GridConstraints(4, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ridersLabel = new JLabel();
        ridersLabel.setText("Riders:");
        contentPane.add(ridersLabel, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ridersList = new JList();
        contentPane.add(ridersList, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        addNewRiderButton = new JButton();
        addNewRiderButton.setText("Add New Rider");
        contentPane.add(addNewRiderButton, new com.intellij.uiDesigner.core.GridConstraints(5, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        basicStatusLabel = new JLabel();
        basicStatusLabel.setText("Label");
        contentPane.add(basicStatusLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        statusLabel = new JLabel();
        statusLabel.setText("Label");
        contentPane.add(statusLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        beginDatePicker = new DateTimePicker();
        contentPane.add(beginDatePicker, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        endDatePicker = new DateTimePicker();
        contentPane.add(endDatePicker, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        sendLabel = new JLabel();
        sendLabel.setText("Send to Username:");
        contentPane.add(sendLabel, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sendField = new JTextField();
        contentPane.add(sendField, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        controlButtonsContainer = new JPanel();
        controlButtonsContainer.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), 0, -1));
        contentPane.add(controlButtonsContainer, new com.intellij.uiDesigner.core.GridConstraints(7, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        cancelButton = new JButton();
        cancelButton.setText("Cancel");
        controlButtonsContainer.add(cancelButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        saveButton = new JButton();
        saveButton.setText("Save");
        controlButtonsContainer.add(saveButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sendButton = new JButton();
        sendButton.setText("Send");
        contentPane.add(sendButton, new com.intellij.uiDesigner.core.GridConstraints(6, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        deleteEventButton = new JButton();
        deleteEventButton.setText("Delete Event");
        contentPane.add(deleteEventButton, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        setEndDateButton = new JButton();
        setEndDateButton.setText("Set Date Now");
        contentPane.add(setEndDateButton, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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