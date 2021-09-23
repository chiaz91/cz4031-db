package app.storage;

import app.util.Log;
import app.util.Utility;

import java.util.ArrayList;

public class Disk {
    private static final String TAG = "Disk";
    int diskSize;
    int maxBlockCount;
    int blockSize;
    int recordCounts;
    ArrayList<Block> blocks;

    public Disk(int diskSize, int blockSize){
        this.diskSize = diskSize;
        this.blockSize = blockSize;
        this.maxBlockCount = diskSize / blockSize;
        this.blocks = new ArrayList<>();
        this.recordCounts = 0;
        log();
    }

    /**
     * Get the total number of blocks exist in the storage
     * @return
     */
    public int getBlocksCount(){
        return blocks.size();
    }

    /**
     * Get the total number of records exist in the storage
     * @return
     */
    public int getRecordCounts(){
        return recordCounts;
    }

    /**
     * Get the used size of storage
     * @return
     */
    public int getUsedSize(){
        return getBlocksCount() * blockSize;
    }


    /**
     * insert the records into first available block for record insertion, however, it can be expensive!!!
     * @param record inserting record
     * @return address of record being inserted
     * @throws Exception
     */
    public Address insertRecord(Record record) throws Exception {
        int blockId = getFirstAvailableBlockId();
        return insertRecordAt(blockId, record);
    }

    /**
     * Attempt to insert record into last block. if last block is not available, record will be inserted into newly created block.
     * noted that on  no checking of availability will be done on prev blocks
     * @param record inserting record
     * @return address of record being inserted
     * @throws Exception
     */
    public Address appendRecord(Record record) throws Exception{
        int blockId = getLastBlockId();
        return insertRecordAt(blockId, record);
    }

    private Address insertRecordAt(int blockId, Record record) throws Exception {
        Block block = null;
        if (blockId>=0){
            block = getBlockAt(blockId);
        }

        // block is not available/not exist, try to create a new block to insert the record
        if (block == null || !block.isAvailable()) {
            if (blocks.size() == maxBlockCount) {
                throw new Exception("Insufficient spaces on disk");
            }
            block = new Block(blockSize);
            blocks.add(block);
            blockId = getLastBlockId();
        }
        int offset = block.insertRecord(record);
        recordCounts++;
        Log.v(String.format("Record inserted at %d-%d", blockId, offset));
        return new Address(blockId, offset);
    }


    public int getFirstAvailableBlockId(){
        int blockId = -1;
        for(int i=0; i<blocks.size(); i++){
            if(blocks.get(i).isAvailable()){
                blockId = i;
                break;
            }
        }
        return blockId;
    }

    public int getLastBlockId(){
        return blocks.size()>0? blocks.size()-1:-1;
    }

    public Block getBlockAt(int blockId){
        return blocks.get(blockId);
    }

    public Record getRecordAt(int blockId, int offset){
        return getBlockAt(blockId).getRecordAt(offset);
    }

    public Record getRecordAt(Address address){
        return getRecordAt(address.getBlockId(), address.getOffset());
    }

    public ArrayList<Record> getRecords(ArrayList<Address> addresses ){
        ArrayList<Record> records = new ArrayList<>();
        for (Address address: addresses) {
            records.add(getRecordAt(address));
        }
        return records;
    }



    public boolean deleteRecordAt(int blockId, int offset) {
        boolean success = getBlockAt(blockId).deleteRecordAt(offset);
        if (success) {
            recordCounts--;
        }
        return success;
    }

    public void deleteRecords(ArrayList<Address> recordAddresses){
        for (Address address: recordAddresses) {
            deleteRecordAt(address.getBlockId(), address.getOffset());
        }
    }


    // debugs only
    public void log(){
        Log.d(TAG, String.format("disk size = %s / %s", Utility.formatFileSize(getUsedSize()), Utility.formatFileSize(diskSize) ));
        Log.d(TAG, String.format("block size = %s", Utility.formatFileSize(blockSize)));
        Log.d(TAG, String.format("blocks = %,d / %,d", blocks.size(), maxBlockCount));
        Log.d(TAG, String.format("records = %,d", recordCounts));
    }
}
