package controller;

import java.util.List;

import model.Date;
import model.Day;
import model.Event;
import model.NUPlannerModel;

/**
 * Represents an AnytimeStrategy. This strategy This scheduling strategy
 * will find the first possible time (starting Sunday at 00:00) that allows all
 * invitees and the host to be present and return an event with that block of time.
 */
public class AnytimeStrategy implements SchedulingStrategy {
  private final NUPlannerModel model;

  /**
   * Creates a new instance of AnytimeStrategy, which takes in a model.
   * @param model some NUPlannerModel that is responsible for retrieving/updating
   *              user information and calendars.
   */
  public AnytimeStrategy(NUPlannerModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Model must not be null.");
    }

    this.model = model;
  }

  @Override
  public Event findEvent(String eventName, String duration,
                         String location, boolean online, List<String> invitedUsers) {
    int intDuration = SchedulingStrategyHelpers.validateEventDetails(
            eventName, duration, location, invitedUsers);

    int days = intDuration / (24 * 60);
    int remainingMinutes = intDuration % (24 * 60);
    int hours = remainingMinutes / 60;
    int minutes = remainingMinutes % 60;

    Day startDay = Day.Sunday;
    String startTime = "0000";
    Day endDay = Day.values()[days];
    String endTime = String.format("%02d%02d", hours, minutes);

    int maxIterations = 7 * 24 * 60 / intDuration; // max iterations per week
    int iterationCount = 0;

    while (iterationCount < maxIterations) {
      Event event = new Event(eventName, location, online,
              new Date(startDay, startTime), new Date(endDay, endTime), invitedUsers);

      boolean conflictFound = false;

      for (String userId : invitedUsers) {
        if (model.doesEventConflictExist(userId, event)) {
          conflictFound = true;
          break;
        }
      }

      if (!conflictFound) {
        return event;
      } else {
        startDay = Day.values()[Day.getIndexByDay(startDay) + 1]; // increment day

        if (startDay == Day.Sunday) {
          startTime = "0000"; // reset time if Sunday
        }

        Date endDate = getEndDate(new Date(startDay, startTime), intDuration);
        endDay = endDate.getDay();
        endTime = endDate.getTime();
      }

      iterationCount++;
    }

    return null;
  }

  private Date getEndDate(Date startDate, int duration) {
    // Add duration to start time
    int totalMinutes = Integer.parseInt(startDate.getTime()) + duration;
    int days = totalMinutes / (24 * 60);
    int remainingMinutes = totalMinutes % (24 * 60);
    int hours = remainingMinutes / 60;
    int minutes = remainingMinutes % 60;

    // Set calculated date and time to the end date
    Day newDay = Day.values()[Day.getIndexByDay(startDate.getDay()) + days];
    String newTime = String.format("%02d%02d", hours, minutes);
    return new Date(newDay, newTime);
  }
}
