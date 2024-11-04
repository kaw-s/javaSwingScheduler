package model;

import java.util.List;

/**
 * Represents a mutable view of NUPlanner, providing mutator methods.
 */
interface MutableNUPlannerModel {
  /**
   * Uploads an XML file representing a schedule and creates a new user, adding it to the
   * existing list of users. An XML file upload should not be allowed if the system
   * already has been loaded into the system.
   *
   * @param fileName name of XML file to be read
   * @throws IllegalArgumentException if fileName is null
   * @throws IllegalArgumentException if fileName is empty String
   * @throws IllegalArgumentException if userId is an empty String
   * @throws IllegalStateException    if a user already exists with that name in the system
   */
  void uploadXMLFile(String fileName);

  /**
   * Creates an event and adds it to a user's schedule and the schedules of all the invitees.
   * Not every user that was invited to the event must exist for the event to be valid in
   * a written schedule.
   *
   * @param userId user who is creating the event
   * @param event  the event being added. The first user who exists in the list of invited users
   *               must be the same as the host, or the userId -- the person who is creating
   *               the event.
   * @throws IllegalArgumentException if userId is null or does not belong in the system
   * @throws IllegalArgumentException if event is null
   * @throws IllegalStateException    if the first invited user is NOT the same as the provided
   *                                  userId or if the list of invited users is empty
   * @throws IllegalStateException    if the host of the event has a time conflict or
   *                                  already has an event with the same name
   */
  void addEvent(String userId, Event event);

  /**
   * Removes an event from a user's schedule. If the userId is the same as the event
   * host name (which can be found by looking at the first element of the invited users
   * of an event), the event should be removed from all the invitees' schedules; otherwise,
   * the event should only be removed from the schedule of the user removing the event.
   *
   * @param userId    name of registered user in the system
   * @param eventName event that is part of a person's schedule. We assume that for the list of
   *                  invited users, the first user will always be the host of the event
   * @throws IllegalArgumentException if the userId is null or does not exist in the system
   * @throws IllegalArgumentException if the eventName is null or if no events in the user's
   *                                  schedule exist with that same name
   */
  void removeEvent(String userId, String eventName);

  /**
   * Modifies an event in the system. It is important to note that in a user's schedule,
   * no two events can have the same name. Any user can modify any event and update any of
   * its fields. If a conflict exists with the new or old host, the event does not get added
   * to any of the invitees' schedules, and the old event remains. If a conflict does not exist
   * with the host, the old event is removed from the invitees' schedules and the new event
   * is added to their schedules. If a conflict exists with the invitees' schedules, the old
   * event is still removed but the new event is not added. Users who have not been uploaded
   * to the system cannot be made the host of a modified event. Any user in the system
   * can modify any event.
   *
   * @param oldEventName  old name of event we are modifying (can be the same as the new one)
   * @param modifiedEvent new event we are replacing an old event with
   * @throws IllegalArgumentException if oldEventName or modifiedEvent are null
   * @throws IllegalStateException    if host of modified event does not exist in system
   * @throws IllegalStateException    if the new event has 0 invited users
   * @throws IllegalStateException    if the host of the event has a time conflict or
   *                                  already has an event with the same name
   */
  void modifyEvent(String oldEventName, Event modifiedEvent);

  /**
   * Schedules an event for a provided list of users. If the host of the event has a
   * time conflict or already has an event with the same name, the event should
   * not be added to the schedules of the invited users. If a time conflict or event name
   * conflict exists with an invited user, we simply skip over that invitee and move on to the
   * next invitee. We only throw an exception if a conflict exists with the host.
   *
   * @param userIds list of user Ids whose schedules should now contain the new event. If a provided
   *                user does not yet exist in the system, we skip over them. If a conflict exists,
   *                we skip over them.
   * @param event   the event to be added
   * @throws IllegalArgumentException if users is null or if event is null
   * @throws IllegalStateException    if the host of the event has a time conflict or
   *                                  already has an event with the same name
   * @throws IllegalStateException    if the new event has 0 invited users or if the
   *                                  first element in event.getInvitedUsers() is not the same
   *                                  as the userId
   */
  void scheduleEvent(List<String> userIds, Event event);

  /**
   * Saves an existing user's schedule to an XML file.
   * @param userId name of a registered user in the system
   * @throws IllegalArgumentException if userId is null or does not exist in the system
   */
  void saveUserToXML(String userId);

  /** Adds a new user to the system.
   * If the user already exists, it will be replaced with the new user.
   * @param user the user to be added
   * @throws IllegalArgumentException if user already exists in the system
   */
  void addUser(User user);
}
