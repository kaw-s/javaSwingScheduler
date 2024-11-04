import model.NUPlanner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import model.Date;
import model.Day;
import model.Event;
import model.User;

/**
 * Tests methods in User class (time conflict testing is done in TestHasTimeConflict class).
 */
public class UserTests {
  private String textViewUser1;

  @Before
  public void setUp() {
    this.textViewUser1 = "User: Alex\n"
            + "Sunday:\n"
            + "Monday:\n"
            + "Tuesday:\n"
            + "        name: \"CS3500 Morning Lecture\"\n"
            + "        time: Tuesday: 0950 -> Tuesday: 1130\n"
            + "        location: \"Churchill Hall 101\"\n"
            + "        online: false\n"
            + "        invitees: Alex\n"
            + "        \"Student Anon\"\n"
            + "        \"Chat\"\n"
            + "\n"
            + "        name: \"CS3500 Afternoon Lecture\"\n"
            + "        time: Tuesday: 1335 -> Tuesday: 1515\n"
            + "        location: \"Churchill Hall 101\"\n"
            + "        online: false\n"
            + "        invitees: Alex\n"
            + "        \"Chat\"\n"
            + "Wednesday:\n"
            + "Thursday:\n"
            + "Friday:\n"
            + "        name: Sleep\n"
            + "        time: Friday: 1800 -> Sunday: 1200\n"
            + "        location: Home\n"
            + "        online: true\n"
            + "        invitees: Alex\n"
            + "Saturday:";
  }

  @Test
  public void testAddEventSortsEventByStartingDate() {
    Event event1 = new Event("\"Event1\"",
            "\"Churchill Hall 101\"",
            false,
            new Date(Day.Wednesday, "0950"),
            new Date(Day.Thursday, "1130"),
            new ArrayList<>(List.of("Alex", "\"Student Anon\"", "\"Chat\"")));

    Event event2 = new Event("\"Event2\"",
            "\"Churchill Hall 101\"",
            false,
            new Date(Day.Monday, "0950"),
            new Date(Day.Tuesday, "1130"),
            new ArrayList<>(List.of("Alex", "\"Student Anon\"", "\"Chat\"")));

    Event event3 = new Event("\"Event3\"",
            "\"Churchill Hall 101\"",
            false,
            new Date(Day.Thursday, "1300"),
            new Date(Day.Wednesday, "1600"),
            new ArrayList<>(List.of("Alex", "\"Student Anon\"", "\"Chat\"")));

    User user = new User("Alex");

    NUPlanner planner = new NUPlanner(false);
    planner.addUser(user);
    planner.addEvent("Alex", event1);
    planner.addEvent("Alex", event2);
    planner.addEvent("Alex", event3);

    // addEvent() takes the event to be added and sorts it against the existing list of events
    Assert.assertEquals(1, user.getEvents().indexOf(event1));
    Assert.assertEquals(0, user.getEvents().indexOf(event2));
    Assert.assertEquals(1, user.getEvents().indexOf(event1));
    Assert.assertEquals(0, user.getEvents().indexOf(event2));
    Assert.assertEquals(2, user.getEvents().indexOf(event3));
  }

  @Test
  public void testToString() {
    Event event1 = new Event("\"CS3500 Morning Lecture\"",
            "\"Churchill Hall 101\"",
            false,
            new Date(Day.Tuesday, "0950"),
            new Date(Day.Tuesday, "1130"),
            new ArrayList<>(List.of("Alex", "\"Student Anon\"", "\"Chat\"")));

    Event event2 = new Event("\"CS3500 Afternoon Lecture\"",
            "\"Churchill Hall 101\"",
            false,
            new Date(Day.Tuesday, "1335"),
            new Date(Day.Tuesday, "1515"),
            new ArrayList<>(List.of("Alex", "\"Chat\"")));

    Event event3 = new Event("Sleep",
            "Home",
            true,
            new Date(Day.Friday, "1800"),
            new Date(Day.Sunday, "1200"),
            new ArrayList<>(List.of("Alex")));

    User user = new User("Alex");

    NUPlanner planner = new NUPlanner(false);
    planner.addUser(user);
    planner.addEvent("Alex", event1);
    planner.addEvent("Alex", event2);
    planner.addEvent("Alex", event3);

    Assert.assertEquals(this.textViewUser1, user.toString());
  }

  @Test
  public void testAddEventFailsDueToEventNameConflict() {
    User user = new User("Alex");
    Event e1 = new Event("Sleep1",
            "Dorm",
            false,
            new Date(Day.Tuesday, "1000"),
            new Date(Day.Wednesday, "1100"),
            new ArrayList<>(List.of("Alex")));
    Event e2 = new Event("Sleep1",
            "Dorm",
            false,
            new Date(Day.Tuesday, "1000"),
            new Date(Day.Wednesday, "1100"),
            new ArrayList<>(List.of("Alex")));
    user.addEvent(e1);
    Assert.assertThrows(IllegalStateException.class, () -> {
      user.addEvent(e2);
    });
  }

  @Test
  public void testAddEventFailsDueToTimeConflict() {
    User user = new User("Alex");
    Event e1 = new Event("Sleep1",
            "Dorm",
            false,
            new Date(Day.Tuesday, "1000"),
            new Date(Day.Wednesday, "1100"),
            new ArrayList<>(List.of("Alex")));
    Event e2 = new Event("Sleep2",
            "Dorm",
            false,
            new Date(Day.Tuesday, "1030"),
            new Date(Day.Wednesday, "1100"),
            new ArrayList<>(List.of("Alex")));
    user.addEvent(e1);
    Assert.assertThrows(IllegalStateException.class, () -> {
      user.addEvent(e2);
    });
  }

  @Test
  public void testRemoveEvent() {
    Event e1 = new Event("Sleep1",
            "Dorm",
            false,
            new Date(Day.Tuesday, "1000"),
            new Date(Day.Wednesday, "1100"),
            new ArrayList<>(List.of("Alex")));

    User user = new User("Alex");
    user.addEvent(e1);
    Assert.assertSame(user.getEvents().get(0), e1);
    user.removeEvent("Sleep");
    Assert.assertSame(user.getEvents().get(0), e1);
    user.removeEvent("Sleep1");
    Assert.assertEquals(0, user.getEvents().size());
  }

  @Test
  public void testConstructorFails() {
    Assert.assertThrows(IllegalArgumentException.class, () -> new User(""));
    Assert.assertThrows(IllegalArgumentException.class, () -> new User(null));
  }
}
