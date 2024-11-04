import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import model.Date;
import model.Day;
import model.Event;
import model.User;

/**
 * Tests the method hasTimeConflict() in User class.
 */
public class TestHasTimeConflict {
  @Test
  public void testHasTimeConflictWithOneDayDifferent() {
    User rhea = new User("Rhea");

    Event event1 = new Event("Sleep2",
            "nowhere",
            true,
            new Date(Day.Thursday, "1335"),
            new Date(Day.Wednesday, "1515"),
            new ArrayList<>(List.of("Rhea")));

    Event event2 = new Event("Sleep",
            "nowhere",
            true,
            new Date(Day.Wednesday, "1800"),
            new Date(Day.Wednesday, "1200"),
            new ArrayList<>(List.of("Rhea")));

    Assert.assertThrows(IllegalStateException.class, () -> {
      rhea.addEvent(event1);
      rhea.addEvent(event2);
    });

    Assert.assertThrows(IllegalStateException.class, () -> {
      rhea.addEvent(event2);
      rhea.addEvent(event1);
    });
  }

  @Test
  public void testHasTimeConflictWithAllDaysSameAndDateStartsWhenOtherEnds() {
    User rhea = new User("Rhea");

    Event event1 = new Event("Sleep1",
            "nowhere",
            true,
            new Date(Day.Wednesday, "1000"),
            new Date(Day.Wednesday, "1100"),
            new ArrayList<>(List.of("Rhea")));

    Event event2 = new Event("Sleep2",
            "nowhere",
            true,
            new Date(Day.Wednesday, "1100"),
            new Date(Day.Wednesday, "1200"),
            new ArrayList<>(List.of("Rhea")));

    rhea.addEvent(event1);
    rhea.addEvent(event2);

    Assert.assertEquals(event1, rhea.getEvents().get(0));
    Assert.assertEquals(event2, rhea.getEvents().get(1));
  }

  @Test
  public void testHasTimeConflictWithAllDaysSameAndDateEndsWhenOtherStarts() {
    User rhea = new User("Rhea");

    Event event1 = new Event("Sleep1",
            "nowhere",
            true,
            new Date(Day.Thursday, "1100"),
            new Date(Day.Thursday, "1200"),
            new ArrayList<>(List.of("Rhea")));

    Event event2 = new Event("Sleep2",
            "nowhere",
            true,
            new Date(Day.Thursday, "1000"),
            new Date(Day.Thursday, "1100"),
            new ArrayList<>(List.of("Rhea")));

    rhea.addEvent(event1);
    rhea.addEvent(event2);

    Assert.assertEquals(event1, rhea.getEvents().get(1));
    Assert.assertEquals(event2, rhea.getEvents().get(0));
  }

  @Test
  public void testHasTimeConflictWithAllDaysSameAndDateEndsWhenOtherStartsReverseOrder() {
    User rhea = new User("Rhea");

    Event event1 = new Event("Sleep1",
            "nowhere",
            true,
            new Date(Day.Friday, "1200"),
            new Date(Day.Friday, "1300"),
            new ArrayList<>(List.of("Rhea")));

    Event event2 = new Event("Sleep2",
            "nowhere",
            true,
            new Date(Day.Friday, "1000"),
            new Date(Day.Friday, "1200"),
            new ArrayList<>(List.of("Rhea")));

    // adding in reverse order to see if the opposite case is hit
    rhea.addEvent(event2);
    rhea.addEvent(event1);

    Assert.assertEquals(event1, rhea.getEvents().get(1));
    Assert.assertEquals(event2, rhea.getEvents().get(0));
  }

  @Test
  public void testHasTimeConflictWithSameDaysExceptOneEndDayDifferent() {
    User rhea = new User("Rhea");

    Event event1 = new Event("Sleep2",
            "nowhere",
            true,
            new Date(Day.Wednesday, "1000"),
            new Date(Day.Tuesday, "1000"),
            new ArrayList<>(List.of("Rhea")));

    Event event2 = new Event("Sleep1",
            "nowhere",
            true,
            new Date(Day.Tuesday, "1200"),
            new Date(Day.Tuesday, "1300"),
            new ArrayList<>(List.of("Rhea")));

    rhea.addEvent(event1);
    rhea.addEvent(event2);

    Assert.assertEquals(event1, rhea.getEvents().get(1));
    Assert.assertEquals(event2, rhea.getEvents().get(0));
  }

  @Test
  public void testHasTimeConflictWithSameDaysExceptOneStartDayDifferent() {
    User rhea = new User("Rhea");

    Event event1 = new Event("Sleep2",
            "nowhere",
            true,
            new Date(Day.Tuesday, "1000"),
            new Date(Day.Wednesday, "1000"),
            new ArrayList<>(List.of("Rhea")));

    Event event2 = new Event("Sleep1",
            "nowhere",
            true,
            new Date(Day.Wednesday, "1200"),
            new Date(Day.Wednesday, "1300"),
            new ArrayList<>(List.of("Rhea")));

    rhea.addEvent(event1);
    rhea.addEvent(event2);

    Assert.assertEquals(event1, rhea.getEvents().get(0));
    Assert.assertEquals(event2, rhea.getEvents().get(1));
  }

  @Test
  public void timeHasTimeConflictWithAllDifferentDaysAndOverlappingDays() {
    User rhea = new User("Rhea");

    Event event1 = new Event("Sleep2",
            "nowhere",
            true,
            new Date(Day.Monday, "1000"),
            new Date(Day.Thursday, "1200"),
            new ArrayList<>(List.of("Rhea")));

    Event event2 = new Event("Sleep1",
            "nowhere",
            true,
            new Date(Day.Wednesday, "1200"),
            new Date(Day.Thursday, "1300"),
            new ArrayList<>(List.of("Rhea")));

    Assert.assertThrows(IllegalStateException.class, () -> {
      rhea.addEvent(event1);
      rhea.addEvent(event2);
    });

    // testing the reverse order to check if the opposite case was hit
    Assert.assertThrows(IllegalStateException.class, () -> {
      rhea.addEvent(event2);
      rhea.addEvent(event1);
    });
  }

  @Test
  public void testHasTimeConflictWhenEndDaySameAsStartDay() {
    User rhea = new User("Rhea");

    Event event1 = new Event("Sleep2",
            "nowhere",
            true,
            new Date(Day.Tuesday, "1000"),
            new Date(Day.Saturday, "1200"),
            new ArrayList<>(List.of("Rhea")));

    Event event2 = new Event("Sleep1",
            "nowhere",
            true,
            new Date(Day.Saturday, "1200"),
            new Date(Day.Tuesday, "0900"),
            new ArrayList<>(List.of("Rhea")));

    rhea.addEvent(event1);
    rhea.addEvent(event2);

    Assert.assertEquals(event1, rhea.getEvents().get(0));
    Assert.assertEquals(event2, rhea.getEvents().get(1));
  }

  @Test
  public void testHasTimeConflictWithDifferentDaysAndOneDateSpanningIntoAnotherWeek() {
    User rhea = new User("Rhea");

    Event event1 = new Event("Sleep2",
            "nowhere",
            true,
            new Date(Day.Tuesday, "1000"),
            new Date(Day.Saturday, "1200"),
            new ArrayList<>(List.of("Rhea")));

    Event event2 = new Event("Sleep1",
            "nowhere",
            true,
            new Date(Day.Friday, "1200"),
            new Date(Day.Tuesday, "0900"),
            new ArrayList<>(List.of("Rhea")));

    Assert.assertThrows(IllegalStateException.class, () -> {
      rhea.addEvent(event1);
      rhea.addEvent(event2);
    });
  }

  @Test
  public void testHasTimeConflictWhenStartAndEndDaysSameButDifferentThanOtherDate() {
    // if one date's days are the same (i.e. both start and end tuesday) and the
    // other date's days are the same (i.e. both start and end wednesday) AND the days
    // are different (i.e. tuesday does not equal wednesday).

    User rhea = new User("Rhea");

    Event event1 = new Event("Sleep2",
            "nowhere",
            true,
            new Date(Day.Tuesday, "1000"),
            new Date(Day.Tuesday, "1200"),
            new ArrayList<>(List.of("Rhea")));

    Event event2 = new Event("Sleep1",
            "nowhere",
            true,
            new Date(Day.Wednesday, "1200"),
            new Date(Day.Wednesday, "1100"),
            new ArrayList<>(List.of("Rhea")));

    rhea.addEvent(event1);
    rhea.addEvent(event2);

    Assert.assertEquals(event1, rhea.getEvents().get(0));
    Assert.assertEquals(event2, rhea.getEvents().get(1));
  }

  @Test
  public void testHasTimeConflictWithOneDayDifferentAndDateSpanningIntoNextWeek() {
    User rhea = new User("Rhea");

    Event event1 = new Event("Sleep1",
            "nowhere",
            true,
            new Date(Day.Tuesday, "1000"),
            new Date(Day.Wednesday, "1100"),
            new ArrayList<>(List.of("Rhea")));

    Event event2 = new Event("Sleep2",
            "nowhere",
            true,
            new Date(Day.Tuesday, "1000"),
            new Date(Day.Tuesday, "0900"),
            new ArrayList<>(List.of("Rhea")));

    Assert.assertThrows(IllegalStateException.class, () -> {
      rhea.addEvent(event1);
      rhea.addEvent(event2);
    });
  }

  @Test
  public void testHasTimeConflictWithStartDayDifferent() {
    // this was already tested for another date in the above tests, but we are
    // ensuring that it works as expected

    User rhea = new User("Rhea");

    Event event1 = new Event("Sleep1",
            "nowhere",
            true,
            new Date(Day.Thursday, "1000"),
            new Date(Day.Tuesday, "1100"),
            new ArrayList<>(List.of("Rhea")));

    Event event2 = new Event("Sleep2",
            "nowhere",
            true,
            new Date(Day.Wednesday, "1000"),
            new Date(Day.Wednesday, "1200"),
            new ArrayList<>(List.of("Rhea")));

    rhea.addEvent(event1);
    rhea.addEvent(event2);

    Assert.assertEquals(event1, rhea.getEvents().get(1));
    Assert.assertEquals(event2, rhea.getEvents().get(0));
  }

  @Test
  public void testHasTimeConflictWhenDateHasSameStartAndEndAndOtherDateDoesNot() {
    // when one date starts and ends on the same day (i.e. start and end wednesday)
    // and the other date starts and ends on different days other than wednesday

    User rhea = new User("Rhea");

    Event event1 = new Event("Sleep2",
            "nowhere",
            true,
            new Date(Day.Wednesday, "1000"),
            new Date(Day.Wednesday, "1200"),
            new ArrayList<>(List.of("Rhea")));

    Event event2 = new Event("Sleep1",
            "nowhere",
            true,
            new Date(Day.Tuesday, "1000"),
            new Date(Day.Monday, "1100"),
            new ArrayList<>(List.of("Rhea")));

    Assert.assertThrows(IllegalStateException.class, () -> {
      rhea.addEvent(event1);
      rhea.addEvent(event2);
    });
  }

  @Test
  public void testHasTimeConflictWhenOneDateSpansIntoNextWeekAndOtherDateDoesNot() {
    User rhea = new User("Rhea");

    Event event1 = new Event("Sleep1",
            "nowhere",
            true,
            new Date(Day.Tuesday, "1000"),
            new Date(Day.Monday, "1100"),
            new ArrayList<>(List.of("Rhea")));

    Event event2 = new Event("Sleep2",
            "nowhere",
            true,
            new Date(Day.Wednesday, "1000"),
            new Date(Day.Wednesday, "1200"),
            new ArrayList<>(List.of("Rhea")));

    Assert.assertThrows(IllegalStateException.class, () -> {
      rhea.addEvent(event1);
      rhea.addEvent(event2);
    });
  }

  @Test
  public void testHasTimeConflictWithAllDaysSameAndOneEventStartsWhenOtherEnds() {
    User rhea = new User("Rhea");

    Event event1 = new Event("Sleep2",
            "nowhere",
            true,
            new Date(Day.Wednesday, "1000"),
            new Date(Day.Wednesday, "0900"),
            new ArrayList<>(List.of("Rhea")));


    Event event2 = new Event("Sleep1",
            "nowhere",
            true,
            new Date(Day.Tuesday, "1000"),
            new Date(Day.Tuesday, "0900"),
            new ArrayList<>(List.of("Rhea")));

    Assert.assertThrows(IllegalStateException.class, () -> {
      rhea.addEvent(event1);
      rhea.addEvent(event2);
    });
  }

  @Test
  public void testHasTimeConflictWhenAllDaysSameExceptOneDateStartDay() {
    User rhea = new User("Rhea");

    Event event1 = new Event("Sleep2",
            "nowhere",
            true,
            new Date(Day.Tuesday, "1000"),
            new Date(Day.Wednesday, "1200"),
            new ArrayList<>(List.of("Rhea")));

    Event event2 = new Event("Sleep1",
            "nowhere",
            true,
            new Date(Day.Wednesday, "1201"),
            new Date(Day.Wednesday, "1300"),
            new ArrayList<>(List.of("Rhea")));

    rhea.addEvent(event1);
    rhea.addEvent(event2);

    Assert.assertEquals(event1, rhea.getEvents().get(0));
    Assert.assertEquals(event2, rhea.getEvents().get(1));
  }

  @Test
  public void testHasTimeConflictWhenDatesAreOpposites() {
    // opposites as in one date is monday -> thursday and another
    // date is thursday -> monday

    User rhea = new User("Rhea");

    Event event1 = new Event("Sleep2",
            "nowhere",
            true,
            new Date(Day.Tuesday, "1000"),
            new Date(Day.Saturday, "1200"),
            new ArrayList<>(List.of("Rhea")));

    Event event2 = new Event("Sleep1",
            "nowhere",
            true,
            new Date(Day.Saturday, "1201"),
            new Date(Day.Tuesday, "0900"),
            new ArrayList<>(List.of("Rhea")));

    rhea.addEvent(event1);
    rhea.addEvent(event2);

    Assert.assertEquals(event1, rhea.getEvents().get(0));
    Assert.assertEquals(event2, rhea.getEvents().get(1));
  }
}
