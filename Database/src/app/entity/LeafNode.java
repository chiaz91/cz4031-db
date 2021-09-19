package app.entity;

import java.util.ArrayList;

public class LeafNode extends Node {

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

        System.out.println("insert record, preinserting key, no. of keys = " + getKeys().size());
        if (this.getRecords().size() == 0) {

            this.records.add(address);
            this.addKey(key);
            return 0;
        }

    //    System.out.println("post if block, no. of keys = " + getKeys().size());
        int index;
        index = super.addKey(key);

        System.out.println("inserting record    no. of records = " + records.size() + "     no. of keys = " + getKeys().size());
        records.add(address);

        for (int i = records.size() -2; i >= index; i--) 
            records.set(i+1, records.get(i));
        
        records.set(index, address);

        System.out.println("done inserting      no. of records = " + records.size());
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
}