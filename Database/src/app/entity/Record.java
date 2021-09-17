package app.entity;

// entity to store actual record 
public class Record {
	// make it public access first, getter and setter come later 
	public String key; // assume 10 chars, so it's 10 bytes
	// TODO: change to float?
	public double avgRating; // float is 4 bytes, double is 8 bytes
	public int numVotes; // int is 4 bytes
	
	public Record(String key, float avgRating, int numVotes) {
		this.key = key;
		this.avgRating = avgRating;
		this.numVotes = numVotes;
	}

	// theoretical size in bytes
	public static int size(){
		return 10+8+4;
	}

	@Override
	public String toString() {
		return "Record [key=" + key + ", avgRating=" + avgRating + ", numVotes=" + numVotes + "]";
	}
}
