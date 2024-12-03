import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
            String json = "";
            while (scanner.hasNextLine()) {json += scanner.nextLine();}
            scanner.close();

            try {
                return CarPoolCalendar.toJavaCalendar((JSONObject) parser.parse(json));
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

    public static Event getEventById(UUID id) {
        String[] calendars = new File(path + "calendars").list();
        return null;
    }
}
