package app.entity;

import app.util.Log;

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

    public Address insertRecord(Record record) throws Exception {
        Block block;
        int index =-1;
        int offset = -1;
//        for(int i=0; i<blocks.size(); i++){
//            block = blocks.get(i);
//            if(block.isAvailable()){
//                index = i;
//                offset = block.insertRecord(record);
//                Log.d("records inserted at "+index+" with offset "+offset);
//                break;
//            }
//        }
        // assume prev block all filled ( which might not be the case...)
        if (blocks.size()>0){
            block = blocks.get(blocks.size()-1);
            if (block.isAvailable()){
                index = blocks.size()-1;
                offset = block.insertRecord(record);
                Log.d("records inserted at "+index+" with offset "+offset);
            }
        }
        // when no blocks are available, insert into a new block
        if (index == -1){
            if (blocks.size() == maxBlockCount){
                throw new Exception("Insufficient spaces on disk");
            }
            block = new Block(blockSize);
            blocks.add(block);
            index = blocks.size() -1;
            offset = block.insertRecord(record);
            Log.d("records inserted at "+index+" with offset "+offset);
        }
        recordCounts++;
        return new Address(index, offset);
        // TODO return Address(blockId, offset)
    }

    public Record getRecord(int blockId, int offset){
        return blocks.get(blockId).getRecordAt(offset);
    }

    public Record getRecord(Address address){
        return getRecord(address.getBlockId(), address.getOffset());
    }

    public int getBlocksCount(){
        return blocks.size();
    }

    public int getRecordCounts(){
        return recordCounts;
    }


    // debugs only
    public void log(){
        Log.i(TAG,"diskSize = "+diskSize+", blockSize = "+blockSize);
        Log.i(TAG,"records = "+recordCounts);
        Log.i(TAG,"blocks = "+blocks.size() + " / "+maxBlockCount);
    }
}
