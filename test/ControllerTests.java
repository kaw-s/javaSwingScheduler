import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import controller.AnytimeStrategy;
import controller.NUPlannerController;
import model.Date;
import model.Day;
import model.Event;
import model.NUPlanner;
import model.NUPlannerModel;
import model.User;

/**
 * Represents a ControllerTests class, whose purpose is to test whether
 * the controller correctly delegated information to the view and
 * updated the model.
 */
public class ControllerTests {
  private NUPlannerModel model;

  @Before
  public void init() {
    this.model = new NUPlanner(true);
  }

  @Test
  public void testControllerFailsWithInvalidConstructorParams() {
    StringBuilder stringBuilder = new StringBuilder();

    FakeView fakeView = new FakeView(stringBuilder);
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      new NUPlannerController(null, null);
    });
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      new NUPlannerController(fakeView, null);
    });
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      new NUPlannerController(null, new AnytimeStrategy(this.model));
    });
  }

  @Test
  public void testModifyModelFailsWhenModelNotAdded() {
    StringBuilder stringBuilder = new StringBuilder();

    Event e = new Event(
            "Sleeping",
            "West A",
            false,
            new Date(Day.Thursday, "1300"),
            new Date(Day.Friday, "1800"),
            new ArrayList<>(List.of("jimmy")));

    FakeView fakeView = new FakeView(stringBuilder);
    NUPlannerController controller = new NUPlannerController(fakeView,
            new AnytimeStrategy(this.model));
    Assert.assertThrows(IllegalArgumentException.class, () ->
            controller.modifyEvent("Sleeping", e));
  }

  @Test
  public void testAddEventFails() {
    StringBuilder stringBuilder = new StringBuilder();

    Event e = new Event(
            "Sleeping",
            "West A",
            false,
            new Date(Day.Thursday, "1300"),
            new Date(Day.Friday, "1800"),
            new ArrayList<>(List.of("jimmy")));

    FakeView fakeView = new FakeView(stringBuilder);
    NUPlannerController controller = new NUPlannerController(fakeView,
            new AnytimeStrategy(this.model));
    controller.launch(this.model);
    String status = controller.addEvent("jimmy", e);
    Assert.assertTrue(stringBuilder.toString().equals("features"));
    Assert.assertEquals(status, "Host has time conflict!");
  }

  @Test
  public void testModifyEvent() {
    StringBuilder stringBuilder = new StringBuilder();

    Event e = new Event(
            "Sleeping",
            "West A",
            false,
            new Date(Day.Thursday, "1300"),
            new Date(Day.Friday, "1800"),
            new ArrayList<>(List.of("jimmy")));

    FakeView fakeView = new FakeView(stringBuilder);
    NUPlannerController controller = new NUPlannerController(fakeView,
            new AnytimeStrategy(this.model));
    controller.launch(this.model);
    controller.modifyEvent("Sleeping", e);
    Assert.assertTrue(stringBuilder.toString().startsWith("features"));
    Assert.assertTrue(stringBuilder.toString().endsWith("refreshed"));
    User targetUser = this.model.getUsers().stream().filter(user ->
            user.getUserId().equals("jimmy")).findFirst().orElse(null);
    Event targetEvent = targetUser.getEvents().stream().filter(event ->
            event.getName().equals("Sleeping")).findFirst().orElse(null);
    Assert.assertTrue(targetEvent.getEndDate().getTime().equals("1800"));
  }

  @Test
  public void testScheduleEvent() {
    StringBuilder stringBuilder = new StringBuilder();
    FakeView fakeView = new FakeView(stringBuilder);
    NUPlannerController controller = new NUPlannerController(fakeView,
            new AnytimeStrategy(this.model));
    controller.launch(this.model);
    String status = controller.scheduleEvent("Eating","50", "somewhere",
            true, new ArrayList<String>(List.of("jimmy")));
    Assert.assertTrue(status.equals("success"));
    Assert.assertTrue(stringBuilder.toString().equals(""));
    User targetUser = this.model.getUsers().stream().filter(u ->
            u.getUserId().equals("jimmy")).findFirst().orElse(null);
    Event event = new Event("Eating", "somewhere", true,
            new Date(Day.Monday, "1200"), new Date(Day.Monday, "1250"),
            new ArrayList<String>(List.of("jimmy")));
    boolean found = false;
    for (Event e : targetUser.getEvents()) {
      if (e.equals(event)) {
        found =  true;
      }
    }

    Assert.assertTrue(found);
  }

  @Test
  public void testScheduleEventFails() {
    StringBuilder stringBuilder = new StringBuilder();

    Event e = new Event(
            "Sleeping",
            "West A",
            false,
            new Date(Day.Monday, "0000"),
            new Date(Day.Monday, "2359"),
            new ArrayList<>(List.of("jimmy")));
    NUPlannerModel model = new NUPlanner(false);
    User user = new User("jimmy");
    model.addUser(user);
    model.addEvent("jimmy", e);
    FakeView fakeView = new FakeView(stringBuilder);
    NUPlannerController controller = new NUPlannerController(fakeView,
            new AnytimeStrategy(this.model));
    controller.launch(this.model);
    String status = controller.scheduleEvent("Eating","50", "somewhere",
            true, new ArrayList<String>(List.of("jimmy")));
    Assert.assertTrue(status.equals("Unable to find time that works for every invitee."));
    Assert.assertTrue(stringBuilder.toString().equals(""));
    User targetUser = this.model.getUsers().stream().filter(u ->
            u.getUserId().equals("jimmy")).findFirst().orElse(null);
    Assert.assertTrue(targetUser.getEvents().size() == 0);
  }

  @Test
  public void testAddEvent() {
    StringBuilder stringBuilder = new StringBuilder();
    Event e = new Event(
            "Napping",
            "West A",
            false,
            new Date(Day.Wednesday, "0800"),
            new Date(Day.Wednesday, "0805"),
            new ArrayList<>(List.of("jimmy")));

    FakeView fakeView = new FakeView(stringBuilder);
    NUPlannerController controller = new NUPlannerController(fakeView,
            new AnytimeStrategy(this.model));
    controller.launch(this.model);
    String status = controller.addEvent("jimmy", e);
    Assert.assertTrue(status.equals("success"));
    Assert.assertTrue(stringBuilder.toString().startsWith("features"));
    Assert.assertTrue(stringBuilder.toString().endsWith("refreshed"));
    User targetUser = this.model.getUsers().stream().filter(user ->
            user.getUserId().equals("jimmy")).findFirst().orElse(null);
    Event targetEvent = targetUser.getEvents().stream().filter(event ->
            event.getName().equals("Napping")).findFirst().orElse(null);
    Assert.assertTrue(targetEvent.getEndDate().getTime().equals("0805"));
  }


  @Test
  public void testRemoveEvent() {
    StringBuilder stringBuilder = new StringBuilder();
    FakeView fakeView = new FakeView(stringBuilder);
    NUPlannerController controller = new NUPlannerController(fakeView,
            new AnytimeStrategy(this.model));
    controller.launch(this.model);
    String status = controller.removeEvent("jimmy", "OOD");
    Assert.assertTrue(status.equals("success"));
    Assert.assertTrue(stringBuilder.toString().startsWith("features"));
    Assert.assertTrue(stringBuilder.toString().endsWith("refreshed"));
    User targetUser = this.model.getUsers().stream().filter(user ->
            user.getUserId().equals("jimmy")).findFirst().orElse(null);
    Event targetEvent = targetUser.getEvents().stream().filter(event ->
            event.getName().equals("Napping")).findFirst().orElse(null);
    Assert.assertTrue(targetEvent == null);
  }


  @Test
  public void testSaveUserToXML() {
    StringBuilder stringBuilder = new StringBuilder();
    FakeView fakeView = new FakeView(stringBuilder);
    NUPlannerController controller = new NUPlannerController(fakeView,
            new AnytimeStrategy(this.model));
    controller.launch(this.model);
    String status = controller.saveUserToXML("jimmy");
    Assert.assertTrue(status.equals("success"));
    Assert.assertTrue(stringBuilder.toString().startsWith("features"));
    Assert.assertTrue(stringBuilder.toString().endsWith("refreshed"));
  }


  @Test
  public void testUploadXMLFile() {
    StringBuilder stringBuilder = new StringBuilder();
    FakeView fakeView = new FakeView(stringBuilder);
    NUPlannerController controller = new NUPlannerController(fakeView,
            new AnytimeStrategy(this.model));
    controller.launch(this.model);
    String status = controller.uploadXMLFile("Alex.xml");
    Assert.assertTrue(status.equals("success"));
    Assert.assertTrue(stringBuilder.toString().startsWith("features"));
    Assert.assertTrue(stringBuilder.toString().endsWith("refreshed"));
    boolean hasUser = false;
    for (User user : this.model.getUsers()) {
      if (user.getUserId().equals("Alex")) {
        hasUser = true;
      }
    }
    Assert.assertTrue(hasUser);
  }
}
