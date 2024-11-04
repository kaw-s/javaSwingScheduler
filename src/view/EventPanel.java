package view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Component;
import java.awt.BorderLayout;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Objects;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;

import model.Date;
import model.Day;
import model.Event;
import model.EventModel;
import model.ReadonlyNUPlannerModel;
import model.User;

/**
 * Represents an event panel, which is the main UI component
 * responsible for creating an event of modifying an existing event.
 */
public class EventPanel extends JPanel {

  private final ReadonlyNUPlannerModel model;
  private EventModel event;
  private final JTextField eventNameTextField;
  private final JTextField durationTextField;
  private final JComboBox<String> locationComboBox;
  private final JTextField locationTextField;
  private final JComboBox<String> startingDayComboBox;
  private final JTextField startingTimeTextField;
  private final JComboBox<String> endingDayComboBox;
  private final JTextField endingTimeTextField;
  private final JList<String> availableUsersJList;
  private final DefaultListModel<String> availableUsersList;
  private final JList<String> invitedUsersJList;
  private final DefaultListModel<String> invitedUsersList;
  private String selectedUserId;
  private final JButton modifyEventBtn;
  private final JButton createEventBtn;
  private final JButton removeEventBtn;
  private boolean displayForScheduling;
  private final JPanel startingDayPanel;
  private final JPanel startingTimePanel;
  private final JPanel endingDayPanel;
  private final JPanel endingTimePanel;
  private final JPanel durationLabelPanel;
  private final JLabel durationLabel;
  private EventFrame parentFrame;

  /**
   * Responsible for creating a new instance of the EventPanel, which holds the functionality
   * for modifying an existing event, removing an existing event, or creating a new one
   * altogether.
   * @param model the readonly model we are working with that provides observation methods about
   *              the state of our scheduling system
   * @param event the event currently being looked at. If null, that means the
   *              user is creating a new
   *              event entirely and did not simply click on
   *              an existing event in the schedule.
   * @param displayForScheduling true if the createEventBtn should have the text "schedule event";
   *                             false if the createEventBtn should have the text "Create event"
   */
  public EventPanel(ReadonlyNUPlannerModel model, EventModel event, boolean displayForScheduling) {
    super();

    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.event = event;

    if (model == null) {
      throw new IllegalArgumentException("Model must not be null.");
    }

    this.displayForScheduling = displayForScheduling;
    this.model = model;

    this.durationTextField = new JTextField();
    this.eventNameTextField = new JTextField();
    this.locationComboBox = new JComboBox<>(new String[]{"is online", "is offline"});
    this.locationTextField = new JTextField();
    this.startingDayComboBox = new JComboBox<>(Day.getDaysArray());
    this.startingTimeTextField = new JTextField();
    this.endingDayComboBox = new JComboBox<>(Day.getDaysArray());
    this.endingTimeTextField = new JTextField();

    this.availableUsersList = new DefaultListModel<String>();
    this.invitedUsersList = new DefaultListModel<String>();
    this.addInvitedUsers();
    this.addAvailableUsers();

    this.modifyEventBtn = new JButton("Modify event");
    this.createEventBtn = new JButton("Create event");
    this.removeEventBtn = new JButton("Remove event");

    this.availableUsersJList = new JList<>(this.availableUsersList);
    this.invitedUsersJList = new JList<>(this.invitedUsersList);

    this.startingDayPanel = new JPanel();
    this.startingTimePanel = new JPanel();
    this.endingDayPanel = new JPanel();
    this.endingTimePanel = new JPanel();

    this.durationLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
    this.durationLabel = new JLabel("Duration in minutes:");

    this.createEventComponent();
    this.createLocationComponent();
    this.add(Box.createRigidArea(new Dimension(0, 5)));
    this.createStartingDayComponent();
    this.add(Box.createRigidArea(new Dimension(0, 5)));
    this.createStartingTimeComponent();
    this.add(Box.createRigidArea(new Dimension(0, 5)));
    this.createEndingDayComponent();
    this.add(Box.createRigidArea(new Dimension(0, 5)));
    this.createEndingTimeComponent();
    this.createDurationComponent();
    this.add(Box.createRigidArea(new Dimension(0, 5)));
    this.createAvailableUsersComponent();
    this.createInvitedUsersComponent();
    this.add(Box.createRigidArea(new Dimension(0, 10)));
    this.createActionButtonsComponent();
  }

  /**
   * Responsible for adding the invited users of an event to
   * this.invitedUsersList.
   */
  private void addInvitedUsers() {
    if (event != null) {
      for (String user : event.getInvitedUsers()) { // adding the invited users
        this.invitedUsersList.addElement(user);
      }
    } else {
      // if event is null, we want to ensure that the first invited user
      // is the host by default
      this.invitedUsersList.addElement(this.selectedUserId);
    }
  }

  /**
   * Responsible for adding the available users in a scheduling system to
   * this.availableUsersList.
   */
  private void addAvailableUsers() {
    for (User user : this.model.getUsers()) {
      // adding existing users to available users; if
      // user already invited to event, we skip over them
      if (!this.invitedUsersList.contains(user.getUserId())) {
        this.availableUsersList.addElement(user.getUserId());
      }
    }
  }

  /**
   * Creates an event UI component, consisting of a label and a text field
   * where the user can input the name of the event.
   */
  private void createEventComponent() {
    JPanel eventNameLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
    JLabel eventNameLabel = new JLabel("Event Name:");
    eventNameLabelPanel.add(eventNameLabel);
    this.add(eventNameLabelPanel);

    this.eventNameTextField.setText(event != null ? event.getName() : "");
    this.add(this.eventNameTextField);
  }

  private void createDurationComponent() {
    durationLabelPanel.add(durationLabel);
    this.add(durationLabelPanel);
    this.add(durationTextField);
  }

  /**
   * Creates a location UI component, which consists of a label, a
   * combo box where the user can select whether the event is online or not,
   * and a text field where the user can input the location of the event.
   */
  private void createLocationComponent() {
    JPanel locationNameLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JLabel locationNameLabel = new JLabel("Location:");
    locationNameLabelPanel.add(locationNameLabel);
    this.add(locationNameLabelPanel);

    JPanel locationInputsPanel = new JPanel();
    locationInputsPanel.setLayout(new BoxLayout(locationInputsPanel, BoxLayout.X_AXIS));
    locationInputsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

    int locationComboBoxStartingIdx = 0;
    if (event != null && !event.getOnline()) {
      locationComboBoxStartingIdx = 1;
    }

    this.locationComboBox.setSelectedIndex(locationComboBoxStartingIdx);
    this.locationTextField.setText(this.event != null ? this.event.getLocation() : "");
    locationInputsPanel.add(this.locationComboBox);
    locationInputsPanel.add(this.locationTextField);
    this.add(locationInputsPanel);
  }

  /**
   * Creates a starting day UI component, consisting of a label and a
   * combo box where the user can select the day of the week.
   */
  private void createStartingDayComponent() {
    startingDayPanel.setLayout(new BoxLayout(startingDayPanel, BoxLayout.X_AXIS));
    startingDayPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    JLabel startingDayLabel = new JLabel("Starting Day:");

    int startingDayComboBoxStartingIdx = 0;
    if (event != null) {
      startingDayComboBoxStartingIdx = Day.getIndexByDay(event.getStartDate().getDay());
    }

    this.startingDayComboBox.setSelectedIndex(startingDayComboBoxStartingIdx);
    startingDayPanel.add(startingDayLabel);
    startingDayPanel.add(this.startingDayComboBox);
    this.add(startingDayPanel);
  }

  /**
   * Creates a starting time UI component, consisting of a label
   * and a text field where the user can input the time.
   */
  private void createStartingTimeComponent() {
    startingTimePanel.setLayout(new BoxLayout(startingTimePanel, BoxLayout.X_AXIS));
    startingTimePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    JLabel startingTimeLabel = new JLabel("Starting time:");
    this.startingTimeTextField.setText(event != null ? event.getStartDate().getTime() : "");
    startingTimePanel.add(startingTimeLabel);
    startingTimePanel.add(this.startingTimeTextField);
    this.add(startingTimePanel);
  }

  /**
   * Creates the ending day UI component, consisting of a text label
   * and a combo box where the user can select the day.
   */
  private void createEndingDayComponent() {
    endingDayPanel.setLayout(new BoxLayout(endingDayPanel, BoxLayout.X_AXIS));
    endingDayPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    JLabel endingDayLabel = new JLabel("Ending Day:");

    int endingDayComboBoxStartingIdx = 0;

    if (event != null) {
      endingDayComboBoxStartingIdx = Day.getIndexByDay(event.getEndDate().getDay());
    }

    this.endingDayComboBox.setSelectedIndex(endingDayComboBoxStartingIdx);
    endingDayPanel.add(endingDayLabel);
    endingDayPanel.add(this.endingDayComboBox);
    this.add(endingDayPanel);
  }

  /**
   * Creates an ending time UI component, consisting of
   * a label a text field where the user can input the ending
   * time of this event.
   */
  private void createEndingTimeComponent() {
    endingTimePanel.setLayout(new BoxLayout(endingTimePanel, BoxLayout.X_AXIS));
    endingTimePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    JLabel endingTimeLabel = new JLabel("Ending time:");
    this.endingTimeTextField.setText(event != null ? event.getEndDate().getTime() : "");
    endingTimePanel.add(endingTimeLabel);
    endingTimePanel.add(this.endingTimeTextField);
    this.add(endingTimePanel);
  }

  /**
   * Creates an available users UI component, which displays the names of the users
   * in the system. If a user has already been invited to the event, then their
   * name will not display.
   */
  private void createAvailableUsersComponent() {
    // creating a panel for the label
    JPanel availableUsersLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
    JLabel availableUsersLabel = new JLabel("Available users");
    availableUsersLabelPanel.add(availableUsersLabel);
    this.add(availableUsersLabelPanel);

    JPanel availableUserInfoLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
    JLabel availableUserInfoLabel = new JLabel("Click on an available user to " +
            "add them to the invited users section!");
    availableUserInfoLabel.setFont(availableUserInfoLabel.getFont().deriveFont(10f));
    availableUserInfoLabelPanel.add(availableUserInfoLabel);
    this.add(availableUserInfoLabelPanel);

    JPanel invitedUserInfoLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
    JLabel invitedUserLabel = new JLabel("Click on an invited user to add them back to " +
            "the available users section!");
    invitedUserLabel.setFont(invitedUserLabel.getFont().deriveFont(10f));
    invitedUserInfoLabelPanel.add(invitedUserLabel);
    this.add(invitedUserInfoLabelPanel);

    // creating a panel for the JList that represents the available users
    JPanel availableUsersPanel = new JPanel(new BorderLayout());
    this.availableUsersJList.setAlignmentX(Component.CENTER_ALIGNMENT);
    availableUsersPanel.add(this.availableUsersJList, BorderLayout.CENTER);
    this.add(availableUsersPanel);

    this.availableUsersJList.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        String selectedUser = availableUsersJList.getSelectedValue();
        invitedUsersList.addElement(selectedUser);
        availableUsersList.removeElement(selectedUser);
        repaint();
      }
    });
  }

  private void createInvitedUsersComponent() {
    // creating a panel for the label
    JPanel invitedUsersLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
    JLabel availableUsersLabel = new JLabel("Invited users");
    availableUsersLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    invitedUsersLabelPanel.add(availableUsersLabel);
    this.add(invitedUsersLabelPanel);

    // creating a panel for the JList that represents the invited users
    JPanel invitedUsersPanel = new JPanel(new BorderLayout());
    this.invitedUsersJList.setAlignmentX(Component.CENTER_ALIGNMENT);
    invitedUsersPanel.add(this.invitedUsersJList, BorderLayout.CENTER);
    this.add(invitedUsersPanel);

    this.invitedUsersJList.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        String selectedUser = invitedUsersJList.getSelectedValue();
        availableUsersList.addElement(selectedUser);
        invitedUsersList.removeElement(selectedUser);
        repaint();
      }
    });
  }

  /**
   * Creates the actions buttons UI element, which can consist of a modify
   * event button, remove event button, and create event button. If an
   * existing event was clicked on, only the modify/remove buttons
   * will be present; otherwise the create button will be present.
   */
  private void createActionButtonsComponent() {
    JPanel actionButtons = new JPanel();

    if (this.event == null) {
      actionButtons.setLayout(new GridLayout(1, 1));
      actionButtons.add(createEventBtn);
    } else {
      actionButtons.setLayout(new GridLayout(1, 2));
      actionButtons.add(modifyEventBtn);
      actionButtons.add(removeEventBtn);
    }
    this.add(actionButtons);
  }

  private void setCreateBtnText() {
    this.createEventBtn.setText(this.displayForScheduling ? "Schedule event" : "Create event");
  }

  /**
   * Determines whether this panel should display certain UI components that are
   * necessary for scheduling an event (such as the duration text field) while
   * hiding those that are NOT necessary for scheduling an event (like start time
   * and end time, for instance).
   * @param displayForScheduling true if this panel should display UI components necessary
   *                             for scheduling an event; false otherwise (which would mean
   *                             displaying UI components necessary for all actions other than
   *                             scheduling an event)
   */
  public void setDisplayForScheduling(boolean displayForScheduling) {
    if (!displayForScheduling) {
      this.durationLabelPanel.setVisible(false);
      this.durationLabel.setVisible(false);
      this.durationTextField.setVisible(false);
    }

    if (displayForScheduling) {
      this.durationLabelPanel.setVisible(true);
      this.durationLabel.setVisible(true);
      this.durationTextField.setVisible(true);
    }

    this.displayForScheduling = displayForScheduling;
    this.setCreateBtnText();
  }

  /**
   * Sets the current userId, which is user who is currently interacting with this event panel.
   * When we reset the current user, we also want to clear all the text fields in this panel and
   * reset some UI components to their default states.
   * @param userId the name of the user who is currently interacting with this event panel.
   */
  public void setSelectedUserId(String userId, boolean displayForScheduling) {
    this.setDisplayForScheduling(displayForScheduling);

    if (!displayForScheduling) {
      this.startingDayPanel.setVisible(true);
      this.startingTimePanel.setVisible(true);
      this.endingDayPanel.setVisible(true);
      this.endingTimePanel.setVisible(true);
    }

    this.selectedUserId = userId;
    this.invitedUsersList.clear();
    this.availableUsersList.clear();
    this.addInvitedUsers();
    this.addAvailableUsers();

    // we want to reset the text and combo box values
    // to clear previously written text
    this.eventNameTextField.setText("");
    this.locationTextField.setText("");
    this.startingDayComboBox.setSelectedIndex(0);
    this.endingDayComboBox.setSelectedIndex(0);
    this.startingTimeTextField.setText("");
    this.endingTimeTextField.setText("");
    this.durationTextField.setText("");

    if (displayForScheduling) {
      this.startingDayPanel.setVisible(false);
      this.startingTimePanel.setVisible(false);
      this.endingDayPanel.setVisible(false);
      this.endingTimePanel.setVisible(false);
    }
  }

  /**
   * Sets the event in this panel by replacing it with
   * a new one that was clicked on by the user somewhere on the
   * schedule.
   * @param event Event that the user clicked on
   */
  public void setEvent(Event event) {
    this.event = event;
    this.repaint();
  }

  /**
   * Sets the parent component of this panel, which is the EventFrame. We require the
   * parent component in order to display a dialog message.
   * @param eventFrame the parent frame that is responsible for creating this event panel
   */
  public void setParentFrame(EventFrame eventFrame) {
    this.parentFrame = eventFrame;
  }

  private Event buildEvent() {
    ArrayList<String> invitedUsers = new ArrayList<>();

    for (int i = 0; i < this.invitedUsersList.getSize(); i++) {
      invitedUsers.add(this.invitedUsersList.getElementAt(i));
    }

    try {
      return new Event(
              this.eventNameTextField.getText(),
              this.locationTextField.getText(),
              Objects.equals(this.locationComboBox.getSelectedItem(), "is online"),
              new Date(Day.getDay((String) this.startingDayComboBox.getSelectedItem()),
                      this.startingTimeTextField.getText()),
              new Date(Day.getDay((String) this.endingDayComboBox.getSelectedItem()),
                      this.endingTimeTextField.getText()),
              invitedUsers
      );
    } catch (IllegalStateException | IllegalArgumentException e) {
      JOptionPane.showMessageDialog(this.parentFrame, e.getMessage());
    }

    return null;
  }

  private void setModifyBtnActionListener(Features features) {
    Event event = this.buildEvent();

    if (event != null) {
      String resultMessage = features.modifyEvent(this.event.getName(), event);

      if (!resultMessage.equals("success")) {
        JOptionPane.showMessageDialog(this.parentFrame, resultMessage);
      } else {
        this.parentFrame.dispose();
      }
    }
  }

  // acts as the action listener for creating an event
  // as well as scheduling an event
  private void setCreateEventBtnActionListener(Features features) {
    // for creating an event
    if (this.createEventBtn.getText().equals("Create event")) {
      Event event = this.buildEvent();

      if (event != null) {
        String resultMessage = features.addEvent(this.selectedUserId, event);

        if (!resultMessage.equals("success")) {
          JOptionPane.showMessageDialog(this.parentFrame, resultMessage);
        } else {
          this.parentFrame.dispose();
        }
      }
    } else {
      // for scheduling an event
      ArrayList<String> invitedUsers = new ArrayList<>();

      for (int i = 0; i < this.invitedUsersList.getSize(); i++) {
        invitedUsers.add(this.invitedUsersList.getElementAt(i));
      }

      String resultMessage = features.scheduleEvent(
              this.eventNameTextField.getText(),
              this.durationTextField.getText(),
              this.locationTextField.getText(),
              Objects.equals(this.locationComboBox.getSelectedItem(), "is online"),
              invitedUsers
      );

      if (!resultMessage.equals("success")) {
        JOptionPane.showMessageDialog(this.parentFrame, resultMessage);
      } else {
        this.parentFrame.dispose();
      }
    }
  }

  private void setRemoveEventBtnActionListener(Features features) {
    String resultMessage = features.removeEvent(this.invitedUsersList.get(0),
            this.eventNameTextField.getText());

    if (!resultMessage.equals("success")) {
      JOptionPane.showMessageDialog(this.parentFrame, resultMessage);
    } else {
      this.parentFrame.dispose();
    }
  }

  /**
   * Connects the actions that a user can perform on the view side to the controller.
   * Here, we listen to user-induced events such as button clicking, and then trigger
   * certain actions on the controller in response.
   * @param features set of features representing the different actions that a client
   *                 can perform.
   */
  public void setFeatures(Features features) {
    this.modifyEventBtn.addActionListener(evt -> this.setModifyBtnActionListener(features));
    this.createEventBtn.addActionListener(evt -> this.setCreateEventBtnActionListener(features));
    this.removeEventBtn.addActionListener(evt -> this.setRemoveEventBtnActionListener(features));
  }
}