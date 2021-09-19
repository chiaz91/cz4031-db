package app.util;

import app.entity.Record;

public class Analyzer {
    private static final String TAG = "Analyzer";

    public static int minKeyLength = Integer.MAX_VALUE;
    public static int maxKeyLength = Integer.MIN_VALUE;
    public static double maxAvgRating = Double.MIN_VALUE;
    public static double minAvgRating = Double.MAX_VALUE;
    public static int maxVotes= Integer.MIN_VALUE;
    public static int minVotes= Integer.MAX_VALUE;


    public static  void analysis(Record record){
        if (record.getTconst().length() < minKeyLength){
            minKeyLength = record.getTconst().length();
        }
        if (record.getTconst().length() > maxKeyLength){
            maxKeyLength = record.getTconst().length();
        }

        if (record.getNumVotes() > maxVotes){
            maxVotes = record.getNumVotes();
        }
        if (record.getNumVotes() < minVotes){
            minVotes = record.getNumVotes();
        }

        if (record.getAvgRating() > maxAvgRating){
            maxAvgRating = record.getAvgRating();
        }
        if (record.getAvgRating() < minAvgRating){
            minAvgRating = record.getAvgRating();
        }
    }

    public static void log(){
        Log.i(TAG, String.format("keyLength = %d - %d", minKeyLength, maxKeyLength));
        Log.i(TAG, String.format("avgRating= %.2f - %.2f", minAvgRating, maxAvgRating));
        Log.i(TAG, String.format("numVotes= %,d - %,d", minVotes, maxVotes));
    }

    public static void reset(){
        minKeyLength = Integer.MAX_VALUE;
        maxKeyLength = Integer.MIN_VALUE;
        maxAvgRating = Double.MIN_VALUE;
        minAvgRating = Double.MAX_VALUE;
        maxVotes= Integer.MIN_VALUE;
        minVotes= Integer.MAX_VALUE;
    }

}
