package view;

/**
 * Represents an IView, which is the main entry point to the GUI.
 * The IView is the heart of the program, and from here, users
 * can view and modify schedules accordingly. This interface also
 * defines a method that allows the view to register specific commands
 * that will be triggered by the controller when invoked by a client.
 * Some examples include saving a schedule or adding an event.
 */
public interface IView extends IFrame {
  /**
   * Ensures that the sub frames and panels of the main system get access to the features
   * and, in doing so, are able to trigger specific feature-related actions when needed.
   * @param features the set of user-induced actions that are performable on the main system.
   */
  void addFeatures(Features features);
}
