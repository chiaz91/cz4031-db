package app.util;

import java.text.SimpleDateFormat;
import java.util.Date;

// FOR DEBUG!!!
// custom log class to allow showing/hiding of log message with ease
// the lower the level, the more important the log it is
public class Log {
	private static final String TAG = "Log";
	public static final int LEVEL_NONE  = 0;
	public static final int LEVEL_ERROR = 1; // error or exception, please quickly fix it
	public static final int LEVEL_WARN  = 2; // might lead to error, but works fine now.
	public static final int LEVEL_INFO  = 3; // important process flow, good to show in log
	public static final int LEVEL_DEBUG = 4; // messages for debugging purpose
	public static final int LEVEL_VERBOSE = 5; // detail and redundant step by step messages
	public static final int LEVEL_ALL = 10;
	public static final String FORMAT_DETAIL = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String FORMAT_SIMPLE = "HH:mm:ss";
	private static int level = LEVEL_INFO;
	private static boolean timestampEnabled = true;
	private static String dateformat = FORMAT_DETAIL;
	private static SimpleDateFormat sdf = new SimpleDateFormat(dateformat);


	/**
	 * Setting level to determine how detail the logger should print
	 * @param level log level
	 */
	public static void setLevel(int level) {
		Log.level = level;
	}

	public static boolean isTimestampEnabled(){
		return timestampEnabled;
	}
	/**
	 * Setting to allow logger to attach timestamp for each log
	 * @param enabled
	 */
	public static void setTimestampEnabled(boolean enabled){
		Log.timestampEnabled = enabled;
	}

	public static String getTimestampFormat(){
		return dateformat;
	}
	/**
	 * Setting to change the timestamp format
	 * @param format date format for log
	 * @see SimpleDateFormat
	 */
	public static void setTimestampFormat(String format){
		dateformat = format;
		sdf = new SimpleDateFormat(format);
	}

	private static void print(int logLevel, String message){
		if (level >= logLevel) {
			if (timestampEnabled){
				System.out.print(sdf.format(new Date())+" ");
			}
			System.out.println(message);
		}
	}

	public static void e(String tag, String msg) {
		print(LEVEL_ERROR, "E/"+String.format("%s: %s", tag, msg));
	}
	public static void w(String tag, String msg) {
		print(LEVEL_WARN,"W/"+String.format("%s: %s", tag, msg));
	}
	public static void i(String tag, String msg) {
		print(LEVEL_INFO, "I/"+String.format("%s: %s", tag, msg));
	}
	public static void d(String tag, String msg) {
		print(LEVEL_DEBUG, "D/"+String.format("%s: %s", tag, msg));
	}
	public static void v(String tag, String msg) {
		print(LEVEL_VERBOSE, "V/"+String.format("%s: %s", tag, msg));
	}
	
	// overloading methods, default tag is used when no tag is provided
	public static void e(String msg) {
		e(TAG, msg);
	}
	public static void w(String msg) {
		w(TAG, msg);
	}
	public static void i(String msg) {
		i(TAG, msg);
	}
	public static void d(String msg) {
		d(TAG, msg);
	}
	public static void v(String msg) {
		v(TAG, msg);
	}


	public static String getLogLevelString(){
		return switch (level) {
			case LEVEL_NONE -> "None";
			case LEVEL_ERROR -> "Error";
			case LEVEL_WARN -> "Warn";
			case LEVEL_INFO -> "Info";
			case LEVEL_DEBUG -> "Debug";
			case LEVEL_VERBOSE -> "Verbose";
			default -> "Unknown";
		};
	}
}
