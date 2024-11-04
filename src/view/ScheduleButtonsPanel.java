package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.ArrayList;
import model.ReadonlyNUPlannerModel;
import model.User;

/**
 * Represents a schedule buttons panel, which contains functionality
 * for switching between users' schedules, creating an event, and scheduling
 * an event.
 */
public class ScheduleButtonsPanel extends JPanel {

  private JComboBox<String> userNameComboBox;
  private final ReadonlyNUPlannerModel model;
  private final EventFrame eventFrame;
  private final JButton toggleHostColorBtn;
  private final SchedulePanel grid1;

  /**
   * Responsible for creating a new instance of ScheduleButtonsPanel, which holds functionality
   * for interacting with a given user's schedule, including toggling between different users'
   * schedule, creating a new event, and scheduling an event.
   * @param model the readonly model we are working with that provides observation methods about
   *              the state of our scheduling system
   * @param grid1 represents the schedule panel
   * @param eventFrame represents the event frame, which holds the event panel
   */
  public ScheduleButtonsPanel(ReadonlyNUPlannerModel model,
                              SchedulePanel grid1, EventFrame eventFrame) {
    super();
    if (model == null) {
      throw new IllegalArgumentException("Model must not be null.");
    }
    this.eventFrame = eventFrame;
    this.model = model;
    this.grid1 = grid1;
    this.setLayout(new GridLayout(1, 2));
    userNameComboBox = new JComboBox<>();
    this.add(this.userNameComboBox);
    initializeBox();
    JButton createEventBtn = new JButton("Create event");
    JButton scheduleEventBtn = new JButton("Schedule event");
    this.toggleHostColorBtn = new JButton("Toggle host color");
    this.add(createEventBtn);
    this.add(scheduleEventBtn);
    this.add(toggleHostColorBtn);
    this.addListenerForHostColorBtn();
    createEventBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ScheduleButtonsPanel.this.eventFrame.makeVisible();
        // setting the selected user to the name of the user who clicked on
        // the create event button
        String selectedUserId = (String) userNameComboBox.getSelectedItem();
        eventFrame.setSelectedUserId(selectedUserId, false);
      }
    });
    scheduleEventBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // setting the selected user to the name of the user who clicked
        // on the schedule event button
        ScheduleButtonsPanel.this.eventFrame.makeVisible();
        String selectedUserId = (String) userNameComboBox.getSelectedItem();
        eventFrame.setSelectedUserId(selectedUserId, true);
      }
    });
    // adding an action listener to get the user selected from the box
    this.userNameComboBox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // setting the selected user to the name of the user selected in the combo box
        String selectedUserId = (String) userNameComboBox.getSelectedItem();
        MenuBar.USER_ID = selectedUserId;
        grid1.setSelectedUserId(selectedUserId);
        eventFrame.setSelectedUserId(selectedUserId, false);
      }
    });
  }

  private void addListenerForHostColorBtn() {
    ScheduleButtonsPanel self = this;
    this.toggleHostColorBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        self.grid1.toggleScheduleHostColor();
      }
    });
  }

  private void initializeBox() {
    this.userNameComboBox.removeAllItems();
    // add default value "<none>" like the screen in assignment
    userNameComboBox.addItem("<none>");
    // Getting the users to populate the combo box
    ArrayList<User> users = model.getUsers();
    // Populate the combo box with user IDs
    for (User user : users) {
      userNameComboBox.addItem(user.getUserId());
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    if (this.userNameComboBox.getItemCount() - 1 != this.model.getUsers().size()) {
      System.out.println("here");
      this.initializeBox();
    }
  }
}
