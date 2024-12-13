import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

public class DBM {
    private static final String path = "database" + File.separatorChar;

    public static User loadUser(String username) {
        try {
            Scanner scanner = new Scanner(new File(path + "users" + File.separatorChar + username));
            JSONParser parser = new JSONParser();
            String json = "";
            while(scanner.hasNextLine()) {json += scanner.nextLine();}
            scanner.close();

            try {
                JSONObject obj = (JSONObject) parser.parse(json);
                return User.toJavaUser(obj);
            } catch (ParseException e) {
                System.out.println("Error: Failed to parse user file");
                return null;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: User not found");
        }
        return null;
    }

    public static boolean userExists(String username) {
        return new File(path + "users" + File.separatorChar + username).exists();
    }

    public static boolean saveUser(User user) {
        File file = new File(path + "users" + File.separatorChar + user.getUsername());
        try {
            //file.createNewFile();
            FileWriter writer = new FileWriter(file);
            JSONObject obj = user.getJSONOBJ();

            writer.write(obj.toJSONString());
            writer.close();
        } catch (IOException e) {
            System.out.println("Failed to save user file");
            return false;
        }
        return true;
    }

    public static CarPoolCalendar loadCalendar(UUID calendarId) {
        File file = new File(path + "calendars" + File.separatorChar + calendarId);
        try {
            Scanner scanner = new Scanner(file);
            JSONParser parser = new JSONParser();
            StringBuilder json = new StringBuilder();
            while (scanner.hasNextLine()) {
                json.append(scanner.nextLine());}
            scanner.close();

            try {
                CarPoolCalendar cal = CarPoolCalendar.toJavaCalendar((JSONObject) parser.parse(json.toString()));
                // Add shared events to cal and update them if they are already there
                ArrayList<UUID> events = new ArrayList<>();
                cal.getEvents().forEach((e) -> events.add(e.getEventId()));
                for (UUID sharedEventId : cal.getSharedEventIds()) {
                    Event eventToBeSaved = getEventById(sharedEventId);
                    if (eventToBeSaved == null) {
                        cal.getEvents().remove(events.indexOf(sharedEventId));
                    } else {
                        if (events.contains(sharedEventId)) {
                            cal.getEvents().set(events.indexOf(sharedEventId), eventToBeSaved);
                        } else {
                            cal.getEvents().add(eventToBeSaved);
                        }
                    }

                }
                return cal;
            } catch (ParseException e) {
                System.out.println("Error: Failed to parse calendar file");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: Calendar file not found");
        }
        return null;
    }

    public static boolean saveCalendar(CarPoolCalendar cal) {
        File file = new File(path + "calendars" + File.separatorChar + cal.getCalendarId().toString());
        try {
           FileWriter writer = new FileWriter(file);
           JSONObject obj = cal.getJSONOBJ();

           writer.write(obj.toJSONString());
           writer.close();
        } catch (IOException e) {
            System.out.println("Failed to save calendar");
            return false;
        }
        return true;
    }

    // True if succeeded, false if failed
    public static boolean saveEvent(Event event) {
        CarPoolCalendar cal = DBM.loadCalendar(event.getParentCalendarId());
        if (cal != null) {
            cal.editEvent(event);
            DBM.saveCalendar(cal);
            return true;
        }
        return false;
    }

    public static Event getEventById(UUID id) {
        String[] calendars = new File(path + "calendars").list();
        for (String fileName : calendars) {
            try {
                Scanner scanner = new Scanner(new File(path + "calendars" + File.separatorChar + fileName));
                JSONParser parser = new JSONParser();
                StringBuilder json = new StringBuilder();
                while (scanner.hasNextLine()) {
                    json.append(scanner.nextLine());}
                scanner.close();

                try {
                    CarPoolCalendar cal = CarPoolCalendar.toJavaCalendar((JSONObject) parser.parse(json.toString()));
                    ArrayList<UUID> eventIds = new ArrayList<>();
                    cal.getEvents().forEach((e) -> eventIds.add(e.getEventId()));
                    if (eventIds.contains(id)) {
                        return cal.getEvents().get(eventIds.indexOf(id));
                    }
                } catch (ParseException e) {
                    System.out.println("Error: Failed to parse calendar file");
                }
            } catch (FileNotFoundException e) {
                System.out.println("Error: Calendar file not found");
            }

        }
        return null;
    }
}
