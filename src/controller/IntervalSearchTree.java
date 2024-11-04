
package controller;

import java.util.ArrayList;

/**
 * A interval search tree that represents a data structure for storing and managing intervals.
 *
 */

public class IntervalSearchTree {

  /**
   * Inner class representing a node in the IntervalSearchTree.
   *
   */
  private static class ISTnode {

    Interval interval;
    ISTnode left;
    ISTnode right;


    /**
     * Constructs a new ISTnode with the given interval.
     * @param interval The interval to be stored in the node.
     */
    ISTnode(Interval interval) {
      this.interval = interval;
    }
  }

  // root node represents the range
  private ISTnode root;

  /**
   * Builds a IntervalSearchTree using a given list of intervals.
   * @param intervals The list of intervals to build the tree from.
   */

  public void buildIST(ArrayList<Interval> intervals) {
    if (intervals == null || intervals.isEmpty()) {
      return;
    }
    this.root = new ISTnode(new Interval(0, 20160)); // Initialize root node with default range
    for (Interval interval : intervals) {
      insertInterval(root, interval);
    }

  }

  private void insertInterval(ISTnode node, Interval interval) {

    if (interval.end < node.interval.start) {
      if (node.left == null) {
        node.left = new ISTnode(interval);
      } else {
        insertInterval(node.left, interval);
      }
    }
    // If interval completely to the right of the node's interval
    else if (interval.start > node.interval.end) {
      if (node.right == null) {
        node.right = new ISTnode(interval);
      } else {
        insertInterval(node.right, interval);
      }
    }
    // If interval intersects with node's interval
    else {
      // Expand node's interval to encompass the new interval
      node.interval.start = Math.min(node.interval.start, interval.start);
      node.interval.end = Math.max(node.interval.end, interval.end);

      // Recursively insert into left subtree
      if (node.left != null) {
        insertInterval(node.left,
                new Interval(interval.start, Math.min(interval.end, node.interval.start)));
      }

      // Recursively insert into right subtree
      if (node.right != null) {
        insertInterval(node.right,
                new Interval(Math.max(interval.start, node.interval.end), interval.end));
      }
    }
  }

  /**
   * Finds the earliest free interval of the specified duration within the given range.
   * @param duration The duration of the interval to find.
   * @return The earliest free interval of the specified duration within the range.
   */

  public Interval findEarliestFreeInterval(long duration) {
    return findEarliestFreeInterval(root, duration, 1980, 8220); // setting values
    // for a specific range, 1980 shouod be monday at 9am, and 8220 should be friday at 5pm
  }

  private Interval findEarliestFreeInterval(ISTnode node, long duration, long minStart,
                                            long maxEnd) {
    // If the current node is null, return an interval starting at 1980
    if (node == null) {
      return new Interval(minStart, minStart + duration);
    }

    long nodeStart = Math.max(node.interval.start, minStart);
    long nodeEnd = Math.min(node.interval.end, maxEnd);

    if (nodeEnd - nodeStart >= duration) {
      return new Interval(nodeStart, nodeStart + duration);
    }

    else if (node.left == null || node.left.interval.end < duration) {
      return new Interval(nodeEnd, nodeEnd + duration);
    }

    else {
      return findEarliestFreeInterval(node.left, duration, minStart, nodeStart);
    }
  }
}