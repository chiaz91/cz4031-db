package app;

import app.index.BPlusTree;
import app.storage.Address;
import app.storage.Disk;
import app.storage.Record;
import app.util.Log;
import app.util.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


// TODO this is another app just for manual testing.. will be delete once testing completed
public class TestApp implements Constants {
	private static final String TAG = "App";
	Scanner scanner = new Scanner(System.in);
	private Disk disk;
	private BPlusTree index;

	public void test1(int blockSize, int numRecords, int... deletingKeys) throws Exception {
		List<Record> records = Utility.generateRecords(numRecords);
		ArrayList<Integer> observingIdx = new ArrayList<>(Arrays.asList(9,10,54,55/*,324,325*/)); // for n=9, h increase on 10, 55, 325

		disk = new Disk(Constants.DISK_SIZE, blockSize);
		index = new BPlusTree(blockSize);
		int c =0;
		Address recordAddr;
		for (Record r: records) {
			// inserting records into disk and create index!
			recordAddr = disk.appendRecord(r);
			index.insert( r.getNumVotes(), recordAddr);
			// TODO: REMOVE TESTING
			c++;
			if ( observingIdx.contains(c) ){
				Log.d("TEST", "OBSERVING ON "+c);
				index.logStructure();
			}
		}
		Log.i(TAG,"After insert into disk");
		disk.log();
		index.logStructure();

		for (int key: deletingKeys){
			testDeletion(key);
		}
	}

	private void testKeySearch(int key){
		Log.d("TEST", "SEARCHING ON KEY "+key+"!!!");
		ArrayList<Address> addresses = index.getRecordsWithKey(key);
		ArrayList<Record> records =  disk.getRecords(addresses);
		if (addresses.size() != records.size()){
			Log.wtf("TEST", "ERROR!!!! Something Wrong");
		}
		for (int i=0; i<addresses.size(); i++) {
			Log.d("TEST", addresses.get(i).toString() + " -> "+records.get(i).toString());
		}
		Log.d("TEST", "------------------------------------------------------");
	}

	private void testRangeSearch(int min, int max){
		Log.d("TEST", "SEARCHING ON RANGE "+min+"~"+max+"!!!");
		ArrayList<Address> addresses = index.getRecordsWithKeyInRange(min, max);
		ArrayList<Record> records = disk.getRecords(addresses);
		if (addresses.size() != records.size()){
			Log.wtf("TEST", "ERROR!!!! Something Wrong");
		}
		for (int i=0; i<addresses.size(); i++) {
			Log.d("TEST", addresses.get(i).toString() + " -> "+records.get(i).toString());
		}
		Log.d("TEST", "------------------------------------------------------");
	}

	private void testDeletion(int key){
		Log.d("TEST", "testDeletion ON KEY "+key+"!!!");
		index.deleteKey(key);
		index.logStructure();
		Log.d("TEST", "------------------------------------------------------");
	}




	// app menu
	private String getOptions(String[] options, boolean includeQuit){
		for (int i = 0; i < options.length; i++) {
			System.out.println(String.format("[%d] %s",i+1, options[i]));
		}
		if (includeQuit){
			System.out.println("[q] quit");
		}
		System.out.print("Enter the option: ");
		return scanner.nextLine();
	}

	private void pause(){
		pause(null);
	}
	private void pause(String message){
		if (message == null){
			message = "Press any key to continue";
		}
		System.out.print(message);
		scanner.nextLine();
	}



	public static void main(String[] args) {
		try {
			Log.setLevel(Log.LEVEL_DEBUG);
			TestApp app = new TestApp();
			app.test1(BLOCK_SIZE_100, 10, 5);
//			app.pause();
//			app.test1(BLOCK_SIZE_100, 15, 15,14,5,4);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
