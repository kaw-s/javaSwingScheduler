package model;

import java.util.ArrayList;

/**
 * Represents an event in a calendar.
 */
public interface EventModel {
  /**
   * Returns the name of the event. All events within a single user's
   * calendar must be unique.
   * @return name of the event
   */
  String getName();

  /**
   * Returns the start date of an event.
   * @return start date of an event
   */
  Date getStartDate();

  /**
   * Returns the end date of an event.
   * @return end date of an event
   */
  Date getEndDate();

  /**
   * Returns whether the event is online.
   * @return true if online; false otherwise
   */
  boolean getOnline();

  /**
   * Returns the location of the event.
   * @return location of the event
   */
  String getLocation();

  /**
   * Returns a list of all the invited users in an event.
   * The first invited user is always the host, and every event
   * must have at least 1 invited user to be considered a valid event.
   * @return list of invited users
   */
  ArrayList<String> getInvitedUsers();

  /**
   * Determines whether there is a time conflict between this event
   * and another event when both occur on the same day. For instance,
   * Monday 1034 -> Monday 1100 would conflict with Monday 1050 -> Monday 1200
   * because the times overlap. But Monday 1000 -> Monday 1200 would not conflict
   * with Monday 1300 -> Monday 1500. It is assumed that this method will
   * only be called when the date being passed has the same start and end day as
   * this event.
   * @param event event we are checking to see if a conflict exists with this one
   * @return true if a conflict exists; false otherwise
   */
  boolean isConflictingTimeSameDay(Event event);
}
