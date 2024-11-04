package view;

import java.awt.Graphics;
import java.awt.Polygon;


/**
 * A strategy interface for drawing schedules based on their event.
 * This should draw a polygon based on an events start and end time. Coordinate points
 * are given to the polygon in clockwise order so each point represents a vertex of the
 * polygon. An example of ordering: top-left coordinate, top-right coordinate, bottom-right
 * coordinate, bottom-left coordinate. This would render a polygon with 4 sides.
 */
public interface DrawEventStrategy {

  /**
   * Draws an event given 4 different coordinates that represent the
   * top left point of the event, top right, bottom right, and bottom left.
   * @param g The graphics object
   * @param startDayX corresponding the x coordinate of top-right point on polygon
   * @param startTimeY corresponds to the y coordinate of top-right point on polygon
   * @param endDayX corresponds to the x coordinate of the bottom-left point
   * @param endTimeY corresponds to the y coordinate of the bottom-left point
   * @param width the width of each column in the grid
   * @param height the length of the entire grid
   * @return Polygon that represents an event
   */
  Polygon drawEvent(Graphics g, int startDayX, int startTimeY, int endDayX,
                    int endTimeY, int width, int height);
}


