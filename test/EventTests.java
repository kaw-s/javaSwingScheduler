import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import model.Date;
import model.Day;
import model.Event;

/**
 * Tests public methods in Event class.
 */
public class EventTests {

  @Test
  public void testToString() {
    Event event = new Event("\"CS3500 Morning Lecture\"",
            "Churchill", false,
            new Date(Day.Tuesday, "0950"),
            new Date(Day.Tuesday, "1130"),
            new ArrayList<>(List.of("\"Prof. Lucia\"", "\"Student Anon\"", "\"Chat\"")));

    String eightSpaces = "        ";
    Assert.assertEquals(eightSpaces + "name: \"CS3500 Morning Lecture\"\n"
                                  + eightSpaces + "time: Tuesday: 0950 -> Tuesday: 1130\n"
                                  + eightSpaces + "location: Churchill\n"
                                  + eightSpaces + "online: false\n"
                                  + eightSpaces + "invitees: \"Prof. Lucia\"\n"
                                  + eightSpaces + "\"Student Anon\"\n"
                                  + eightSpaces + "\"Chat\"", event.toString());
  }

  @Test
  public void testEventCreationWithInvalidDate() {
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      new Event("\"CS3500 Morning Lecture\"",
              "Churchill", false,
              new Date(Day.Tuesday, "1130"),
              new Date(Day.Tuesday, "1130"),
              new ArrayList<>(List.of("\"Prof. Lucia\"", "\"Student Anon\"", "\"Chat\"")));
    });
  }

  @Test
  public void testEventCreationWithInvalidNameAndLocation() {
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      // invalid event name
      new Event("",
              "Churchill", false,
              new Date(Day.Tuesday, "1130"),
              new Date(Day.Tuesday, "1130"),
              new ArrayList<>(List.of("\"Prof. Lucia\"", "\"Student Anon\"", "\"Chat\"")));
    });

    // invalid location
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      new Event("event",
              "", false,
              new Date(Day.Tuesday, "1130"),
              new Date(Day.Tuesday, "1130"),
              new ArrayList<>(List.of("\"Prof. Lucia\"", "\"Student Anon\"", "\"Chat\"")));
    });
  }

  @Test
  public void testEventCreationWithNullArguments() {
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      new Event(null,
              null, false,
              null,
              null,
              null);
    });
  }

  @Test
  public void testIsConflictingTimeSameDay() {
    Event event = new Event("\"CS3500 Morning Lecture\"",
            "Churchill", false,
            new Date(Day.Tuesday, "0950"),
            new Date(Day.Tuesday, "1130"),
            new ArrayList<>(List.of("\"Prof. Lucia\"", "\"Student Anon\"", "\"Chat\"")));

    Event event2 = new Event("\"CS3500 Morning Lecture\"",
            "Churchill", false,
            new Date(Day.Tuesday, "0950"),
            new Date(Day.Tuesday, "1130"),
            new ArrayList<>(List.of("\"Prof. Lucia\"", "\"Student Anon\"", "\"Chat\"")));

    Event event3 = new Event("\"CS3500 Morning Lecture\"",
            "Churchill", false,
            new Date(Day.Tuesday, "1130"),
            new Date(Day.Tuesday, "1200"),
            new ArrayList<>(List.of("\"Prof. Lucia\"", "\"Student Anon\"", "\"Chat\"")));

    Event event4 = new Event("\"CS3500 Morning Lecture\"",
            "Churchill", false,
            new Date(Day.Tuesday, "0900"),
            new Date(Day.Tuesday, "0950"),
            new ArrayList<>(List.of("\"Prof. Lucia\"", "\"Student Anon\"", "\"Chat\"")));

    Event event5 = new Event("\"CS3500 Morning Lecture\"",
            "Churchill", false,
            new Date(Day.Tuesday, "1500"),
            new Date(Day.Tuesday, "1600"),
            new ArrayList<>(List.of("\"Prof. Lucia\"", "\"Student Anon\"", "\"Chat\"")));

    Event event6 = new Event("\"CS3500 Morning Lecture\"",
            "Churchill", false,
            new Date(Day.Tuesday, "0900"),
            new Date(Day.Tuesday, "1030"),
            new ArrayList<>(List.of("\"Prof. Lucia\"", "\"Student Anon\"", "\"Chat\"")));

    Assert.assertTrue(event.isConflictingTimeSameDay(event2));
    Assert.assertFalse(event.isConflictingTimeSameDay(event3));
    Assert.assertFalse(event.isConflictingTimeSameDay(event4));
    Assert.assertFalse(event.isConflictingTimeSameDay(event5));
    Assert.assertTrue(event.isConflictingTimeSameDay(event6));
  }
}

