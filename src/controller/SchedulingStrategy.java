package controller;

import java.util.List;

import model.Event;

/**
 * Represents the interface for a SchedulingStrategy and defines the
 * methods (in this case, we only have one method)
 * that all scheduling strategies should be capable of.
 */
public interface SchedulingStrategy {

  /**
   * Finds an event in a given time block where all invited users can attend (i.e.
   * all invited users have no conflicts, whether that be event name conflicts or
   * time conflicts).
   * @param eventName name of the event
   * @param duration duration of the event (in minutes)
   * @param location location of the event
   * @param online whether the event is online or not
   * @param invitedUsers list of invited users
   * @return the event, if no users have conflicts; null otherwise
   */
  Event findEvent(String eventName, String duration, String location, boolean online,
                  List<String> invitedUsers);
}
