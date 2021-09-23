package app.index;

import app.storage.Address;
import app.util.Log;

import java.util.ArrayList;

public class LeafNode extends Node {
    private static final String TAG = "Leaf";

    private ArrayList<Address> records;
    private Node next;

    // constructor
    public LeafNode() {

        super();
        records = new ArrayList<Address>(4);
        setIsLeaf(true);
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

        records.add(address);

        for (int i = records.size() -2; i >= index; i--) 
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

    public void splitPrep() {

        deleteKeys();
        records = new ArrayList<Address>(4);
    }


    @Override
    void logStructure() {
        Log.d(TAG, this.toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("L[");
        for (int i=0; i<getKeys().size(); i++){
            if (i>0){
                sb.append(",");
            }
             sb.append(String.format("{%d-%d=>%s}", i, getKey(i), getRecord(i)));
        }
        sb.append("]");
        return sb.toString();
    }
}