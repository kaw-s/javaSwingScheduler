package model;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import helpers.FileHelper;

/**
 * Represents a NUPlanner, a calendar-based system that contains users and their schedules.
 * One invariant that we have decided to enforce is that each event in the system must have at
 * least one invited user, and the first invited user must be the host of the event.
 * This invariant ensures that events are properly organized and that the host is always
 * included in the list of invited users. Everytime an event is modified or added to a user's
 * schedule, we check the above criteria and throw an exception if it is not met.
 */
public class NUPlanner implements NUPlannerModel {

  private final List<User> users;

  /**
   * Creates a new NUPlanner scheduling system object.
   * @param buildDefaultUsers true of the model should create default users; false otherwise
   */
  public NUPlanner(boolean buildDefaultUsers) {
    this.users = new ArrayList<>();

    if (buildDefaultUsers) {
      this.uploadXMLFile("bobby.xml");
      this.uploadXMLFile("jimmy.xml");
      this.uploadXMLFile("testUser.xml");
      this.uploadXMLFile("adrian.xml");
    }
  }

  /**
   * Builds a new NUPlanner object with a pre-made set of users and their schedules. It is assumed
   * that every user is unique and their schedules have no conflicting events.
   * @param users list of users
   */
  public NUPlanner(List<User> users) {
    if (users == null) {
      throw new IllegalArgumentException("Users must not be null.");
    }

    this.users = users;
  }

  /**
   * Builds an event from a hashmap of data that was read from a file and adds it to a list of
   * events.
   * @param event  an event
   * @param events list of events
   */
  private void buildEvent(HashMap<String, ArrayList<String>> event, ArrayList<Event> events) {
    String name = event.get("name").get(0);
    String location = event.get("location").get(1).split(":")[1];
    boolean online = Boolean.parseBoolean(event.get("location").get(0).split(":")[1]);
    String startingDay = event.get("time").get(0).split(":")[1];
    String startingTime = event.get("time").get(1).split(":")[1];
    String endingDay = event.get("time").get(2).split(":")[1];
    String endingTime = event.get("time").get(3).split(":")[1];
    ArrayList<String> newInvitedUsers = new ArrayList<>();
    event.get("users").forEach(user -> newInvitedUsers.add(user.split("uid:")[1]));

    events.add(
        new Event(name, location, online,
            new Date(Day.valueOf(startingDay), startingTime),
            new Date(Day.valueOf(endingDay), endingTime), newInvitedUsers)
    );
  }

  @Override
  public void uploadXMLFile(String fileName) {
    if (fileName == null) {
      throw new IllegalArgumentException("File name cannot be null.");
    } else if (fileName.isEmpty()) {
      throw new IllegalArgumentException("File name cannot be an empty String.");
    }

    Document document = FileHelper.createXMlDoc(fileName);
    String userId = FileHelper.readUserIDFromXML(document);

    if (userId.trim().isEmpty()) {
      throw new IllegalArgumentException("UserId must not be an empty string.");
    }

    // if user already exists, we should not allow the file upload
    int matchingUserIdx = this.findUserIndexByName(userId);
    if (matchingUserIdx > -1) {
      throw new IllegalStateException("Cannot upload a new XML file for a user that "
          + "already exists in the system.");
    }

    Map<Integer, HashMap<String, ArrayList<String>>>
        schedule = FileHelper.readUserScheduleFromXML(fileName);

    ArrayList<Event> events = new ArrayList<>();
    User user = new User(userId);
    this.buildUserFromXML(schedule, events, user);
  }

  /**
   * Creates a new user given a hashmap of data that was constructed after parsing an XML file.
   *
   * @param schedule the schedule of the user
   * @param events   list of events on the users schedule that have yet to be added
   * @param user     the user whose schedule is being built
   * @throws IllegalStateException if there are no events
   * @throws IllegalStateException if any event has 0 invitees
   */
  private void buildUserFromXML(Map<Integer, HashMap<String, ArrayList<String>>> schedule,
      ArrayList<Event> events, User user) {
    schedule.values().forEach(e -> this.buildEvent(e, events));

    // if there exist no <event></event> tags in the XML, then we can assume that
    // a user does not have any events, and therefore we throw an IllegalStateException.
    // If there exists an <event></event> tag then we assume that there is content inside it
    if (events.isEmpty()) {
      throw new IllegalStateException("Must have at least 1 event.");
    }

    // there should always be a <users></users> tag in the XML.
    // Invitees are treated like an arrayList, and so we assume that the tag
    // will always exist. However, if there is no content inside the tag, that is
    // when we throw an IllegalStateException because we expect there to be at least
    // one invitee
    for (Event e : events) {
      if (e.getInvitedUsers().isEmpty()) {
        throw new IllegalStateException("Event must have at least 1 invitee.");
      }

      user.addEvent(e);
    }

    this.users.add(user);
  }

  @Override
  public boolean doesEventConflictExist(String userId, Event event) {
    if (userId == null || event == null) {
      throw new IllegalArgumentException("userId and event must not be null.");
    } else if (this.findUserIndexByName(userId) == -1) {
      throw new IllegalArgumentException("Invalid userId.");
    }

    User user = this.users.get(this.findUserIndexByName(userId));

    return user.getSchedule().hasEventNameConflict(event.getName())
        || user.getSchedule().hasTimeConflict(event);
  }

  @Override
  public boolean doesEventConflictExistForManyUsers(List<String> userIds, Event event) {
    if (userIds == null || event == null) {
      throw new IllegalArgumentException("userId and event must not be null.");
    }

    for (String userId : userIds) {
      if (this.findUserIndexByName(userId) == -1) {
        throw new IllegalArgumentException("Invalid userId provided: " + userId);
      }

      User user = this.users.get(this.findUserIndexByName(userId));
      if (user.getSchedule().hasEventNameConflict(event.getName())
              || user.getSchedule().hasTimeConflict(event)) {
        return true;
      }
    }

    return false;
  }


  @Override
  public String displayUserSchedule(String userId) {
    if (userId == null) {
      throw new IllegalArgumentException("Username cannot be null.");
    } else if (this.findUserIndexByName(userId) == -1) {
      throw new IllegalArgumentException("Invalid username.");
    }

    int idx = this.findUserIndexByName(userId);
    return this.users.get(idx).toString();
  }

  @Override
  public void addEvent(String userId, Event event) {
    if (userId == null || event == null) {
      throw new IllegalArgumentException("Username and event cannot be null.");
    } else if (this.findUserIndexByName(userId) == -1) {
      throw new IllegalArgumentException("Invalid username.");
    } else if (event.getInvitedUsers().isEmpty()) {
      throw new IllegalStateException("Must have at least 1 invited user.");
    } else if (!event.getInvitedUsers().get(0).equals(userId)) {
      throw new IllegalStateException("First invited user must be the same as host, or userId");
    }

    User host = this.users.get(this.findUserIndexByName(userId));
    // addEvent will throw an exception if a time conflict or name conflict
    // exists, and code execution will stop. This means that we essentially
    // fail to move on to the subsequent code and do not add the invitees to the event

    host.addEvent(event); // can throw e if name or time conflict

    ArrayList<String> invitedUsers = event.getInvitedUsers();

    for (String invitedUser : invitedUsers) {
      // we ignore adding an event to the host, because we already did so above.
      // if we were to add the event to the host schedule again, we would have a conflict
      // we can safely assume that userId is the same as the host id because
      // we already check for that above
      if (!invitedUser.equals(userId)) {
        int userIdx = this.findUserIndexByName(invitedUser);

        if (userIdx > -1) {
          User targetUser = this.users.get(userIdx);
          // addEvent() will fail if a time or name conflict exists.
          // But because targetUser is an invited user, addEvent() will not throw an
          // exception, so we can simply ignore the failed call of adding an event to
          // this specific target user and move on to the next one
          targetUser.addEvent(event);
        }
      }
    }
  }

  @Override
  public void removeEvent(String userId, String eventName) {
    if (userId == null || eventName == null) {
      throw new IllegalArgumentException("Username and eventName cannot be null.");
    } else if (this.findUserIndexByName(userId) == -1) {
      throw new IllegalArgumentException("Invalid username.");
    }

    // checks to see if an event exists in the schedule of username and stores it as an optional
    Optional<Event> optionalEvent = this.users.get(this.findUserIndexByName(userId))
        .getEvents().stream().filter(e -> e.getName().equals(eventName)).findFirst();

    if (optionalEvent.isEmpty()) {
      throw new IllegalArgumentException("Invalid eventName.");
    }

    // we have to call event.get() to return the actual event, as we are using an optional
    ArrayList<String> invitees = optionalEvent.get().getInvitedUsers();

    // if username is the host --> remove event from all invitees' schedules
    // otherwise remove event only from user's schedule
    if (invitees.get(0).equals(userId)) {
      invitees.forEach(invitee -> {
        int userIdx = this.findUserIndexByName(invitee);
        if (userIdx > -1) { // the invitee might not exist in the system
          User user = this.users.get(userIdx);
          user.removeEvent(eventName);
        }
      });
    } else {
      this.users.get(this.findUserIndexByName(userId)).removeEvent(eventName);
    }
  }

  /**
   * Retrieves the index of a user with a matching name.
   *
   * @param userId name we are searching for
   * @return the index of a user with a matching name, otherwise -1
   */
  private int findUserIndexByName(String userId) {
    for (int i = 0; i < this.users.size(); i++) {
      if (this.users.get(i).getUserId().equals(userId)) {
        return i;
      }
    }

    return -1;
  }

  @Override
  public void modifyEvent(String oldEventName, Event modifiedEvent) {
    if (oldEventName == null || modifiedEvent == null) {
      throw new IllegalArgumentException("oldEventName and modifiedEvent must not be null");
    } else if (modifiedEvent.getInvitedUsers().isEmpty()) {
      throw new IllegalStateException("Must have at least 1 invitee.");
    }

    String hostName = modifiedEvent.getInvitedUsers().get(0);
    int hostNameIdx = this.findUserIndexByName(hostName);

    if (hostNameIdx == -1) {
      throw new IllegalStateException("Host of event is not loaded into the system yet.");
    }

    User host = this.users.get(this.findUserIndexByName(hostName));

    Optional<Event> oldEvent = host.getEvents()
        .stream().filter(e -> e.getName().equals(oldEventName)).findFirst();

    host.removeEvent(oldEventName);
    this.handleAddModifiedEvent(host, modifiedEvent, oldEventName, oldEvent);
  }

  /**
   * Handles adding the modified event to the schedules of the users who were invited to the
   * event.
   *
   * @param host          the host of the event
   * @param modifiedEvent the modified event
   * @param oldEventName  the name of the old event (must be unique in every users' schedule)
   * @throws IllegalStateException if an event name conflict or time conflict exists with the
   *                               host
   */
  private void handleAddModifiedEvent(User host, Event modifiedEvent, String oldEventName,
      Optional<Event> oldEvent) {
    try {
      host.addEvent(modifiedEvent); // will throw error if new host has conflict

      this.users.forEach(user -> {
        int userIdx = this.findUserIndexByName(user.getUserId());
        // we already added the new event to the host, so we want to
        // avoid doing it again because a conflict would exist
        if (userIdx > -1 && !user.getUserId().equals(host.getUserId())) {
          user.removeEvent(oldEventName);
          user.addEvent(modifiedEvent);
        }
      });
    } catch (IllegalStateException ex) {
      // if a conflict exists with the host's schedule when adding the new event, we simply
      // add back the old event and nothing changes
      oldEvent.ifPresent(host::addEvent);
      // rethrow the exception because the event was unable to be added to the host
      throw ex;
    }
  }

  @Override
  public void scheduleEvent(List<String> userIds, Event event) {
    // NOTE: this method is incomplete. The current available specifications are not
    // clear as to how this method should be implemented, but below is our educated guess.
    // We are waiting on clearer instructions to be released. For instance, we do not know
    // whether the provided event will contain the host on the first line of invited users
    // or not. It is not clear apparent whether the event is user-created (as in it will have
    // a host), or if it is system-created.

    if (userIds == null || event == null) {
      throw new IllegalArgumentException("Users and event must not be null.");
    }

    userIds.forEach(userId -> {
      int userIdx = this.findUserIndexByName(userId);
      if (userIdx > -1) {
        User user = this.users.get(userIdx);
        user.addEvent(event);
      }
    });
  }

  @Override
  public void saveUserToXML(String userId) {
    if (userId == null) {
      throw new IllegalArgumentException("User must not be null.");
    } else if (this.findUserIndexByName(userId) == -1) {
      throw new IllegalArgumentException("Invalid user.");
    }

    FileHelper.writeToFile(this.users.get(this.findUserIndexByName(userId)), userId + ".xml");
  }

  @Override
  public ArrayList<Event> checkOccurringMeetings(String userId, Date date) {
    if (userId == null || date == null) {
      throw new IllegalArgumentException("User and date must not be null.");
    } else if (this.findUserIndexByName(userId) == -1) {
      throw new IllegalArgumentException("User does not exist.");
    }

    ArrayList<Event> occurringMeetings = new ArrayList<>();
    ArrayList<Event> userEvents = this.users.get(this.findUserIndexByName(userId)).getEvents();

    for (Event event : userEvents) {
      if (Date.doesSingleDateOccurBetweenTwoDates(event.getStartDate(),
          event.getEndDate(), date)) {
        occurringMeetings.add(event);
      }
    }

    return occurringMeetings;
  }

  @Override
  public ArrayList<User> getUsers() {
    return (ArrayList<User>) this.users;
  }

  @Override
  public void addUser(User user) {
    int userIdx = this.findUserIndexByName(user.getUserId());

    if (userIdx > -1) {
      throw new IllegalArgumentException("User already exists in system.");
    }

    this.users.add(user);
  }
}

