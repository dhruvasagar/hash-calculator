package hashcalculator;

import java.awt.Cursor;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.util.concurrent.Callable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import hashcalculator.HashCalculator;
import hashcalculator.utils.ClipboardHelper;

public class Calculable implements Callable<String> {

  private final HashCalculator hc;

  private MessageDigest md;

  public Calculable(final HashCalculator hc) {
    this.hc = hc;
  }

  public String call() {
    String hex = "";
    byte buffer[] = new byte[8192];
    byte digest[] = new byte[8192];		

    try {
      hc.setStatusProgressBarValue(0);
      hc.setStatus("Calculating, Please wait...");
      hc.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

      md = MessageDigest.getInstance(hc.getAlgorithm());
      if(hc.getUserText().equals("")) {
        if(hc.isTextMode())
          hc.setStatus("Please fill in the text to be hashed...") ;
        else
          hc.setStatus("Please select the File to be hashed...");
      } else {
        if(hc.isTextMode()) {
          buffer = hc.getUserText().getBytes();
          md.update(buffer);
          digest = md.digest();
          int j=0;
          for (int i = 0; i < digest.length; i++) {
            hc.setStatusProgressBarValue(j += (i*100)/digest.length);
            hc.updateStatusProgressBar();//.update(statusProgressBar.getGraphics());
            int b = digest[i] & 0xff;
            if (Integer.toHexString(b).length() == 1) hex = hex  +  "0";
            hex  = hex + Integer.toHexString(b);										
          }		
          hc.setHashText(hc.getAlgorithm()  +  " Hash: "  +  hex);
          ClipboardHelper.copyString(hex);
          hc.setStatus(hc.getAlgorithm()  +  " Hash copied to ClipBoard");
        } else {
          FileInputStream in = new FileInputStream(hc.getUserText());
          int length=0,total=in.available(),readPercentage=0,lenRead=0,calcPercentage=0;
          while((length=in.read(buffer))!=-1) {	
            lenRead += length;
            readPercentage=((lenRead*74)/total);
            hc.setStatusProgressBarValue(readPercentage);
            hc.updateStatusProgressBar();//.update(statusProgressBar.getGraphics());
            md.update(buffer,0,length);
          }
          digest=md.digest();
          for (int i = 0; i < digest.length; i++) {
            calcPercentage=readPercentage + (((i + 1)*26)/digest.length);
            hc.setStatusProgressBarValue(calcPercentage);
            hc.updateStatusProgressBar();//.update(statusProgressBar.getGraphics());
            int b = digest[i] & 0xff;
            if (Integer.toHexString(b).length() == 1) hex = hex  +  "0";
            hex  = hex + Integer.toHexString(b);
          }
          hc.setHashText(hc.getAlgorithm()  +  " Hash: "  +  hex);
          ClipboardHelper.copyString(hex);
          hc.setStatus(hc.getAlgorithm()  +  " Hash copied to ClipBoard");
          in.close();
        }
        hc.updateUI();
      }
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    hc.setCursor(Cursor.getDefaultCursor());
    return hex;
  }

}
