package app.entity;


public class Block {
    int maxRecords;
    int curRecords;
    Record[] data;

    public Block(int size){
        this.curRecords = 0;
        this.maxRecords = size / Record.size();
        this.data = new Record[maxRecords];
    }

    public boolean isAvailable(){
        return curRecords<maxRecords;
    }

    public int insertRecord(Record record) throws Exception {
        if (!isAvailable()){
            throw new Exception("Not enough space for insertion");
        }

        int offset = -1;
        // find first empty space, insert record
        for (int i = 0; i < data.length ; i++) {
            if (data[i] == null ){
                data[i] = record;
                offset = i;
                curRecords++;
                break;
            }
        }
        return offset;
    }

    public boolean deleteRecordAt(int offset){
        boolean success = false;
        if (data[offset]!=null){
            data[offset] = null;
            curRecords--;
            success = true;
        }
        return success;
    }

    public Record getRecordAt(int offset){
        return data[offset];
    }


}
