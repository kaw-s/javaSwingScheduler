package view;

import java.awt.Graphics;
import java.awt.Polygon;

/**
 * An implementation of DrawEventStrategy that draws an event which ends on the
 * same day that it starts.
 */
public class EndsSameDay implements DrawEventStrategy {

  @Override
  public Polygon drawEvent(Graphics g, int startDayX, int startTimeY, int endDayX, int endTimeY,
                           int width, int height) {
    Polygon eventShape = new Polygon();
    eventShape.addPoint(startDayX, startTimeY); // top-right
    eventShape.addPoint(startDayX + (width), startTimeY); // top-left
    eventShape.addPoint(startDayX + (width), endTimeY); // bottom-left
    eventShape.addPoint(startDayX, endTimeY); // bottom-right
    return eventShape;
  }
}
