package app.util;

import app.entity.Record;

public class Analyzer {
    public static int maxKeyLength = Integer.MIN_VALUE;
    public static double maxAvgRating = Double.MIN_VALUE;
    public static double minAvgRating = Double.MAX_VALUE;
    public static int maxVotes= Integer.MIN_VALUE;
    public static int minVotes= Integer.MAX_VALUE;


    public static  void analysis(Record record){
        if (record.key.length() > maxKeyLength){
            maxKeyLength = record.key.length();
        }

        if (record.numVotes > maxVotes){
            maxVotes = record.numVotes;
        }

        if (record.numVotes < minVotes){
            minVotes = record.numVotes;
        }

        if (record.avgRating > maxAvgRating){
            maxAvgRating = record.avgRating;
        }
        if (record.avgRating < minAvgRating){
            minAvgRating = record.avgRating;
        }
    }

    public static void log(){
        Log.i("Analyzer.maxKeyLength = "+maxKeyLength);
        Log.i(String.format("Analyzer.avgRating= %.5f - %.5f", minAvgRating, maxAvgRating));
        Log.i(String.format("Analyzer.numVotes= %d - %d", minVotes, maxVotes));
    }

    public static void reset(){
        maxKeyLength = Integer.MIN_VALUE;
        maxAvgRating = Double.MIN_VALUE;
        minAvgRating = Double.MAX_VALUE;
        maxVotes= Integer.MIN_VALUE;
        minVotes= Integer.MAX_VALUE;
    }

}
