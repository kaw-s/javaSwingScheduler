package view;

import java.awt.Graphics;
import java.awt.Polygon;

/**
 * An implementation of DrawEventStrategy that draws an event which ends
 * within the week shown on the event panel (does not go on to the next week
 * and lasts more than one day).
 */
public class EndsSameWeekStrategy implements DrawEventStrategy {

  @Override
  public Polygon drawEvent(Graphics g, int startDayX, int startTimeY, int endDayX, int endTimeY,
                           int width, int height) {
    Polygon eventShape = new Polygon();
    eventShape.addPoint(startDayX, startTimeY); // top-right edge start
    eventShape.addPoint(startDayX + (width), startTimeY); // top-left edge end
    eventShape.addPoint(startDayX + (width), 0); // top-left edge start
    eventShape.addPoint(endDayX + (width), 0); // top-left edge end
    eventShape.addPoint(endDayX + (width), endTimeY); // bottom-right edge start
    eventShape.addPoint(endDayX, endTimeY); // bottom-right edge end
    eventShape.addPoint(endDayX, height); // bottom-left edge start
    eventShape.addPoint(startDayX, height); // bottom-left edge end
    return eventShape;
  }
}
