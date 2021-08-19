/*
 * This program uses the Random Contraction Algorithm
 * to find the Minimum Cut of an Undirected Graph.
 * 
 * The input file contains an Adjacency List of an undirected graph.
 * The first column are vertex labels (1 to 200) and the following numbers are
 * the vertices that share an edge with the vertex from the first column.
 */

package minCut;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class RandomContraction {
	
	final int arrayOffset = 1;
	final int nodeOffset = 1;
	private ArrayList<ArrayList<Integer> > adjacencyList;
	ArrayList<Integer> nodeArray;
	
	/*
	 * This function merges two nodes (i.e. 1 and 3) and their
	 * corresponding rows into one (i.e. 3 into 1). It deletes one
	 * of the nodes (i.e. 3) and it's corresponding row.
	 * Then replaces all occurrences of the deleted node (i.e. 3)
	 * with the node it was merged into (i.e. 1).
	 */
	public void mergeNodes(int nodeA, int nodeB) {
		
		this.removeDuplicateEdges(nodeA, nodeB);
		this.removeDuplicateEdges(nodeB, nodeA);
		
		if(nodeA<nodeB) {
			
			this.adjacencyList.get( nodeA - arrayOffset ).addAll(
					this.adjacencyList.get( nodeB - arrayOffset )
						.subList( nodeOffset, this.adjacencyList.get( nodeB - arrayOffset ).size() ));
			this.removeNode( nodeB );
			this.replaceEntries( nodeB, nodeA );
		}
		else {
			
			this.adjacencyList.get( nodeB - arrayOffset ).addAll(
					this.adjacencyList.get( nodeA - arrayOffset )
						.subList( nodeOffset, this.adjacencyList.get( nodeA - arrayOffset ).size() ));
			this.removeNode( nodeA );
			this.replaceEntries( nodeA, nodeB );
		}
	}
	
	/*
	 * This function removes nodes if they appear
	 * more than once within a row.
	 */
	public void removeDuplicateEdges(int nodeNumber, int edgeNumber) {
		ArrayList<Integer> removeList = new ArrayList<Integer>();
		removeList.add(edgeNumber);
	     
	    this.adjacencyList.get(nodeNumber-arrayOffset).removeAll(removeList);
	}
	
	/*
	 * This function removes a node from the 'table' or adjacencyList data structure
	 * as well as the first column (1-200) or nodeArray data structure.
	 */
	public void removeNode(int nodeNumber){
		this.adjacencyList.get(nodeNumber-arrayOffset).clear();
		Integer deleteInteger = nodeNumber;
		this.nodeArray.remove(deleteInteger);
	}
	
	/*
	 * This function deletes a node and replaces it with another node
	 */
	public void replaceEntries(int eliminationNode, int replacementNode) {//3,1

		for (int i = 0; i < this.adjacencyList.size(); i++) {
            for (int j = 0; j < this.adjacencyList.get(i).size(); j++) {
               if(this.adjacencyList.get(i).get(j) == eliminationNode) {
            	   this.adjacencyList.get(i).set(j, replacementNode);
               }
            }
		}
	}
	
	/*
	 * This function selects a random node from an
	 * ArrayList<Integer> data structure passed to it.
	 */
	public static int getRandom( List<Integer> array ) {
	    int rnd = new Random().nextInt(array .size());
	    return array .get(rnd);
	}
	
	/*
	 * This function reads the input from file and loads it
	 * into an adjacencyList and the first column i.e. nodes 1-200
	 * into nodeArray.
	 */
	public void importGraph() {

		this.adjacencyList = new ArrayList<ArrayList<Integer>>();
		this.nodeArray = new ArrayList<Integer>();
		
		try {
			File myObj = new File("./src/minCut/nodeGraphSimple.txt");
			Scanner myReader = new Scanner(myObj);
		
			int nodeIdx = 0;
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
			
				this.adjacencyList.add( new ArrayList<Integer>() );
				for (String node: data.split("\t")) {
					Integer nodeInt = Integer.parseInt(node);
					this.adjacencyList.get(nodeIdx).add( nodeInt );
				}
				nodeIdx++;
				this.nodeArray.add(nodeIdx);
		    }
		    myReader.close();
		} catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		}
	}
	
	/*
	 * This function selects a vertex node from the first column at random (1-200)
	 * then selects a node from that row (i.e. the first node selected must have
	 * an edge with the second node selected) and then computes minimum cut
	 * between these two nodes.
	 */
	public int getMinCut(int numIterations) { 
        int minCut = 9999;
        for( int iter = 0; iter < numIterations; iter++) {
        	this.importGraph();
	        while( this.nodeArray.size() > 2 ) {
				int startNode = getRandom(this.nodeArray);
				//System.out.println("random node: "+ startNode);
				
				int endNode = getRandom(this.adjacencyList.get(startNode-1).subList(1, this.adjacencyList.get(startNode-1).size()) );
				//System.out.println("endNode node: "+ endNode);
				this.mergeNodes(startNode, endNode);
	        }
        }
        return minCut;
	}
	
	/*
	 * Constructor - Here aList is an ArrayList of ArrayLists
	 */
	public RandomContraction() {
		this.adjacencyList = new ArrayList<ArrayList<Integer> >();
	    this.nodeArray = new ArrayList<Integer>();// allocate memory on heap
	}
	
    public static void main(String[] args)
    {
    	RandomContraction obj = new RandomContraction();
        
    	int numberIter = 1000;
        int minCut = obj.getMinCut(numberIter);
	        	        
        int cut = obj.adjacencyList.get(obj.nodeArray.get(0)-1).size()-1;
	    if( cut < minCut ) {
	    	minCut = cut;
	    }

		
        for (int i = 0; i < obj.adjacencyList.size(); i++) {
            for (int j = 0; j < obj.adjacencyList.get(i).size(); j++) {	//to length of current sock
                System.out.print(obj.adjacencyList.get(i).get(j) + " "); //print each element in sock
            }
            System.out.print("\n");
        }
        
        System.out.print("Array: { ");
        for (int i = 0; i < obj.nodeArray.size(); i++) {
            System.out.print(obj.nodeArray.get(i)+ " ");//print nodeArray
        }
        System.out.println("}. ");
        
        System.out.println( "The Min-cut found is: " + minCut );
    }
}
    
    
