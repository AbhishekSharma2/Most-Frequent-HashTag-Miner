package com.cop5536.model;

/**
 * POJO for modeling a node in a fibonacci heap
 * 
 * @author ABHI
 *
 */
public class Node {

	private String key;
	
	private Node parent;
	private Node child;
	private Node right;
	private Node left;

	private long data;
	private boolean childCut;
	private long degree;
	
	/**
	 * Constructor for creating a node with "key" and "data"
	 * 
	 * @param key
	 * @param data
	 */
	public Node(String key, long data){
		
		setKey(key);
		setData(data);
		setRight(this);
		setLeft(this);
	}
	/**
	 * Default constructor
	 * 
	 */
	public Node(){
		super();
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	public Node getChild() {
		return child;
	}
	public void setChild(Node child) {
		this.child = child;
	}
	
	
	public Node getRight() {
		return right;
	}

	public void setRight(Node right) {
		this.right = right;
	}

	public Node getLeft() {
		return left;
	}

	public void setLeft(Node left) {
		this.left = left;
	}

	public long getData() {
		return data;
	}
	public void setData(long data) {
		this.data = data;
	}
	public boolean isChildCut() {
		return childCut;
	}
	public void setChildCut(boolean childCut) {
		this.childCut = childCut;
	}
	public long getDegree() {
		return degree;
	}
	public void setDegree(long degree) {
		this.degree = degree;
	}

	/**
	 * Performs the cut operation
	 * 
	 * @param node
	 * @param max
	 */
	public void cut(Node node, Node max) {
		
		 node.getLeft().setRight(node.getRight());
         node.getRight().setLeft(node.getLeft());
         setDegree(getDegree() - 1);
         if (getDegree() == 0) {
             setChild(null);
         } else if (getChild() == node){
             setChild(node.getRight());
         }
		 node.setRight(max);
         node.setLeft(max.getLeft());
         max.setLeft(node);
         node.getLeft().setRight(node);
         node.setParent(null);
         node.setChildCut(false);
	}

	/**
	 * Performs the cascading cut operation
	 * 
	 * @param max
	 */
	public void cascadingCut(Node max) {
		// TODO Auto-generated method stub
		 Node z = getParent();
         // if there's a parent...
         if (z != null) {
             if (isChildCut()) {
                 // it's marked, cut it from parent
                 z.cut(this, max);
                 // cut its parent as well
                 z.cascadingCut(max);
             } else {
                 // if y is unmarked, set it marked
                 setChildCut(true);//mark = true;
             }
         }
	}
	
	 /**
     * Make this node a child of the given parent node. All linkages
     * are updated, the degree of the parent is incremented, and
     * mark is set to false.
     *
     * @param  parent  the new parent node.
     */
    public void link(Node parent) {
        left.right = right;
        right.left = left;
        this.parent = parent;
        if (parent.child == null) {
            parent.child = this;
            right = this;
            left = this;
        } else {
            left = parent.child;
            right = parent.child.right;
            parent.child.right = this;
            right.left = this;
        }
        parent.degree++;
        childCut = false;
    }
}
