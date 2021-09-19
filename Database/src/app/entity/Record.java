package app.entity;

// entity to store actual record 
public class Record {
	// make it public access first, getter and setter come later 
	public String tconst; // assume 10 chars, so it's 10 bytes
	// TODO: change to float?
	public float avgRating; // float is 4 bytes, double is 8 bytes
	public int numVotes; // int is 4 bytes
	
	public Record(String tconst, float avgRating, int numVotes) {
		this.tconst = tconst;
		this.avgRating = avgRating;
		this.numVotes = numVotes;
	}

	// theoretical size in bytes
	public static int size(){
		return 10+4+4;
	}

	@Override
	public String toString() {
		return "Record [tconst=" + tconst + ", avgRating=" + avgRating + ", numVotes=" + numVotes + "]";
	}
}
