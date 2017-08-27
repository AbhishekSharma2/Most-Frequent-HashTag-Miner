package com.cop5536.fibonacciHeap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.cop5536.constants.StringConstants;
import com.cop5536.model.Node;
/**
 * 
 * Fibonacci PriorityQueue max heap implementation
 * 
 * @author ABHI
 *
 */

public class FibonacciPriorityQueue implements IFibonacciHeap {

	static final Logger MAINLOGGER = Logger.getLogger(StringConstants.MAINLOGGER);
	
	private Node max;
	
	public Node getMax() {
		return max;
	}

	private int n;

	/**
	 * Inserts a node in a Max fibonacci heap
	 * Steps taken-->
	 * 			1. Node is inserted to the left of max node
	 * 			2. Pointer adjustments are made in top level circular list
	 * 			3. Max heap pointer is updated if required
	 * 
	 * @return Node of Fibonacci heap
	 * 
	 */
	@Override
	public Node insert(String key, long data) {
		MAINLOGGER.debug("###############[INSERT] STARTING ###########################");
		MAINLOGGER.debug("[INSERT] INSERTING NODE " + key+ "("+data+")");
		Node node = new Node(key, data);
        if (max != null) {
        	node.setRight(max);
            node.setLeft(max.getLeft());
            max.setLeft(node);
            node.getLeft().setRight(node);
            if  (data > max.getData()) {
            	max = node;
            }
        }else {
        	max = node;
        }
        n++;
        MAINLOGGER.debug("[MAX REPORTING] MAX AFTER INSERT COMPLETED IS " + max.getKey()+ "("+max.getData()+")");
        MAINLOGGER.debug("###############[INSERT] COMPLETE###########################");
        //print();
        return node;
	}
	
	/**
	 * Increase the data associated with a node with amount newData
	 * 
	 * @param node
	 * @param newData
	 * @param key
	 */
	public void increaseKey(Node node, long newData, String key) {
        increaseKey(node, newData, key, false);
    }
	
	/**
	 * Increases the data in a node and if the new value is greater than the value in the parent
	 * a cut operation is performed followed by a cascading cut
	 * 
	 * @param node
	 * @param newData
	 * @param key
	 * @param b
	 */
	private void increaseKey(Node node, long newData, String key, boolean b) {

		MAINLOGGER.debug("###############[INCREASE KEY] STARTING ###########################");
		MAINLOGGER.debug("[INCREASE KEY] NODE " + key+ "("+newData+")");
        node.setKey(key);// = key; // waste step
        node.setData(newData);//data = newData;
        
        Node y = node.getParent();//parent;
        
        if (y != null && (b || newData > y.getData())) {
        	MAINLOGGER.debug("[INCREASE KEY] CUTTING THE NODE " + node.getKey()+ "("+node.getData()+") FROM PARENT "+ y.getKey()+ "("+y.getData()+")");
        	y.cut(node, max);
        	MAINLOGGER.debug("[INCREASE KEY] CUTTING COMPLETE " + node.getKey()+ "("+node.getData()+")");
        	//print();
        	MAINLOGGER.debug("[INCREASE KEY] CASCADING CUT " + node.getKey()+ "("+node.getData()+")");
            y.cascadingCut(max);
            MAINLOGGER.debug("[INCREASE KEY] CASCADING CUT COMPLETE " + node.getKey()+ "("+node.getData()+")");
            //if (newData > y.getData()) { bug
            if (newData > max.getData()) {
                max = node;
            }
        }
        if(max.getData() < newData){
        	max = node;
        }
        MAINLOGGER.debug("[MAX REPORTING] MAX AFTER INCREASE KEY COMPLETED IS " + max.getKey()+ "("+max.getData()+")");
        MAINLOGGER.debug("###############[INCREASE KEY] COMPLETED ###########################");
        //print();
	}
	
	/**
	 * Remove Max operation 
	 * 
	 * @return max element of a Max Fibonacci heap
	 * 
	 */
	public Node removeMax() {
        Node z = max;
        /**
         * Handle the case for empty heap
         */
        MAINLOGGER.debug("#####################[REMOVE MAX] REMOVING MAX ITEM FROM HEAP##################################################");
        if (z == null) {
            return null;
        }
        if (z.getChild() != null) {
            z.getChild().setParent(null);
            for (Node x = z.getChild().getRight(); x != z.getChild(); x = x.getRight()){
            	x.setParent(null);
            }
            Node maxLeft = max.getLeft();
            Node zchildLeft = z.getChild().getLeft();
            max.setLeft(zchildLeft);
            zchildLeft.setRight(max);
            maxLeft.setRight(z.getChild());
            z.getChild().setLeft(maxLeft);
        }
        z.getLeft().setRight(z.getRight());
        z.getRight().setLeft(z.getLeft());
        if (z == z.getRight()) {
            max = null;
        } else {
            max = z.getRight();
            //print();
            pairwiseCombine();
            
        }
        n--;
        MAINLOGGER.debug("[MAX REPORTING] MAX AFTER REMOVE MAX COMPLETED IS " + max.getKey()+ "("+max.getData()+")");
        MAINLOGGER.debug("#####################[REMOVE MAX] completed##################################################");
        //print();
        return z;
    }

	/**
	 * Perform a pairwise combine operation
	 * Combine nodes with equal degree in the root level
	 * 
	 */
	private void pairwiseCombine() {
		MAINLOGGER.debug("#####################[PAIRWISE COMBINE] STARTING ##################################################");
	        Node[] A = new Node[45];

	        // For each root list node look for others of the same degree.
	        Node start = max;
	        Node w = max;
	        do {
	            Node x = w;
	            // Because x might be moved, save its sibling now.
	            Node nextW = w.getRight();
	            int d = (int) x.getDegree();
	            while (A[d] != null) {
	                // Make one of the nodes a child of the other.
	                Node y = A[(int) d];
	                if (x.getData() < y.getData()) {
	                    Node temp = y;
	                    y = x;
	                    x = temp;
	                }
	                if (y == start) {
	                    start = start.getRight();
	                }
	                if (y == nextW) {
	                    nextW = nextW.getRight();
	                }
	                y.link(x);
	                A[d] = null;
	                d++;
	            }
	            A[d] = x;
	            w = nextW;
	        } while (w != start);

	        max = start;
	        // Find the max key again.
	        for (Node a : A) {
	            if (a != null && a.getData() > max.getData()) {
	                max = a;
	            }
	        }
	        MAINLOGGER.debug("[MAX REPORTING] MAX AFTER PAIRWISE COMBINE COMPLETED IS " + max.getKey()+ "("+max.getData()+")");
	        MAINLOGGER.debug("#####################[PAIRWISE COMBINE] COMPLETED ##################################################");
	}

	/**
	 * Method for debugging
	 * 
	 */
	public void print(){
		Node temp = max;
		BufferedWriter bufw = null;
		try {
			bufw = new BufferedWriter(new FileWriter(new File("F:\\UFL\\Semester1\\Projects\\Semester1\\ADS\\Output\\TreeFile.txt")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MAINLOGGER.debug("PRINTING TREE");
		while(temp.getRight() != max){
			MAINLOGGER.debug("TREE IS " + temp.getKey()+"("+temp.getData()+")"+"<-->");
			//MAINLOGGER.debug(temp.getKey()+"("+temp.getData()+")"+"<-->");
			System.out.print(temp.getKey()+"("+temp.getData()+")"+"<-->");
			temp = temp.getRight();
		}
		MAINLOGGER.debug(temp.getKey()+"("+temp.getData()+")"+"<--> END<-->");
		System.out.println(temp.getKey()+"("+temp.getData()+")"+"<--> END<-->");
		temp = max;
		
		StringBuilder sb = new StringBuilder();
		StringBuilder childSb = new StringBuilder();
		while(temp.getRight() != max){
			
			sb.append(temp.getKey()+"("+temp.getData()+")"+"<-->");
			temp = temp.getRight();
		}
		sb.append(temp.getKey()+"("+temp.getData()+")");
		try {
			bufw.write(sb.toString());
			bufw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
