package hashcalculator.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.JComboBox;

import hashcalculator.Images;
import hashcalculator.HashCalculator;
import hashcalculator.utils.ClipboardHelper;

public class ToolBar extends JToolBar {

  private static final long serialVersionUID=0L;

  private JButton clear;
  private JButton save;
  private JButton select;
  private JButton copy;
  private JButton paste;
  private JButton modes;
  private JButton calculate;
  private JButton help;
  private JButton about;
  private JButton contact;
  private JButton exit;

  private JComboBox algorithm;

  private final HashCalculator hc;

  public ToolBar(final HashCalculator hc) {
    this.hc = hc;

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
    this.clear = btn;
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
    this.save = btn;
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
    this.select = btn;
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
    this.copy = btn;
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
    this.paste = btn;
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
    this.modes = btn;
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
    this.calculate = btn;
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
    this.help = btn;
    add(btn);

    btn=new JButton("", Images.aboutGif);
    btn.setToolTipText("Click for information about Hash Caclulator");
    btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae)
      {
        hc.getAboutHC();
      }				
    });
    this.about = btn;
    add(btn);

    btn=new JButton("", Images.contactGif);
    btn.setToolTipText("Click to get Authors Contact Information");
    btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae)
      {
        hc.getContactInfo();
      }				
    });
    this.contact = btn;
    add(btn);

    addSeparator();

    algorithm=new JComboBox(new String[]{"MD2","MD5","SHA-1","SHA-256","SHA-384","SHA-512"});
    algorithm.setToolTipText("Select the hashing algorithm to be used");
    algorithm.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae)
      {
        hc.setStatus(getAlgorithm() + " Hash Algorithm Selected");
        hc.setTitle("The - " + getAlgorithm() + " - Hash Calculator : " + ((hc.isTextMode()) ? "Text" : "File") + " Mode");
          hc.setToolTips(getAlgorithm());
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
    this.exit = btn;
    add(btn);
  }

  public String getAlgorithm() {
    return algorithm.getSelectedItem().toString();
  }

  public void setButtonsEnabled() {
    this.clear.setEnabled(!hc.getUserText().equals("") || !hc.getHashText().equals(""));
    this.save.setEnabled(!hc.getHashText().equals(""));
    this.select.setEnabled(!hc.isTextMode());
    this.copy.setEnabled(!hc.getHashText().equals(""));
    this.paste.setEnabled(hc.isTextMode() && !ClipboardHelper.pasteString().equals(""));
  }
}
