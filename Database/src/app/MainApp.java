package app;

import java.util.ArrayList;
import java.util.List;

import app.entity.Address;
import app.entity.Disk;
import app.entity.Record;
import app.util.Log;
import app.util.Utility;

public class MainApp {
	private static final String TAG = "App";
	static final int DISK_SIZE  = 100*1000*1000;
	static final int BLOCK_SIZE  = 100;
	private Disk disk;
	private BPlusTree index;


	public void run() throws Exception {
		// read records from data file
		List<Record> records = Utility.readRecord("data.tsv");
		disk = new Disk(DISK_SIZE, BLOCK_SIZE);
		index = new BPlusTree(BLOCK_SIZE);

		Log.i(TAG,"Before insert into disk");
		Address recordAddr;
		for (Record r: records) {
			// inserting records into disk and create index!
			recordAddr = disk.appendRecord(r);
			index.insert( r.getNumVotes(), recordAddr);
		}
		Log.i(TAG,"After insert into disk");
		disk.log();

		// TODO do experiences
//		doExperience1();
	}

	public void doExperience1(){
		ArrayList<Address> e1Records = index.getRecordsWithKey(500);
		double avgRating = 0;
		Record tempRecord;
		for (Address addr: e1Records) {
			tempRecord = disk.getRecordAt(addr);
			avgRating += tempRecord.getAvgRating();
		}
		avgRating /= e1Records.size();
		Log.i("result="+avgRating);
	}

	public static void main(String[] args) {
		try {
			Log.setLevel(Log.LEVEL_DEBUG);
			new MainApp().run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
