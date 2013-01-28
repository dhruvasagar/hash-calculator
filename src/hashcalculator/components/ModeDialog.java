package hashcalculator.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.GridLayout;

import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.ButtonGroup;

import hashcalculator.HashCalculator;

public class ModeDialog extends JDialog {

  private static final long serialVersionUID=0L;

  private final HashCalculator hc;

  public ModeDialog(final HashCalculator hc) {
    super(hc);

    this.hc = hc;

    setModal(true);
    setSize(200,100);
    setTitle("Modes");
    setResizable(false);
    setLocationRelativeTo(hc);
    setLayout(new GridLayout(2,2));

    ButtonGroup textOrFile=new ButtonGroup();

    final JCheckBox hashText = new JCheckBox("Text", hc.isTextMode());
    hashText.setMnemonic('T');
    hashText.setToolTipText("Select to change to Text Mode");
    hashText.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        hc.setStatus("Text Mode");
      }
    });
    textOrFile.add(hashText);
    add(hashText);

    final JCheckBox hashFile=new JCheckBox("File", !hc.isTextMode());
    hashFile.setMnemonic('F');
    hashFile.setToolTipText("Select to change to File Mode");
    hashFile.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        hc.setStatus("File Mode");
      }
    });
    textOrFile.add(hashFile);
    add(hashFile);

    JButton ok=new JButton("OK");
    ok.setMnemonic('O');
    ok.setToolTipText("Click to confirm Mode");
    ok.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        if(hashFile.isSelected()) {
          hc.setTextMode(false, true);
        } else {
          hc.setTextMode(true, true);
        }
        setVisible(false);
      }
    });
    add(ok);

    JButton cancel=new JButton("Cancel");
    cancel.setMnemonic('C');
    cancel.setToolTipText("Click to cancel any changes");
    cancel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        if(hc.isTextMode()) {
          hc.setStatus("Text Mode");
        } else {
          hc.setStatus("File Mode");
        }
        setVisible(false);
      }
    });

    if(hc.isTextMode()) {
      hc.setStatus("Text Mode");
    } else {
      hc.setStatus("File Mode");
    }

    add(cancel);
    setVisible(true);
  }

}
