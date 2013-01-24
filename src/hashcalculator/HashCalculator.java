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
 * This has been developed using
 * Websphere Studio Application Developer (WSAD)
 * which is a (C) IBM Product.
 * 
 * It has been compiled using J2SDK v 1.5.09.
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
import java.awt.GridBagConstraints;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;

import hashcalculator.Images;
import hashcalculator.utils.ClipboardHelper;
import hashcalculator.components.ToolBar;
import hashcalculator.components.MenuBar;

/**
 * @author Dhruva Sagar
 * @version 1.3
 * created on Nov 20, 2006 9:01:47 PM
 */
public class HashCalculator extends JFrame {

  private static final String version = "v1.1";
  private static final long serialVersionUID=0L; 

  private JProgressBar statusProgressBar;

  private static Timer calcTimer;

  private Container contentPane;
  private Component me=this;


  private byte buffer[];
  private byte digest[];

  private JButton calcHash;
  private JButton chooseFile;

  private JLabel enterText;

  private JFileChooser fileChooser;

  private JTextField hashText;
  private JTextField userText;

  private String hex="";

  private MessageDigest md;

  private ToolBar toolBar;
  private MenuBar menuBar;

  private final JPopupMenu myContextMenu = new JPopupMenu();
  private final JPopupMenu textFieldContextMenu= new JPopupMenu();

  private final Hashtable<String, String> messages=new Hashtable<String, String>();

  private JLabel status;

  private boolean textMode=true;

  public boolean isTextMode() {
    return textMode;
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
    calcHash.setToolTipText("Click to calculate the " + toolBar.getAlgorithm() + " Hash");
    lc.add(calcHash);
    contentPane.add(lc,BorderLayout.WEST);
    getRootPane().setDefaultButton(calcHash);

    JPanel uh=new JPanel();
    uh.setLayout(new BoxLayout(uh,BoxLayout.Y_AXIS));

    JPanel sel=new JPanel();
    sel.setLayout(new BoxLayout(sel,BoxLayout.X_AXIS));

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
    chooseFile.setVisible(!textMode);
    sel.add(chooseFile);
    uh.add(sel);

    hashText=new JTextField(40);
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
    final Thread calcThread=new Thread() {
      public void run() {
        try {
          buffer = new byte[8192];
          digest = new byte[8192];		
          statusProgressBar.setValue(0);
          status.setText("Calculating, Please wait...");
          getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
          md=MessageDigest.getInstance(toolBar.getAlgorithm());
          if(userText.getText().equals("")) {
            if(textMode)
              status.setText("Please fill in the text to be hashed...") ;
            else
              status.setText("Please select the File to be hashed...");
          } else {
            if(textMode) {
              buffer=userText.getText().getBytes();
              md.update(buffer);
              digest=md.digest();
              hex="";
              int j=0;
              for (int i = 0; i < digest.length; i++)
              {
                statusProgressBar.setValue(j += (i*100)/digest.length);
                statusProgressBar.update(statusProgressBar.getGraphics());
                int b = digest[i] & 0xff;
                if (Integer.toHexString(b).length() == 1) hex = hex  +  "0";
                hex  = hex + Integer.toHexString(b);										
              }		
              hashText.setText(toolBar.getAlgorithm()  +  " Hash: "  +  hex);
              ClipboardHelper.copyString(hex);
              status.setText(toolBar.getAlgorithm()  +  " Hash copied to ClipBoard");
            } else {
              FileInputStream in=new FileInputStream(userText.getText());
              int length=0,total=in.available(),readPercentage=0,lenRead=0,calcPercentage=0;
              while((length=in.read(buffer))!=-1) {	
                lenRead += length;
                readPercentage=((lenRead*74)/total);
                statusProgressBar.setValue(readPercentage);
                statusProgressBar.update(statusProgressBar.getGraphics());
                md.update(buffer,0,length);
              }
              digest=md.digest();
              hex="";
              for (int i = 0; i < digest.length; i++) {
                calcPercentage=readPercentage + (((i + 1)*26)/digest.length);
                statusProgressBar.setValue(calcPercentage);
                statusProgressBar.update(statusProgressBar.getGraphics());
                int b = digest[i] & 0xff;
                if (Integer.toHexString(b).length() == 1) hex = hex  +  "0";
                hex  = hex + Integer.toHexString(b);
              }
              hashText.setText(toolBar.getAlgorithm()  +  " Hash: "  +  hex);
              ClipboardHelper.copyString(hex);
              status.setText(toolBar.getAlgorithm()  +  " Hash copied to ClipBoard");
              in.close();
            }
            menuBar.setItemsEnabled();
            toolBar.setButtonsEnabled();
            setTextFieldContextMenuEnabled();
          }
        } catch (NoSuchAlgorithmException e) {
          e.printStackTrace();
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
        getRootPane().setCursor(Cursor.getDefaultCursor());
      }
    };

    try {
      calcThread.setDaemon(true);
      calcThread.start();
      calcThread.join();
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
    statusProgressBar.setValue(0);
    menuBar.setItemsEnabled();
    toolBar.setButtonsEnabled();
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
    JOptionPane.showMessageDialog(me,"<html><center><table border='0' cellspacing='0' cellpadding='0'>" +
        "<font face='Comic Sans MS'>" +
        "<td><img src=" + getClass().getResource("../images/me.jpg") + "></td>" +
        "<td width='10'></td>" +
        "<td>Contact <font color='red'>Dhruva Sagar</font> at:<br>" +
        "</font><a href=\"mailto:dhruva.sagar@gmail.com?Subject=Hello dude, Here are my Comments\">dhruva.sagar@gmail.com</a><br>" +
        "<font face='Comic Sans MS'>All Comments are welcome<br>" +
        "<center><img src=" + getClass().getResource("../images/D.gif") + "</center>" +
        "</font></td></table>" +
        "</center></html>","Contact At",JOptionPane.PLAIN_MESSAGE);
  }

  /**
   * Method to get the Help Contents for user's help.
   * created : Nov 20, 2006 9:01:05 PM
   */
  public void getHelpContents()
  {
    JDialog helpContents=new JDialog(this);
    DefaultMutableTreeNode root;
    DefaultMutableTreeNode innerNode;
    DefaultMutableTreeNode child;
    JScrollPane treeScroller;
    JScrollPane areaPane;
    JSplitPane split;
    JTabbedPane tabs;
    JToolBar toolBar;

    final JTree contents;
    final JEditorPane htmlArea=new JEditorPane();

    UIManager.put("Tree.closedIcon",Images.emptyGif);
    UIManager.put("Tree.collapsedIcon",Images.plusGif);
    UIManager.put("Tree.expandedIcon",Images.minusGif);
    UIManager.put("Tree.leafIcon",Images.topicGif);
    UIManager.put("Tree.openIcon",Images.emptyGif);

    helpContents.setLayout(new BorderLayout());
    helpContents.setTitle("Help Contents");
    helpContents.setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(),(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()-100);
    helpContents.setLocationRelativeTo(me);
    helpContents.setModal(true);

    toolBar=new JToolBar();
    JButton btn = new JButton("",Images.backGif);
    toolBar.add(btn);
    btn = new JButton("",Images.forwardGif);
    toolBar.add(btn);
    toolBar.addSeparator();
    btn=new JButton("",Images.homeGif);
    toolBar.add(btn);
    toolBar.addSeparator();
    btn=new JButton("Print");
    toolBar.add(btn);
    btn=new JButton("Exit");
    toolBar.add(btn);
    toolBar.setFloatable(false);
    helpContents.add(toolBar,BorderLayout.NORTH);

    root=new DefaultMutableTreeNode("Hash Calculator");

    innerNode =new DefaultMutableTreeNode("Notice");
    root.add(innerNode);

    innerNode=new DefaultMutableTreeNode("License");
    root.add(innerNode);

    innerNode=new DefaultMutableTreeNode("Getting Started");
    child=new DefaultMutableTreeNode("Accessibilty");
    child.add(new DefaultMutableTreeNode("blah blah"));
    innerNode.add(child);
    child=new DefaultMutableTreeNode("Starting");
    innerNode.add(child);
    child=new DefaultMutableTreeNode("Exiting");
    innerNode.add(child);
    root.add(innerNode);

    innerNode=new DefaultMutableTreeNode("Modes");
    child=new DefaultMutableTreeNode("Text Mode");
    innerNode.add(child);
    child=new DefaultMutableTreeNode("File Mode");
    innerNode.add(child);
    root.add(innerNode);

    contents=new JTree(root);
    contents.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    contents.addTreeSelectionListener(new TreeSelectionListener() {
      public void valueChanged(TreeSelectionEvent arg0)
      {
        String helpDocBase="../HelpContents/";
        String selection=contents.getSelectionPath().getLastPathComponent().toString();
        try {
          htmlArea.setPage(getClass().getResource(helpDocBase + selection + ".htm"));
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
    treeScroller=new JScrollPane(contents);
    tabs=new JTabbedPane();
    tabs.addTab("Contents",treeScroller);
    tabs.addTab("Search",new JPanel());

    htmlArea.setEditable(false);
    areaPane=new JScrollPane(htmlArea);

    split=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,tabs,areaPane);
    split.setDividerLocation(200);
    split.setDividerSize(1);
    helpContents.add(split,BorderLayout.CENTER);

    helpContents.setVisible(true);
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
      public boolean accept(File f)
    {		    
      return f.getName().toLowerCase().endsWith(".hash") || f.isDirectory();
    }
    public String getDescription()
    {
      return "Hash Files";
    }
    });
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    if(!userText.getText().equals("")&&!hashText.getText().equals(""))
    {	
      try {
        while(!done || (option!=JOptionPane.CANCEL_OPTION)) {
          option=fileChooser.showSaveDialog(me);
          if(option==JFileChooser.APPROVE_OPTION&&!fileChooser.getSelectedFile().exists())
            fileName=(fileChooser.getSelectedFile().toString().endsWith(".hash")?fileChooser.getSelectedFile().toString():fileChooser.getSelectedFile().toString() + ".hash");
          else
            if(option==JFileChooser.APPROVE_OPTION) fileName=fileChooser.getSelectedFile().toString();
          if(option==JFileChooser.APPROVE_OPTION)
          {
            if(!fileChooser.getSelectedFile().exists())
            {
              out=new PrintWriter(new FileOutputStream(fileName));
              out.println(toolBar.getAlgorithm()  +  " Hash of "  +  "\"" + userText.getText()  +  "\""  +  " is:"  +  hex);
              out.close();
              status.setText(fileName + " saved");
              done=true;
              break;
            }
            else
            {
              option=JOptionPane.showConfirmDialog(me,"File already exists, Are you sure you want to use the file?","Confirmation",JOptionPane.YES_NO_CANCEL_OPTION); 
              if(option==JOptionPane.YES_OPTION)
              {
                String opn[]={"Overwrite", "Append", "Cancel"};
                option=JOptionPane.showOptionDialog(me,"Do you want to Overwrite or Append the File?","Confirmation",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,opn,opn[0]);
                if(option==JOptionPane.YES_OPTION)
                {
                  out=new PrintWriter(new FileOutputStream(fileName)); 
                  out.println(toolBar.getAlgorithm()  +  " Hash of "  +  "\""  +  userText.getText()  +  "\""  +  " is:"  +  hex);
                  out.close();
                  status.setText(fileName + " saved");
                  done=true;
                  break;
                }
                else if(option==JOptionPane.NO_OPTION)
                {
                  in=new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
                  String line;
                  while((line = in.readLine()) != null)
                    data.add(line);
                  in.close();
                  out=new PrintWriter(new FileOutputStream(fileName));
                  int i=0;
                  while(i<data.size()) {out.println(data.get(i).toString());i++;} 
                  out.println(toolBar.getAlgorithm()  +  " Hash of "  +  "\""  +  userText.getText()  +  "\""  +  " is:"  +  hex);
                  out.close();
                  status.setText(fileName + " saved");
                  done=true;
                  break;
                }
                else
                  break;
              }
              else if(option==JOptionPane.NO_OPTION)
              {
                done=false;
                continue;
              }
              else
                break;
            }
          }
          else
            break;									
        }
      }
      catch (FileNotFoundException e) {
        status.setText("Unable to write the File");
        e.printStackTrace();
      }
      catch (IOException e) {
        status.setText("Unable to write the File");
        e.printStackTrace();
      }
      catch (ArrayIndexOutOfBoundsException e) {
        status.setText("Unable to write the File");
        e.printStackTrace();
      }
    }
    else if(userText.getText().equals("")||hashText.getText().equals(""))
    {
      if(textMode)
        status.setText("Enter Text in Text Field and click to calculate hash");
      else
        status.setText("Select the File to be Hashed");
    }
  }

  /**
   * Method to set the Messages that are displayed in the application.
   * created : Nov 20, 2006 8:53:33 PM
   */
  private void setMessages()
  {
    messages.put("title","The - " + toolBar.getAlgorithm() + " - Hash Calculator : " + ((textMode)?"Text":"File") + " Mode");
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
  public void setMode()
  {
    final JDialog options=new JDialog(this);
    options.setResizable(false);
    options.setLayout(new GridLayout(2,2));
    options.setTitle("Modes");
    options.setSize(200,100);
    options.setLocationRelativeTo(me);
    options.setModal(true);

    ButtonGroup textOrFile=new ButtonGroup();

    final JCheckBox hashText=new JCheckBox("Text",textMode);
    hashText.setMnemonic('T');
    hashText.setToolTipText("Select to change to Text Mode");
    hashText.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae)
    {
      status.setText("Text Mode");
    }
    });
    textOrFile.add(hashText);
    options.add(hashText);

    final JCheckBox hashFile=new JCheckBox("File",!textMode);
    hashFile.setMnemonic('F');
    hashFile.setToolTipText("Select to change to File Mode");
    hashFile.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae)
    {
      status.setText("File Mode");
    }
    });
    textOrFile.add(hashFile);
    options.add(hashFile);

    JButton ok=new JButton("OK");
    ok.setMnemonic('O');
    ok.setToolTipText("Click to confirm Mode");
    ok.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae)
    {
      if(hashFile.isSelected())
      textMode=false;
      else
      textMode=true;
    status.setText(((textMode)?"Text":"File") + " Mode");
    setTitle("The - " + toolBar.getAlgorithm() + " - Hash Calculator : " + ((textMode)?"Text":"File") + " Mode");
    if(textMode)clear();
    chooseFile.setVisible(!textMode);
    userText.setEditable(textMode);
    options.dispose();
    clear();
    }
    });
    options.add(ok);

    JButton cancel=new JButton("Cancel");
    cancel.setMnemonic('C');
    cancel.setToolTipText("Click to cancel any changes");
    cancel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae)
    {
      if(textMode)
      status.setText("Text Mode");
      else
      status.setText("File Mode");
    options.dispose();
    }
    });
    if(textMode)
      status.setText("Text Mode");
    else
      status.setText("File Mode");
    options.add(cancel);
    options.setVisible(true);
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
      public void actionPerformed(ActionEvent ae)
    {
      calculate();
    }
    });
    myContextMenu.add(item);
    myContextMenu.addSeparator();

    item = new JMenuItem("Clear",Images.clearGif);
    item.setEnabled(false);
    item.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae)
    {
      clear();
    }
    });
    myContextMenu.add(item);

    item = new JMenuItem("Save",Images.saveGif);
    item.setEnabled(false);
    item.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae)
    {
      saveInFile();
    }
    });
    myContextMenu.add(item);

    item = new JMenuItem("Select File",Images.selectGif);
    item.setEnabled(false);
    item.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae)
    {
      selectFile();
    }
    });
    myContextMenu.add(item);
    myContextMenu.addSeparator();

    JMenu menu1=new JMenu("Edit Text Field");
    item = new JMenuItem("Cut", Images.cutGif);
    item.setEnabled(false);
    item.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae)
    {
      cutText();
    }
    });
    menu1.add(item);

    item = new JMenuItem("Copy", Images.copyGif);
    item.setEnabled(false);
    item.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae)
    {
      ClipboardHelper.copyString(userText.getText());
    }
    });
    menu1.add(item);

    item = new JMenuItem("Paste", Images.pasteGif);
    item.setEnabled(!ClipboardHelper.pasteString().equals(""));
    item.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae)
    {
      pasteCopiedText();
    }
    });
    menu1.add(item);

    myContextMenu.add(menu1);

    item = new JMenuItem("Copy Hash", Images.copyGif);
    item.setEnabled(false);
    item.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae)
    {
      copyHash();
    }
    });
    myContextMenu.add(item);
    myContextMenu.addSeparator();

    item = new JMenuItem("Modes", Images.modesGif);
    item.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae)
    {
      setMode();
    }
    });
    myContextMenu.add(item);

    myContextMenu.addSeparator();

    item = new JMenuItem("Exit",Images.exitGif);
    item.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae)
    {
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
    final JWindow splashWin=new JWindow();
    splashWin.setAlwaysOnTop(true);
    splashWin.setSize(250,130);
    splashWin.setLocationRelativeTo(null);

    final JPanel splash=new JPanel();
    splash.setSize(250,130);
    splash.setLayout(new GridBagLayout());
    splash.setBorder(BorderFactory.createLineBorder(Color.BLACK));		

    GridBagConstraints gbc=new GridBagConstraints();

    gbc.gridx=0;gbc.gridy=0;
    JLabel iconLabel=new JLabel("",new ImageIcon("./images/md5.gif"),SwingConstants.CENTER);
    iconLabel.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
    iconLabel.setHorizontalTextPosition(JLabel.CENTER);
    iconLabel.setVerticalTextPosition(JLabel.CENTER);
    splash.add(iconLabel,gbc);

    gbc.gridx++;
    JLabel h=new JLabel("<html>Hash Calculator " + version + "<br>Developed by : <font color=#AA0022>Dhruva Sagar</font></html>");
    splash.add(h,gbc);

    gbc.gridx=0;gbc.gridy++;
    final JTextField text=new JTextField("Loading",7);
    text.setEditable(false);
    text.setBorder(BorderFactory.createEmptyBorder());
    text.setFont(new Font("Comic Sans MS",Font.BOLD,12));
    splash.add(text,gbc);

    gbc.gridx++;
    final JProgressBar pb=new JProgressBar();
    pb.setMinimum(0);
    pb.setMaximum(100);
    pb.setSize(110,10);
    splash.add(pb,gbc);

    calcTimer = new Timer(500,new ActionListener() {
      int count=0;
      int progress=0;
      long start = System.currentTimeMillis(),end=0;
      public void actionPerformed(ActionEvent e) {
        pb.setValue(progress);
        text.setText(text.getText() + ".");
        count++;
        if(count==4) {
          text.setText("Loading");
          count=0;
        }
        end = System.currentTimeMillis();
        progress += (end-start)/400;
        if(end-start > 6000) {
          hc.setVisible(true);
          hc.setEnabled(false);
        }
        if(end-start > 7500) {
          endTimer();
          splashWin.dispose();
          hc.setEnabled(true);
        }
      }
    });		
    calcTimer.start();
    splashWin.getContentPane().add(splash);
    splashWin.setVisible(true);
  }

  private static void endTimer() {
    calcTimer.stop();
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
