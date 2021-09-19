package app.entity;
import java.util.ArrayList;

public class ParentNode extends Node {

    private ArrayList<Node> children;

    // constructor
    public ParentNode() {

        super();
        children = new ArrayList<Node>(5);
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
            this.addKey(child.findSmallestKey());
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

        return index;
    }
}


// SOLVE PARENTS ACTING LIKE LEAF!! Need to change parent to have first child be from previous parent's biggest key (or 0 for first parent) to first of self key
// child nodes stay the same, but keys need to change to second child onward