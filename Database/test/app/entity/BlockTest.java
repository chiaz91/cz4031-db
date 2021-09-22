package app.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlockTest {
    final int BLOCK_SIZE_100 = 100;
    final int BLOCK_SIZE_500 = 500;
    final int RECORD_SIZE = 18;
    Record r1 = new Record("tt00000001", 0.0f, 1);
    Record r2 = new Record("tt00000002", 0.0f, 2);
    Record r3 = new Record("tt00000003", 0.0f, 3);
    Record r4 = new Record("tt00000004", 0.0f, 4);
    Record r5 = new Record("tt00000005", 0.0f, 5);
    Record r6 = new Record("tt00000005", 0.0f, 6);

    @Test
    void testMaxRecordSizes(){
        int max100 = BLOCK_SIZE_100 / RECORD_SIZE;
        int max500 = BLOCK_SIZE_500 / RECORD_SIZE;

        Block block100 = new Block(BLOCK_SIZE_100);
        Block block500 = new Block(BLOCK_SIZE_500);

        // check the max record sizes is calculated correctly
        assertEquals(max100, block100.maxRecords);
        assertEquals(max500, block500.maxRecords);
    }

    @Test
    void testInsertOne() throws Exception {
        Block block = new Block(BLOCK_SIZE_100);
        // check record can be inserted into block
        assertTrue(block.isAvailable());
        int offset = block.insertRecord(r1);

        assertEquals(0, offset);
        assertEquals(1, block.curRecords);
    }

    @Test
    void testInsertMulti() throws Exception {
        Block block = new Block(BLOCK_SIZE_100);
        int offset;
        // insert 5 records, check block is no longer available
        assertTrue(block.isAvailable());
        offset = block.insertRecord(r1);
        assertEquals(0, offset);
        offset = block.insertRecord(r2);
        assertEquals(1, offset);
        offset = block.insertRecord(r3);
        assertEquals(2, offset);
        offset = block.insertRecord(r4);
        assertEquals(3, offset);
        offset = block.insertRecord(r5);
        assertEquals(4, offset);
        assertEquals(5, block.curRecords);
        assertFalse(block.isAvailable());
    }

    @Test
    void testDeleteOne() throws Exception {
        Block block = new Block(BLOCK_SIZE_100);
        block.insertRecord(r1);


        // check record is indeed deleted
        assertEquals(1, block.curRecords);
        block.deleteRecordAt(0);
        assertEquals(0, block.curRecords);
    }

    @Test
    void testInsertAfterDelete() throws Exception {
        Block block = new Block(BLOCK_SIZE_100);
        int offset;

        // test record can correctly insert to first empty space
        block.insertRecord(r1);
        block.insertRecord(r2);
        block.insertRecord(r3);
        assertEquals(3, block.curRecords);
        block.deleteRecordAt(1);
        assertEquals(2, block.curRecords);
        offset = block.insertRecord(r4);
        assertEquals(1, offset);
    }


    
    @Test
    void testNotEnoughSpace(){
        // adding record when no space available, an exception is thrown
        Block block = new Block(BLOCK_SIZE_100);
        Throwable exception = assertThrows(Exception.class, ()->{
            block.insertRecord(r1);
            block.insertRecord(r2);
            block.insertRecord(r3);
            block.insertRecord(r4);
            block.insertRecord(r5);
            block.insertRecord(r6);
        });

        assertEquals("Not enough space for insertion", exception.getMessage());
    }

}