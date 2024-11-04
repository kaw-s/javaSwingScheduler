package view;

import model.NUPlanner;

/**
 * Represents an NUPlannerTextView that handles the textual rendering logic
 * of a scheduling system.
 */
public class NUPlannerTextView {
  private final NUPlanner planner;

  /**
   * Builds an NUPlannerTextView object.
   * @param planner some NUPlanner
   */
  public NUPlannerTextView(NUPlanner planner) {
    if (planner == null) {
      throw new IllegalArgumentException("Planner cannot be null.");
    }

    this.planner = planner;
  }

  @Override
  public String toString() {
    String textualView = "";

    for (int i = 0; i < this.planner.getUsers().size(); i++) {
      textualView += this.planner.getUsers().get(i).toString()
              + (i < this.planner.getUsers().size() - 1 ? "\n\n" : "");
    }

    return textualView;
  }
}
