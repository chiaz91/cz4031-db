package app.entity;

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
        Block availableBlock = null;
        int blockId =-1;
        int offset;
        // find the first available block
        for(int i=0; i<blocks.size(); i++){
            if(blocks.get(i).isAvailable()){
                availableBlock = getBlockAt(i);
                blockId = i;
                break;
            }
        }
        // when no blocks are available, create a new block
        if (availableBlock == null){
            if (blocks.size() == maxBlockCount){
                throw new Exception("Insufficient spaces on disk");
            }
            availableBlock = new Block(blockSize);
            blocks.add(availableBlock);
            blockId = getLastBlockId();
        }
        // insert record into available block
        offset = availableBlock.insertRecord(record);
        Log.v(String.format("Record inserted at %d-%d", blockId, offset));
        recordCounts++;
        return new Address(blockId, offset);
    }


    /**
     * Attempt to insert record into last block. if last block is not available, record will be inserted into newly created block.
     * noted that on  no checking of availability will be done on prev blocks
     * @param record inserting record
     * @return address of record being inserted
     * @throws Exception
     */
    public Address appendRecord(Record record) throws Exception{
        int blockId = -1;
        int offset;
        Block lastBlock=null;
        if (blocks.size()>0){
            blockId = getLastBlockId();
            lastBlock = getBlockAt(blockId);
        }
        // last block is not available/not exist, try to create a new block to insert the record
        if (lastBlock == null || !lastBlock.isAvailable()) {
            if (blocks.size() == maxBlockCount) {
                throw new Exception("Insufficient spaces on disk");
            }
            lastBlock = new Block(blockSize);
            blocks.add(lastBlock);
            blockId = getLastBlockId();
        }
        offset = lastBlock.insertRecord(record);
        recordCounts++;
        Log.v(String.format("Record inserted at %d-%d", blockId, offset));
        return new Address(blockId, offset);
    }

    private int getLastBlockId(){
        return blocks.size()-1;
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



    public void deleteRecordAt(int blockId, int offset) {
        getBlockAt(blockId).deleteRecordAt(offset);
    }

    public void deleteRecords(ArrayList<Address> recordAddresses){
        for (Address address: recordAddresses) {
            deleteRecordAt(address.getBlockId(), address.getOffset());
        }
    }


    // debugs only
    public void log(){
        Log.i(TAG, String.format("disk size = %s / %s", Utility.formatFileSize(getUsedSize()), Utility.formatFileSize(diskSize) ));
        Log.i(TAG, String.format("block size = %s", Utility.formatFileSize(blockSize)));
        Log.i(TAG, String.format("blocks = %,d / %,d", blocks.size(), maxBlockCount));
        Log.i(TAG, String.format("records = %,d", recordCounts));
    }
}
