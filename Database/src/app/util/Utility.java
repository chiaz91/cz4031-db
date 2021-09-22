package app.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import app.entity.Record;


public class Utility {
	private static final String TAG = "Utility";

	/**
	 * Loading and parsing the records from .tsv file
	 * @param path path to data file
	 * @return list of records
	 * @throws Exception
	 */
	public static List<Record> readRecord(String path) throws Exception {
		File dataFile = new File(path);
		Log.i(TAG, "Loading records from "+path);
		if (!dataFile.exists()) {
			// file is not exist, try to add current directory and try again
			String dir = System.getProperty("user.dir");
			dataFile = new File(dir, path);
			Log.i(TAG, "Failed, re-attempt to load record from "+dataFile.getAbsolutePath());
			if (!dataFile.exists()){
				throw new FileNotFoundException("File not exist");
			}
		}

		BufferedReader br = null;
		List<Record> records = new ArrayList<>();
		String line;
		String[] parts = null;
		try {
			br = new BufferedReader( new FileReader(dataFile));
			// reading header first (to be skipped)
			br.readLine();
			while((line = br.readLine()) != null) {
				parts = line.split("\\t");
				Record record = new Record(parts[0], Float.parseFloat(parts[1]), Integer.parseInt( parts[2]));
				records.add( record );
				Analyzer.analysis(record);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				}catch (IOException e) {
					Log.e(e.getMessage());
				}
			}
		}
		Log.d(TAG, "total records: "+records.size());
		Analyzer.log();
		return records;
	}

	/**
	 * Convert file size into human-readable format
	 * @param size file size in bytes
	 * @return formatted size
	 */
	public static String formatFileSize(int size){
		String[] suffix = { "B", "KB", "MB", "GB", "TB" };
		int order = 0;
		if (size>0){
			order = (int) (Math.log(size)/Math.log(1024));
		}
		double normSize = size / Math.pow(1024, order);
		return String.format("%.2f %s", normSize, suffix[order]);
	}


	// for DEBUG
//	public static void logDirs(){
//		String path1 = System.getProperty("user.dir");
//		String path2 = new File("").getAbsolutePath();
//		String root  = Paths.get(path1).getRoot().toAbsolutePath().toString();
//		Log.d("user.dir", path1);
//		Log.d("File.absPath", path2);
//		Log.d("root", root);
//	}
}
