package app.index;

import app.util.Log;

import java.util.ArrayList;

public class ParentNode extends Node {
    private static final String TAG = "Node.P";

    private ArrayList<Node> children;

    // constructor
    public ParentNode() {

        super();
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

    // add child at index 0 
    public void addChild(Node child, int index) {

        children.add(0, child);
        child.setParent(this);
        deleteKeys();
        
        for (int i = 0; i < children.size(); i++) {

            if (i != 0)
                addKey(children.get(i).findSmallestKey());
        }
    }

    // prepare parentnode for splitting
    public void splitPrep() {

        deleteKeys();
        children = new ArrayList<Node>();
    }

    // delete a child
    public void deleteChild(Node child) {

        children.remove(child);
        deleteKeys();
        
        for (int i = 0; i < children.size(); i++) {

            if (i != 0)
                addKey(children.get(i).findSmallestKey());
        }
    }

    // delete all children
    public void deleteChildren() {

        children = new ArrayList<Node>();
    }

    // get the child before
    public Node getBefore(Node node) {

        if (children.indexOf(node) != 0)
            return children.get(children.indexOf(node)-1);

        return null;
    }

    // get the child after
    public Node getAfter(Node node) {

        if (children.indexOf(node) != children.size()-1)
            return children.get(children.indexOf(node)+1);

        return null;
    }


    @Override
    void logStructure() {
        Log.d(TAG, this.toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i=0; i<getKeys().size(); i++){
            if (i>0){
                sb.append(", ");
            }
            sb.append(String.format("%d:{%d}", i, getKey(i) ));
        }
        sb.append("]");
        return sb.toString();
    }

}
