package app.index;

import app.storage.Address;
import app.util.Log;
import java.util.ArrayList;

public class BPlusTree {
    private static final String TAG = "B+Tree";
    private static final int SIZE_POINTER = 6; // for 64 bits system
    private static final int SIZE_KEY = 4; // for int value
    int maxKeys;
    int parentMinKeys;
    int leafMinKeys;
    Node root;

    public BPlusTree(int blockSize){
        // TODO: calculate n (max number of keys)?
        maxKeys = (blockSize-SIZE_POINTER) / (SIZE_KEY+SIZE_POINTER);
        parentMinKeys = (int) Math.floor(maxKeys/2);
        leafMinKeys = (int) Math.floor((maxKeys+1)/2);
        Log.i(TAG, "init: blockSize = "+blockSize+", maxKeys = "+maxKeys);
        Log.i(TAG, "MinKeys: parent="+parentMinKeys+", leaf="+leafMinKeys);
        root = createFirst(maxKeys);
    }

    // to create first node
    public LeafNode createFirst(int max) {

        LeafNode newRoot = new LeafNode(max);
        newRoot.setIsRoot(true);
        return newRoot;
    }

    // to insert a record into the tree
    public void insert(int key, Address address) {

        this.insertToLeaf(this.searchLeaf(key), key, address);
    }

    // to search for the right leafnode for record insertion
    public LeafNode searchLeaf(int key) {

        // if root is a leaf, return root
        if (this.root.getIsLeaf())
            return (LeafNode) root;

        ParentNode parent = (ParentNode) root;
        ArrayList<Integer> keys;

        // finding correct first level parent
        while (!parent.getChild(0).getIsLeaf()) {

            keys = parent.getKeys();
            
            for (int i = keys.size() -1; i >= 0; i--) {

                if (keys.get(i) <= key) {

                    parent = (ParentNode) parent.getChild(i+1);
                    break;
                }

                else if (i == 0)
                    parent = (ParentNode) parent.getChild(0);
            }
        }

        // finding correct leaf
        keys = parent.getKeys();
        for (int i = keys.size() -1; i >= 0; i--) {

            if (keys.get(i) <= key)
                return (LeafNode) parent.getChild(i+1);
        }

        return (LeafNode) parent.getChild(0);
    }

    // to insert record into leafnode
    public void insertToLeaf(LeafNode leaf, int key, Address address) {

        if (leaf.getKeys().size() < maxKeys) 
            leaf.addRecord(key, address);

        else {

            splitLeaf(leaf, key, address);
        }
    }

    //to split a full leafnode
    public void splitLeaf(LeafNode old,int key, Address address) {

        int keys[] = new int[maxKeys+1];
        Address addresses[] = new Address[maxKeys+1];
        LeafNode leaf2 = new LeafNode(maxKeys);
        int i;

        //getting full and sorted lists of keys and addresses
        for (i = 0; i < maxKeys; i++) {

            keys[i] = old.getKey(i);
            addresses[i] = old.getRecord(i);
        }

        for (i = maxKeys-1; i >= 0; i--) {
            
            if (keys[i] <= key) {

                i++;
                keys[i] = key;
                addresses[i] = address;
                break;
            }

            keys[i+1] = keys[i];
            addresses[i+1] = addresses[i];
        } 

        //clearing old leafnode values
        old.splitPrep();

        //putting the keys and addresses into the two leafnodes
        for (i = 0; i < leafMinKeys-1; i++) 
            old.addRecord(keys[i], addresses[i]);

        for (i = leafMinKeys-1; i < maxKeys+1; i++) 
            leaf2.addRecord(keys[i], addresses[i]);

        //setting old leafnode to point to new leafnode and new leafnode to point to next leafnode
        leaf2.setNext(old.getNext());
        old.setNext(leaf2);

        //setting parents for new leafnode
        if (old.getIsRoot()) {

            ParentNode newRoot = new ParentNode(maxKeys);
            old.setIsRoot(false);
            newRoot.setIsRoot(true);
            newRoot.addChild(old);
            newRoot.addChild(leaf2);
            root = newRoot;
        }

        else if (old.getParent().getKeys().size() < maxKeys)
            old.getParent().addChild(leaf2);

        else 
            splitParent(old.getParent(), leaf2);
    }

    //to split a full parent node
    public void splitParent(ParentNode parent, Node child) {

        Node children[] = new Node[maxKeys+2];
        int keys[] = new int[maxKeys+2];
        int key = child.findSmallestKey();
        ParentNode parent2 = new ParentNode(maxKeys);

        // getting full and sorted lists of keys and children
        for (int i = 0; i < maxKeys+1; i++)  {

            children[i] = parent.getChild(i);
            keys[i] = children[i].findSmallestKey();
        }
        
        for (int i = maxKeys; i >= 0; i--) {

            if (keys[i] <= key) {

                i++;
                keys[i] = key;
                children[i] = child;
                break;
            }

            keys[i+1] = keys[i];
            children[i+1] = children[i];
        }

        //clearing old parent values
        parent.splitPrep();

        // putting the children into the two parentnodes
        for (int i = 0; i < parentMinKeys-1; i++) 
            parent.addChild(children[i]);

        for (int i = parentMinKeys-1; i < maxKeys+2; i++) 
            parent2.addChild(children[i]);

        //setting parent for the new parentnode
        if (parent.getIsRoot()) {

            ParentNode newRoot = new ParentNode(maxKeys);
            parent.setIsRoot(false);
            newRoot.setIsRoot(true);
            newRoot.addChild(parent);
            newRoot.addChild(parent2);
            root = newRoot;
        }

        else if (parent.getParent().getKeys().size() < maxKeys)
            parent.getParent().addChild(parent2);

        else 
            splitParent(parent.getParent(), parent2);
    }



    // TODO for Experiment 2


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
