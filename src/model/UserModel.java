package model;

import java.util.ArrayList;

/**
 * Represents a User in a scheduling system.
 */
public interface UserModel {

  /**
   * Retrieves the userId of a user.
   * @return userId
   */
  String getUserId();

  /**
   * Adds an Event to a user's schedule. Calls addEvent()
   * in the Schedule class.
   * @param e Event to be added
   */
  void addEvent(Event e);

  /**
   * Retrieves a list of events in a user's schedule. Calls the getEvents()
   * method in the Schedule class.
   * @return list of events
   */
  ArrayList<Event> getEvents();

  /**
   * Retrieves the schedule belonging to this user.
   * @return user schedule
   */
  Schedule getSchedule();

  /**
   * Removes an event from a user's schedule. Calls the removeEvent()
   * method in the Schedule class.
   * @param eventName name of event to be removed
   */
  void removeEvent(String eventName);
}
