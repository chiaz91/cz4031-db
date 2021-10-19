package app.index;

import java.util.ArrayList;

//  n = 4
public abstract class Node {
    
    private ArrayList<Integer> keys;
    private ParentNode parent;
    private boolean isLeaf;
    private boolean isRoot;


    // constructor
    public Node() {

        keys = new ArrayList<Integer>();
        isLeaf = false;
        isRoot = false;
    }
    
    // get whether it is a leaf
    public boolean getIsLeaf() {

        return isLeaf;
    }

    // set whether it is a leaf
    public void setIsLeaf(boolean leafiness) {

        isLeaf = leafiness;
    }

    // get whether it is root
    public boolean getIsRoot() {

        return isRoot;
    }

    // set whether it is root
    public void setIsRoot(boolean rootiness) {

        isRoot = rootiness;
    }

    // get node's parent
    public ParentNode getParent() {

        return parent;
    }

    // set node's parent
    public void setParent(ParentNode progenitor) {

        parent = progenitor;
    }

    // get arraylist of all keys
    public ArrayList<Integer> getKeys() {

        return keys;
    }

    // get key at index
    public int getKey(int index) {


        return keys.get(index);
    }

    // add key
    public int addKey(int key) {

        if (this.getKeys().size() == 0) {

            this.keys.add(key);
            return 0;
        }

        int i;
        keys.add(key);
        for (i = keys.size() -2; i >= 0; i--) {

            if (keys.get(i) <= key) {

                i++;
                keys.set(i, key);
                break;
            }

            keys.set(i+1, keys.get(i));
            if (i == 0) {

                keys.set(i, key);
                break;
            }
        }
        
        return i;
    }

    // delete key from index
    public void deleteKey(int index) {

        keys.remove(index);
    }

    // for deleting keys before splitting
    public void deleteKeys() {

        keys = new ArrayList<Integer>();
    }

    // find smallest key (more for use by parentnode but placed here for first level of parents)
    public int findSmallestKey() {

        int key;
        ParentNode copy;

        if (!this.getIsLeaf()) {

            copy = (ParentNode) this;

            while (!copy.getChild(0).getIsLeaf())
                copy = (ParentNode) copy.getChild(0);
            
            key = copy.getChild(0).getKey(0);
        }

        else 
            key = this.getKey(0);

        return key;
    }

    // delete the node
    public void deleteNode() {

        if (parent != null) {

            parent.deleteChild(this);
            parent = null;
        }

        if (this.isLeaf) {
            
            LeafNode copy = (LeafNode) this;
            copy.deleteRecords();
            copy.setNext(null);
        }

        else {

            ParentNode copy = (ParentNode) this;
            copy.deleteChildren();
        }

        isLeaf = false;
        isRoot = false;
        keys = new ArrayList<Integer>();
    }

    abstract void logStructure();
}
