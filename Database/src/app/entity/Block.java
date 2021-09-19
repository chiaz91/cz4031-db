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
        if (offset == -1){
            throw new Exception("Not enough space for insertion");
        }
        return offset;
    }

    public void deleteRecordAt(int offset){
        data[offset] = null;
    }

    public Record getRecordAt(int offset){
        return data[offset];
    }


}
