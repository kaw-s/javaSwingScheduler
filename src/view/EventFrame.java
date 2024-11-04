package view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import model.Event;
import model.ReadonlyNUPlannerModel;

/**
 * Represents an EventFrame, which is the wrapping window
 * for the EventPanel, which in turn is responsible for allowing the user
 * to modify an existing event or create a new one, or even
 * remove one.
 */
public class EventFrame extends JFrame implements IFrame {

  private final EventPanel eventPanel;

  /**
   * Creates a new instance of event frame, responsible for creating the event panel and
   * adding it to this frame.
   * @param model the readonly model we are working with that provides observation methods about
   *              the state of our scheduling system
   * @param event the event currently being looked at. If null, that means the
   *              user is creating a new
   *              event entirely and did not simply click on
   *              an existing event in the schedule.
   */
  public EventFrame(ReadonlyNUPlannerModel model, Event event) {
    super();
    this.setSize(1000, 1000);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.setLayout(new BorderLayout());
    this.eventPanel = new EventPanel(model, event, false);
    this.eventPanel.setPreferredSize(new Dimension(500, 600));
    this.eventPanel.setParentFrame(this);
    this.add(this.eventPanel);
    this.pack();
  }

  @Override
  public void refresh() {
    repaint(); // acts as a refresh
    this.eventPanel.repaint();
  }

  @Override
  public void makeVisible() {
    setVisible(true);
  }

  /**
   * Sets the selected userId to the user who is responsible for
   * opening this event frame.
   * @param userId the id of the user who opened this event frame
   */
  public void setSelectedUserId(String userId, boolean displayForScheduling) {
    this.eventPanel.setSelectedUserId(userId, displayForScheduling);
  }

  /**
   * Determines whether the event panel should display the
   * UI components necessary for scheduling an event.
   * @param displayForScheduling true if the event panel should display components necessary
   *                             for scheduling an event; false otherwise
   */
  public void setDisplayForScheduling(boolean displayForScheduling) {
    this.eventPanel.setDisplayForScheduling(displayForScheduling);
  }

  /**
   * Passes down the features reference to the panel so that the panel can
   * execute methods on the controller when the client performs a particular
   * action.
   * @param features set of features representing the different actions that a client
   *                 can perform. These actions will be determined by the view, which
   *                 then delegates them off to the controller to handle.
   */
  public void setFeatures(Features features) {
    this.eventPanel.setFeatures(features);
  }
}
