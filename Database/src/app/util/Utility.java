package app.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.entity.Record;


public class Utility {
	
	public static List<Record> readRecord(String path) throws Exception {
		Log.d("attempt to load "+path);

		File file = new File(path);
		if (!file.exists()) {
			throw new FileNotFoundException("File not exist");
		}
		
		BufferedReader br = null;
		List<Record> records = new ArrayList<>();
		String line;
		String[] parts = null;
		try {
			br = new BufferedReader( new FileReader(path));
			// reading header first
			line = br.readLine();
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
		Log.i("total records: "+records.size());
		Analyzer.log();
		return records;
	}
}
