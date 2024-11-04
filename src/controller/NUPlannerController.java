package controller;

import java.util.List;

import model.Event;
import model.NUPlannerModel;
import view.Features;
import view.IView;

/**
 * Represents an NUPlannerController, which is responsible for processing
 * client-side actions on the GUI side and delegating them to the NUPlannerModel.
 */

public class NUPlannerController implements Features {
  private final IView mainSystemFrame;
  private NUPlannerModel model;
  private SchedulingStrategy schedulingStrategy;

  /**
   * Creates a new instance of the NUPlannerController with the provided main system frame view
   * and scheduling strategy, which is responsible for delegating user-induced actions
   * between the view and model.
   *
   * @param mainSystemFrame the main system frame view associated with the controller
   * @param schedulingStrategy the scheduling strategy to be used by the controller
   * @throws IllegalArgumentException if either mainSystemFrame or schedulingStrategy is null
   */
  public NUPlannerController(IView mainSystemFrame,
                             SchedulingStrategy schedulingStrategy) {
    if (mainSystemFrame == null || schedulingStrategy == null) {
      throw new IllegalArgumentException("mainSystemFrame and schedulingStrategy cannot be null.");
    }

    this.mainSystemFrame = mainSystemFrame;
    this.schedulingStrategy = schedulingStrategy;
  }

  /**
   * Launches the NUPlannerController by linking it with the specified NUPlannerModel.
   * This method initializes the controller with the provided model and allows interaction
   * with the main system frame view by adding necessary features. This method must
   * be called in order for feature methods to delegate back to the view.
   *
   * @param model the NUPlannerModel to be associated with this controller
   * @throws IllegalArgumentException if the provided model is null
   */
  public void launch(NUPlannerModel model) {
    if (model == null) {
      throw new IllegalArgumentException("model cannot be null");
    }

    this.model = model;
    this.mainSystemFrame.addFeatures(this);
  }

  /**
   * Sets the scheduling strategy and allows the client to configure, at run time,
   * the strategy to be executed when an event is scheduled. Strategy can either
   * be anytime or workhours.
   * @param schedulingStrategy scheduling strategy to be executed at runtime
   */
  public void setSchedulingStrategy(SchedulingStrategy schedulingStrategy) {
    if (schedulingStrategy == null) {
      throw new IllegalArgumentException("schedulingStrategy cannot be null.");
    }

    this.schedulingStrategy = schedulingStrategy;
  }

  @Override
  public String modifyEvent(String oldEventName, Event modifiedEvent) {
    if (this.model == null) {
      throw new IllegalArgumentException("Model must not be null.");
    } else if (oldEventName == null || modifiedEvent == null) {
      throw new IllegalArgumentException("oldEventName and modifiedEvent must not be null.");
    }

    try {
      this.model.modifyEvent(oldEventName, modifiedEvent);
      this.mainSystemFrame.refresh();
      return "success";
    } catch (IllegalStateException | IllegalArgumentException e) {
      return e.getMessage();
    }
  }

  @Override
  public String addEvent(String userId, Event event) {
    if (this.model == null) {
      throw new IllegalArgumentException("Model must not be null.");
    } else if (userId == null || event == null) {
      throw new IllegalArgumentException("userId and event must not be null.");
    }

    try {
      this.model.addEvent(userId, event);
      this.mainSystemFrame.refresh();
      return "success";
    } catch (IllegalStateException | IllegalArgumentException e) {
      return e.getMessage();
    }
  }

  @Override
  public String removeEvent(String userId, String eventName) {
    if (this.model == null) {
      throw new IllegalArgumentException("Model must not be null.");
    } else if (userId == null || eventName == null) {
      throw new IllegalArgumentException("userId and eventName must not be null");
    }

    try {
      this.model.removeEvent(userId, eventName);
      this.mainSystemFrame.refresh();
      return "success";
    } catch (IllegalStateException | IllegalArgumentException e) {
      return e.getMessage();
    }
  }

  @Override
  public String scheduleEvent(String eventName, String duration, String location,
                              boolean online, List<String> invitedUsers) {
    if (this.model == null) {
      return "Model must not be null.";
    } else if (eventName == null || duration == null || location == null || invitedUsers == null) {
      throw new IllegalArgumentException("eventName, duration, location, "
              + "and invitedUsers must not be null.");
    }

    try {
      Event event = this.schedulingStrategy.findEvent(eventName,
              duration, location, online, invitedUsers);

      if (event != null) {
        this.model.addEvent(event.getInvitedUsers().get(0), event);
        this.mainSystemFrame.refresh();
        return "success";
      } else {
        return "Unable to find time that works for every invitee.";
      }
    } catch (IllegalStateException | IllegalArgumentException e) {
      return e.getMessage();
    }
  }

  @Override
  public String saveUserToXML(String userId) {
    if (this.model == null) {
      throw new IllegalArgumentException("Model must not be null.");
    } else if (userId == null) {
      throw new IllegalArgumentException("userId must not be null.");
    }

    try {
      this.model.saveUserToXML(userId);
      this.mainSystemFrame.refresh();
      return "success";
    } catch (IllegalStateException | IllegalArgumentException e) {
      return e.getMessage();
    }
  }

  @Override
  public String uploadXMLFile(String path) {
    if (this.model == null) {
      throw new IllegalArgumentException("Model must not be null.");
    } else if (path == null) {
      throw new IllegalArgumentException("path must not be null.");
    }

    try {
      this.model.uploadXMLFile(path);
      this.mainSystemFrame.refresh();
      return "success";
    } catch (Exception e) {
      // can include a variety of file parsing exceptions and conflict exceptions.
      // Not worth it to catch every single type of exception
      return e.getMessage();
    }
  }
}
