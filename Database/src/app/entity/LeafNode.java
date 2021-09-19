package app.entity;
import java.util.ArrayList;

public class LeafNode extends Node {

    private ArrayList<Address> records;
    private Node next;

    // constructor
    public LeafNode() {

        super();
        records = new ArrayList<Address>(4);
        super.setIsLeaf(true);
    }

    // get arraylist of all records
    public ArrayList<Address> getRecords() {

        return records;
    }

    // get record at index
    public Address getRecord(int index) {

        return records.get(index);
    }

    // add record
    public int addRecord(int key, Address address) {

        if (this.getRecords().size() == 0) {

            this.records.add(address);
            this.addKey(key);
            return 0;
        }

        int index;
        index = super.addKey(key);

        for (int i = records.size() -1; i >= index; i--) 
            records.set(i+1, records.get(i));
        
        records.set(index, address);
        return index;
    }

    // get next leafnode
    public Node getNext() {

        return next;
    }

    // set next leafnode
    public void setNext(Node sister) {

        next = sister;
    }
}