package view;

import java.awt.Polygon;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import model.Day;
import model.Event;
import model.ReadonlyNUPlannerModel;
import model.User;

/**
 * A Schedule Panel is a JPanel that has grid lines to represent the 7 days of the week as columns
 * and the 24 hours in a day as rows. It also renders red polygons that corresponding to the time
 * that an event takes up in a given week.
 */

public class SchedulePanel extends JPanel {

  private final ReadonlyNUPlannerModel model;
  private String selectedUserId;
  private Features features;

  public boolean enableHostHasSeparateColor;

  // maps the polygons to their corresponding events
  private final Map<Polygon, Event> eventMap;
  private final boolean startOnSat;

  /**
   * Constructs a SchedulePanel from a readonly model.
   * @param model the readonly model we are working with that provides observation methods about
   *              the state of our scheduling system
   * @param startOnSat true if the schedule should start on Saturday; Monday otherwise
   */
  public SchedulePanel(ReadonlyNUPlannerModel model, boolean startOnSat) {
    super();
    this.model = model;
    this.selectedUserId = null;
    this.eventMap = new HashMap<>();
    this.enableHostHasSeparateColor = false;
    this.startOnSat = startOnSat;

    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        Event clickedEvent = clickInBounds(x, y);
        if (clickedEvent != null) {
          openEventFrame(clickedEvent);
        }
      }
    });
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g); // always do this

    if (selectedUserId != null) {
      drawEvents(g);
    }

    drawGrid(g);
  }

  /**
   * Determines whether the host's events should be distinguished
   * from all the events on a user's calendar, and also
   * repaints this schedule panel in order to re-render all the
   * events with different colors.
   */
  public void toggleScheduleHostColor() {
    this.enableHostHasSeparateColor = !this.enableHostHasSeparateColor;
    this.repaint();
  }

  private void drawGrid(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    g2.setColor(Color.BLACK);

    // 7 columns, each column represents a day of the week
    for (int i = 1; i < 8; i++) {
      g.drawLine(i * getWidth() / 7, 0, i * getWidth() / 7, getHeight());
    }

    // 24 rows, each row represents 1 hour
    for (int i = 0; i < 24; i++) {
      if (i % 4 == 0) {
        g2.setStroke(new BasicStroke(3)); // bold line every 4 hours starting with 00:00
      } else {
        g2.setStroke(new BasicStroke(1));
      }

      g.drawLine(0, i * (getHeight() / 24), getWidth(), i * (getHeight() / 24));
    }
  }

  /**
   * Sets the selection of a user from the comboBox of the panel. Repaints the panel after a new
   * userId is set.
   * @param userId the userId of the current user's schedule we are looking at
   */
  public void setSelectedUserId(String userId) {
    this.selectedUserId = userId;
    repaint();
  }

  private ArrayList<Event> getUserEvents() {
    for (User user : model.getUsers()) {
      if (user.getUserId().equals(this.selectedUserId)) {
        return user.getEvents();
      }
    }

    throw new IllegalStateException("Invalid user!");
  }

  private void drawEvents(Graphics g) {
    eventMap.clear();
    Graphics2D g2 = (Graphics2D) g;

    if (this.selectedUserId.equals("<none>")) {
      System.out.println("selected none");
    } else {
      for (Event event : this.getUserEvents()) {
        int colWidth = getWidth() / 7;
        int height = getHeight();

        int startDay = event.getStartDate().getDay().equals(Day.Saturday)
                ? 0 : Day.getIndexByDay(event.getStartDate().getDay()) + 1;
        int endDay = event.getEndDate().getDay().equals(Day.Saturday)
                ? 0 : Day.getIndexByDay(event.getEndDate().getDay()) + 1;

        int startDayX = (colWidth * startDay); // start day as x value
        int endDayX = (colWidth * endDay); //end day x value

        if (!this.startOnSat) {
          startDayX =
                colWidth * Day.getIndexByDay(event.getStartDate().getDay());
          // start day as x value
          endDayX = colWidth * Day.getIndexByDay(event.getEndDate().getDay());
          //end day x value
        }

        int startTimeY = (event.getStartDate().getTotalMinutes() * height) / 1440; // y val start
        int endTimeY = (event.getEndDate().getTotalMinutes() * height) / 1440; // y val end

        DrawEventStrategy drawing = StrategyFactory.getStrategy(startDayX, startTimeY, endDayX,
                endTimeY); // create a strategy based on the events start and end time

        Polygon eventShape = drawing.drawEvent(g, startDayX, startTimeY, endDayX, endTimeY,
                colWidth, height); // draw the event from the chosen strategy

        // Fill the polygon
        if (!this.enableHostHasSeparateColor) {
          g2.setColor(Color.RED);
        } else {
          if (event.getInvitedUsers().get(0).equals(selectedUserId)) {
            g2.setColor(Color.CYAN);
          } else {
            g2.setColor(Color.RED);
          }
        }
        g2.fillPolygon(eventShape); // fill in the polygon
        eventMap.put(eventShape, event); // put the polygon in the map with the event it represents
      }
    }
  }

  private Event clickInBounds(int x, int y) {
    for (Map.Entry<Polygon, Event> poly : this.eventMap.entrySet()) {
      Polygon key = poly.getKey();
      Event event = poly.getValue();

      if (key.contains(x, y)) {
        return event;
      }
    }

    return null;
  }

  private void openEventFrame(Event event) {
    EventFrame eventFrame = new EventFrame(this.model, event);
    eventFrame.setFeatures(this.features);
    eventFrame.setDisplayForScheduling(false);
    eventFrame.makeVisible();
  }

  public void setFeatures(Features features) {
    this.features = features;
  }
}

