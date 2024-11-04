package view;

import java.awt.Graphics;
import java.awt.Polygon;

/**
 * An implementation of DrawEventStrategy that draws an event which ends
 * on the following week (this shape fills in the rest of the event panel
 * after the start of the event).
 */
public class EndNextWeekStrategy implements DrawEventStrategy {

  @Override
  public Polygon drawEvent(Graphics g, int startDayX, int startTimeY, int endDayX, int endTimeY,
                           int width, int height) {
    Polygon eventShape = new Polygon();
    eventShape.addPoint(startDayX, startTimeY); //  top-right edge start
    eventShape.addPoint(startDayX + (width), startTimeY); // top-right edge end
    eventShape.addPoint(startDayX + (width), 0); // top-left edge start
    eventShape.addPoint(7 * (width), 0); // top-left edge end
    eventShape.addPoint(7 * (width), height); // bottom edge start
    eventShape.addPoint(startDayX, height); // bottom edge end
    return eventShape;
  }
}
