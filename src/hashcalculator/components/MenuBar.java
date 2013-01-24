package hashcalculator.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import hashcalculator.Images;
import hashcalculator.HashCalculator;
import hashcalculator.utils.ClipboardHelper;

public class MenuBar extends JMenuBar {

  private static final long serialVersionUID=0L;

  private final HashCalculator hc;

  public MenuBar(final HashCalculator hc) {
    this.hc = hc;

    JMenu menu=new JMenu("File");
    menu.setMnemonic('F');
    JMenuItem item=new JMenuItem("Clear",Images.clearGif);
    item.setEnabled(false);
    item.setMnemonic('C');
    item.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        hc.clear();
      }
    });
    menu.add(item);

    item=new JMenuItem("Save",Images.saveGif);
    item.setMnemonic('S');
    item.setEnabled(false);
    item.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        hc.saveInFile();
      }
    });
    menu.add(item);

    item=new JMenuItem("Select File",Images.selectGif);
    item.setMnemonic('F');
    item.setEnabled(false);
    item.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        hc.selectFile();
      }
    });
    menu.add(item);

    menu.addSeparator();

    item=new JMenuItem("Calculate",Images.calcGif);
    item.setMnemonic('A');
    item.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        hc.calculate();
      }
    });
    menu.add(item);

    menu.addSeparator();

    item=new JMenuItem("Exit",Images.exitGif);
    item.setMnemonic('X');
    item.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        hc.exit();
      }
    });		
    menu.add(item);

    add(menu);

    menu=new JMenu("Edit");
    menu.setMnemonic('E');

    JMenu menu1=new JMenu("Edit Text Field");
    menu1.setMnemonic('E');
    item = new JMenuItem("Cut",Images.cutGif);
    item.setEnabled(false);
    item.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae) {
        hc.cutText();
      }
    });
    menu1.add(item);

    item = new JMenuItem("Copy",Images.copyGif);
    item.setEnabled(false);
    item.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae) {
        ClipboardHelper.copyString(hc.getUserText());
      }
    });
    menu1.add(item);

    item = new JMenuItem("Paste",Images.pasteGif);
    item.setEnabled(!ClipboardHelper.pasteString().equals(""));
    item.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        hc.pasteCopiedText();
      }
    });
    menu1.add(item);

    menu.add(menu1);

    item=new JMenuItem("Copy Hash",Images.copyGif);
    item.setEnabled(false);
    item.setMnemonic('C');
    item.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        hc.copyHash();
      }
    });
    menu.add(item);

    menu.addSeparator();

    item=new JMenuItem("Modes",Images.modesGif);
    item.setMnemonic('M');
    item.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        hc.setMode();
      }
    });
    menu.add(item);

    menu.addSeparator();

    add(menu);

    menu=new JMenu("Help");
    menu.setMnemonic('H');
    item=new JMenuItem("Help Contents",Images.helpGif);
    item.setMnemonic('C');
    item.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        hc.getHelpContents();
      }			
    });
    menu.add(item);

    item=new JMenuItem("About Hash Calculator",Images.aboutGif);
    item.setMnemonic('A');
    item.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        hc.getAboutHC();
      }				
    });
    menu.add(item);

    menu.addSeparator();

    item=new JMenuItem("Contact Author",Images.contactGif);
    item.setMnemonic('O');
    item.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        hc.getContactInfo();
      }				
    });
    menu.add(item);

    add(menu);
  }

  public void setItemsEnabled() {
    JMenuItem item=(JMenuItem)getSubElements()[0].getSubElements()[0].getSubElements()[0]; //Clear
    item.setEnabled(!hc.getUserText().equals("")||!hc.getHashText().equals(""));

    item=(JMenuItem)getSubElements()[0].getSubElements()[0].getSubElements()[1]; //Save
    item.setEnabled(!hc.getHashText().equals(""));

    item=(JMenuItem)getSubElements()[0].getSubElements()[0].getSubElements()[2]; //Select File
    item.setEnabled(!hc.isTextMode());

    item=(JMenuItem)getSubElements()[1].getSubElements()[0].getSubElements()[0]; //Edit Text Menu
    item.setEnabled(hc.isTextMode());

    item=(JMenuItem)getSubElements()[1].getSubElements()[0].getSubElements()[0].getSubElements()[0].getSubElements()[0];//Cut
    item.setEnabled(!hc.getUserText().equals(""));

    item=(JMenuItem)getSubElements()[1].getSubElements()[0].getSubElements()[0].getSubElements()[0].getSubElements()[1];//Copy
    item.setEnabled(!hc.getUserText().equals(""));

    item=(JMenuItem)getSubElements()[1].getSubElements()[0].getSubElements()[0].getSubElements()[0].getSubElements()[2]; //Paste
    item.setEnabled(!ClipboardHelper.pasteString().equals("") && hc.isTextMode());

    item=(JMenuItem)getSubElements()[1].getSubElements()[0].getSubElements()[1];//Copy Hash
    item.setEnabled(!hc.getHashText().equals(""));
  }

}
