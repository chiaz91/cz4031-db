package app.index;

import app.util.Log;

import java.util.ArrayList;

public class ParentNode extends Node {
    private static final String TAG = "Parent";

    private ArrayList<Node> children;

    // constructor
    public ParentNode(int max) {

        super(max);
        children = new ArrayList<Node>();
    }

    // get arraylist of all children
    public ArrayList<Node> getChildren() {

        return children;
    }

    // get child at index
    public Node getChild(int index) {

        return children.get(index);
    }

    // add child
    public int addChild(Node child) {

        if (children.size() == 0) {

            children.add(child);
            child.setParent(this);
            return 0;
        }

        int key = child.findSmallestKey();
        int smallest = this.findSmallestKey();
        int index;

        if (key < smallest) {

            this.addKey(smallest);
            this.children.add(0, child);
            index = 0;
        }

        else {

            index = this.addKey(key);

            this.children.add(index +1, child);
        }

        child.setParent(this);
        return index;
    }

    public void splitPrep() {

        deleteKeys();
        children = new ArrayList<Node>();
    }



    @Override
    void logStructure() {
        Log.d(TAG, this.toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("P[");
        for (int i=0; i<getKeys().size(); i++){
            if (i>0){
                sb.append(",");
            }
            sb.append(String.format("{%d-%d}", i, getKey(i) ));
        }
        sb.append("]");
        return sb.toString();
    }

}
