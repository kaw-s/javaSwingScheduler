package model;

/**
 * Model representing a date that must have a day of the week and a time.
 * Day is an enum that consists of the days of the week Monday -> Sunday
 * starting with an uppercase character. Time is in the army time format "0000"
 */
public class Date implements DateModel {
  private final Day day;
  private final String time;

  /**
   * Creates a new date object with a day and time.
   * @param day  enum representing the day of the date, monday-sunday
   * @param time the time of day, in the format "0000"
   */
  public Date(Day day, String time) {
    if (day == null || time == null) {
      throw new IllegalArgumentException("Day and time cannot be null.");
    } else if (!this.isDateValid(time)) {
      throw new IllegalArgumentException("Invalid time.");
    }

    this.day = day;
    this.time = time;
  }

  @Override
  public Day getDay() {
    return this.day;
  }

  @Override
  public String getTime() {
    return this.time;
  }

  @Override
  public int getTotalMinutes() {
    int hours = Integer.parseInt(this.time.substring(0, 2));
    int minutes = Integer.parseInt(this.time.substring(2, 4));
    return (hours * 60) + minutes;
  }

  /**
   * Checks to see whether a time is valid. In order for a time to be valid,
   * it must have 4 characters and be in the format "0000", where the first two zeroes
   * represent the hours and the second set of zeroes represent the minutes. A time is
   * valid if it is considered to be in army time, where the hours do not exceed 23 and
   * fall below 0. It goes without saying that minutes should not exceed 59 either nor
   * fall below 0.
   * @param time time we are validating
   * @return true or false depending on whether day and time are valid
   */
  private boolean isDateValid(String time) {
    if (time.length() != 4) {
      return false;
    }

    try {
      int hours = Integer.parseInt(time.substring(0, 2));
      int minutes = Integer.parseInt(time.substring(2, 4));

      return minutes >= 0 && minutes <= 59
              && hours >= 0 && hours <= 23;
    } catch (NumberFormatException ex) {
      return false;
    }
  }

  /**
   * Checks to see whether a given date falls in between a given start date and given end date.
   * For example, Monday 1000 will fall between Monday 0900 and Monday 1500.
   * @param currentEventStartDate start date that represents a stat day and start time
   * @param currentEventEndDate end date that represents an end day and end time
   * @param targetDate date we are checking against a start date and end date
   * @return true or false depending on whether target date lies in
   *         between currentEventStartDate and currentEventEndDate
   */
  public static boolean doesSingleDateOccurBetweenTwoDates(
          Date currentEventStartDate, Date currentEventEndDate, Date targetDate) {
    int targetDayInMinutes = targetDate.getTotalMinutes();

    if (currentEventStartDate.getDay().equals(targetDate.getDay())
            && currentEventEndDate.getDay().equals(targetDate.getDay())) {
      // checking if start and end date are on the same day as target date day
      int eventStartTotalMinutes = currentEventStartDate.getTotalMinutes();
      int eventEndTotalMinutes = currentEventEndDate.getTotalMinutes();
      return eventStartTotalMinutes == targetDayInMinutes
              || eventEndTotalMinutes == targetDayInMinutes
              || (eventStartTotalMinutes < targetDayInMinutes
              && targetDayInMinutes < eventEndTotalMinutes);
    } else if (currentEventStartDate.getDay().equals(targetDate.getDay())) {
      // checking if start date is the same as target date day
      int eventStartTotalMinutes = currentEventStartDate.getTotalMinutes();
      return targetDayInMinutes >= eventStartTotalMinutes;
    } else if (currentEventEndDate.getDay().equals(targetDate.getDay())) {
      // checking if end date is the same as target date day
      int eventEndTotalMinutes = currentEventEndDate.getTotalMinutes();
      return targetDayInMinutes <= eventEndTotalMinutes;
    } else {
      // if target day falls in between start date day and end date day
      int targetDayInt = targetDate.getDay().ordinal();
      int startDayInt = currentEventStartDate.getDay().ordinal();
      int endDayInt = currentEventEndDate.getDay().ordinal();

      return targetDayInt > startDayInt && targetDayInt < endDayInt;
    }
  }
}
