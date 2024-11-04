# Overview

The codebase is solving the problem of building a dynamic scheduling system
where users can create their own events/meetings and invite other users. Some assumptions
that are made include the following: every user id is unique, so no two users can ever
have the same name. Additionally, every event in a single user's schedule must be unique, so
no two events can ever have the same name. Some prerequisites include keeping "prof.xml" in the
src directory, as it will be used for testing purposes. We have also documented an invariant for the
model, which can be found at the top of the file model/NUPlanner.

## Quick Start

```java
// creating the main planner/central system 
NUPlanner planner = new NUPlanner();

// creating a user 
User userAlex = new User("Alex");

// adding the user to the existing user base 
planner.addUser(userAlex);

// creating a standalone event object
Event event = new Event("Ping Pong",
                "East Village", false,
                new Date("Tuesday", "1500"),
                new Date("Tuesday", "1600"),
                new ArrayList<>(List.of("Alex", "Mark", "James")));

// adding the event to a user by passing in the userId and the event
planner.addEvent("Alex", event);
```

## Key Components

The model NUPlanner is the main driving force of the program; through it, we can add users to
an existing user base, modify events, add events, remove events, upload XML files that
represent users, check occurring meetings at a given time, display a user's schedule, and
schedule meetings (which will likely be modified as there is not enough current information to
fully solve this problem). The model maintains a collection of all the users in the system as well, allowing
for the smooth updating of another user if required. For instance, if we call the method addEvent() on the planner,
the first argument would be the userId of the host and the second argument would be the event. We would
traverse the collection of users until a matching one is found and add the given event. We would also
traverse the collection of users to find the invitees, updating their schedules as well
by passing in the same event. Moving on from the main model, we have the User, which users are created from.
For the most part, methods in this object will be called directly from the main model, and they should never
be called directly on a User-created object. The User class does not have any knowledge of any existing
users, so we should never be calling the addEvent() method in the user class directly; if we want to add an event,
modify one, remove one, etc., we should always do so using the corresponding NUPlanner model methods. Doing so
will ensure that the addition/removal of events are done so across all the users that are part of the event,
including the host AND the invitees. The User class is driven by the NUPlanner, which controls
which methods on the NUPlanner class are called. The User class also contains a Schedule (discussed
below) which in turn contains all the events that a given user has created/is invited to. In 
addition, we also have the MainSystemFrame, which is the entry point to the view. This component 
comprises frames and panels, that all work together to display the scheduling system. 
We then have the NUPlannerController, which is responsible for delegating between the view and 
model. the controller implements an interface called Features, and has methods that will be triggered 
by the view for a specific user-induced action. 

## Key Subcomponents

Some of the subcomponents that exist are the Date, Event, Schedule, and FileHelper classes. We also
have day, which is an enum representing the days of the week, Monday -> Sunday.
The Date class essentially just imitates a timestamp, taking in a day (mon-sun) and
an army time (in the format 0000). The Event class represents an actual event that may
appear on a user's schedule; it includes fields such as a start date, end date,
a list of invitees, location, whether the event is online, and the name of the event. The Schedule
class exists to add/remove/update events on a user's schedule. The methods in Schedule
also exist in User, and we call them from the User class. The only difference is that the Schedule
methods are complete with functionality, while the methods in the User class simply exist to call
the methods in the schedule class and pass the correct arguments. For example, we have addEvent() in
both the User and Schedule class. From the method in user, we call the method on schedule, and schedule
is then responsible for handling the specific event modifications.
The FileHelper class exists mainly to provide helper methods that read and write to XML files. These methods
should only ever be called from the main model class, as the main model class is responsible
for uploading XML files and saving users to XML files. 

## Source Organization

In the root directory, we have a README.md file, a src and test directory. If you are reading
this right now, it means you have successfully located the README.md file. As for the src
directory, it holds all the functionality necessary to creating an interactive scheduling
system. In src/ we have a helpers directory, model directory, and view directory.
The helpers directory provides a file called FileHelper, which provides functionality for
writing and creating XML files; we never actually create a new instance of this class. In
the model/ directory, we have the main model called NUPlanner, which is otherwise
known as the central system. From here, we can manage the existing user base and add/remove/modify
events as necessary and update users. In the model/ directory, we also have an Event class, Date class,
User class, Day Class, and Schedule class, which all represent necessary components. In the view/ directory,
we provide
a class called NUPlannerTextView, which is responsible for rendering a textual view
of all the schedules of the users in a system. Given the above, we have created a
model/view/controller type of setup, though for now the controller is not yet created. Lastly,
we have the test/ folder, which tests implementation-specific code across all the different
components.

## Changes for part 2

We had modified the no-args constructor to take a boolean; if set to true, 
the model will upload default user schedules for testing purposes. We needed a way to 
create users by simply instantiating the model, so we thought that the best 
way of doing so was in the constructor. We added a constructor in the model that takes in a boolean; 
if true, we upload some default users; false otherwise. We also added another constructor that takes in an 
ArrayList<User> in case the person testing the program wanted to create their own set of 
users and work with that instead. We also added a new method in the model called 
doesEventConflictExist(String userId, Event event), which determines whether a name or time 
conflict exists in that user's schedule given an event. This satisfies one of the new requirements 
regarding an observation for the model. Additionally, we changed the class ArrayList fields into List 
fields, which is something that our grader for the last assignment pointed out: we should never be initializing 
fields with interface designations. 

We also created a jar file that can be run manually. This file can be found in the 
root directory of the project. It is called "homework6.jar"

When selecting a time, ensure that there is no colon. For example, instead of 08:45, 
we take in time as 0845. Similarly, instead of 13:45, we use 1345. 

## Changes for part 3

We went through all the methods in every single one of our Java files and 
ensured to change ArrayList<Object> to List<Object>. It is improper to have a parameter 
that takes in a non-interface type, so we made this fix. We also Fixed the README by adding 
a section about the controller and view in the key subcomponents section. We also removed 
some interfaces in the view that were not doing anything and had no methods defined. Additionally, 
this time we created a jar file that actually compiles with version 11. We also made a class field 
private instead of keeping it as public in SchedulePanel.java. We also added a picture of 
the scheduling frame to the source. Created a new method doesEventConflictExistForManyUsers in the model 
that checks whether a conflict exists for many different invited users. 

### Command line arguments 

"workhours" and "anytime" are valid command line arguments to set the strategy. We took
the default strings from the assignment specifications. 

### Extra Credit 

We implemented resizeable views for the main system frame. We use the paintComponent to draw grid lines 
and events in a dynamic fashion based on the current size of the schedule panel. The grid lines are drawn 
relative to the width and height of the panel. The width  of the column and height of the row are 
calculated based on the current size of the panel, so as the panel is resized, the grid lines adjust 
accordingly and maintain the proper layout. As far as events, the position and size of each 
event polygon is calculated relative to the current size of the panel; as a result, when the panel 
is resized, the events are redrawn to fit within the new dimension. 

### HOMEWORK 9 

Level 0 - We were able to get the toggle event color feature working perfectly. 

Level 1 - Our model already allows for the creation of a date starting on Saturday 7:00 
and ending Saturday 6:59. We don't really have a concept of what the "first" day of the week 
is (except for the view, which we would have to modify for level 2), but we do have a concept 
of the relative order of the week (i.e. Tuesday comes after Monday, in a cyclical pattern, which is necessary).

Level 2 - We were able to visualize the planner starting on Saturday. In order to test this, pass "startSat" as a second 
command line argument. Make sure the first one is the scheduling strategy "anytime" or "workhours". If you omit the second 
command line argument, the default is to visualize the planner starting on Monday. 

