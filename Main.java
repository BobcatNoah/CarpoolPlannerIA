import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        //User user = new User("dombrowskinoah", "password");
        //user.getRiders().add(new Rider("Clara Dombrowski"));
        //user.getDrivers().add(new Driver("Noah Dombrowski", 6, true, true, true));

        //CarPoolCalendar cal = new CarPoolCalendar();
        //user.setCalendarId(cal.getCalendarId());

        //DBM.saveCalendar(cal);
        //DBM.saveUser(user);
        User user = DBM.loadUser("dombrowskinoah");
        CarPoolCalendar cal = DBM.loadCalendar(user.getCalendarId());

        cal.getEvents().add(new Event("Soccer Practice", LocalDateTime.now(), LocalDateTime.now().plusHours(5), cal.getCalendarId()));

        DBM.saveCalendar(cal);

        System.out.println(user);
        System.out.println(cal);

        System.out.println("Just to be safe: \n" + DBM.loadCalendar(user.getCalendarId()));
    }
}
