package app;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
//		List<Record> records = Utility.readRecord(DATA_TEST_FILE_PATH);

		//TODO: generate sorted records (REMOVE LATER!!!!))
		List<Record> records = Utility.generateRecords(55,5);

		disk = new Disk(Constants.DISK_SIZE, blockSize);
		index = new BPlusTree(blockSize);

		Log.i(TAG,"Before insert into disk");
		Address recordAddr;
		for (Record r: records) {
			// inserting records into disk and create index!
			recordAddr = disk.appendRecord(r);
			index.insert( r.getNumVotes(), recordAddr);
		}
		Log.i(TAG,"After insert into disk");
		disk.log();
		index.logStructure();

		testSearch();


		// TODO do experiences
//		doExperience1();
	}

	private void testSearch(){
		Log.d(TAG, "Test Searching Index!!");
		ArrayList<Address> addresses = index.getRecordsWithKey(2);
		ArrayList<Record> records = disk.getRecords(addresses);
		if (addresses.size() != records.size()){
			Log.wtf(TAG, "ERROR!!!! Something Wrong");
		}
		for (int i=0; i<addresses.size(); i++) {
			Log.d(TAG, addresses.get(i).toString() + " -> "+records.get(i).toString());
		}
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
		System.out.print("Press any key to continue");
		scanner.nextLine();
	}

	public void displayMainMenu() throws Exception {
		String[] menu = {
				"Experience with block size 100B",
				"Experience with block size 500B",
				"Show team members",
//				"Log setting"
		};
		String input;
		do {
			System.out.println("CZ4031 - Database Assignment 1 (Group X)");
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
				case "3" -> {
					displayTeamMembers();
					pause();
				}
				case "4" -> displayLogSetting();
			}
		} while (!input.equals("q"));
	}

	// TODO: to be filled up?
	public void displayTeamMembers(){
		System.out.println("Team members");
		System.out.println("[1] <Matric No.> <Name>");
		System.out.println("[2] <Matric No.> <Name>");
		System.out.println("[3] <Matric No.> <Name>");
		System.out.println("[4] <Matric No.> <Name>");
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
			Log.setTimestampEnabled(false);
			MainApp app = new MainApp();
//			app.displayMainMenu();
			app.run(BLOCK_SIZE_100);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
