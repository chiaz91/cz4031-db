package app.util;

// custom log class to allow showing/hiding of log message with ease
public class Log {
	public static final int LEVEL_NONE  = 0;
	public static final int LEVEL_ERROR = 1;
	public static final int LEVEL_INFO  = 2;
	public static final int LEVEL_DEBUG = 3;
	private static int level = LEVEL_INFO;
	
	
	public static void setLevel(int level) {
		Log.level = level;
	}

	private static void print(int logLevel, String message){
		if (level >= logLevel) {
			System.out.println(message);
		}
	}
	
	public static void d(String tag, String msg) {
		print(LEVEL_DEBUG, tag+": "+msg);
	}
	
	public static void e(String tag, String msg) {
		print(LEVEL_ERROR, tag+": "+msg);
	}
	
	public static void i(String tag, String msg) {
		print(LEVEL_INFO, tag+": "+msg);
	}
	
	// overloading methods  
	public static void d(String msg) {
		d("Log.DEBUG", msg);
	}
	
	public static void e(String msg) {
		e("Log.ERROR", msg);
	}
	
	public static void i(String msg) {
		i("Log.INFO", msg);
	}

}
