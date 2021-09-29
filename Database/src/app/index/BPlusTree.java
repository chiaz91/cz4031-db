package app.index;

import app.storage.Address;
import app.util.Log;
import java.util.ArrayList;

public class BPlusTree {
    private static final String TAG = "B+Tree";
    private static final int SIZE_POINTER = 6; // for 64 bits system; RAM use 64bit for addressing -> 2^6 = 6B
    private static final int SIZE_KEY = 4; // for int value
    int maxKeys;
    int parentMinKeys;
    int leafMinKeys;
    Node root;
    int height;
    int nodeCount;

    public BPlusTree(int blockSize){
        // TODO: calculate n (max number of keys)?
        // n keys + n+1 pointer
        maxKeys = (blockSize-SIZE_POINTER) / (SIZE_KEY+SIZE_POINTER); // n
        parentMinKeys = (int) Math.floor(maxKeys/2);
        leafMinKeys = (int) Math.floor((maxKeys+1)/2);
        Log.i(TAG, "init: blockSize = "+blockSize+", maxKeys = "+maxKeys);
        Log.i(TAG, "MinKeys: parent="+parentMinKeys+", leaf="+leafMinKeys);
        root = createFirst();
        nodeCount = 0;
    }

    // to create first node
    public LeafNode createFirst() {

        LeafNode newRoot = new LeafNode();
        newRoot.setIsRoot(true);
        height = 1;
        nodeCount = 1;
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
        LeafNode leaf2 = new LeafNode();
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
        for (i = 0; i < leafMinKeys; i++) 
            old.addRecord(keys[i], addresses[i]);

        for (i = leafMinKeys; i < maxKeys+1; i++) 
            leaf2.addRecord(keys[i], addresses[i]);

        //setting old leafnode to point to new leafnode and new leafnode to point to next leafnode
        leaf2.setNext(old.getNext());
        old.setNext(leaf2);

        //setting parents for new leafnode
        if (old.getIsRoot()) {

            ParentNode newRoot = new ParentNode();
            old.setIsRoot(false);
            newRoot.setIsRoot(true);
            newRoot.addChild(old);
            newRoot.addChild(leaf2);
            root = newRoot;
            height++;
        }

        else if (old.getParent().getKeys().size() < maxKeys)
            old.getParent().addChild(leaf2);

        else 
            splitParent(old.getParent(), leaf2);

        // updating nodeCount
        nodeCount++;
    }

    //to split a full parent node
    public void splitParent(ParentNode parent, Node child) {

        Node children[] = new Node[maxKeys+2];
        int keys[] = new int[maxKeys+2];
        int key = child.findSmallestKey();
        ParentNode parent2 = new ParentNode();

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
        for (int i = 0; i < parentMinKeys+2; i++) 
            parent.addChild(children[i]);

        for (int i = parentMinKeys+2; i < maxKeys+2; i++) 
            parent2.addChild(children[i]);

        //setting parent for the new parentnode
        if (parent.getIsRoot()) {

            ParentNode newRoot = new ParentNode();
            parent.setIsRoot(false);
            newRoot.setIsRoot(true);
            newRoot.addChild(parent);
            newRoot.addChild(parent2);
            root = newRoot;
            height++;
        }

        else if (parent.getParent().getKeys().size() < maxKeys)
            parent.getParent().addChild(parent2);

        else 
            splitParent(parent.getParent(), parent2);

        // updating nodeCount
        nodeCount++;
    }

    public void deleteKey(int key) {

        ArrayList<Integer> keys;
        LeafNode leaf;

        while (getRecordsWithKey(key).size() != 0) {

            leaf = searchLeaf(key);
            keys = leaf.getKeys();
            
            for (int i = 0; i < keys.size(); i++) {
                
                if (keys.get(i) == key) {

                    leaf.deleteKey(i);

                    if (!leaf.getIsRoot())
                        resetLeaf(leaf);

                    break;
                }
            }
        }
    }

    public void resetLeaf(LeafNode node) {

        if (node.getKeys().size() >= leafMinKeys) 
            return;

        if (node.getKeys().size() == 0) {

            node.deleteNode();
            resetParent(node.getParent());
            return;
        }
        
        LeafNode before = (LeafNode) node.getParent().getBefore(node);
        LeafNode after = (LeafNode) node.getParent().getAfter(node);
        int needed = leafMinKeys - node.getKeys().size();
        int bSpare = 0;
        int aSpare = 0;

        if (before != null) 
            bSpare += before.getKeys().size() - leafMinKeys;

        if (after != null) 
            aSpare += after.getKeys().size() - leafMinKeys;

        if (needed > aSpare + bSpare) {

            for (int i = 0; i < maxKeys-(bSpare+leafMinKeys); i++) {

                before.addRecord(node.getKey(i), node.getRecord(i));
            }

            for (int i = maxKeys-(bSpare+leafMinKeys); i < needed; i++) {

                after.addRecord(node.getKey(i), node.getRecord(i));
            }

            node.deleteNode();
        }

        resetParent(node.getParent());
    }

    public void resetParent(ParentNode parent) {

        if (parent.getIsRoot()) {

            if (parent.getKeys().size() > 1)
                return;

            else {

                parent.deleteNode();
                return;
            }
        }
        
        ParentNode before = (ParentNode) parent.getParent().getBefore(parent);
        ParentNode after = (ParentNode) parent.getParent().getAfter(parent);
        int needed = leafMinKeys - parent.getKeys().size();
        int bSpare = 0;
        int aSpare = 0;

        if (before != null) 
            bSpare += before.getKeys().size() - leafMinKeys;

        if (after != null) 
            aSpare += after.getKeys().size() - leafMinKeys;

        if (needed > aSpare + bSpare) {

            for (int i = 0; i < maxKeys-(bSpare+leafMinKeys); i++) {

                before.addChild(parent.getChild(i));
            }

            for (int i = maxKeys-(bSpare+leafMinKeys); i < needed; i++) {

                after.addChild(parent.getChild(i));
            }

            parent.deleteNode();
        }

        resetParent(parent.getParent());
    }


    // TODO for Experiment 2 (partially done)


    // TODO for Experiment 3
    public ArrayList<Address> getRecordsWithKey(int key){
        // TODO: traverse through the tree, loop the leaf nodes with same key
        ArrayList<Address> result = new ArrayList<>();
        int blockAccess = 1; // access the root??
        Node curNode = root;
        ParentNode parentNode;
        // searching for leaf node with key
        while (!curNode.getIsLeaf()){
            parentNode = (ParentNode) curNode;
            for (int i=0; i<parentNode.getKeys().size(); i++) {
                if ( key <= parentNode.getKey(i)){
                    Log.d("getRecordsWithKey",String.format("[%d] key(%d) <= curKey(%d)", i, key, parentNode.getKey(i) ));
                    curNode = parentNode.getChild(i);
                    blockAccess++;
                    break;
                }
                if (i >= parentNode.getKeys().size()-1){
                    Log.d("getRecordsWithKey",String.format("[%d is last key]  key(%d) >= curKey(%d)", i, key, parentNode.getKey(i) ));
                    curNode = parentNode.getChild(i+1);
                    blockAccess++;
                    break;
                }
            }
        }
        // after leaf node is found, find all records with same key
        if (curNode == null){
            Log.wtf("getRecordsWithKey","ERROR!!! Unable to find key!?");
            return result;
        }
        if (!curNode.getIsLeaf()){
            Log.wtf("getRecordsWithKey","ERROR!!! This should not be happen");
        }
        LeafNode curLeaf = (LeafNode) curNode;
        boolean done = false;
        while(!done && curLeaf!=null){
            // finding same keys within leaf node
            for (int i=0; i<curLeaf.getKeys().size(); i++){
                // found same key, add into result list
                if (curLeaf.getKey(i) == key){
                    result.add(curLeaf.getRecord(i));
                    continue;
                }
                // if curKey > searching key, no need to continue searching
                if (curLeaf.getKey(i) > key){
                    done = true;
                    break;
                }
            }
            if (!done){
                // trying to check sibling node has remaining records of same key
                if (curLeaf.getNext()!= null){
                    curLeaf = (LeafNode) curLeaf.getNext();
                    Log.d("Proceed to sibling node");
                    blockAccess++;
                } else {
                    break;
                }
            }
        }

        Log.d("getRecordsWithKey", "result.size = "+result.size());
        Log.d("getRecordsWithKey", "blockAccess = "+blockAccess);

        return result;
    }

    public void treeStats() {

        ArrayList<Integer> rootKeys = new ArrayList<Integer>();
        ArrayList<Integer> firstKeys = new ArrayList<Integer>();
        ParentNode rootCopy = (ParentNode) root;
        Node first = rootCopy.getChild(0);

        for (int i = 0; i < root.getKeys().size(); i++) {

            rootKeys.add(root.getKey(i));
        }

        for (int i = 0; i < first.getKeys().size(); i++) {

            firstKeys.add(first.getKey(i));
        }

        Log.d("treeStats", "n = " + maxKeys + ", number of nodes = " + nodeCount + ", height = " + height);
        Log.d("rootContents", "root node contents = " + rootKeys);
        Log.d("firstContents", "first child contents = " + firstKeys);
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


    public void logStructure(){
        logStructure(0, root);
    }

    // recursive logging of tree structure
    private void logStructure(int level, Node curNode){
        if (curNode == null){
            curNode = root;
        }

        System.out.print("h="+level+"; ");
        curNode.logStructure();
        if (curNode.getIsLeaf()){
            return;
        }
        ParentNode parentNode = (ParentNode) curNode;
        for (Node child: parentNode.getChildren()) {
            logStructure(level+1, child);
        }
    }
}
