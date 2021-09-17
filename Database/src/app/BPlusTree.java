package app;

import app.entity.Address;
import app.util.Log;

import java.util.ArrayList;

public class BPlusTree {
    private static final int SIZE_POINTER = 6; // for 64 bits system
    private static final int SIZE_KEY = 4; // for int value
    int maxKeys;

    public BPlusTree(int blockSize){
        // TODO: calculate numkeys?
        maxKeys = (blockSize-SIZE_POINTER) / (SIZE_KEY+SIZE_POINTER);
        Log.i("BPlusTree.init: blockSize = "+blockSize+", maxKeys = "+maxKeys);
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
    public void removeRecordsWithKey(){

    }
}
