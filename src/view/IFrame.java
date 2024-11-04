package view;


/**
 * Defines the behaviors that all frames should be capable of, including refreshing the view
 to reflect any changes in the system state and making the view visible to spin up the system.
 Implementations of this interface should ensure that the refresh() method updates the frame's
 content to accurately represent the current state of the system, while the makeVisible() method
 should be responsible for displaying the frame on the screen to initialize the system interface.
 */
public interface IFrame {
  /**
   * Refresh the view to reflect any changes in the system state.
   */
  void refresh();

  /**
   * Make the view visible to spin up the system.
   */
  void makeVisible();
}


