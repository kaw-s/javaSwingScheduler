package view;

import controller.AnytimeStrategy;
import controller.NUPlannerController;
import controller.SchedulingStrategy;
import controller.WorkHoursStrategy;
import model.NUPlanner;
import model.NUPlannerModel;

/**
 * Represents the runnable class, which we can run to spin up
 * the GUI for the scheduling system.
 */
public class Main {
  /**
   * command line entry point for running the GUI. In here, we create a new instance
   * of the MainSystemFrame and pass in a readonly NU Planner model.
   * @param args command line arguments
   */
  public static void main(String[] args) {
    if (args.length == 0) {
      throw new IllegalArgumentException("Scheduling strategy must be provided.");
    }

    String schedulingStrategy = args[0];

    if (!schedulingStrategy.equals("anytime") && !schedulingStrategy.equals("workhours")) {
      throw new IllegalArgumentException("Invalid strategy type.");
    }

    boolean startOnSat = args.length == 2 && args[1].equals("startSat");

    NUPlannerModel model = new NUPlanner(true);

    SchedulingStrategy strategy = schedulingStrategy.equals("anytime")
            ? new AnytimeStrategy(model) : new WorkHoursStrategy(model);

    MainSystemFrame view = new MainSystemFrame(model, startOnSat);
    NUPlannerController controller = new NUPlannerController(view, strategy);
    controller.launch(model);
  }
}
