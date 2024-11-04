package view;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import model.ReadonlyNUPlannerModel;

/**
 * Represents the entry point to the GUI and holds the scheduling
 * system's functionality.
 */
public class MainSystemFrame extends JFrame implements IFrame, IView {
  private final EventFrame eventFrame;
  private final SchedulePanel schedulePanel;
  private final MenuBar menuBar;
  private final ScheduleButtonsPanel scheduleButtonsPanel;

  /**
   * Responsible for creating a new instance of MainSystemFrame and adding
   * components to it that allow for the user to interact with the system. Such
   * components include a schedule panel, event frame and menu bar.
   * @param model the readonly model we are working with that provides observation methods about
   *              the state of our scheduling system
   */
  public MainSystemFrame(ReadonlyNUPlannerModel model, boolean startOnSat) {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.schedulePanel = new SchedulePanel(model, startOnSat);
    this.setSize(800, 800);
    this.menuBar = new MenuBar(this);
    this.setJMenuBar(menuBar);

    this.setLayout(new BorderLayout());
    this.add(schedulePanel, BorderLayout.CENTER);
    this.eventFrame = new EventFrame(model, null);
    this.scheduleButtonsPanel =
            new ScheduleButtonsPanel(model, schedulePanel, eventFrame);
    this.add(scheduleButtonsPanel, BorderLayout.SOUTH);
    this.setVisible(true);
  }

  @Override
  public void refresh() {
    this.repaint();
    this.schedulePanel.repaint();
    this.eventFrame.refresh();
    this.menuBar.repaint();
    this.scheduleButtonsPanel.repaint();
  }

  @Override
  public void makeVisible() {
    setVisible(true);
  }

  @Override
  public void addFeatures(Features features) {
    this.eventFrame.setFeatures(features);
    this.schedulePanel.setFeatures(features);
    this.menuBar.setFeatures(features);
  }
}

