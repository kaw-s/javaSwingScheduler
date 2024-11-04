package helpers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import model.Event;
import model.User;

/**
 * Represents a helpers.FileHelper class that contains useful methods for reading and
 * writing XML files.
 */
public class FileHelper {

  /**
   * Takes an existing User and writes the contents of their events
   * to an XML file.
   * @param fileName name of file to be written to
   */
  public static void writeToFile(User user, String fileName) {
    if (user == null || fileName == null) {
      throw new IllegalArgumentException("User and fileName must not be null.");
    }

    ArrayList<Event> events = user.getEvents();

    try {
      Writer file = new FileWriter(fileName);
      file.write("<?xml version=\"1.0\"?>\n");
      file.write("<schedule id=\"" + user.getUserId() + "\">\n");

      for (Event event : events) {
        file.write("    <event>\n");
        file.write("        <name>" + event.getName() + "</name>\n");

        file.write("        <time>\n");
        file.write("            <start-day>" + event.getStartDate().getDay() + "</start-day>\n");
        file.write("            <start>" + event.getStartDate().getTime() + "</start>\n");
        file.write("            <end-day>" + event.getEndDate().getDay() + "</end-day>\n");
        file.write("            <end>" + event.getEndDate().getTime() + "</end>\n");
        file.write("        </time>\n");

        file.write("        <location>\n");
        file.write("            <online>" + event.getOnline() + "</online>\n");
        file.write("            <place>" + event.getLocation() + "</place>\n");
        file.write("        </location>\n");

        file.write("        <users>\n");
        for (String username : event.getInvitedUsers()) {
          file.write("            <uid>" + username + "</uid>\n");
        }
        file.write("        </users>\n");
        file.write("    </event>\n");
      }

      file.write("</schedule>");
      file.close();
    } catch (IOException ex) {
      throw new RuntimeException(ex.getMessage());
    }
  }

  /**
   * Retrieves the user id from an XML
   * file, (ex: schedule id="Prof. Lucia" where the id is "Prof. Lucia").
   * @param xmlDoc some xml document representing a user
   * @return the user id
   */
  public static String readUserIDFromXML(Document xmlDoc) {
    if (xmlDoc == null) {
      throw new IllegalArgumentException("xmlDoc must not be null.");
    }

    return xmlDoc.getElementsByTagName("schedule")
            .item(0)
            .getAttributes()
            .getNamedItem("id")
            .getNodeValue();
  }

  /**
   * creates an XML doc and starts up the process of reading from one.
   * @param fileName name of the file to be read
   * @return document representing an XML tree
   */
  public static Document createXMlDoc(String fileName) {
    if (fileName == null) {
      throw new IllegalArgumentException("FileName must not be null.");
    }

    try {
      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document xmlDoc = builder.parse(new File(fileName));
      xmlDoc.getDocumentElement().normalize();

      return xmlDoc;
    } catch (ParserConfigurationException ex) {
      throw new IllegalStateException("Error in creating the builder");
    } catch (IOException ioEx) {
      throw new IllegalStateException("Error in opening the file");
    } catch (SAXException saxEx) {
      throw new IllegalStateException("Error in parsing the file");
    }
  }

  /**
   * retrieves all the content inside a single event tag.
   * @param child a document element that represents an event
   * @param eventContent content within a single event element
   */
  private static void getSingleEvent(Node child, List<String> eventContent) {
    NodeList childNodes = child.getChildNodes();
    for (int grandChildIdx = 0; grandChildIdx < childNodes.getLength(); grandChildIdx++) {
      Node grandChildNode = childNodes.item(grandChildIdx);

      if (grandChildNode.getNodeType() == Node.ELEMENT_NODE) {
        Element grandChild = (Element) grandChildNode;
        eventContent.add(grandChild.getTagName() + ":" + grandChild.getTextContent());
      }
    }
  }

  /**
   * retrieves all events represented by the event tag and their contents.
   * @param childNode an event node
   * @param event hashmap representing the contents of all events
   */
  private static void getAllEvents(Node childNode, Map<String, ArrayList<String>> event) {
    if (childNode.getNodeType() == Node.ELEMENT_NODE) {
      Element child = (Element) childNode;

      ArrayList<String> eventContent = new ArrayList<>();
      event.put(child.getTagName(), eventContent);

      // <name> has no nested content associated with it, so we can skip the call to get nested
      // contents via getSingleEvent() and simply get the text contents inside <name>
      if (child.getTagName().equals("name")) {
        eventContent.add(child.getTextContent());
      } else {
        getSingleEvent(child, eventContent);
      }
    }
  }

  /**
   * parses the xml code within a schedule tag.
   * @param schedule hashmap representing the schedule
   * @param nodeList all the nodes in a file document
   */
  private static void parseXML(Map<Integer, HashMap<String, ArrayList<String>>> schedule,
                               NodeList nodeList) {
    for (int item = 0; item < nodeList.getLength(); item++) {
      Node current = nodeList.item(item);

      if (current.getNodeType() == Node.ELEMENT_NODE) {
        HashMap<String, ArrayList<String>> event = new HashMap<>();
        schedule.put(schedule.size() + 1, event);
        NodeList elemChildren = current.getChildNodes();

        for (int childIdx = 0; childIdx < elemChildren.getLength(); childIdx++ ) {
          Node childNode = elemChildren.item(childIdx);
          getAllEvents(childNode, event);
        }
      }
    }
  }

  /**
   * reads the schedule associated with a user from an XML file.
   * @param fileName name of a file associated with a user
   * @return hashmap containing schedule content
   */
  public static Map<Integer, HashMap<String,
            ArrayList<String>>> readUserScheduleFromXML(String fileName) {
    if (fileName == null) {
      throw new IllegalArgumentException("fileName must not be null.");
    }

    HashMap<Integer, HashMap<String, ArrayList<String>>> schedule = new HashMap<>();

    Document xmlDoc = createXMlDoc(fileName);
    Node tutorialsNode = xmlDoc.getElementsByTagName("schedule").item(0);
    NodeList nodeList = tutorialsNode.getChildNodes();
    parseXML(schedule, nodeList);

    return schedule;
  }
}
