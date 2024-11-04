package view;

/**
 * A factory class that creates a strategy determined by the start and end of an event.
 */
public class StrategyFactory {

  /**
   * Create one of 3 strategies  based on which case the event is.
   * The 3 event cases are as follows:
   * 1. event starts and ends on the same day
   * 2. event ends on the following week
   * 3. event ends on the current week
   * @param startDayX start day value
   * @param startTimeY start time value
   * @param endDayX end day value
   * @param endTimeY end time value
   * @return DrawEvent strategy that corresponds with the type of event
   */
  public static DrawEventStrategy getStrategy(int startDayX, int startTimeY, int endDayX,
                                              int endTimeY) {
    // where an event starts and ends on the same day of the week (Mon, tues, wed, thurs)
    if (startDayX == endDayX) {
      if (startTimeY < endTimeY) {
        // case 1: the day is the same exact day i.e. (this monday and this monday)
        return new EndsSameDay();
      } if (startTimeY > endTimeY) {
        // case 2: the end day is the following week i.e. (this monday and NEXT monday)
        return new EndNextWeekStrategy();
      }
    } else if (startDayX > endDayX) {
      // case 2
      return new EndNextWeekStrategy();
    }
    // case 3
    return new EndsSameWeekStrategy();
  }
}
