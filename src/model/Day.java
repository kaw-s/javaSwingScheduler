package model;

/**
 * Enum representing the different days of the week.
 */
public enum Day {
  Sunday,
  Monday,
  Tuesday,
  Wednesday,
  Thursday,
  Friday,
  Saturday;

  /**
   * Returns the day of the week numerically. For instance, if Sunday
   * is passed into this method, 0 will be returned; if Monday is passed in,
   * 1 will be returned; 2 for Tuesday; 3 for Wednesday, until we get to 6 for Sunday.
   * @param day the day of the week
   * @return an index from 0-6 representing the day of the week numerically
   * @throws IllegalArgumentException if day is invalid or null
   */
  public static int getIndexByDay(Day day) {
    if (day == null) {
      throw new IllegalArgumentException("Invalid day");
    }

    for (int i = 0; i < Day.values().length; i++) {
      if (Day.values()[i].equals(day)) {
        return i;
      }
    }

    throw new IllegalArgumentException("Invalid day");
  }

  /**
   * Builds an array of the days of the week.
   * @return array consisting of the days of the week
   */
  public static String[] getDaysArray() {
    String[] daysArray = new String[Day.values().length];

    for (int i = 0; i < Day.values().length; i++) {
      daysArray[i] = Day.values()[i].toString();
    }

    return daysArray;
  }

  /**
   * Takes in a day String and returns a Day enum value.
   * @param day the day of the week
   * @return Enum representing the day
   */
  public static Day getDay(String day) {
    for (int i = 0; i < Day.values().length; i++) {
      if (Day.values()[i].toString().equals(day)) {
        return Day.values()[i];
      }
    }

    throw new IllegalArgumentException("Invalid day");
  }
}

