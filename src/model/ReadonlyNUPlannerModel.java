package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a read-only view of NUPlanner, providing observation methods.
 */
public interface ReadonlyNUPlannerModel {

  /**
   * Displays the schedule of a given user.
   *
   * @param userId username of a registered user in the system
   * @throws IllegalArgumentException if userId is null or does not exist in the system
   */
  String displayUserSchedule(String userId);

  /**
   * Retrieves a list of all the events occurring at a given time.
   *
   * @param userId name of a registered user in the system
   * @param date   date that we are comparing against other event dates
   * @return list of all the events occurring at a given time
   * @throws IllegalArgumentException if userId or date are null
   * @throws IllegalArgumentException if userId does not exist in System
   */
  ArrayList<Event> checkOccurringMeetings(String userId, Date date);

  /**
   * Checks to see whether an event name or time conflict exists
   * when a given event is compared with all the events on a user's schedule.
   * @param userId userId of the user's schedule
   * @param event event we are checking to see if a conflict exists with it
   * @return true if a conflict exists; false otherwise
   * @throws IllegalArgumentException if userId does not correspond with an existing user
   * @throws IllegalArgumentException if userId or event are null
   */
  boolean doesEventConflictExist(String userId, Event event);

  /**
   * Checks to see whether an event name or time conflict exists
   * when a given event is compared with all the events on many users' schedules.
   * @param userIds userIds of the users whose schedules we are checking for a conflict
   * @param event event we are checking to see if a conflict exists with it
   * @return true if a conflict exists; false otherwise
   * @throws IllegalArgumentException if userId does not correspond with an existing user
   * @throws IllegalArgumentException if userId or event are null
   */
  boolean doesEventConflictExistForManyUsers(List<String> userIds, Event event);

  /**
   * Retrieves a list of existing users in the system.
   *
   * @return list of existing users in the system
   */
  ArrayList<User> getUsers();
}