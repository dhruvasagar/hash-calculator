/**
 * <pre>Hash Calculator is a small utility which helps one to
 * calculate hash of an input text or a selected file.
 * On the internet as it is now becoming a more and more
 * popular method of validating the contents of downloaded
 * files and their contents, this tool helps do so.
 * 
 * It is a single class extends JFrame and is hence a 
 * swing component, hence will run on any machine that
 * has JVM.
 * 
 * It has been compiled using OpenJDK 1.6.0_24
 * 
 * For any queries or reporting any bugs please contact
 * Dhruva Sagar at dhruva[DOT]sagar[AT]gmail[DOT]com.
 *
 * Contact information and help is also been attached
 * along this to help the user.</pre>
 */

package hashcalculator;

import java.awt.Font;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.GridBagConstraints;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JTree;
import javax.swing.JMenu;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;
import javax.swing.JMenuItem;
import javax.swing.ImageIcon;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JFileChooser;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;

import java.util.concurrent.Future;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ExecutionException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;

import hashcalculator.Images;
import hashcalculator.utils.Logger;
import hashcalculator.CalculateThread;
import hashcalculator.components.MenuBar;
import hashcalculator.components.ToolBar;
import hashcalculator.utils.ClipboardHelper;
import hashcalculator.components.ModeDialog;
import hashcalculator.components.HelpContent;
import hashcalculator.components.SplashScreen;

/**
 * @author Dhruva Sagar
 * @version 1.3
 * created on Nov 20, 2006 9:01:47 PM
 */
public class HashCalculator extends JFrame {

  private static final long serialVersionUID=0L; 

  private static final String version = "1.2";

  private JLabel status;
  private ToolBar toolBar;

  private MenuBar menuBar;
  private JLabel enterText;
  private JButton calcHash;
  private JButton chooseFile;
  private JTextField hashText;
  private JTextField userText;
  private Component me = this;
  private Container contentPane;
  private ModeDialog modeDialog;
  private JFileChooser fileChooser;
  private JProgressBar statusProgressBar;
  private final JPopupMenu myContextMenu = new JPopupMenu();
  private final JPopupMenu textFieldContextMenu= new JPopupMenu();

  private String hex = "";
  private boolean textMode = true;
  private final Hashtable<String, String> messages=new Hashtable<String, String>();

  public String getVersion() {
    return version;
  }

  public boolean isTextMode() {
    return textMode;
  }

  public void setTextMode(boolean value) {
    this.textMode = value;
  }

  public void setTextMode(boolean value, boolean updateUI) {
    setTextMode(value);

    if( updateUI ) {
      setStatus((isTextMode() ? "Text" : "File") + " Mode");
      setTitle("The - " + getAlgorithm() + " - Hash Calculator : " + (isTextMode() ? "Text" : "File") + " Mode");
      chooseFile.setVisible(!isTextMode());
      clear();
    }
  }

  public void setStatusProgressBarValue(int value) {
    this.statusProgressBar.setValue(value);
  }

  public void updateStatusProgressBar() {
    this.statusProgressBar.update(this.statusProgressBar.getGraphics());
  }

  public String getAlgorithm() {
    return toolBar.getAlgorithm();
  }

  /**
   * The Constructor where all the GUI is build and added.
   * created on Nov 20, 2006 8:53:26 PM
   */
  public HashCalculator() {
    setTitle("The - MD2 - Hash Calculator : "  +  ((textMode)?"Text":"File")  +  " Mode");
    setSize(600,200);
    setResizable(false);
    setDefaultLookAndFeelDecorated(true);
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    setLocation( (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()-getSize().getWidth())/2 , (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()-getSize().getHeight())/2 );
    setIconImage(Images.iconGif.getImage());
    addWindowListener(new WindowAdapter() {
      public void windowActivated(WindowEvent e) {
        userText.requestFocusInWindow();
      }
      public void windowClosing(WindowEvent arg0) {
        exit();
      }						
    });

    contentPane=getContentPane();
    contentPane.setLayout(new BorderLayout());
    setMyMenuBar();
    setMyToolBar();
    setMyContextMenu();
    setMessages();

    JPanel lc=new JPanel();
    lc.setLayout(new GridLayout(2,1));
    enterText=new JLabel("Enter the text for hashing: ");
    lc.add(enterText);

    calcHash=new JButton("Caculate Hash", Images.calcGif);
    calcHash.addActionListener(new ActionListener()	{
      public void actionPerformed(ActionEvent ae) {
        calculate();
      }
    });
    calcHash.setMnemonic('C');
    calcHash.setToolTipText("Click to calculate the " + getAlgorithm() + " Hash");
    lc.add(calcHash);
    contentPane.add(lc,BorderLayout.WEST);
    getRootPane().setDefaultButton(calcHash);

    JPanel uh=new JPanel();
    uh.setLayout(new GridLayout(2,1));

    JPanel sel=new JPanel();
    sel.setLayout(new BoxLayout(sel, BoxLayout.X_AXIS));

    userText=new JTextField(30);
    userText.setEditable(textMode);
    userText.setFont(new Font("helvetica", Font.BOLD, 12));
    userText.setToolTipText("Enter the text to be hashed");
    setTextFieldContextMenu();
    userText.add(textFieldContextMenu);
    sel.add(userText);

    chooseFile=new JButton("...");
    chooseFile.setToolTipText("Click to select the File");
    chooseFile.setVisible(!textMode);
    chooseFile.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        selectFile();
      }
    });
    sel.add(chooseFile);
    uh.add(sel);

    hashText=new JTextField(30);
    hashText.setEditable(false);
    hashText.setToolTipText("The calculated Hash is displayed here");
    hashText.setFont(new Font("helvetica", Font.BOLD, 12));
    hashText.addMouseListener(new MouseAdapter(){
      public void mousePressed(MouseEvent e) { checkPopup(e); }
      public void mouseClicked(MouseEvent e) { checkPopup(e); }
      public void mouseReleased(MouseEvent e) { checkPopup(e); }
      private void checkPopup(MouseEvent e) {
        if (e.isPopupTrigger(  )) {
          menuBar.setItemsEnabled();
          setMyContextMenuEnabled();
          myContextMenu.show(e.getComponent(  ), e.getX(  ), e.getY(  ));
        }
      }
    });
    uh.add(hashText);

    contentPane.add(uh,BorderLayout.EAST);

    JPanel statusBar=new JPanel();
    statusBar.setLayout(new BoxLayout(statusBar,BoxLayout.X_AXIS));

    status=new JLabel();
    status.setHorizontalAlignment(JTextField.CENTER);
    status.setFont(new Font("Arial",Font.BOLD,12));
    status.setText("Hash Calculator v1.0.0");

    statusProgressBar=new JProgressBar();		
    statusProgressBar.setMinimum(0);
    statusProgressBar.setMaximum(100);
    statusProgressBar.setToolTipText("Indicates Progress of Calculating Hash");

    JSplitPane spl=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,status,statusProgressBar);
    spl.setDividerSize(1);
    spl.setDividerLocation(500);		

    statusBar.add(spl);

    contentPane.add(statusBar,BorderLayout.SOUTH);
  }

  /**
   * Method for selecting a file.
   * created : Nov 30, 2006 7:57:27 PM
   */
  public void selectFile()
  {
    fileChooser=new JFileChooser();
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    int option=fileChooser.showOpenDialog(me);
    if(option==JFileChooser.APPROVE_OPTION && (fileChooser.getSelectedFile()!=null))
      userText.setText(fileChooser.getSelectedFile().getAbsolutePath());
  }

  /**
   * Main method that calculates the Hash.
   * created : Nov 20, 2006 8:55:14 PM
   */
  public void calculate()
  {
    final ExecutorService service;
    final Future<String> calculable;

    service = Executors.newFixedThreadPool(1);
    calculable = service.submit(new Calculable(this));

    try {
      //calcThread.setDaemon(true);
      //calcThread.start();
      //calcThread.join();
      hex = calculable.get();
    } catch (Exception e) {
      e.printStackTrace();
    }
    setExtendedState(JFrame.NORMAL);
    toFront();
  }

  /**
   * Method to clear the Hash Calculator screen's components.
   * created : Nov 20, 2006 9:02:47 PM
   */
  public void clear() {
    userText.setText("");
    hashText.setText("");
    menuBar.setItemsEnabled();
    toolBar.setButtonsEnabled();
    statusProgressBar.setValue(0);
    setTextFieldContextMenuEnabled();
    status.setText((textMode)?"Enter Text in Text Field to calculate Hash":"Select the file to be Hashed");					
  }

  /**
   * Method to copy the calculated Hash into Clip Board.
   * created : Nov 20, 2006 9:02:43 PM
   */
  public void copyHash() {
    if(!userText.getText().equals("")) {
      ClipboardHelper.copyString(hex);
      status.setText("Hash Copied to Clip Board");
    } else {
      status.setText("Enter Text in Text Field to calculate Hash first");
    }
  }

  /**
   * Method to exit the Application, if the user confirms.
   * created : Nov 20, 2006 9:02:06 PM
   */
  public void exit() {
    int option=JOptionPane.showConfirmDialog(me,"Are you sure you want to exit?","Confirmation",JOptionPane.OK_CANCEL_OPTION);
    if(option==JOptionPane.OK_OPTION) {
      dispose();System.exit(0);
    } else {
      status.setText("Cancelled Exit"); 		
    }
  }

  /**
   * Method to get information about Hash Calculator.
   * created : Nov 20, 2006 9:02:50 PM
   */
  public void getAboutHC() {
    JOptionPane.showMessageDialog(me, "<html>" +
        "<h1>" +
          "<font face='Comic Sans MS' color='blue' size='5'>" +
            "<center>Hash Calculator " + version + "</center>" +
          "</font>" +
        "</h1>" +
        "<br><hr>" +
        "<center>" +
          "It calculates the <br> MD2 <br> MD5 <br> SHA-1 <br> SHA-256 <br> SHA-384 <br> SHA-512 <br>" +
        "</center>" +
        "Hash of a input text as Selected from the Combo Box<br>" +
        "in the screen and displays it in the following text field.<br><br>" +
        "It automatically copies the hash into the Clip Board.<br><br>" +
        "<center>" +
          "Made By:<br>" +
          "<font size='4' color='red'>Dhruva Sagar</font>" +
          "</center><br>" +
        "</html>", "About Hash Calculator", JOptionPane.PLAIN_MESSAGE, Images.md5Gif);
  }

  /**
   * Method to get my contact information.
   * created : Nov 20, 2006 9:02:36 PM
   */
  public void getContactInfo()
  {
    JOptionPane.showMessageDialog(me, "<html>" +
        "<center>" +
          "<table border='0' cellspacing='0' cellpadding='0'>" +
            "<td>" +
              "<font face='Comic Sans MS'>" +
                "Contact <font color='red'>Dhruva Sagar</font> at:<br>" +
              "</font>" +
              "<a href=\"mailto:dhruva.sagar@gmail.com?Subject=Hello dude, Here are my Comments\">dhruva.sagar@gmail.com</a><br>" +
              "<font face='Comic Sans MS'>All Comments are welcome<br></font>" +
            "</td>" +
          "</table>" +
        "</center>" +
      "</html>", "Contact At", JOptionPane.PLAIN_MESSAGE, Images.meGif);
  }

  /**
   * Method to get the Help Contents for user's help.
   * created : Nov 20, 2006 9:01:05 PM
   */
  public void getHelpContents() {
    new HelpContent(this);
  }

  /**
   * Method to paste Copied Text into the User's Text Field.
   * created : Nov 20, 2006 9:02:39 PM
   */
  public void pasteCopiedText()
  {
    String pasted = ClipboardHelper.pasteString();
    if (pasted.equals(""))
      status.setText("Clip Board is Empty");
    else
    {
      if(userText.getSelectedText()!=null)
        userText.setText(userText.getText().replace(userText.getSelectedText(),pasted));				 
      else
        userText.setText(pasted);
      status.setText("Clip Board Contents Pasted into Text Field");
    }
    menuBar.setItemsEnabled();
    toolBar.setButtonsEnabled();
    setTextFieldContextMenuEnabled();			
  }

  /**
   * Method for saving the calculated Hash into a File.
   * created : Nov 20, 2006 9:02:24 PM
   */
  public void saveInFile()
  {
    fileChooser=new JFileChooser();
    PrintWriter out=null;
    BufferedReader in=null;
    int option=-9999;
    boolean done=false;
    String fileName="";
    ArrayList<String> data=new ArrayList<String>();
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fileChooser.setFileFilter(new FileFilter() {
      public boolean accept(File f) {		    
        return f.getName().toLowerCase().endsWith(".hash") || f.isDirectory();
      }
      public String getDescription() {
        return "Hash Files";
      }
    });

    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    if(!userText.getText().equals("")&&!hashText.getText().equals("")) {	
      try {
        while(!done || (option!=JOptionPane.CANCEL_OPTION)) {
          option=fileChooser.showSaveDialog(me);

          if(option==JFileChooser.APPROVE_OPTION&&!fileChooser.getSelectedFile().exists()) {
            fileName=(fileChooser.getSelectedFile().toString().endsWith(".hash")?fileChooser.getSelectedFile().toString():fileChooser.getSelectedFile().toString() + ".hash");
          } else {
            if(option==JFileChooser.APPROVE_OPTION) fileName=fileChooser.getSelectedFile().toString();
          }

          if(option==JFileChooser.APPROVE_OPTION) {
            if(!fileChooser.getSelectedFile().exists()) {
              out=new PrintWriter(new FileOutputStream(fileName));
              out.println(getAlgorithm() + " Hash of " + "\"" + userText.getText() + "\"" + " is:" + hex);
              out.close();
              status.setText(fileName + " saved");
              done=true;
              break;
            } else {
              option=JOptionPane.showConfirmDialog(me,"File already exists, Are you sure you want to use the file?","Confirmation",JOptionPane.YES_NO_CANCEL_OPTION); 
              if(option == JOptionPane.YES_OPTION) {
                String opn[]={"Overwrite", "Append", "Cancel"};
                option=JOptionPane.showOptionDialog(me,"Do you want to Overwrite or Append the File?","Confirmation",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,opn,opn[0]);

                if(option == JOptionPane.YES_OPTION) {
                  out=new PrintWriter(new FileOutputStream(fileName)); 
                  out.println(getAlgorithm() + " Hash of " + "\"" + userText.getText() + "\"" + " is:" + hex);
                  out.close();
                  status.setText(fileName + " saved");
                  done=true;
                  break;
                } else if(option==JOptionPane.NO_OPTION) {
                  in=new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
                  String line;
                  while((line = in.readLine()) != null)
                    data.add(line);
                  in.close();
                  out=new PrintWriter(new FileOutputStream(fileName));
                  int i=0;
                  while(i<data.size()) {out.println(data.get(i).toString());i++;} 
                  out.println(getAlgorithm() + " Hash of " + "\"" + userText.getText() + "\"" + " is:" + hex);
                  out.close();
                  status.setText(fileName + " saved");
                  done=true;
                  break;
                } else {
                  break;
                }
              } else if(option == JOptionPane.NO_OPTION) {
                done=false;
                continue;
              } else {
                break;
              }
            }
          } else {
            break;									
          }
        }
      } catch (FileNotFoundException e) {
        status.setText("Unable to write the File");
        e.printStackTrace();
      } catch (IOException e) {
        status.setText("Unable to write the File");
        e.printStackTrace();
      } catch (ArrayIndexOutOfBoundsException e) {
        status.setText("Unable to write the File");
        e.printStackTrace();
      }
    } else if(userText.getText().equals("")||hashText.getText().equals("")) {
      if(textMode) {
        setStatus("Enter Text in Text Field and click to calculate hash");
      } else {
        setStatus("Select the File to be Hashed");
      }
    }
  }

  /**
   * Method to set the Messages that are displayed in the application.
   * created : Nov 20, 2006 8:53:33 PM
   */
  private void setMessages()
  {
    messages.put("title","The - " + getAlgorithm() + " - Hash Calculator : " + (textMode ? "Text" : "File") + " Mode");
    messages.put("clearStatusInTextMode","Please enter the text in the Text Field to Hash first");
    messages.put("clearStatusInFileMode","Please select the file to be Hashed First");
    messages.put("saveSuccessStatus","Hash saved in the File");
    messages.put("saveFailureStatus","Unable to save to the File");
    messages.put("saveCancelledStatus","Save Cancelled");
    messages.put("copyStatusWhenFieldEmptyInTextMode",messages.get("clearStatusInTextMode"));
    messages.put("copyStatusWhenFieldEmptyInFileMode",messages.get("clearStatusInFileMode"));
    messages.put("pasteSuccessStatus","Clip Board contents paste into Text Field");
    messages.put("pasteFailureStatus","Nothing saved in Clip Board to paste");
    messages.put("modeStatusInTextMode","Text Mode");
    messages.put("modeStatusInFileMode","File Mode");
  }

  /**
   * Method to set the application Mode :
   *		Text Mode : In this mode people can enter text to be Hashed.
   *		File Mode : In this mode people can select an existing file to be Hashed.
   * created : Nov 20, 2006 9:02:54 PM
   */
  public void setMode() {
    if ( this.modeDialog == null ) {
      this.modeDialog = new ModeDialog(this);
    } else {
      this.modeDialog.setVisible(true);
    }
  }
  

  /**
   * Method to set My Context Menu's options Enabled.
   * created : Nov 30, 2006 2:43:28 PM
   * @param arg boolean
   */
  private void setMyContextMenuEnabled()
  {
    JMenuItem item=(JMenuItem)myContextMenu.getSubElements()[1];	//Clear Menu
    item.setEnabled(!userText.getText().equals("")||!hashText.getText().equals(""));
    item=(JMenuItem)myContextMenu.getSubElements()[2];				//Save Menu
    item.setEnabled(!hashText.getText().equals(""));
    item=(JMenuItem)myContextMenu.getSubElements()[3];				//Select Menu
    item.setEnabled(!textMode);
    item=(JMenuItem)myContextMenu.getSubElements()[4];				//Edit Text Menu
    item.setEnabled(textMode);
    item=(JMenuItem)myContextMenu.getSubElements()[4].getSubElements()[0].getSubElements()[0];//Cut Menu
    item.setEnabled(!userText.getText().equals(""));
    item=(JMenuItem)myContextMenu.getSubElements()[4].getSubElements()[0].getSubElements()[1];//Copy Menu
    item.setEnabled(!userText.getText().equals(""));
    item=(JMenuItem)myContextMenu.getSubElements()[4].getSubElements()[0].getSubElements()[2];//Paste
    item.setEnabled(!ClipboardHelper.pasteString().equals(""));
    item=(JMenuItem)myContextMenu.getSubElements()[5];				//Copy Hash
    item.setEnabled(!hashText.getText().equals(""));
  }

  /**
   * Method to set application context menu. 
   * created : Nov 29, 2006 12:02:11 PM
   */
  private void setMyContextMenu()
  {
    JMenuItem item=new JMenuItem("Calculate",Images.calcGif);
    item.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae) {
        calculate();
      }
    });
    myContextMenu.add(item);
    myContextMenu.addSeparator();

    item = new JMenuItem("Clear",Images.clearGif);
    item.setEnabled(false);
    item.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae) {
        clear();
      }
    });
    myContextMenu.add(item);

    item = new JMenuItem("Save",Images.saveGif);
    item.setEnabled(false);
    item.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae) {
        saveInFile();
      }
    });
    myContextMenu.add(item);

    item = new JMenuItem("Select File",Images.selectGif);
    item.setEnabled(false);
    item.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae) {
        selectFile();
      }
    });
    myContextMenu.add(item);
    myContextMenu.addSeparator();

    JMenu menu1=new JMenu("Edit Text Field");
    item = new JMenuItem("Cut", Images.cutGif);
    item.setEnabled(false);
    item.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae) {
        cutText();
      }
    });
    menu1.add(item);

    item = new JMenuItem("Copy", Images.copyGif);
    item.setEnabled(false);
    item.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae) {
        ClipboardHelper.copyString(userText.getText());
      }
    });
    menu1.add(item);

    item = new JMenuItem("Paste", Images.pasteGif);
    item.setEnabled(!ClipboardHelper.pasteString().equals(""));
    item.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        pasteCopiedText();
      }
    });
    menu1.add(item);

    myContextMenu.add(menu1);

    item = new JMenuItem("Copy Hash", Images.copyGif);
    item.setEnabled(false);
    item.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae) {
        copyHash();
      }
    });
    myContextMenu.add(item);
    myContextMenu.addSeparator();

    item = new JMenuItem("Modes", Images.modesGif);
    item.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae) {
        setMode();
      }
    });
    myContextMenu.add(item);

    myContextMenu.addSeparator();

    item = new JMenuItem("Exit",Images.exitGif);
    item.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae) {
        exit();
      }
    });
    myContextMenu.add(item);

    addMouseListener(new MouseAdapter(){
      public void mousePressed(MouseEvent e) { checkPopup(e); }
      public void mouseClicked(MouseEvent e) { checkPopup(e); }
      public void mouseReleased(MouseEvent e) { checkPopup(e); }
      private void checkPopup(MouseEvent e) {
        menuBar.setItemsEnabled();
        if (e.isPopupTrigger(  )) {
          menuBar.setItemsEnabled();
          toolBar.setButtonsEnabled();
          setTextFieldContextMenuEnabled();
          myContextMenu.show(e.getComponent(  ), e.getX(  ), e.getY(  ));
        }
      }
    });
  }

  /**
   * Method to cut text from Text Field
   * created : Dec 6, 2006 11:14:50 AM
   */
  public void cutText()
  {
    if(userText.getSelectedText()!=null)
    {
      ClipboardHelper.copyString(userText.getSelectedText());
      userText.setText(userText.getText().replace(userText.getSelectedText(),""));
    }
    else
    {
      ClipboardHelper.copyString(userText.getText());
      userText.setText("");
    }		
    menuBar.setItemsEnabled();
    toolBar.setButtonsEnabled();
    setTextFieldContextMenuEnabled();
  }

  /**
   * Method to set The Text Field Context Menu's options Enabled.
   * created : Nov 30, 2006 2:35:31 PM
   */
  private void setTextFieldContextMenuEnabled()
  {
    JMenuItem item = (JMenuItem)textFieldContextMenu.getSubElements()[0]; //cut
    item.setEnabled(textMode && !userText.getText().equals(""));
    item = (JMenuItem)textFieldContextMenu.getSubElements()[1];			//copy
    item.setEnabled(textMode && !userText.getText().equals(""));
    item = (JMenuItem)textFieldContextMenu.getSubElements()[2];			//paste
    item.setEnabled(textMode && !ClipboardHelper.pasteString().equals(""));
  }

  /**
   * Method to set userText Text Field's Context Menu.
   * created : Nov 29, 2006 12:45:59 PM
   */
  private void setTextFieldContextMenu()
  {
    JMenuItem item = new JMenuItem("Cut",Images.cutGif);
    item.setEnabled(false);
    item.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae)
    {
      cutText();
    }
    });
    textFieldContextMenu.add(item);

    item = new JMenuItem("Copy",Images.copyGif);
    item.setEnabled(false);
    item.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae)
    {
      ClipboardHelper.copyString(userText.getSelectedText());
    }
    });
    textFieldContextMenu.add(item);

    item = new JMenuItem("Paste",Images.pasteGif);
    item.setEnabled(!ClipboardHelper.pasteString().equals(""));
    item.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae)
    {
      pasteCopiedText();
    }
    });
    textFieldContextMenu.add(item);

    userText.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) { checkPopup(e); }
      public void mouseClicked(MouseEvent e) { checkPopup(e); }
      public void mouseReleased(MouseEvent e) { checkPopup(e); }
      private void checkPopup(MouseEvent e) {
        if (e.isPopupTrigger(  )) {
          menuBar.setItemsEnabled();
          toolBar.setButtonsEnabled();
          setTextFieldContextMenuEnabled();
          if(textMode) textFieldContextMenu.show(e.getComponent(  ), e.getX(  ), e.getY(  ));
        }
      }
    });

    userText.addKeyListener(new KeyListener(){
      public void keyTyped(KeyEvent arg0) {
        menuBar.setItemsEnabled();
        toolBar.setButtonsEnabled();
        setTextFieldContextMenuEnabled();
      }

      public void keyPressed(KeyEvent arg0) {
        menuBar.setItemsEnabled();
        toolBar.setButtonsEnabled();
        setTextFieldContextMenuEnabled();
      }

      public void keyReleased(KeyEvent arg0) {
        menuBar.setItemsEnabled();
        toolBar.setButtonsEnabled();
        setTextFieldContextMenuEnabled();
      }			
    });
  }

  /**
   * Method to set application MenuBar.
   * created : Nov 20, 2006 9:02:33 PM
   */
  private void setMyMenuBar() {
    menuBar = new MenuBar(this);
    setJMenuBar(menuBar);
  }

  /**
   * Method to set application ToolBar.
   * created : Nov 20, 2006 9:02:28 PM
   */
  private void setMyToolBar() {
    toolBar = new ToolBar(this);
    contentPane.add(toolBar, BorderLayout.NORTH);
  }

  private static void showSplash(final HashCalculator hc) {
    new SplashScreen(hc);
  }

  public void updateToolTips() {
    calcHash.setToolTipText("Click to calculate the " + getAlgorithm() + " Hash");
  }

  public void setStatus(String message) {
    status.setText(message);
  }

  public String getUserText() {
    return this.userText.getText();
  }

  public String getHashText() {
    return this.hashText.getText();
  }

  public void setHashText(String text) {
    this.hashText.setText(text);
  }

  public void updateUI() {
    this.menuBar.setItemsEnabled();
    this.toolBar.setButtonsEnabled();
    this.setTextFieldContextMenuEnabled();
  }

  /**
   * Main method which instantiates the application and shows it.
   * created : Nov 20, 2006 9:02:09 PM
   */
  public static void main(String[] args) {
    boolean showSplash = true;

    CommandLineParser parser = new BasicParser();
    Option helpOption = new Option("h", "help", false, "Program Help.");
    Options options = new Options();
    options.addOption(helpOption);

    Option authorOption = new Option("a", "author", false, "Program Author.");
    options.addOption(authorOption);

    Option debugOption = new Option("d", "debug", false, "Debug Mode.");
    options.addOption(debugOption);

    Option noSplashOption = new Option("ns", "no-splash", false, "Skip Splash Screen.");
    options.addOption(noSplashOption);

    try {
      CommandLine cli = parser.parse(options, args);

      if ( cli.hasOption(helpOption.getOpt()) ) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("HashCalculator [OPTIONS]", options);
        return;
      } else if ( cli.hasOption(authorOption.getOpt()) ) {
      } else if ( cli.hasOption(debugOption.getOpt()) ) {
        showSplash = false;
      } else if ( cli.hasOption(noSplashOption.getOpt()) ) {
        showSplash = false;
      }

      if ( showSplash ) {
        showSplash(new HashCalculator());		
      } else {
        HashCalculator hc = new HashCalculator();
        hc.setVisible(true);
        hc.setEnabled(true);
      }
    } catch(ParseException pe) {
      pe.printStackTrace();
    }
  }
}
