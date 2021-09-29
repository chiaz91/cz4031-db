package app;

import java.util.*;

import app.storage.Address;
import app.storage.Disk;
import app.storage.Record;
import app.index.BPlusTree;
import app.util.Log;
import app.util.Utility;

public class MainApp implements Constants {
	private static final String TAG = "App";
	Scanner scanner = new Scanner(System.in);
	private Disk disk;
	private BPlusTree index;


	public void run(int blockSize) throws Exception {
		// read records from data file
		List<Record> records = Utility.readRecord(DATA_FILE_PATH);

		disk = new Disk(Constants.DISK_SIZE, blockSize);
		index = new BPlusTree(blockSize);

		Log.i(TAG,"Running program with block size of "+blockSize);
		Log.i(TAG,"Prepare to insert records into storage and create index");
		Address recordAddr;
		for (Record r: records) {
			// inserting records into disk and create index!
			recordAddr = disk.appendRecord(r);
			index.insert( r.getNumVotes(), recordAddr);
		}
		Log.i(TAG,"Record inserted into storage and index created");
		disk.log();
//		index.logStructure(1); // printing root and first level?

		index.treeStats();

		// TODO do experiences
		pause("Press any key to start experience 3");
		doExperience3();
		pause("Press any key to start experience 4");
		doExperience4();
//		pause("Press any key to start experience 5");
		doExperience5();
	}


	public void doExperience3(){
		Log.i(TAG,"Experience 3 started, getting records with numVotes of 500");
		ArrayList<Address> e3RecordAddresses = index.getRecordsWithKey(500);
		ArrayList<Record> records = disk.getRecords(e3RecordAddresses);
		// records collected, do calculate average rating
		double avgRating = 0;
		for (Record record: records) {
			avgRating += record.getAvgRating();
		}
		avgRating /= records.size();
		Log.i("Average rating="+avgRating);
	}

	public void doExperience4(){
		Log.i(TAG,"Experience 4 started, getting records with numVotes between 30k-40k ");
		ArrayList<Address> e4RecordAddresses = index.getRecordsWithKeyInRange(30000,40000);
		ArrayList<Record> records = disk.getRecords(e4RecordAddresses);
		// records collected, do calculate average rating
		double avgRating = 0;
		for (Record record: records) {
			avgRating += record.getAvgRating();
		}
		avgRating /= records.size();
		Log.i("Average rating="+avgRating);
	}

	// TODO: complete experience 5
	public void doExperience5(){

	}


	// TODO: removing the test run
	public void testRun(int blockSize) throws Exception {
		// read records from data file
		//TODO: generate sorted records (REMOVE LATER!!!!))
		List<Record> records = Utility.generateRecords(55,6);
		ArrayList<Integer> observingIdx = new ArrayList<>(Arrays.asList(9,10,54,55/*,324,325*/)); // for n=9, h increase on 10, 55, 325

		disk = new Disk(Constants.DISK_SIZE, blockSize);
		index = new BPlusTree(blockSize);

		Log.i(TAG,"Before insert into disk");
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


		// TODO: TO DELETE FOLLOWING
		// try search valid and invalid keys
		for (int i = -1; i < 11; i++) {
			testSearch(i);
		}
//		testSearch2(1,5);
	}

	private void testSearch(int key){
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

	private void testSearch2(int min, int max){
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
	// TEST RUN ABOVE


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

	public void displayMainMenu() throws Exception {
		String[] menu = {
				"Experience with block size 100B",
				"Experience with block size 500B",
				"Log setting"
		};
		String input;
		do {
			System.out.println("CZ4031 - Database Assignment 1 (Group "+GROUP_NUM+")");
			input = getOptions(menu, true);
			switch (input) {
				case "1" -> {
					run(BLOCK_SIZE_100);
					pause();
				}
				case "2" -> {
					run(BLOCK_SIZE_500);
					pause();
				}
				case "3" -> displayLogSetting();
			}
		} while (!input.equals("q"));
	}

	public void displayLogSetting(){
		String[] menu;
		String input;
		do {
			menu = new String[]{
					String.format("Adjust log level (current: %s)", Log.getLogLevelString()),
					String.format("include timestamp (current %b)", Log.isTimestampEnabled()),
					String.format("change timestamp format (current: %s)", Log.getTimestampFormat())
			};
			System.out.println("Log Setting");
			input = getOptions(menu, true);
			switch (input){
				case "1" -> adjustLogLevel();
				case "2" -> adjustLogTimestamp();
				case "3" -> adjustLogTimestampFormat();
			}
		} while (!input.equals("q"));

	}

	private void adjustLogLevel(){
		String[] menu = {
				"None", "Error", "Warn", "Info", "Debug", "Verbose"
		};
		String input = getOptions(menu, false);
		switch (input){
			case "1" -> Log.setLevel(Log.LEVEL_NONE);
			case "2" -> Log.setLevel(Log.LEVEL_ERROR);
			case "3" -> Log.setLevel(Log.LEVEL_WARN);
			case "4" -> Log.setLevel(Log.LEVEL_INFO);
			case "5" -> Log.setLevel(Log.LEVEL_DEBUG);
			case "6" -> Log.setLevel(Log.LEVEL_VERBOSE);
		}
	}
	private void adjustLogTimestamp(){
		String[] menu = {"Enable", "Disable"};
		String input = getOptions(menu, false);
		switch (input){
			case "1" -> Log.setTimestampEnabled(true);
			case "2" -> Log.setTimestampEnabled(false);
		}
	}
	private void adjustLogTimestampFormat(){
		String[] menu = {"Detail", "Simple"};
		String input = getOptions(menu, false);
		switch (input){
			case "1" -> Log.setTimestampFormat(Log.FORMAT_DETAIL);
			case "2" -> Log.setTimestampFormat(Log.FORMAT_SIMPLE);
		}
	}





	public static void main(String[] args) {
		try {
			Log.setLevel(Log.LEVEL_DEBUG);
			MainApp app = new MainApp();
			// TODO: change to display main menu later
			app.displayMainMenu();
//			app.run(BLOCK_SIZE_100);
//			app.testRun(BLOCK_SIZE_100);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
