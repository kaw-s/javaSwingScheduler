package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an event in a calendar.
 */
public class Event implements EventModel {
  // We are choosing to make these fields final because they will never be modified.
  // If a user chooses to modify an event, the resulting action will simply delete
  // the old event and create a new one. It is too cumbersome to have to check every
  // field on the old and new event to see what was changed, and it is easier to simply
  // do away with the old and create the new. Therefore, the fields will be final
  // because even when modifying an event, we are never actually changing any of its values.
  // We are simply deleting the entire event instead.

  private final String name;
  private final String location;
  private final boolean online;
  private final Date startDate;
  private final Date endDate;
  private final List<String> invitedUsers;

  /**
   * Creates a new event.
   * @param name name of event
   * @param location location of event
   * @param online whether event is online or not
   * @param startDate start date of event
   * @param endDate end date of event
   * @param invitedUsers list of invited users to event
   * @throws IllegalArgumentException if start time and time are equal when the days are the same
   * @throws IllegalArgumentException if the location is empty (our design requires that location
   *                                  be nonempty)
   */
  public Event(String name,
               String location,
               boolean online,
               Date startDate,
               Date endDate,
               List<String> invitedUsers) {
    if (name == null || location == null || startDate == null || endDate == null) {
      throw new IllegalArgumentException("location, online, startDate, endDate, and "
              + "invited users must not be null");
    } else if (startDate.getTotalMinutes() == endDate.getTotalMinutes()
            && startDate.getDay().equals(endDate.getDay())) {
      throw new IllegalArgumentException("Start time and end time cannot be equal when "
              + "days are the same!");
    } else if (location.trim().equals("") || name.trim().equals("")) {
      // this would happen if, for instance, <location></location> tag was empty, and
      // nothing was inside

      // <location>" "</location> is still a valid location; the location is
      // simpy empty quotes. Weird, but it works

      throw new IllegalArgumentException("Location and name cannot be empty.");
    }

    this.name = name;
    this.location = location;
    this.online = online;
    this.startDate = startDate;
    this.endDate = endDate;
    this.invitedUsers = invitedUsers;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public Date getStartDate() {
    return this.startDate;
  }

  @Override
  public Date getEndDate() {
    return this.endDate;
  }

  @Override
  public boolean getOnline() {
    return this.online;
  }

  @Override
  public String getLocation() {
    return this.location;
  }

  @Override
  public ArrayList<String> getInvitedUsers() {
    return (ArrayList<String>) this.invitedUsers;
  }

  @Override
  public boolean isConflictingTimeSameDay(Event event) {
    int thisEndTime = Integer.parseInt(this.endDate.getTime());
    int eventStartTime = Integer.parseInt(event.startDate.getTime());
    int thisStartTime = Integer.parseInt(this.startDate.getTime());
    int eventEndTime = Integer.parseInt(event.endDate.getTime());
    return thisEndTime > eventStartTime && eventEndTime > thisStartTime;
  }

  @Override
  public String toString() {
    String invitees = "";
    String eightSpaces = "        "; // we require 8 spaces as per specifications on website

    for (int i = 0; i < this.invitedUsers.size(); i++) {
      invitees += this.invitedUsers.get(i)
              + (i == this.invitedUsers.size() - 1 ? "" : "\n" + eightSpaces);
    }

    return eightSpaces + "name: " + this.name + "\n"
            + eightSpaces + "time: " + this.getStartDate().getDay() + ": "
            + this.getStartDate().getTime()
            + " -> " + this.getEndDate().getDay() + ": " + this.getEndDate().getTime() + "\n"
            + eightSpaces + "location: " + this.location + "\n"
            + eightSpaces + "online: " + this.online + "\n"
            + eightSpaces + "invitees: " + invitees;
  }
}
