package app.index;

import java.util.ArrayList;

public class ParentNode extends Node {

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

    public void splitPrep() {

        deleteKeys();
        children = new ArrayList<Node>();
    }
}
