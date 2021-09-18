package app;

import app.entity.Address;
import app.util.Log;

import java.util.ArrayList;

public class BPlusTree {
    private static final String TAG = "B+Tree";
    private static final int SIZE_POINTER = 6; // for 64 bits system
    private static final int SIZE_KEY = 4; // for int value
    int maxKeys;
    int parentMinKeys;
    int leafMinKeys;

    public BPlusTree(int blockSize){
        // TODO: calculate n (max number of keys)?
        maxKeys = (blockSize-SIZE_POINTER) / (SIZE_KEY+SIZE_POINTER);
        parentMinKeys = (int) Math.floor(maxKeys/2);
        leafMinKeys = (int) Math.floor((maxKeys+1)/2);
        Log.i(TAG, "init: blockSize = "+blockSize+", maxKeys = "+maxKeys);
        Log.i(TAG, "MinKeys: parent="+parentMinKeys+", leaf="+leafMinKeys);
    }

//    private abstract static class Node{
//        public int[] keys;
//        public boolean isRoot;
//        public boolean isLeaf;
//
//        public Node(int n){
//            keys = new int[n];
//            isRoot = false;
//            isLeaf = false;
//        }
//
//    }
//
//    private class ParentNode extends Node{
//        Node[] pointers;
//        public ParentNode(int n){
//            super(n);
//            pointers = new Node[n+1];
//        }
//
//    }
//
//    private class LeafNode extends Node{
//        Address[] pointers;
//        LeafNode next;
//        public LeafNode(int n){
//            super(n);
//            pointers = new Address[n];
//            isLeaf = true;
//        }
//    }

    // TODO for Experiment 2
    public void insert(int key, Address address){
        // TODO: construct tree with node
    }

    // TODO for Experiment 3
    public ArrayList<Address> getRecordsWithKey(int key){
        // TODO: traverse through the tree, loop the leaf nodes with same key
        return null;
    }

    // TODO for Experiment 4
    public ArrayList<Address> getRecordsWithKeyInRange(int min, int max){
        // TODO: traverse through the tree, loop the leaf nodes with keys in range
        return null;
    }

    // TODO for Experiment 5
    public ArrayList<Address> removeRecordsWithKey(){
        // list of address need to be return, so app can use it to delete records from disk
        return null;
    }
}
