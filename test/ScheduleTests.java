import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import model.Date;
import model.Day;
import model.Event;
import model.Schedule;

/**
 * Tests methods in Schedule class.
 */
public class ScheduleTests {
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
  public void testRemoveEvent() {
    Schedule schedule = new Schedule("Alex");
    Event e = new Event("\"CS3500 Morning Lecture\"",
            "\"Churchill Hall 101\"",
            false,
            new Date(Day.Tuesday, "0950"),
            new Date(Day.Tuesday, "1130"),
            new ArrayList<>(List.of("Alex", "\"Student Anon\"", "\"Chat\"")));
    schedule.addEvent(e);
    Assert.assertTrue(schedule.getEvents().get(0).equals(e));
  }

  @Test
  public void testConstructorFails() {
    Assert.assertThrows(IllegalArgumentException.class, () -> new Schedule(""));
    Assert.assertThrows(IllegalArgumentException.class, () -> new Schedule(null));
  }

  @Test
  public void testAddEventFailsDueToTimeConflict() {
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

    Schedule schedule = new Schedule("Alex");
    schedule.addEvent(e1);

    Assert.assertThrows(IllegalStateException.class, () -> {
      schedule.addEvent(e2);
    });
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

    Schedule schedule = new Schedule("Alex");

    schedule.addEvent(event1);
    schedule.addEvent(event2);
    schedule.addEvent(event3);

    Assert.assertEquals(this.textViewUser1, schedule.toString());
  }
}
