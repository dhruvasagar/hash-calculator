package hashcalculator.components;

import javax.swing.Timer;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JProgressBar;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.Color;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import hashcalculator.Images;
import hashcalculator.HashCalculator;

public class SplashScreen extends JWindow {

  private static final long serialVersionUID=0L;

  private static Timer calcTimer;

  private final HashCalculator hc;

  public SplashScreen(final HashCalculator hc) {
    this.hc = hc;

    setAlwaysOnTop(true);
    setSize(250,130);
    setLocationRelativeTo(null);

    final JPanel splash=new JPanel();
    splash.setSize(250,130);
    splash.setLayout(new GridBagLayout());
    splash.setBorder(BorderFactory.createLineBorder(Color.BLACK));		

    GridBagConstraints gbc=new GridBagConstraints();
    gbc.insets = new Insets(2,2,2,2);

    gbc.gridx=0;
    gbc.gridy=0;
    gbc.weightx=0.5;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    JLabel iconLabel=new JLabel("", Images.md5Gif, SwingConstants.CENTER);
    splash.add(iconLabel, gbc);

    gbc.gridx=1;
    gbc.gridy=0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    JLabel h=new JLabel("<html>Hash Calculator <center>" + hc.getVersion() + "</center>Developed by : <font color=#AA0022>Dhruva Sagar</font></html>");
    splash.add(h,gbc);

    gbc.gridx=0;
    gbc.gridy++;
    gbc.weightx=0.5;
    final JTextField text=new JTextField("Loading", 7);
    text.setEditable(false);
    text.setBorder(BorderFactory.createEmptyBorder());
    text.setFont(new Font("Comic Sans MS",Font.BOLD,12));
    splash.add(text,gbc);

    gbc.gridx=1;
    gbc.gridy=1;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    final JProgressBar pb=new JProgressBar();
    pb.setMinimum(0);
    pb.setMaximum(100);
    pb.setPreferredSize(new Dimension(250, 10));
    splash.add(pb,gbc);

    final JWindow me = this;
    calcTimer = new Timer(200, new ActionListener() {
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
          calcTimer.stop();
          me.dispose();
          hc.setEnabled(true);
        }
      }
    });		
    calcTimer.start();
    getContentPane().add(splash);
    setVisible(true);
  }
}
