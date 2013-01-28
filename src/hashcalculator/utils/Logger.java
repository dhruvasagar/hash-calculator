package hashcalculator.utils;

import java.util.Date;
import java.util.ArrayList;

public class Logger {
	
	private static final ArrayList<String> messages = new ArrayList<String>();

  private static void log(String level, String message) {
		Date datetime = new Date();
    String msg = "[" + datetime.toString() + "] [" + level + "] " + message;
		messages.add(msg);
    System.out.println(msg);
  }
	
	public static void debug(String message) {
    log("DEBUG", message);
	}
	
	public static void info(String message) {
    log("INFO", message);
	}
	
	public static void warn(String message) {
    log("WARN", message);
	}
	
	public static void error(String message) {
    log("ERROR", message);
	}
	
	public static void severe(String message) {
    log("SEVERE", message);
	}
}
