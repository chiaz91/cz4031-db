package app.index;

import app.storage.Address;

import java.util.ArrayList;

public class LeafNode extends Node {

    private ArrayList<Address> records;
    private Node next;

    // constructor
    public LeafNode(int max) {

        super(max);
        records = new ArrayList<Address>();
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
        records = new ArrayList<Address>();
    }
}