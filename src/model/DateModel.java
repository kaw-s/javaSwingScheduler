package model;

/**
 * Model representing a date that must have a day of the week and a time.
 * Day is an enum that consists of the days of the week Monday -> Sunday
 * starting with an uppercase character. Time is in the army time format "0000"
 */
public interface DateModel {
  /**
   * Returns an enum representing the day of the week.
   * @return day of the week
   */
  Day getDay();

  /**
   * Returns the time of a given date.
   * @return time that a date is occurring on
   */
  String getTime();

  /**
   * Returns the total minutes of a date. Example1: "1054"
   * will return (10 * 60) + 54. Example2: "1000" will return
   * (10 * 60).
   * @return total minutes of a date
   */
  int getTotalMinutes();
}
