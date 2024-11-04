package view;

import java.util.List;

import model.Event;

/**
 * Represents a Features interface, containing all the various actions that a client
 * can perform on the GUI side. These actions, when executed, directly modify
 * the state of the model in some way, shape or form.
 */
public interface Features {
  /**
   * Modifies an event on a user's schedule given an old event name and a new event.
   * @param oldEventName name of old event
   * @param modifiedEvent new event to replace old
   * @return "success" if event was modified without error; some error message otherwise
   */
  String modifyEvent(String oldEventName, Event modifiedEvent);

  /**
   * Responsible for scheduling an event on a host's schedule and invitees' schedules.
   * Depending on the command line arguments, this method will either schedule an
   * event on a host and invitees' schedules during workhours or anytime. Refer to the
   * strategies "AnytimeStrategy" and "WorkHoursStrategy" for a more concrete explanation.
   * @param eventName the name of the event
   * @param duration the duration of the event, in minutes
   * @param location location of the event
   * @param online whether the event is online or not
   * @param invitedUsers list of invited users
   * @return "success" if an event was added to every invitees' schedule; some error message
   *          otherwise
   */
  String scheduleEvent(String eventName, String duration,
                       String location, boolean online, List<String> invitedUsers);

  /**
   * Adds an event to a user's schedule given a user id and the event to be added.
   * @param userId id of the user whose schedule we want to add the event to
   * @param event event to be added
   * @return "success" if event was added without error; some error message otherwise
   */
  String addEvent(String userId, Event event);

  /**
   * Removes an event on a user's schedule given a user id and the name of the event to be removed.
   * @param userId id of the user whose schedule we want to remove an event from
   * @param eventName name of the event to be removed
   * @return "success" if event was removed without error; some error message otherwise
   */
  String removeEvent(String userId, String eventName);

  /**
   * Saves a user's schedule to an XML file given the id of a user in the system.
   * @param userId id of the user whose schedule we want to save to an XML file
   * @return "success" if user was saved without error; some error message otherwise
   */
  String saveUserToXML(String userId);

  /**
   * Uploads an XML file given a file path and uploads a new user and their schedule
   * to the main system.
   * @param path path to the XML file
   * @return "success" if user was uploaded without error; some error message otherwise
   */
  String uploadXMLFile(String path);
}
