import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import controller.AnytimeStrategy;
import controller.SchedulingStrategy;
import controller.WorkHoursStrategy;
import model.Date;
import model.Day;
import model.Event;
import model.NUPlanner;

/**
 * Tests the workhours and anytime strategy implementations.
 */
public class StrategyTests {
  @Test
  public void testAnytimeStrategySucceeds() {
    NUPlanner model = new NUPlanner(true);
    SchedulingStrategy anytimeStrategy = new AnytimeStrategy(model);
    Event event = anytimeStrategy.findEvent("reading", "30",
            "nowhere", false, new ArrayList<String>(List.of("jimmy", "bobby")));

    Event expectedEvent = new Event("reading", "nowhere", false,
            new Date(Day.Sunday, "0000"), new Date(Day.Sunday, "0030"),
            new ArrayList<String>(List.of("jimmy", "bobby")));

    Assert.assertTrue(event.getLocation().equals(expectedEvent.getLocation()));
    Assert.assertTrue(event.getInvitedUsers().equals(expectedEvent.getInvitedUsers()));
    Assert.assertTrue(event.getStartDate().getDay().equals(expectedEvent.getStartDate().getDay()));
    Assert.assertTrue(event.getStartDate().getTime()
            .equals(expectedEvent.getStartDate().getTime()));
    Assert.assertTrue(event.getEndDate().getDay().equals(expectedEvent.getEndDate().getDay()));
    Assert.assertTrue(event.getEndDate().getTime().equals(expectedEvent.getEndDate().getTime()));
    Assert.assertTrue(event.getOnline() == expectedEvent.getOnline());
    Assert.assertTrue(event.getName().equals(expectedEvent.getName()));

    event.getInvitedUsers().forEach(user -> {
      Assert.assertTrue(!model.doesEventConflictExist(user, event));
    });
  }

  @Test
  public void testWorkhoursStrategySucceeds() {
    NUPlanner model = new NUPlanner(true);
    SchedulingStrategy workhoursStrategy = new WorkHoursStrategy(model);
    Event event = workhoursStrategy.findEvent("reading", "30",
            "nowhere", false, new ArrayList<String>(List.of("jimmy", "bobby")));

    Event expectedEvent = new Event("reading", "nowhere", false,
            new Date(Day.Sunday, "0900"), new Date(Day.Sunday, "0930"),
            new ArrayList<String>(List.of("jimmy", "bobby")));

    Assert.assertTrue(event.getLocation().equals(expectedEvent.getLocation()));
    Assert.assertTrue(event.getInvitedUsers().equals(expectedEvent.getInvitedUsers()));
    Assert.assertTrue(event.getStartDate().getDay().equals(expectedEvent.getStartDate().getDay()));
    Assert.assertTrue(event.getStartDate()
            .getTime().equals(expectedEvent.getStartDate().getTime()));
    Assert.assertTrue(event.getEndDate().getDay().equals(expectedEvent.getEndDate().getDay()));
    Assert.assertTrue(event.getEndDate().getTime().equals(expectedEvent.getEndDate().getTime()));
    Assert.assertTrue(event.getOnline() == expectedEvent.getOnline());
    Assert.assertTrue(event.getName().equals(expectedEvent.getName()));

    event.getInvitedUsers().forEach(user -> {
      Assert.assertTrue(!model.doesEventConflictExist(user, event));
    });
  }
}
