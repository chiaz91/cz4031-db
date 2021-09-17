package app;

import java.util.List;

import app.entity.Disk;
import app.entity.Record;
import app.util.Log;
import app.util.Utility;

public class MainApp {
	static final int DISK_SIZE  = 100*1000*1000;
	static final int BLOCK_SIZE  = 100;

	public static void main(String[] args) {
		try {
			Log.setLevel(Log.LEVEL_INFO);
			List<Record> records = Utility.readRecord("data.tsv");


			Log.i("Before insert into disk");
			Disk disk = new Disk(DISK_SIZE, BLOCK_SIZE);
			for (Record r: records) {
				disk.insertRecord(r);
			}
			Log.i("After insert into disk");
			disk.log();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
