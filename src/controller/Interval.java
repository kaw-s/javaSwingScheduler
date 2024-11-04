package controller;

/**
 * Represents a time interval. Where start is the
 * begining of the interval and end is the ending.
 */
public class Interval {
  long start;
  long end;

  /**
   * Constructs an interval from the start and end times.
   * @param start the begining of an interval.
   * @param end the ending of an interval.
   */

  public Interval(long start, long end) {
    this.start = start;
    this.end = end;
  }

  /**
   * Determines wether another interval overlaps with this interval.
   * @param other the interval being examined.
   * @return true of there is an overlap.
   */
  public boolean overlaps(Interval other) {
    // Check if either interval starts within the other interval
    // or if the other interval starts within this interval
    return (this.start <= other.end && this.start >= other.start) ||
            (other.start <= this.end && other.start >= this.start);
  }


}
