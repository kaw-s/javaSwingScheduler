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
import view.NUPlannerTextView;

/**
 * Tests methods in NUPlannerTextView class.
 */
public class NUPlannerTextViewTests {
  private User user1;
  private User user2;
  private User user3;
  private String textViewUser1;
  private String textViewUser2;
  private String textViewUser3;
  private NUPlanner planner;

  @Before
  public void setUp() {
    this.planner = new NUPlanner(false);
    this.buildUser1();
    this.buildUser2();
    this.buildUser3();

    this.buildTextViewUser1();
    this.buildTextViewUser2();
    this.buildTextViewUser3();
  }

  private void buildUser1() {
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

    this.user1 = new User("Alex");
    this.planner.addUser(user1);

    this.planner.addEvent("Alex", event1);
    this.planner.addEvent("Alex", event2);
    this.planner.addEvent("Alex", event3);
  }

  private void buildUser2() {
    Event event1 = new Event("\"CS3500 Morning Lecture\"",
            "\"Churchill Hall 101\"",
            false,
            new Date(Day.Tuesday, "0950"),
            new Date(Day.Tuesday, "1130"),
            new ArrayList<>(List.of("Bob", "\"Student Anon\"", "\"Chat\"")));

    Event event2 = new Event("\"CS3500 Afternoon Lecture\"",
            "\"Churchill Hall 101\"",
            false,
            new Date(Day.Tuesday, "1335"),
            new Date(Day.Tuesday, "1515"),
            new ArrayList<>(List.of("Bob", "\"Chat\"")));

    Event event3 = new Event("Sleep",
            "Home",
            true,
            new Date(Day.Friday, "1800"),
            new Date(Day.Sunday, "1200"),
            new ArrayList<>(List.of("Bob")));

    this.user2 = new User("Bob");

    this.planner.addUser(user2);

    this.planner.addEvent("Bob", event1);
    this.planner.addEvent("Bob", event2);
    this.planner.addEvent("Bob", event3);
  }

  private void buildUser3() {
    Event event1 = new Event("\"CS3500 Morning Lecture\"",
            "\"Churchill Hall 101\"",
            false,
            new Date(Day.Tuesday, "0950"),
            new Date(Day.Tuesday, "1130"),
            new ArrayList<>(List.of("Sheena", "\"Student Anon\"", "\"Chat\"")));

    Event event2 = new Event("\"CS3500 Afternoon Lecture\"",
            "\"Churchill Hall 101\"",
            false,
            new Date(Day.Tuesday, "1335"),
            new Date(Day.Tuesday, "1515"),
            new ArrayList<>(List.of("Sheena", "\"Chat\"")));

    Event event3 = new Event("Sleep",
            "Home",
            true,
            new Date(Day.Friday, "1800"),
            new Date(Day.Sunday, "1200"),
            new ArrayList<>(List.of("Sheena")));

    this.user3 = new User("Sheena");
    this.planner.addUser(user3);
    this.planner.addEvent("Sheena", event3);
    this.planner.addEvent("Sheena", event2);
    this.planner.addEvent("Sheena", event1);
  }

  private void buildTextViewUser1() {
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
            + "        \"Chat\"\n\n"
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
            + "Saturday:\n";
  }

  private void buildTextViewUser2() {
    this.textViewUser2 = "User: Bob\n"
            + "Sunday:\n"
            + "Monday:\n"
            + "Tuesday:\n"
            + "        name: \"CS3500 Morning Lecture\"\n"
            + "        time: Tuesday: 0950 -> Tuesday: 1130\n"
            + "        location: \"Churchill Hall 101\"\n"
            + "        online: false\n"
            + "        invitees: Bob\n"
            + "        \"Student Anon\"\n"
            + "        \"Chat\"\n"
            + "\n"
            + "        name: \"CS3500 Afternoon Lecture\"\n"
            + "        time: Tuesday: 1335 -> Tuesday: 1515\n"
            + "        location: \"Churchill Hall 101\"\n"
            + "        online: false\n"
            + "        invitees: Bob\n"
            + "        \"Chat\"\n"
            + "Wednesday:\n"
            + "Thursday:\n"
            + "Friday:\n"
            + "        name: Sleep\n"
            + "        time: Friday: 1800 -> Sunday: 1200\n"
            + "        location: Home\n"
            + "        online: true\n"
            + "        invitees: Bob\n"
            + "Saturday:\n";
  }

  private void buildTextViewUser3() {
    this.textViewUser3 = "User: Sheena\n"
            + "Sunday:\n"
            + "Monday:\n"
            + "Tuesday:\n"
            + "        name: \"CS3500 Morning Lecture\"\n"
            + "        time: Tuesday: 0950 -> Tuesday: 1130\n"
            + "        location: \"Churchill Hall 101\"\n"
            + "        online: false\n"
            + "        invitees: Sheena\n"
            + "        \"Student Anon\"\n"
            + "        \"Chat\"\n"
            + "\n"
            + "        name: \"CS3500 Afternoon Lecture\"\n"
            + "        time: Tuesday: 1335 -> Tuesday: 1515\n"
            + "        location: \"Churchill Hall 101\"\n"
            + "        online: false\n"
            + "        invitees: Sheena\n"
            + "        \"Chat\"\n"
            + "Wednesday:\n"
            + "Thursday:\n"
            + "Friday:\n"
            + "        name: Sleep\n"
            + "        time: Friday: 1800 -> Sunday: 1200\n"
            + "        location: Home\n"
            + "        online: true\n"
            + "        invitees: Sheena\n"
            + "Saturday:";
  }

  @Test
  public void testToString() {
    NUPlanner planner = new NUPlanner(false);
    planner.addUser(this.user1);
    planner.addUser(this.user2);
    planner.addUser(this.user3);
    NUPlannerTextView plannerTextView = new NUPlannerTextView(planner);
    Assert.assertEquals(this.textViewUser1 + "\n"
            + this.textViewUser2 + "\n" + this.textViewUser3, plannerTextView.toString());
  }

  @Test
  public void testBuildTextViewWithNullPlanner() {
    Assert.assertThrows(IllegalArgumentException.class, () -> new NUPlannerTextView(null));
  }
}
