package app.storage;


public class Record {
	public String tconst;
	public float avgRating;
	public int numVotes;
	
	public Record(String tconst, float avgRating, int numVotes) {
		this.tconst = tconst;
		this.avgRating = avgRating;
		this.numVotes = numVotes;
	}

	public String getTconst() {
		return tconst;
	}

	public void setTconst(String tconst) {
		this.tconst = tconst;
	}

	public float getAvgRating() {
		return avgRating;
	}

	public void setAvgRating(float avgRating) {
		this.avgRating = avgRating;
	}

	public int getNumVotes() {
		return numVotes;
	}

	public void setNumVotes(int numVotes) {
		this.numVotes = numVotes;
	}

	/**
	 * Theoretical size in bytes with assumption of...
	 * - tconst is fixed with 10 chars -> 10B
	 * - avgRating is float -> 4B
	 * - numVote is int -> 4B
	 * @return size of record
	 */
	public static int size(){
		return 18;
	}

	@Override
	public String toString() {
		return "Record [tconst=" + tconst + ", avgRating=" + avgRating + ", numVotes=" + numVotes + "]";
	}
}
