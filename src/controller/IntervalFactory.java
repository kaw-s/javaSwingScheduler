package controller;

import model.Event;

/**
 * A factory class for creating an interval given, an event.
 */

public class IntervalFactory {

  /**
   * Creates an Interval object from an event using its starting and ending
   * time information. We are converting everything into minutes, so Sunday would start at
   * 0 minutes, Monday would start at 1440 minutes, and so on. Then we add the minutes that
   * an event starts at i.e. 0100 -> totalMinutes = 60, and we are adding this to the day's
   * minutes.
   *
   * @param event the event we are creating an interval for.
   * @return An interval object that represents an events start and end time.
   */

  public static Interval getInterval(Event event) {
    long startDay = event.getStartDate().getDay().ordinal();
    long endDay = event.getEndDate().getDay().ordinal();
    long startTotalMins = event.getStartDate().getTotalMinutes();
    long endTotalMins = event.getEndDate().getTotalMinutes();

    if (startDay == endDay) {
      if (startTotalMins < endTotalMins) {
        // case 1: the day is the same exact day i.e. (this monday and this monday)
        return new Interval((startDay * 1440) + startTotalMins, (endDay * 1440) + endTotalMins);
      }
      // ends the following week
      if (startTotalMins > endTotalMins) {
        return new Interval((startDay * 1440) + startTotalMins,
                ((endDay + 7) * 1440) + endTotalMins);
      }
      // ends the following week
      else if (startDay > endDay) {
        // case 2
        return new Interval((startDay * 1440) + startTotalMins,
                ((endDay + 7) * 1440) + endTotalMins);

      }
    }
    return new Interval((startDay * 1440) + startTotalMins, (endDay * 1440) + endTotalMins);
  }
}
