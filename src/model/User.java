package model;

import java.util.ArrayList;

/**
 * Represents a User in a scheduling-based system. At the moment, User class does not
 * really have a clear purpose. Its main objective is to delegate tasks by calling
 * methods in the Schedule class. User class holds no true logic, but Schedule class does.
 * If we were to test both Schedule and User class, we would have duplicate test code, as
 * all User class does is call the same methods in Schedule. Therefore, we have decided to
 * test the methods in User class more than those than in Schedule. Schedule.addEvent() does the
 * same as User.addEvent(), as User.addEvent() calls Schedule.addEvent().
 * And Schedule.removeEvent() does the same as User.removeEvent(), as User.removeEvent()
 * calls Schedule.removeEvent().
 */
public class User implements UserModel {
  private final String userId;
  private final Schedule schedule;

  /**
   * Creates a new user object with a userId that represents a user.
   * Every userId should be unique in the sense that no two users can share the same
   * userId. The userId should match the userId of the schedule id in an XML file. The uniqueness
   * of the userId is not validated through here, but rather in NUPlanner, whose addUser() method
   * checks to see if an existing user has the same id as the user attempting to be added.
   * @param userId unique identifier of user
   */
  public User(String userId) {
    if (userId == null) {
      throw new IllegalArgumentException("Name and events cannot be null.");
    } else if (userId.isEmpty()) {
      throw new IllegalArgumentException("Name cannot be an empty String.");
    }

    this.userId = userId;
    this.schedule = new Schedule(userId);
  }

  @Override
  public String getUserId() {
    return this.userId;
  }

  @Override
  public void addEvent(Event e) {
    this.schedule.addEvent(e);
  }

  @Override
  public ArrayList<Event> getEvents() {
    return this.schedule.getEvents();
  }

  @Override
  public Schedule getSchedule() {
    return this.schedule;
  }

  @Override
  public void removeEvent(String eventName) {
    this.schedule.removeEvent(eventName);
  }

  @Override
  public String toString() {
    // we do not technically need to have toString() here, as printing the user
    // schedule should purely be a power that only the schedule possesses. However,
    // we have also made it so that this.toString() prints the user schedule. Printing the
    // schedule will result in the userId being printed as well as all the events the user
    // is invited to. The userId and the schedule are all components of the User class as well
    // as the schedule class, which is why we think it would make sense that the User.toString()
    // method should yield the same result as Schedule.toString(). In our tests, we call this
    // method to test the textual rendering instead of Schedule.toString(), though we could
    // easily do that as well. To avoid testing the same method twice, we just test this one.
    return this.schedule.toString();
  }
}


