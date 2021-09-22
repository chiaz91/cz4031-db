package app.entity;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DiskTest {
    final static int BLOCK_SIZE = 100;

    ArrayList<Record> generateRecords(int num){
        ArrayList<Record> records = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            String tconst = String.format("tt%08d", i+1);
            records.add( new Record(tconst, 0f, i+1));
        }
        return records;
    }



    @Test
    void testInit(){
        Disk disk = new Disk(1000, BLOCK_SIZE);

        assertEquals(1000, disk.diskSize);
        assertEquals(100, disk.blockSize);
        assertEquals(10, disk.maxBlockCount);
        assertEquals(0, disk.recordCounts);
        assertEquals(0, disk.getBlocksCount());
        assertEquals(0, disk.getUsedSize());
    }

    // INSERTING
    @Test
    void testInsertOne() throws Exception {
        Disk disk = new Disk(1000, BLOCK_SIZE);
        ArrayList<Record> records = generateRecords(1);

        Address address = disk.insertRecord(records.get(0));
        assertEquals(1, disk.recordCounts);
        assertEquals(1, disk.getBlocksCount());
        assertEquals(BLOCK_SIZE, disk.getUsedSize());
        assertEquals(0, address.blockId);
        assertEquals(0, address.offset);
    }

    @Test
    void testAppendOne() throws Exception {
        Disk disk = new Disk(1000, BLOCK_SIZE);
        ArrayList<Record> records = generateRecords(1);

        Address address = disk.appendRecord(records.get(0));
        assertEquals(1, disk.recordCounts);
        assertEquals(1, disk.getBlocksCount());
        assertEquals(BLOCK_SIZE, disk.getUsedSize());
        assertEquals(0, address.blockId);
        assertEquals(0, address.offset);
    }



    @Test
    void testInsertMultiple() throws Exception {
        // prepare
        Disk disk = new Disk(1000, BLOCK_SIZE);

        ArrayList<Record> records = generateRecords(10);
        for (int i=0; i<records.size(); i++) {
            Address address = disk.insertRecord(records.get(i));
            assertEquals(i/5, address.blockId);
            assertEquals(i%5, address.offset);
        }

        assertEquals(10, disk.recordCounts);
        assertEquals(2, disk.getBlocksCount());
    }

    @Test
    void testAppendMultiple() throws Exception {
        // prepare
        Disk disk = new Disk(1000, BLOCK_SIZE);

        ArrayList<Record> records = generateRecords(10);
        for (int i=0; i<records.size(); i++) {
            Address address = disk.appendRecord(records.get(i));
            assertEquals(i/5, address.blockId);
            assertEquals(i%5, address.offset);
        }

        assertEquals(10, disk.recordCounts);
        assertEquals(2, disk.getBlocksCount());
    }

    @Test
    void testFirstAvailableBlockId() throws Exception {
        // prepare
        Disk disk = new Disk(1000, BLOCK_SIZE);
        // check before data insert
        int blockId = disk.getFirstAvailableBlockId();
        assertEquals(-1, blockId);

        ArrayList<Record> records = generateRecords(11);
        for (int i=0; i<records.size(); i++) {
            disk.insertRecord(records.get(i));
        }
        disk.deleteRecordAt(0,1);

        blockId = disk.getFirstAvailableBlockId();
        assertEquals(0, blockId);
    }

    @Test
    void testLastBlockId() throws Exception {
        // prepare
        Disk disk = new Disk(1000, BLOCK_SIZE);
        // check before data insert
        int blockId = disk.getLastBlockId();
        assertEquals(-1, blockId);


        ArrayList<Record> records = generateRecords(11);
        for (int i=0; i<records.size(); i++) {
            disk.insertRecord(records.get(i));
        }
        disk.deleteRecordAt(0,1);

        blockId = disk.getLastBlockId();
        assertEquals(2, blockId);
    }


    @Test
    void testInsertAddress() throws Exception {
        // prepare
        Disk disk = new Disk(1000, BLOCK_SIZE);

        ArrayList<Record> records = generateRecords(11);
        for (int i=0; i<records.size()-1; i++) {
            disk.insertRecord(records.get(i));
        }
        disk.deleteRecordAt(0,1);
        assertEquals(9, disk.recordCounts);
        assertEquals(2, disk.getBlocksCount());
        assertEquals(BLOCK_SIZE*2, disk.getUsedSize());

        // do insert, check address
        Address address = disk.insertRecord(records.get(3));
        assertEquals(10, disk.recordCounts);
        assertEquals(2, disk.getBlocksCount());
        assertEquals(0, address.blockId);
        assertEquals(1, address.offset);
    }

    @Test
    void testAppendAddress() throws Exception {
        // prepare
        Disk disk = new Disk(1000, BLOCK_SIZE);

        ArrayList<Record> records = generateRecords(11);
        for (int i=0; i<records.size()-1; i++) {
            disk.insertRecord(records.get(i));
        }
        disk.deleteRecordAt(0,1);
        assertEquals(9, disk.recordCounts);
        assertEquals(2, disk.getBlocksCount());
        assertEquals(BLOCK_SIZE*2, disk.getUsedSize());

        // do append, check address
        Address address = disk.appendRecord(records.get(3));
        assertEquals(10, disk.recordCounts);
        assertEquals(3, disk.getBlocksCount());
        assertEquals(2, address.blockId);
        assertEquals(0, address.offset);
    }



    @Test
    void testInsufficientSpace() throws InterruptedException {
        Disk disk = new Disk(100, BLOCK_SIZE);
        ArrayList<Record> records = generateRecords(6);
        // with disk size 100 and block size 100, it should be only allow holding of 5 records, adding 6th will throw exception

        Throwable exception = assertThrows(Exception.class, ()->{
            for (Record r :records) {
                disk.appendRecord(r);
            }
        });
        assertEquals("Insufficient spaces on disk", exception.getMessage() );
    }

    // RETRIEVING
    @Test
    void testGetRecordAt() throws Exception {
        // prepare
        Disk disk = new Disk(1000, BLOCK_SIZE);
        ArrayList<Record> records = generateRecords(6);
        for (Record r: records) {
            disk.appendRecord(r);
        }

        Record expecting = records.get(1);
        // by int
        Record r = disk.getRecordAt(0,1);
        assertEquals(expecting, r);
        // by address
        Address address = new Address(0, 1);
        r = disk.getRecordAt(address);
        assertEquals(expecting, r);
    }




    // DELETION
    @Test
    void testDelete() throws Exception {
        // prepare
        Disk disk = new Disk(1000, BLOCK_SIZE);
        ArrayList<Record> records = generateRecords(1);

        disk.appendRecord(records.get(0));
        assertEquals(1, disk.recordCounts);
        assertEquals(1, disk.getBlocksCount());

        // do delete
        boolean success = disk.deleteRecordAt(0,0);
        assertTrue(success);
        assertEquals(0, disk.recordCounts);
        assertEquals(1, disk.getBlocksCount());
        assertEquals(BLOCK_SIZE, disk.getUsedSize());
    }


    @Test
    void testDeleteWhenEmpty() throws Exception {
        // prepare
        Disk disk = new Disk(1000, BLOCK_SIZE);

        // do delete, when no blocks are created yet
        Throwable exception = assertThrows(Exception.class, ()->{
            disk.deleteRecordAt(0,0);
        });
        assertEquals("Index 0 out of bounds for length 0", exception.getMessage());
    }

    @Test
    void testDeleteDuplicate() throws Exception {
        // prepare
        Disk disk = new Disk(1000, BLOCK_SIZE);
        ArrayList<Record> records = generateRecords(1);
        disk.appendRecord(records.get(0));
        assertEquals(1, disk.recordCounts);
        assertEquals(1, disk.getBlocksCount());

        // do delete
        boolean success = disk.deleteRecordAt(0,0);
        assertTrue(success);
        assertEquals(0, disk.recordCounts);
        assertEquals(1, disk.getBlocksCount());
        // repeat delete
        success = disk.deleteRecordAt(0,0);
        assertFalse(success);
        assertEquals(0, disk.recordCounts);
        assertEquals(1, disk.getBlocksCount());
    }


    @Test
    void testDeleteByListAddress() throws Exception {
        // prepare
        Disk disk = new Disk(1000, BLOCK_SIZE);
        ArrayList<Record> records = generateRecords(6);
        ArrayList<Address> addresses = new ArrayList<>();

        for (Record r : records) {
            Address newAddr = disk.appendRecord(r);
            addresses.add(newAddr);
        }
        assertEquals(6, disk.recordCounts);
        assertEquals(2, disk.getBlocksCount());
        assertEquals(6, addresses.size());

        // remove all records with list of address
        disk.deleteRecords(addresses);
        assertEquals(0, disk.recordCounts);
        assertEquals(2, disk.getBlocksCount());
    }


}