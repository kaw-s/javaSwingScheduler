package controller;

import java.util.ArrayList;
import java.util.List;

import model.Date;
import model.Day;
import model.Event;
import model.NUPlannerModel;
import model.User;

/**
 * Represents a WorkHoursStrategy. This scheduling strategy will find the
 * first possible time from Monday to Friday (inclusive) between the hours of
 * 0900 and 1700 (inclusive) where all invitees and the host can attend the
 * even and return an event with that block of time. Note this means it is
 * impossible to schedule an event that goes to next week.
 * Otherwise, it would not be a work hours' event.
 */
public class WorkHoursStrategy implements SchedulingStrategy {
  private final NUPlannerModel model;

  /**
   * Creates a new instance of WorkHoursStrategy, which takes in a model.
   * @param model some NUPlannerModel that is responsible for retrieving/updating
   *              user information and calendars.
   */
  public WorkHoursStrategy(NUPlannerModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Model must not be null.");
    }
    this.model = model;
  }

  private User getUser(String userId) {
    List<User> modelUsers = model.getUsers();
    for (User user : modelUsers) {
      if (user.getUserId().equals(userId)) {
        return user;
      }
    }
    return null;
  }

  private List<User> getAllUsers(List<String> invitedUsers) {
    List<User> invitees = new ArrayList<>();
    for (String id : invitedUsers) {
      User user = getUser(id);
      if (user != null) {
        invitees.add(user);
      } else {
        // Handle the case where user is nul
        System.err.println("User with ID " + id + " not found.");
      }
    }
    return invitees;
  }

  private List<Interval> getAllUsersIntervals(List<User> invitees) {
    List<Interval> intervals = new ArrayList<>();
    for (User user : invitees) {
      for (Event event : user.getEvents()) {
        Interval anInterval = assignInterval(event);
        intervals.add(anInterval);
      }
    }
    return intervals;
  }

  private Interval assignInterval(Event event) {
    return IntervalFactory.getInterval(event);
  }

  @Override
  public Event findEvent(String eventName, String duration,
                         String location, boolean online, List<String> invitedUsers) {
    ArrayList<User> users = (ArrayList<User>) getAllUsers(invitedUsers);
    List<Interval> allUsersIntervals = getAllUsersIntervals(users);
    IntervalSearchTree ist = new IntervalSearchTree();
    ist.buildIST((ArrayList<Interval>) allUsersIntervals);
    long requiredDuration = Long.parseLong(duration);

    // Find the earliest available interval that can accommodate the event duration
    Interval timeInterval = ist.findEarliestFreeInterval(requiredDuration);

    if (timeInterval == null) {
      throw new IllegalArgumentException("No available interval found for the given duration.");
    }

    long starting = timeInterval.start;
    long ending = starting + requiredDuration;

    int startDayIndex = (int) ((starting / (1440 * 7)) % 7);
    // Calculate the day index within a week
    int endDayIndex = (int) ((ending / (1440 * 7)) % 7);
    // Calculate the day index within a week

    Day startDay = Day.values()[startDayIndex];
    Day endDay = Day.values()[endDayIndex];

    // Calculate the time within the day
    int startTotalMinutes = (int) ((starting % (1440 * 7)) % 1440);
    int endTotalMinutes = (int) ((ending % (1440 * 7)) % 1440);

    // Format start time
    String startTimeHours = String.format("%02d", startTotalMinutes / 60);
    String startTimeMinutes = String.format("%02d", startTotalMinutes % 60);
    String startTime = startTimeHours + startTimeMinutes;

    // Format end time
    String endTimeHours = String.format("%02d", endTotalMinutes / 60);
    String endTimeMinutes = String.format("%02d", endTotalMinutes % 60);
    String endTime = endTimeHours + endTimeMinutes;

    Date startDate = new Date(startDay, startTime);
    Date endDate = new Date(endDay, endTime);

    return new Event(eventName, location, online, startDate, endDate, invitedUsers);
  }
}