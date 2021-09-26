package app.storage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecordTest {

    @Test
    void testConstructor(){
        String tconst = "tt00000001";
        float avgRating = 3.5f;
        int numVotes = 1000;

        Record r = new Record(tconst, avgRating, numVotes);
        assertEquals(tconst, r.getTconst());
        assertEquals(avgRating, r.getAvgRating());
        assertEquals(numVotes, r.getNumVotes());
    }

    @Test
    void testSize(){
        int expectValue = 18;
        assertEquals(expectValue, Record.size());
    }

}