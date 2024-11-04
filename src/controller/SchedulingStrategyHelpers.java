package controller;

import java.util.List;

/**
 * Represents a schedulingStrategyHelpers class, which is responsible for
 * possessing logic that is shared between both strategies.
 */
public class SchedulingStrategyHelpers {

  /**
   * Inspects the details of the event and ensures that they are valid.
   * @param eventName name of the event
   * @param duration duration (string in minutes, should not be negative,
   *                 and should be between 0 and 10081 minutes, exclusive)
   * @param location location of the event
   * @param invitedUsers list of invited users
   * @return returns the duration, in minutes, if the event details are valid
   * @throws IllegalArgumentException if an event detail is invalid
   */
  public static int validateEventDetails(String eventName,
                                         String duration,
                                         String location, List<String> invitedUsers) {
    if (eventName == null || duration == null || location == null || invitedUsers == null) {
      throw new IllegalArgumentException("Arguments must not be null");
    } else if (invitedUsers.isEmpty()) {
      throw new IllegalArgumentException("invitedUsers must not be empty.");
    } else if (eventName.trim().isEmpty() || duration.trim().isEmpty()
            || location.trim().isEmpty()) {
      throw new IllegalArgumentException("eventName, duration, and location must not be empty.");
    }

    int intDuration;

    try {
      intDuration = Integer.parseInt(duration);

      if (intDuration <= 0 || intDuration > 10080) {
        throw new IllegalArgumentException("Duration must be greater " +
                "than 0 and not exceed 10080 minutes.");
      }

      return intDuration;
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Duration must be a valid integer in minutes.");
    }
  }
}
