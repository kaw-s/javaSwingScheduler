package model;

import java.util.ArrayList;
import java.util.List;

/**
 * The methods in the user class simply call
 * the methods in this class. For example, in User, we have addEvent(), which is supposed
 * to call the addEvent() method in schedule. We have simply decided to test the methods in User
 * class more efficiently over the methods in Schedule, as testing both sets of
 * classes would result in
 * duplicate test code. We have chosen to follow this design pattern because, although there exists
 * some redundant functionality, it clearly marks a distinction between a user and a schedule. A
 * schedule belongs to a user, so it would make sense for changes to a schedule to
 * be done from within a schedule class that belongs to a particular user, not
 * necessarily in that user's class.
 */
public class Schedule implements ScheduleModel {
  // not final because when we sort the events, we are choosing to replace
  // this.events with a new ArrayList instead of modifying the current one
  private List<Event> events;

  private final String userId;

  /**
   * Builds a schedule and sets the events to an empty ArrayList.
   */
  public Schedule(String userId) {
    if (userId == null || userId.isEmpty()) {
      throw new IllegalArgumentException("userId cannot be null or empty.");
    }

    this.events = new ArrayList<>();
    this.userId = userId;
  }

  @Override
  public ArrayList<Event> getEvents() {
    return (ArrayList<Event>)this.events;
  }

  @Override
  public void removeEvent(String eventName) {
    this.events.removeIf(event -> event.getName().equals(eventName));
  }

  @Override
  public void addEvent(Event event) throws IllegalStateException {
    String host = event.getInvitedUsers().get(0);

    if (this.hasTimeConflict(event)) {
      if (host.equals(this.userId)) {
        throw new IllegalStateException("Host has time conflict!");
      } // if userId is not the same as the host of the event => ignore conflict, do not add event
    } else if (this.hasEventNameConflict(event.getName())) {
      if (host.equals(this.userId)) {
        throw new IllegalStateException("Host has event name conflict!");
      } // if userId is not the same as the host of the event => ignore conflict, do not add event
    } else {
      this.events.add(event);
      this.sortEventsByStartingDate();
    }
  }

  /**
   * Checks to see whether the name of an event to be added has a name conflict with
   * an existing event.
   * @param name name of event to be added.
   * @return true if a name conflict exists; false otherwise
   */
  public boolean hasEventNameConflict(String name) {
    for (Event e : this.events) {
      if (e.getName().equals(name)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Checks to see whether a time conflict exists between existing events and a given event.
   * @param event event we are checking to see that conflicts with another
   * @return true if the event conflicts with another; false otherwise
   */
  public boolean hasTimeConflict(Event event) {
    for (Event e : this.events) {
      if (hasConflictHelper(event, e)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Checks to see whether a time conflict exists between two events.
   * @param newEvent an existing event in this.events
   * @param currentE the event we want to add to this.events
   * @return true if a conflict exists; false otherwise
   */
  private boolean hasConflictHelper(Event newEvent, Event currentE) {
    // stores the day of the week of the 4 days being considered
    Day newStartDay = newEvent.getStartDate().getDay();
    Day curStartDay = currentE.getStartDate().getDay();
    Day newEndDay = newEvent.getEndDate().getDay();
    Day curEndDay = currentE.getEndDate().getDay();

    // stores the start and end times of the events
    int newStartTime = Integer.parseInt(newEvent.getStartDate().getTime());
    int newEndTime = Integer.parseInt(newEvent.getEndDate().getTime());
    int curStartTime = Integer.parseInt(currentE.getStartDate().getTime());
    int curEndTime = Integer.parseInt(currentE.getEndDate().getTime());

    // stores the ordinal value of each day being considered
    int daysInWeek = Day.values().length;
    int newStartOffset = newStartDay.ordinal();
    int newEndOffset = newEndDay.ordinal();
    int curStartOffset = curStartDay.ordinal();
    int curEndOffset = curEndDay.ordinal();

    // if the two events have the same start and end day => check time conflict on same day
    if (newStartDay.equals(curStartDay) && newEndDay.equals(curEndDay)) {
      return newEvent.isConflictingTimeSameDay(currentE);
      // if the ordinal value of the end day is less than the ordinal value of the start day
      // this means that the event continues to the next week so add 7 to the value end
      // day and store this in its offset
    } else if (newEndOffset < newStartOffset) {
      newEndOffset += daysInWeek;
    }
    if (curEndOffset < curStartOffset) {
      curEndOffset += daysInWeek;
    }

    // if end day is the same as the start day then check if the then end time occurs before the
    // start time, which means the event spans an entire week so add 7 to the end day value
    if (newEndOffset == newStartOffset && newEndTime < newStartTime) {
      newEndOffset += daysInWeek;
    }
    if (curEndOffset == curStartOffset && curEndTime < curStartTime) {
      curEndOffset += daysInWeek;
    }
    // checking if there is overlap in ranges of events using updated offset values
    if (!(newEndOffset <= curStartOffset || newStartOffset >= curEndOffset)) {
      return true;
    } else if (newStartDay.equals(curEndDay)) {
      return newStartTime < curEndTime;
    } else if (newEndDay.equals(curStartDay)) {
      return newEndTime > curStartTime;
    }
    return false;
  }

  /**
   * Inserts an event into a sorted list of events and sorts it according to its start day
   * and start time. For example, Friday at 1000 comes before Saturday 1200, so it will come
   * before in the ArrayList to be returned.
   * @param e the event to be sorted
   * @param sortedEvents list of sorted events
   */
  private void sortEventsByStartingDateInsertionSort(Event e, List<Event> sortedEvents) {
    sortedEvents.add(e);

    int i = sortedEvents.size() - 1;

    int startDay = Day.getIndexByDay(e.getStartDate().getDay());
    int totalMinutes = e.getStartDate().getTotalMinutes();

    while (i > 0) {
      int targetStartDay = Day.getIndexByDay(sortedEvents.get(i - 1)
              .getStartDate().getDay());
      int targetTotalMinutes = sortedEvents.get(i - 1).getStartDate().getTotalMinutes();

      if (startDay < targetStartDay
              || (startDay == targetStartDay && totalMinutes < targetTotalMinutes)) {
        Event temp = sortedEvents.get(i - 1);
        sortedEvents.set(i - 1, e);
        sortedEvents.set(i, temp);
      }

      i--;
    }
  }

  /**
   * Builds a sorted events list and sets this.events to the new sorted list.
   */
  private void sortEventsByStartingDate() {
    ArrayList<Event> sortedEvents = new ArrayList<>();

    for (Event event : this.events) {
      this.sortEventsByStartingDateInsertionSort(event, sortedEvents);
    }

    this.events = sortedEvents;
  }

  @Override
  public String toString() {
    // responsible for printing out this user's schedule. This method is tested via
    // the User class toString() method, where this.toString() is called from.
    // we do not need to call sortEventsByStartingDate() because we can assume
    // that the dates are already sorted according to starting date. Each time
    // that an event is added to this.events, it gets sorted according to its starting date.
    // This is taken care of in this.addEvent()

    List<ArrayList<Event>> eventsByDays = this.splitEventsIntoDifferentDays();
    String textualSchedule = "User: " + this.userId + "\n";

    for (int i = 0; i < eventsByDays.size(); i++) {
      String day = Day.values()[i].toString();
      textualSchedule += (i != 0 ? "\n" : "") + day + ":";

      for (int k = 0; k < eventsByDays.get(i).size(); k++) {
        textualSchedule += "\n" + eventsByDays.get(i).get(k).toString()
                + (k < eventsByDays.get(i).size() - 1 ? "\n" : "");
      }
    }

    return textualSchedule;
  }

  /**
   * Orders events according to the day of the week in which they occur.
   * @return an ordered 2d list containing events corresponding with days of the week
   */
  private List<ArrayList<Event>> splitEventsIntoDifferentDays() {
    ArrayList<ArrayList<Event>> scheduleEvents = new ArrayList<>();

    for (int i = 0; i < Day.values().length; i++) {
      scheduleEvents.add(new ArrayList<>());
    }

    for (Event event : this.getEvents()) {
      int dayNum = Day.getIndexByDay(event.getStartDate().getDay());
      scheduleEvents.get(dayNum).add(event);
    }

    return scheduleEvents;
  }
}
