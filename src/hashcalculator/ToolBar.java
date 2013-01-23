package hashcalculator;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.JComboBox;

import hashcalculator.Images;
import hashcalculator.HashCalculator;

public class ToolBar extends JToolBar {

  private static final long serialVersionUID=0L;

  public JComboBox algorithm;

  public String getAlgorithm() {
    return algorithm.getSelectedItem().toString();
  }

  public ToolBar(final HashCalculator hc) {
    setFloatable(false);
    JButton btn=new JButton("", Images.clearGif);
    btn.setToolTipText("Click to clear everything");
    btn.setEnabled(false);
    btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae)
      {
        hc.clear();
      }
    });
    add(btn);

    btn=new JButton("", Images.saveGif);
    btn.setEnabled(false);
    btn.setToolTipText("Click to save the result into a file");
    btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae)
      {
        hc.saveInFile();
      }
    });
    add(btn);

    btn=new JButton("", Images.selectGif);
    btn.setEnabled(false);
    btn.setToolTipText("Click to select the file to Hash");
    btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae)
      {
        hc.selectFile();
      }
    });
    add(btn);
    addSeparator();

    btn=new JButton("", Images.copyGif);
    btn.setEnabled(false);
    btn.setToolTipText("Click to copy the hash");
    btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae)
      {
        hc.copyHash();			 
      }
    });
    add(btn);

    btn=new JButton("", Images.pasteGif);
    btn.setEnabled(!ClipboardHelper.pasteString().equals(""));
    btn.setToolTipText("Click to paste text from Clip Board");
    btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae)
      {
        hc.pasteCopiedText();
      }
    });
    add(btn);
    addSeparator();

    btn=new JButton("", Images.modesGif);
    btn.setToolTipText("Click to select the Mode");
    btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae)
      {
        hc.setMode();
      }
    });
    add(btn);
    addSeparator();

    btn=new JButton("", Images.calcGif);
    btn.setToolTipText("Click to calculate the Hash");
    btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae)
      {
        hc.calculate();
      }
    });
    add(btn);
    addSeparator();

    btn=new JButton("", Images.helpGif);
    btn.setToolTipText("Click to get the Help Contents");
    btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae)
      {
        hc.getHelpContents();
      }			
    });
    add(btn);

    btn=new JButton("", Images.aboutGif);
    btn.setToolTipText("Click for information about Hash Caclulator");
    btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae)
      {
        hc.getAboutHC();
      }				
    });
    add(btn);

    btn=new JButton("", Images.contactGif);
    btn.setToolTipText("Click to get Authors Contact Information");
    btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae)
      {
        hc.getContactInfo();
      }				
    });
    add(btn);

    addSeparator();

    algorithm=new JComboBox(new String[]{"MD2","MD5","SHA-1","SHA-256","SHA-384","SHA-512"});
    algorithm.setToolTipText("Select the hashing algorithm to be used");
    algorithm.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae)
      {
        hc.setStatus(getAlgorithm() + " Hash Algorithm Selected");
        hc.setTitle("The - " + getAlgorithm() + " - Hash Calculator : " + ((hc.isTextMode()) ? "Text" : "File") + " Mode");
      }
    });
    add(algorithm);
    addSeparator();

    btn=new JButton("", Images.exitGif);
    btn.setToolTipText("Click to exit from application");
    btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae)
      {
        hc.exit();
      }
      });
    add(btn);
  }
}
