package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Represents a menu bar, consisting of menu items that have the ability
 * to either upload a new calendar to the system or save an existing one.
 */
public class MenuBar extends JMenuBar {
  private final JMenuItem addCalendarMenuItem;
  private final JMenuItem saveCalendarMenuItem;
  private Features features;
  private JFrame parentFrame;

  public static String USER_ID;

  /**
   * Responsible for creating an instance of the MenuBar,
   * adding menu items that have the functionality of
   * adding a calendar and saving a calendar.
   */
  public MenuBar(JFrame parentFrame) {
    // creating a file bar that will contain the menu items
    JMenu fileBar = new JMenu("File");
    this.add(fileBar);
    this.parentFrame = parentFrame;

    // creating the menu items
    this.addCalendarMenuItem = new JMenuItem("Add calendar");
    this.saveCalendarMenuItem = new JMenuItem("Save calendar");

    // setting the action listeners
    this.setActionAddCalendarMenuItem();
    this.setActionSaveCalendarMenuItem();

    fileBar.add(this.addCalendarMenuItem);
    fileBar.add(this.saveCalendarMenuItem);
  }

  /**
   * Responsible for creating the action listener for the add
   * calendar menu item.
   */
  private void setActionAddCalendarMenuItem() {
    this.addCalendarMenuItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        getPath(false);
      }
    });
  }

  /**
   * Responsible for creating the action listener for the save calendar
   * menu item.
   */
  private void setActionSaveCalendarMenuItem() {
    this.saveCalendarMenuItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        getPath(true);
      }
    });
  }

  /**
   * Opens up a file choose window that allows the user
   * to specify a path to an XML file or a folder.
   * @param onlyAllowFolderPath if set to true, user can only select a folder.
   *                            This would be in the case that the user
   *                            wants to save the XML to a directory.
   */
  private void getPath(boolean onlyAllowFolderPath) {
    String currentDirectory = System.getProperty("user.dir");
    JFileChooser fileChooser = new JFileChooser(currentDirectory);

    if (onlyAllowFolderPath) {
      fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }

    int status = fileChooser.showOpenDialog(MenuBar.this);

    if (status == JFileChooser.APPROVE_OPTION) {
      if (onlyAllowFolderPath) {
        String resultMessage = this.features.saveUserToXML(USER_ID);
        if (!resultMessage.equals("success")) {
          JOptionPane.showMessageDialog(this.parentFrame, resultMessage);
        }
      } else {
        // user uploading
        String resultMessage = this.features.uploadXMLFile(fileChooser.getSelectedFile().getPath());
        if (!resultMessage.equals("success")) {
          JOptionPane.showMessageDialog(this.parentFrame, resultMessage);
        }
      }
    } else {
      System.out.println("User cancelled selection.");
    }
  }



  public void setFeatures(Features features) {
    this.features = features;
  }
}

