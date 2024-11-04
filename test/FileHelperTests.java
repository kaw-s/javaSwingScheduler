import model.NUPlanner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import helpers.FileHelper;
import model.Date;
import model.Day;
import model.Event;
import model.User;

/**
 * Tests methods in FileHelper class.
 */
public class FileHelperTests {
  private String expectedStringXML;

  @Before
  public void setUp() {
    this.expectedStringXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" "
            + "standalone=\"no\"?><schedule id=\"Mike\">\n"
            + "    <event>\n"
            + "        <name>\"Event 2\"</name>\n"
            + "        <time>\n"
            + "            <start-day>Tuesday</start-day>\n"
            + "            <start>0900</start>\n"
            + "            <end-day>Tuesday</end-day>\n"
            + "            <end>0930</end>\n"
            + "        </time>\n"
            + "        <location>\n"
            + "            <online>false</online>\n"
            + "            <place>Churchill</place>\n"
            + "        </location>\n"
            + "        <users>\n"
            + "            <uid>Mike</uid>\n"
            + "        </users>\n"
            + "    </event>\n"
            + "    <event>\n"
            + "        <name>\"Event 1\"</name>\n"
            + "        <time>\n"
            + "            <start-day>Tuesday</start-day>\n"
            + "            <start>1000</start>\n"
            + "            <end-day>Tuesday</end-day>\n"
            + "            <end>1200</end>\n"
            + "        </time>\n"
            + "        <location>\n"
            + "            <online>false</online>\n"
            + "            <place>Churchill</place>\n"
            + "        </location>\n"
            + "        <users>\n"
            + "            <uid>Mike</uid>\n"
            + "        </users>\n"
            + "    </event>\n"
            + "</schedule>";
  }

  @Test
  public void testWriteToFile() throws Exception {
    User userMike = new User("Mike");

    Event e1 = new Event("\"Event 1\"",
            "Churchill", false,
            new Date(Day.Tuesday, "1000"),
            new Date(Day.Tuesday, "1200"),
            new ArrayList<>(List.of("Mike")));

    Event e2 = new Event("\"Event 2\"",
            "Churchill", false,
            new Date(Day.Tuesday, "0900"),
            new Date(Day.Tuesday, "0930"),
            new ArrayList<>(List.of("Mike")));

    NUPlanner planner = new NUPlanner(false);
    planner.addUser(userMike);
    planner.addEvent("Mike", e1);
    planner.addEvent("Mike", e2);
    planner.saveUserToXML("Mike");

    File file = new File("Mike.xml");

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document document = builder.parse(file);

    DOMSource dom = new DOMSource(document);
    Transformer transformer = TransformerFactory.newInstance()
            .newTransformer();
    StringWriter stringWriter = new StringWriter();
    transformer.transform(dom, new StreamResult(stringWriter));

    Assert.assertEquals(this.expectedStringXML, stringWriter.toString());
  }

  @Test
  public void testReadUserScheduleFromXMLWithNullArgument() {
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      FileHelper.readUserScheduleFromXML(null);
    });
  }

  @Test
  public void testCreateXMlDocWithNullArgument() {
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      FileHelper.createXMlDoc(null);
    });
  }

  @Test
  public void testReadUserIDFromXMLWithNullArgument() {
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      FileHelper.readUserIDFromXML(null);
    });
  }

  @Test
  public void testWriteToFileWithNullArgument() {
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      FileHelper.writeToFile(null, "fileName");
    });

    Assert.assertThrows(IllegalArgumentException.class, () -> {
      FileHelper.writeToFile(new User("Alex"), null);
    });

    Assert.assertThrows(IllegalArgumentException.class, () -> {
      FileHelper.writeToFile(null, null);
    });
  }
}

