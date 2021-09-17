package app.entity;

public class Address {
    int blockId;
    int offset;

    public Address(int blockId, int offset){
        this.blockId=blockId;
        this.offset=offset;
    }

    public int getBlockId() {
        return blockId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
