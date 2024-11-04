package model;

import java.util.ArrayList;

/**
 * Represents a schedule belonging to a User.
 */
public interface ScheduleModel {
  /**
   * Retrieves a list of events corresponding to a user's schedule.
   * @return list of events
   */
  ArrayList<Event> getEvents();

  /**
   * Adds an event to this user's schedule. If a time conflict or name conflict
   * exists and the host of the event being added is the same as this user,
   * an exception is thrown; otherwise, the event is added. Note: if this user is the
   * host, this method is not responsible for adding the event to invitees' schedules; that
   * logic is taken care of in the NUPlanner model method addEvent(). This method should only be
   * called by the addEvent() method in User, which in turns call this addEvent(). This
   * ensures that the event will be added to other invitees' schedules. Because this class
   * does not have access to other users' schedules (and neither does the User class),
   * it does not have the capabilities to add the event to other users' schedules.
   * @param e event to be added
   * @throws IllegalStateException if a name conflict exists and the host
   *                               of the event is this user
   * @throws IllegalStateException if a time conflict exists and the host
   *                               of the event is this user
   */
  void addEvent(Event e);

  /**
   * Removes an event with a given name. If the event to be removed
   * does not exist, we choose not to throw an error.
   * @param eventName name of event to be removed
   */
  void removeEvent(String eventName);
}
