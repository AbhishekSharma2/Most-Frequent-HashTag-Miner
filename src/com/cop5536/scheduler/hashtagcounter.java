package com.cop5536.scheduler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cop5536.constants.StringConstants;
import com.cop5536.fibonacciHeap.FibonacciPriorityQueue;
import com.cop5536.model.Node;

/**
 * hashtagcounter class to execute the program
 * 
 * Expected input(input file name) : args[0]
 * Expected output : output_file.txt
 * 
 * @author ABHI
 *
 */
public class hashtagcounter {
	
	static final Logger MAINLOGGER = Logger.getLogger(StringConstants.MAINLOGGER);
	
	/**
	 * Main method to read in the input file 
	 * and generate the output file having the required 
	 * n most popular hash tags
	 *  
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
		long start = System.currentTimeMillis();
		
		if(args.length > 0){
			Map<String,Node> linkMap = new LinkedHashMap<String,Node>();
			String fileName = args[0];
			File file = new File(fileName);
			BufferedReader br = null;
			BufferedWriter bufw = null;
			try{
				//open a handle to read file
				FibonacciPriorityQueue priorityQueue = new FibonacciPriorityQueue();
				br = new BufferedReader(new FileReader(file));
				bufw = new BufferedWriter(new FileWriter(new File(StringConstants.OUTPUT_FILE_NAME)));
				
				String s = null;
				MAINLOGGER.debug("*****************STARTED WITH FIBONACCI HEAP*******************************");
				while((s = br.readLine()) != null){
					// Branch for query and insertions
					//MAINLOGGER.debug("[READ] Read a line from input File [" + s+"]");
					if(s.contains("#")){
						long data = Integer.valueOf(s.split(" ")[1]);
						String key = s.split(" ")[0].replaceAll("#", "");
						if(linkMap.containsKey(key)){
							Node tempNode = linkMap.get(key);
							long newData = tempNode.getData() + data;
							//MAINLOGGER.debug("[INCREASE KEY] "+ key+" ("+ data+")" + " [NEW AMOUNT] "+ newData);
							priorityQueue.increaseKey(tempNode, newData, key);
						}else{
							// insert a node into the heap	
							//MAINLOGGER.debug("[INSERT]"+ key+" ("+ data+")");
							Node insertedNode = priorityQueue.insert(key, data);
							linkMap.put(key, insertedNode);
						}
					}else if(s.equalsIgnoreCase("stop")){
						MAINLOGGER.debug("******************[STOP] Execution complete as stop encountered****************************");
						break;
					}else{
						// assuming this condition would lead to a query which would lead to 
						//remove max n number of times
						MAINLOGGER.debug("[REMOVE MAX] Number of times" + s);
						//priorityQueue.print();
						long n = Integer.valueOf(s);
						StringBuilder sb = new StringBuilder();
						boolean flag = false;
						List<Node> tempList= new ArrayList<>();
						while(n > 0){
							MAINLOGGER.debug("MAX NODE BEFORE REMOVE MAX IS " + priorityQueue.getMax().getKey() + "( "+priorityQueue.getMax().getData()+")");
							Node node = priorityQueue.removeMax();
							MAINLOGGER.debug("MAX NODE AFTER REMOVE MAX IS " + priorityQueue.getMax().getKey() + "( "+priorityQueue.getMax().getData()+")");
							//priorityQueue.print();	
							if(node != null){
								//sb.append(node.getKey()+"("+node.getData()+"),");
								sb.append(node.getKey()+node.getData()+",");
								flag = true;
								tempList.add(node);
							}
							n--;
						}
						MAINLOGGER.debug("******************************REMOVE MAX COMPLETED FOR ONE QUERY****************************************************");
						if(flag){
							sb.delete(sb.length() -1, sb.length());
							bufw.write(sb.append("\n").toString());
						}
						//reinsert the values back
						for(Node nodeR:tempList){
							Node tempNode = priorityQueue.insert(nodeR.getKey(), nodeR.getData());
							linkMap.put(nodeR.getKey(), tempNode);
						}
					}
				}
				MAINLOGGER.debug("******************************[COMPLETED] Normal Exit*******************************************");
			}catch(Exception e){
				e.printStackTrace();
				MAINLOGGER.debug("APP - 0001 [Failed in scheduler]"+e);
			}finally{
				if(br != null){
					br.close();
				}
				if(bufw != null){
					bufw.close();
				}
			}
		}
		System.out.println(System.currentTimeMillis() - start);
	}
}
