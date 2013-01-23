package hashcalculator;

import java.awt.Toolkit;
import java.io.IOException;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;

/**
 * @author Dhruva Sagar
 * @version 1.0
 * created on Jan 23, 2013 16:55:00
 */
public class ClipboardHelper {

  private static Clipboard clipboard = null;

  /**
   * Method to copy a Trasferable Object which is inturn
   * used by the copyString method to copy the string into
   * the Clip Board. It allows other type of objects also
   * to be copied into the Clip Board.
   * created : Nov 20, 2006 8:59:21 PM
   * @param Trasferable contents : an Object of Transferable Class.
   */
  private static void copyTransferableObject (Transferable contents)
  {
    getClipboard();
    clipboard.setContents(contents, null);
  }

  /**
   * Method to get the Clip Board contents, it initializes the
   * ClipBoard object. It starts a sub thread to copy the contents.
   * created : Nov 20, 2006 8:55:21 PM
   */
  private static void getClipboard ()
  {
    // this is our simple thread that grabs the clipboard
    Thread clipThread = new Thread() {
      public void run() {
        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      }
    };

    // start the thread as a daemon thread and wait for it to die
    if (clipboard == null) {
      try {
        clipThread.setDaemon(true);
        clipThread.start();
        clipThread.join();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Method to paste an Object from the Clip Board.
   * created : Nov 20, 2006 8:59:54 PM
   * @return Object
   * @param DataFlavor flavor	: 
   * 							  
   * @throws UnsupportedFlavorException
   * @throws IOException
   */
  private static Object pasteObject (DataFlavor flavor) throws UnsupportedFlavorException, IOException
  {
    Object obj = null;
    getClipboard();

    Transferable content = clipboard.getContents(null);
    if (content != null)
      obj = content.getTransferData(flavor);

    return obj;
  }

  /**
   * Method to copy a string into the Clipboard
   * created : Nov 20, 2006 9:45:21 PM
   * @param String data : it is the String that is to be
   * 						copied into the Clip Board.
   */
  public static void copyString (String data)
  {
    copyTransferableObject(new StringSelection(data));
  }

  /**
   * Method to paste a String from the Clip Board.
   * created : Nov 20, 2006 8:59:48 PM
   * @return String
   */
  public static String pasteString ()
  {
    String data = "";
    try {
      data = (String)pasteObject(DataFlavor.stringFlavor);
    } catch (Exception e) {
      System.err.println("Error getting String from clipboard: " + e);
    }

    return data;
  }
}
