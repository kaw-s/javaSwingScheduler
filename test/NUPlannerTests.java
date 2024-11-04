import model.NUPlanner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import model.Date;
import model.Day;
import model.Event;
import model.User;

/**
 * Tests methods in NUPlanner class.
 */
public class NUPlannerTests {
  private String expectedStringXML;

  @Before
  public void setUp() {
    this.expectedStringXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" "
            + "standalone=\"no\"?><schedule id=\"Alex\">\n"
            + "    <event>\n"
            + "        <name>\"Event 2\"</name>\n"
            + "        <time>\n"
            + "            <start-day>Tuesday</start-day>\n"
            + "            <start>0700</start>\n"
            + "            <end-day>Tuesday</end-day>\n"
            + "            <end>0800</end>\n"
            + "        </time>\n"
            + "        <location>\n"
            + "            <online>false</online>\n"
            + "            <place>Churchill</place>\n"
            + "        </location>\n"
            + "        <users>\n"
            + "            <uid>Alex</uid>\n"
            + "            <uid>\"Student Anon\"</uid>\n"
            + "            <uid>\"Chat\"</uid>\n"
            + "        </users>\n"
            + "    </event>\n"
            + "    <event>\n"
            + "        <name>\"Event 1\"</name>\n"
            + "        <time>\n"
            + "            <start-day>Tuesday</start-day>\n"
            + "            <start>0950</start>\n"
            + "            <end-day>Tuesday</end-day>\n"
            + "            <end>1130</end>\n"
            + "        </time>\n"
            + "        <location>\n"
            + "            <online>false</online>\n"
            + "            <place>Churchill</place>\n"
            + "        </location>\n"
            + "        <users>\n"
            + "            <uid>Alex</uid>\n"
            + "            <uid>\"Student Anon\"</uid>\n"
            + "            <uid>\"Chat\"</uid>\n"
            + "        </users>\n"
            + "    </event>\n"
            + "</schedule>";
  }

  @Test
  public void UploadXMLFileFailsWhenNoEvents() {
    NUPlanner planner = new NUPlanner(false);
    Assert.assertThrows(IllegalStateException.class, () -> planner
            .uploadXMLFile("noEventsUser.xml"));
  }

  @Test
  public void testUploadXMLFileFailsWhenEmptyUserId() {
    NUPlanner planner = new NUPlanner(false);
    Assert.assertThrows(IllegalStateException.class, () ->
            planner.uploadXMLFile("noUserIdUser.xml"));
  }

  @Test
  public void testUploadXMLFileFailsWithInvalidFileName() {
    NUPlanner planner = new NUPlanner(false);
    Assert.assertThrows(IllegalArgumentException.class, () -> planner.uploadXMLFile(null));
    Assert.assertThrows(IllegalArgumentException.class, () -> planner.uploadXMLFile(""));
    Assert.assertThrows(IllegalStateException.class, () ->
            planner.uploadXMLFile("unknownFile.xml"));
    Assert.assertThrows(IllegalStateException.class, () -> planner.uploadXMLFile("whatever"));
  }

  @Test
  public void testUploadXMLFileFailsWithNoInvitees() {
    NUPlanner planner = new NUPlanner(false);
    Assert.assertThrows(IllegalStateException.class, () -> planner
            .uploadXMLFile("noInviteesUser.xml"));
  }

  @Test
  public void testDisplayUserSchedule() {
    User user = new User("Alex");

    Event e1 = new Event("\"Event 1\"",
            "Churchill", false,
            new Date(Day.Tuesday, "0950"),
            new Date(Day.Tuesday, "1130"),
            new ArrayList<>(List.of("Alex", "\"Student Anon\"", "\"Chat\"")));

    Event e2 = new Event("\"Event 2\"",
            "Churchill", false,
            new Date(Day.Tuesday, "0700"),
            new Date(Day.Tuesday, "0800"),
            new ArrayList<>(List.of("Alex", "\"Student Anon\"", "\"Chat\"")));

    NUPlanner planner = new NUPlanner(false);
    planner.addUser(user);
    planner.addEvent("Alex", e1);
    planner.addEvent("Alex", e2);

    Assert.assertEquals(user.toString(), planner.displayUserSchedule("Alex"));
  }

  @Test
  public void testSingleArgConstructor() {
    Assert.assertThrows(IllegalArgumentException.class, () -> new NUPlanner(null));
  }

  @Test
  public void testDisplayUserScheduleWithInvalidArguments() {
    NUPlanner planner = new NUPlanner(false);
    // User "Alex" does not exist in the system yet
    Assert.assertThrows(IllegalArgumentException.class, () -> planner.displayUserSchedule("Alex"));
    Assert.assertThrows(IllegalArgumentException.class, () -> planner.displayUserSchedule(null));
  }

  @Test
  public void testCheckingOccurringMeetingsFailsWithNullArguments() {
    NUPlanner planner = new NUPlanner(false);

    Assert.assertThrows(IllegalArgumentException.class, () -> {
      planner.checkOccurringMeetings(null, null);
    });

    Assert.assertThrows(IllegalArgumentException.class, () -> {
      planner.checkOccurringMeetings(null, new Date(Day.Tuesday, "1000"));
    });

    Assert.assertThrows(IllegalArgumentException.class, () -> {
      planner.checkOccurringMeetings("Alex", null); // Alex not in system
    });
  }

  @Test
  public void testCheckingOccurringMeetingsFailedWithInvalidUser() {
    NUPlanner planner = new NUPlanner(false);

    Assert.assertThrows(IllegalArgumentException.class, () -> {
      planner.checkOccurringMeetings("Alex", new Date(Day.Tuesday, "1000"));
    });
  }

  @Test
  public void testCheckOccurringMeetings() {
    User alex = new User("Alex");
    User anon = new User("Anon");

    Event e1 = new Event("\"Event 1\"", "Churchill", false, new Date(Day.Tuesday, "0950"),
            new Date(Day.Tuesday, "1130"),
            new ArrayList<>(List.of("Alex", "Anon", "\"Chat\"")));
    Event e2 = new Event("\"Event 2\"", "Churchill", false, new Date(Day.Tuesday, "1130"),
            new Date(Day.Tuesday, "1350"),
            new ArrayList<>(List.of("Alex")));

    NUPlanner planner = new NUPlanner(false);
    planner.addUser(alex);
    planner.addUser(anon);
    planner.addEvent("Alex", e1);
    planner.addEvent("Alex", e2);

    ArrayList<Event> alexEvents1 = planner.checkOccurringMeetings("Alex",
            new Date(Day.Tuesday, "1130"));
    Assert.assertTrue(alexEvents1.size() == 2
            && alexEvents1.get(0).equals(e1) && alexEvents1.get(1).equals(e2));

    ArrayList<Event> alexEvents2 = planner.checkOccurringMeetings("Alex",
            new Date(Day.Wednesday, "1230"));

    Assert.assertEquals(0, alexEvents2.size());

    ArrayList<Event> anonEvents1 = planner.checkOccurringMeetings("Anon",
            new Date(Day.Tuesday, "1050"));
    Assert.assertEquals(1, anonEvents1.size());
  }

  @Test
  public void testHostFailsToAddEventDueToNameConflictAndInviteesAlsoFailToAddEvent() {
    NUPlanner planner = new NUPlanner(false);

    User user1 = new User("Alex");
    planner.addUser(user1);
    Event e1 = new Event("Event 1",
            "Churchill", false,
            new Date(Day.Tuesday, "0950"),
            new Date(Day.Tuesday, "1130"),
            new ArrayList<>(List.of("Alex", "Student Anon")));
    planner.addEvent("Alex", e1);

    User user2 = new User("Mike");
    planner.addUser(user2);
    Event e2 = new Event("Event 2",
            "Churchill", false,
            new Date(Day.Tuesday, "1350"),
            new Date(Day.Tuesday, "1500"),
            new ArrayList<>(List.of("Mike", "Student Anon", "Alex")));
    planner.addEvent("Mike", e2);

    User user3 = new User("Chat");
    planner.addUser(user3);

    Assert.assertThrows(IllegalStateException.class, () -> {
      planner.addEvent("Alex", new Event("Event 2", "Churchill", false,
              new Date(Day.Tuesday, "1700"),
              new Date(Day.Tuesday, "1800"),
              new ArrayList<>(List.of("Alex", "Mike", "Chat"))));
    });

    // we are asserting that because Alex had a name conflict with "Event 2"
    // (because Mike created the event and added Alex to it), when Alex attempts to
    // create a new event with the name "Event 2", the action fails. However,
    // we are checking that mike and chat never got the event added because the
    // host failed to add the event first due to a conflict.
    Assert.assertEquals(2, user1.getEvents().size());
    Assert.assertEquals(1, user2.getEvents().size());
    Assert.assertEquals(0, user3.getEvents().size());
  }

  @Test
  public void testSaveUserToXMLFailsWithNullArgument() {
    NUPlanner planner = new NUPlanner(false);
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      planner.saveUserToXML(null);
    });
  }

  @Test
  public void testSaveToUserFailsWithInvalidUserId() {
    NUPlanner planner = new NUPlanner(false);
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      planner.saveUserToXML("Alex"); // Alex not in system yet
    });
  }

  @Test
  public void testSaveUserToXML() throws Exception {
    User userAlex = new User("Alex");

    Event e1 = new Event("\"Event 1\"",
            "Churchill", false,
            new Date(Day.Tuesday, "0950"),
            new Date(Day.Tuesday, "1130"),
            new ArrayList<>(List.of("Alex", "\"Student Anon\"", "\"Chat\"")));

    Event e2 = new Event("\"Event 2\"",
            "Churchill", false,
            new Date(Day.Tuesday, "0700"),
            new Date(Day.Tuesday, "0800"),
            new ArrayList<>(List.of("Alex", "\"Student Anon\"", "\"Chat\"")));

    NUPlanner planner = new NUPlanner(false);
    planner.addUser(userAlex);
    planner.addEvent("Alex", e1);
    planner.addEvent("Alex", e2);
    planner.saveUserToXML("Alex");

    File file = new File("Alex.xml");

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document document = builder.parse(file);

    DOMSource dom = new DOMSource(document);
    Transformer transformer = TransformerFactory.newInstance()
            .newTransformer();
    StringWriter stringWriter = new StringWriter();
    transformer.transform(dom, new StreamResult(stringWriter));

    Assert.assertEquals(this.expectedStringXML, stringWriter.toString());
  }

  @Test
  public void testAddUser() {
    NUPlanner planner = new NUPlanner(false);
    User user = new User("Alex");
    planner.addUser(user);
    Assert.assertEquals(1, planner.getUsers().size());
  }

  @Test
  public void testAddEventFailsBecauseFirstInviteeIsNotHost() {
    NUPlanner planner = new NUPlanner(false);
    User user = new User("Alex");
    planner.addUser(user);

    Event event = new Event("\"Event 2\"",
            "Churchill", false,
            new Date(Day.Tuesday, "0700"),
            new Date(Day.Tuesday, "0800"),
            new ArrayList<>(List.of("Bob", "\"Student Anon\"", "\"Chat\"")));

    Assert.assertThrows(IllegalStateException.class, () -> {
      planner.addEvent("Alex", event);
    });
  }

  @Test
  public void testAddEventFailsWithNullArguments() {
    NUPlanner planner = new NUPlanner(false);

    Event event = new Event("\"Event 2\"",
            "Churchill", false,
            new Date(Day.Tuesday, "0700"),
            new Date(Day.Tuesday, "0800"),
            new ArrayList<>(List.of("Bob")));

    Assert.assertThrows(IllegalArgumentException.class, () -> planner.addEvent(null, event));
    Assert.assertThrows(IllegalArgumentException.class, () -> planner.addEvent("Alex", null));
    Assert.assertThrows(IllegalArgumentException.class, () -> planner.addEvent(null, null));
  }

  @Test
  public void testAddEventFailsWithNoInvitedUsers() {
    Event event = new Event("Event",
            "Churchill", false,
            new Date(Day.Tuesday, "0700"),
            new Date(Day.Tuesday, "0800"),
            new ArrayList<>(List.of()));

    User user = new User("Alex");

    NUPlanner planner = new NUPlanner(false);
    planner.addUser(user);
    Assert.assertThrows(IllegalStateException.class, () -> planner.addEvent("Alex", event));
  }

  @Test
  public void testAddEventFailsWhenUserNotInSystem() {
    Event event = new Event("Event",
            "Churchill", false,
            new Date(Day.Tuesday, "0700"),
            new Date(Day.Tuesday, "0800"),
            new ArrayList<>(List.of("Alex")));

    NUPlanner planner = new NUPlanner(false);
    Assert.assertThrows(IllegalArgumentException.class, () -> planner.addEvent("Alex", event));
  }

  @Test
  public void testAddEventGetsAddedToInviteeSchedules() {
    Event event = new Event("Event",
            "Churchill", false,
            new Date(Day.Tuesday, "0700"),
            new Date(Day.Tuesday, "0800"),
            new ArrayList<>(List.of("Alex", "Bob")));

    User alex = new User("Alex");
    User bob = new User("Bob");
    User mike = new User("Mike");

    NUPlanner planner = new NUPlanner(false);

    planner.addUser(alex);
    planner.addUser(bob);

    planner.addEvent("Alex", event);
    Assert.assertEquals(1, bob.getEvents().size());
    Assert.assertEquals(1, alex.getEvents().size());
  }

  @Test
  public void testEventDoesNotGetAddedToInviteeBecauseOfHostConflict() {
    Event event = new Event("Event",
            "Churchill", false,
            new Date(Day.Tuesday, "0700"),
            new Date(Day.Tuesday, "0800"),
            new ArrayList<>(List.of("Alex", "Bob")));

    Event nameConflictEvent = new Event("Event",
            "Churchill", false,
            new Date(Day.Tuesday, "1200"),
            new Date(Day.Tuesday, "1500"),
            new ArrayList<>(List.of("Alex", "Bob")));

    User alex = new User("Alex");
    User bob = new User("Bob");

    NUPlanner planner = new NUPlanner(false);

    planner.addUser(alex);
    planner.addUser(bob);

    planner.addEvent("Alex", event);

    Assert.assertThrows(IllegalStateException.class, () -> planner
            .addEvent("Alex", nameConflictEvent));

    Assert.assertEquals(1, alex.getEvents().size());
    Assert.assertEquals(1, bob.getEvents().size());
  }

  @Test
  public void testEventGetsAddedToHostButNotInviteeDueToConflict() {
    Event event = new Event("Event",
            "Churchill", false,
            new Date(Day.Tuesday, "0700"),
            new Date(Day.Tuesday, "0800"),
            new ArrayList<>(List.of("Alex", "Bob")));

    Event bobEvent = new Event("Sleeping",
            "Churchill", false,
            new Date(Day.Tuesday, "0700"),
            new Date(Day.Tuesday, "0800"),
            new ArrayList<>(List.of("Bob")));

    User alex = new User("Alex");
    User bob = new User("Bob");

    NUPlanner planner = new NUPlanner(false);

    planner.addUser(alex);
    planner.addUser(bob);

    planner.addEvent("Bob", bobEvent);
    planner.addEvent("Alex", event);

    Assert.assertEquals(1, alex.getEvents().size());
    Assert.assertEquals(1, bob.getEvents().size());
    Assert.assertEquals(bobEvent, bob.getEvents().get(0));
  }

  @Test
  public void testAddEventDoesNotGetAddedToInviteeNotInSystem() {
    Event event = new Event("Event",
            "Churchill", false,
            new Date(Day.Tuesday, "0700"),
            new Date(Day.Tuesday, "0800"),
            new ArrayList<>(List.of("Alex", "Bob")));

    User alex = new User("Alex");
    User bob = new User("Bob");

    NUPlanner planner = new NUPlanner(false);
    planner.addUser(alex);
    planner.addEvent("Alex", event);
    Assert.assertEquals(1, alex.getEvents().size());
    Assert.assertEquals(0, bob.getEvents().size());
  }

  @Test
  public void testRemoveEvent() {
    NUPlanner planner = new NUPlanner(false);
    User user = new User("Alex");
    planner.addUser(user);
    Event e = new Event("Event 2",
            "Churchill", false,
            new Date(Day.Tuesday, "0700"),
            new Date(Day.Tuesday, "0800"),
            new ArrayList<>(List.of("Alex", "\"Student Anon\"", "\"Chat\"")));
    planner.addEvent("Alex", e);
    Assert.assertEquals(1, user.getEvents().size());
    planner.removeEvent("Alex", "Event 2");
    Assert.assertEquals(0, user.getEvents().size());
  }

  @Test
  public void testRemoveEventWithNullArguments() {
    NUPlanner planner = new NUPlanner(false);
    Assert.assertThrows(IllegalArgumentException.class, () -> planner.removeEvent("Alex", null));
    Assert.assertThrows(IllegalArgumentException.class, () -> planner.removeEvent(null, "Event"));
    Assert.assertThrows(IllegalArgumentException.class, () -> planner.removeEvent(null, null));
  }

  @Test
  public void testRemoveEventWithInvalidUser() {
    NUPlanner planner = new NUPlanner(false);
    Assert.assertThrows(IllegalArgumentException.class, () -> planner
            .removeEvent("Alex", "Event"));
  }

  @Test
  public void testRemoveEventFailsWithInvalidEventName() {
    Event event = new Event("Event",
            "Churchill", false,
            new Date(Day.Tuesday, "0700"),
            new Date(Day.Tuesday, "0800"),
            new ArrayList<>(List.of("Alex")));

    User alex = new User("Alex");
    NUPlanner planner = new NUPlanner(false);
    planner.addUser(alex);
    planner.addEvent("Alex", event);
    Assert.assertThrows(IllegalArgumentException.class, () -> planner
            .removeEvent("Alex", "InvalidEventName"));
  }

  @Test
  public void testRemoveEventOnHostAndInvitees() {
    Event event = new Event("Event",
            "Churchill", false,
            new Date(Day.Tuesday, "0700"),
            new Date(Day.Tuesday, "0800"),
            new ArrayList<>(List.of("Alex", "Bob")));

    User alex = new User("Alex");
    User bob = new User("Bob");
    NUPlanner planner = new NUPlanner(false);
    planner.addUser(alex);
    planner.addUser(bob);
    planner.addEvent("Alex", event);

    planner.removeEvent("Alex", "Event");
    Assert.assertEquals(0, alex.getEvents().size());
    Assert.assertEquals(0, bob.getEvents().size());
  }

  @Test
  public void testRemoveEvenOnInvitee() {
    Event event = new Event("Event",
            "Churchill", false,
            new Date(Day.Tuesday, "0700"),
            new Date(Day.Tuesday, "0800"),
            new ArrayList<>(List.of("Alex", "Bob")));

    User alex = new User("Alex");
    User bob = new User("Bob");
    NUPlanner planner = new NUPlanner(false);
    planner.addUser(alex);
    planner.addUser(bob);
    planner.addEvent("Alex", event);

    planner.removeEvent("Bob", "Event");
    Assert.assertEquals(1, alex.getEvents().size());
    Assert.assertEquals(0, bob.getEvents().size());
  }

  @Test
  public void testModifyEvent() {
    NUPlanner planner = new NUPlanner(false);

    User sheena = new User("Sheena");
    User alex = new User("Alex");

    planner.addUser(sheena);
    planner.addUser(alex);

    Event e = new Event("Event 2",
            "Churchill", false,
            new Date(Day.Tuesday, "0700"),
            new Date(Day.Tuesday, "0800"),
            new ArrayList<>(List.of("Sheena", "Alex")));

    Event newEvent = new Event("Event 1",
            "Churchill", false,
            new Date(Day.Tuesday, "0700"),
            new Date(Day.Tuesday, "0800"),
            new ArrayList<>(List.of("Sheena", "Alex")));

    planner.addEvent("Sheena", e);
    Assert.assertThrows(IllegalStateException.class, () -> {
      planner.modifyEvent("Event 1", newEvent); // time conflict
    });
    Assert.assertEquals(1, alex.getEvents().size());
    Assert.assertEquals(sheena.getEvents().get(0), e);
  }

  @Test
  public void testModifyEventFailsWithNullArguments() {
    NUPlanner planner = new NUPlanner(false);
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      planner.modifyEvent(null, null);
    });

    Assert.assertThrows(IllegalArgumentException.class, () -> {
      planner.modifyEvent(null, new Event("Event 2",
              "Churchill", false,
              new Date(Day.Tuesday, "0700"),
              new Date(Day.Tuesday, "0800"),
              new ArrayList<>(List.of("Sheena", "Alex"))));
    });

    Assert.assertThrows(IllegalArgumentException.class, () -> {
      planner.modifyEvent("OldEventName", null);
    });
  }

  @Test
  public void testModifyEventFailsWhenNewEventHasNoInvitees() {
    NUPlanner planner = new NUPlanner(false);
    Assert.assertThrows(IllegalStateException.class, () -> {
      planner.modifyEvent("OldEventName", new Event("Event 2",
              "Churchill", false,
              new Date(Day.Tuesday, "0700"),
              new Date(Day.Tuesday, "0800"),
              new ArrayList<>(List.of())));
    });
  }

  @Test
  public void testModifyEventFailsWhenHostNameNotInSystem() {
    NUPlanner planner = new NUPlanner(false);
    Assert.assertThrows(IllegalStateException.class, () -> {
      planner.modifyEvent("OldEventName", new Event("Event 2",
              "Churchill", false,
              new Date(Day.Tuesday, "0700"),
              new Date(Day.Tuesday, "0800"),
              new ArrayList<>(List.of("Alex")))); // Alex not in system
    });
  }

  @Test
  public void testModifyEventFailsWhenHostHasTimeConflict() {
    Event event1 = new Event("Event 1",
            "Churchill", false,
            new Date(Day.Tuesday, "0700"),
            new Date(Day.Tuesday, "0800"),
            new ArrayList<>(List.of("Sheena", "Alex")));

    Event event2 = new Event("Event 2",
            "Churchill", false,
            new Date(Day.Tuesday, "1000"),
            new Date(Day.Tuesday, "1200"),
            new ArrayList<>(List.of("Sheena", "Alex")));

    User sheena = new User("Sheena");
    User alex = new User("Alex");
    NUPlanner planner = new NUPlanner(false);
    planner.addUser(sheena);
    planner.addUser(alex);
    planner.addEvent("Sheena", event1);
    planner.addEvent("Sheena", event2);

    Event newEvent = new Event("Event 1",
            "Churchill", false,
            new Date(Day.Tuesday, "0630"),
            new Date(Day.Tuesday, "0800"),
            new ArrayList<>(List.of("Sheena", "Alex")));

    Assert.assertThrows(IllegalStateException.class, () -> {
      planner.modifyEvent("Event 2", newEvent);
    });
  }

  @Test
  public void testModifyEventFailsWhenHostHasEventNameConflictAndInviteesStillHaveEvent() {
    Event event1 = new Event("Event 1",
            "Churchill", false,
            new Date(Day.Tuesday, "0700"),
            new Date(Day.Tuesday, "0800"),
            new ArrayList<>(List.of("Sheena", "Alex")));

    Event event2 = new Event("Event 2",
            "Churchill", false,
            new Date(Day.Tuesday, "1000"),
            new Date(Day.Tuesday, "1200"),
            new ArrayList<>(List.of("Sheena", "Alex")));

    User sheena = new User("Sheena");
    User alex = new User("Alex");
    NUPlanner planner = new NUPlanner(false);
    planner.addUser(sheena);
    planner.addUser(alex);

    planner.addEvent("Sheena", event1);
    planner.addEvent("Sheena", event2);

    Event newEvent = new Event("Event 2",
            "Churchill", false,
            new Date(Day.Tuesday, "1800"),
            new Date(Day.Tuesday, "2000"),
            new ArrayList<>(List.of("Sheena", "Alex")));

    Assert.assertThrows(IllegalStateException.class, () -> {
      planner.modifyEvent("Event 1", newEvent);
    });

    Assert.assertEquals(2, alex.getEvents().size());
    Assert.assertEquals(2, sheena.getEvents().size());
  }

  @Test
  public void testModifyEventGotModifiedForInvitees() {
    Event event1 = new Event("Event 1",
            "Churchill", false,
            new Date(Day.Tuesday, "0700"),
            new Date(Day.Tuesday, "0800"),
            new ArrayList<>(List.of("Alex", "Bob")));

    User kaia = new User("Kaia");
    User alex = new User("Alex");
    User bob = new User("Bob");
    NUPlanner planner = new NUPlanner(false);
    planner.addUser(alex);
    planner.addUser(bob);
    planner.addEvent("Alex", event1);

    planner.modifyEvent("Event 1", new Event("Event 2",
            "Churchill", false,
            new Date(Day.Tuesday, "1000"),
            new Date(Day.Tuesday, "1200"),
            new ArrayList<>(List.of("Bob", "Alex", "Kaia"))));

    Assert.assertEquals("1000", bob.getEvents().get(0).getStartDate().getTime());
    Assert.assertEquals("1200", bob.getEvents().get(0).getEndDate().getTime());
    Assert.assertEquals("1000", alex.getEvents().get(0).getStartDate().getTime());
    Assert.assertEquals("1200", alex.getEvents().get(0).getEndDate().getTime());
    Assert.assertEquals("Event 2", alex.getEvents().get(0).getName());
    Assert.assertEquals("Event 2", bob.getEvents().get(0).getName());
    // also checking to see that nothing got added to Kaia's schedule, as they were never
    // loaded into the system to begin with, even though she is an invitee -- this is ok!
    Assert.assertEquals(0, kaia.getEvents().size());
  }

  @Test
  public void testModifyEventFailsWhenHostNotInSystem() {
    Event event1 = new Event("Event 1",
            "Churchill", false,
            new Date(Day.Tuesday, "0700"),
            new Date(Day.Tuesday, "0800"),
            new ArrayList<>(List.of("Alex", "Bob")));

    User alex = new User("Alex");
    User bob = new User("Bob");
    NUPlanner planner = new NUPlanner(false);
    planner.addUser(alex);
    planner.addUser(bob);
    planner.addEvent("Alex", event1);

    Assert.assertThrows(IllegalStateException.class, () -> {
      planner.modifyEvent("Event 1", new Event("Event 2",
              "Churchill", false,
              new Date(Day.Tuesday, "1000"),
              new Date(Day.Tuesday, "1200"),
              new ArrayList<>(List.of("Sheena", "Alex")))); // sheena is not in the system
    });
  }

  @Test
  public void testScheduleEventFailsWithNullArguments() {
    NUPlanner planner = new NUPlanner(false);

    Assert.assertThrows(IllegalArgumentException.class, () -> {
      planner.scheduleEvent(null, null);
    });

    Assert.assertThrows(IllegalArgumentException.class, () -> {
      planner.scheduleEvent(new ArrayList<>(), null);
    });

    Assert.assertThrows(IllegalArgumentException.class, () -> {
      planner.scheduleEvent(null, new Event("Event 1",
              "Churchill", false,
              new Date(Day.Tuesday, "0700"),
              new Date(Day.Tuesday, "0800"),
              new ArrayList<>(List.of("Alex", "Bob"))));
    });
  }

  @Test
  public void testDoesConflictExist() {
    NUPlanner planner = new NUPlanner(false);

    User user = new User("Alex");

    Event e = new Event("Event 1",
            "Churchill", false,
            new Date(Day.Tuesday, "0700"),
            new Date(Day.Tuesday, "0800"),
            new ArrayList<>(List.of("Alex", "Bob")));

    planner.addUser(user);
    planner.addEvent("Alex", e);

    Event e2 = new Event("Event 1",
            "Churchill", false,
            new Date(Day.Tuesday, "0700"),
            new Date(Day.Tuesday, "0800"),
            new ArrayList<>(List.of("Alex", "Bob")));

    Assert.assertTrue(planner.doesEventConflictExist("Alex", e2));
  }
}
