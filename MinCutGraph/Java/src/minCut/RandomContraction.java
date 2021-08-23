/*
 * This program uses the Random Contraction Algorithm
 * to find the Minimum Cut of an Undirected Graph.
 * 
 * The input file contains an Adjacency List of an undirected graph.
 * The first column are vertex labels (1 to 200) and the following numbers are
 * the vertices that share an edge with the vertex from the first column 
 * (aka out-going edges).
 */

package minCut;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class RandomContraction {
	
	final private int nodeConvention = 1;
	final private int firstNode = 1;
	final private int maxMinCut = 9999;
	private int numNodes = 0;
	private ArrayList<ArrayList<Integer> > adjacencyList;
	ArrayList<Integer> nodeList;
	
	/*
	 * This function merges two nodes (i.e. 1 and 3) and their
	 * corresponding rows into one (i.e. 3 into 1). It deletes one
	 * of the nodes (i.e. 3) and it's corresponding row.
	 * Then replaces all occurrences of the deleted node (i.e. 3)
	 * with the node it was merged into (i.e. 1). 
	 * 
	 * By convention: always merges the larger node into the smaller 
	 * one, in other words the smaller nodes absorbs the larger
	 * (simply by numbering - it has no effect on the algorithm).
	 * 
	 * [1] Merge: Combine out-going nodes of node A and B -> node AB (effectively)
	 * [2] Remove: Remove smaller node
	 * [3] Absorb: Larger node into smaller node
	 */
	public void mergeNodes(int nodeA, int nodeB) {
		
		this.removeEdge(nodeA, nodeB);
		this.removeEdge(nodeB, nodeA);
		
		if( nodeA < nodeB ) {
			// [1]
			this.adjacencyList.get( nodeA - nodeConvention ).addAll(
					this.adjacencyList.get( nodeB - nodeConvention )
						.subList( firstNode, this.adjacencyList.get( nodeB - nodeConvention ).size() ));
			this.removeNode( nodeB ); // [2]
			this.replaceEntries( nodeB, nodeA ); // [3]
		}
		else {
			// [1]
			this.adjacencyList.get( nodeB - nodeConvention ).addAll(
					this.adjacencyList.get( nodeA - nodeConvention )
						.subList( firstNode, this.adjacencyList.get( nodeA - nodeConvention ).size() ));
			this.removeNode( nodeA ); // [2]
			this.replaceEntries( nodeA, nodeB ); // [3]
		}
	}
	
	/*
	 * This function removes edges from the adjacency list.
	 * Removes the edgeNumber from the node; deleting the
	 * out-going edge from nodeNumber to edgeNumber.
	 */
	public void removeEdge(int nodeNumber, int edgeNumber) {
		ArrayList<Integer> removeList = new ArrayList<Integer>();
		removeList.add( edgeNumber );
	     
	    this.adjacencyList.get( nodeNumber - nodeConvention ).removeAll( removeList );
	}
	
	/*
	 * This function removes a node from the 'table' or adjacencyList data structure.
	 * As well it removes the node (1-200) from nodeList data structure.
	 */
	public void removeNode(int nodeNumber){
		this.adjacencyList.get( nodeNumber-nodeConvention ).clear();
		this.nodeList.remove( (Integer) nodeNumber );
	}
	
	/*
	 * This function deletes a node and replaces it with another node
	 * 
	 * Example: 3,1 makes all edges to node 3 into edges to node 1, this is
	 * part of the merging of two nodes or rewiring of all edges to
	 * node 3 to now point to node 1.
	 */
	public void replaceEntries( int eliminationNode, int replacementNode ) {

		for (int srcIdx = 0; srcIdx < this.adjacencyList.size(); srcIdx++) {
			for (int destIdx = 0; destIdx < this.adjacencyList.get(srcIdx).size(); destIdx++) {
			   if( this.adjacencyList.get( srcIdx ).get( destIdx ) == eliminationNode ) {
				   this.adjacencyList.get( srcIdx ).set( destIdx, replacementNode );
			   }
			}
		}
	}
	
	/*
	 * This function selects a random node from an
	 * ArrayList<Integer> data structure passed to it.
	 */
	public static int getRandomNode( List<Integer> array ) {
	    int randomNumber = new Random().nextInt( array.size() );
	    return array.get( randomNumber );
	}
	
	/*
	 * This function reads the input from file and loads it
	 * into an adjacencyList [1]. The first column i.e. nodes 1-200
	 * is also loaded into a second list data structure, nodeArray [2].
	 */
	public void importGraph( String filename ) {

		this.adjacencyList = new ArrayList<ArrayList<Integer>>();
		this.nodeList = new ArrayList<Integer>();
		
		try {
			File fileHandler = new File( filename );
			Scanner fileScanner = new Scanner( fileHandler );
		
			int node = 0;
			while ( fileScanner.hasNextLine() ) {
				String fileLine = fileScanner.nextLine();
			
				this.adjacencyList.add( new ArrayList<Integer>() );
				for (String edgeString: fileLine.split("\t")) {
					Integer edge = Integer.parseInt( edgeString );
					this.adjacencyList.get( node ).add( edge ); // [1]
				}
				node++;
				
				this.nodeList.add(node); // [2]
		    }
		    fileScanner.close();
		} catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		}
	}
	
	/*
	 * This function selects a vertex node from the first column at random (1-200) [1]
	 * then selects a node from that row (i.e. the first node selected must have
	 * an edge with the second node selected - must be connected).
	 * The minimum cut is calculated by contracting the graph into two sets
	 * of nodes (set A and B) and counting the edges connected between
	 * them (definition of a cut). The cut is recorded and another iteration
	 * is done; over all iterations the minimum is recorded. One iteration 
	 * has a 1/n^2 chance to calculate a mininum cut. After n^2 * ln(n) 
	 * iterations the algorithm has a 1/n failure rate. In other words for
	 * a graph with 200 nodes, iterations = 200^2 * ln(200) = 200k the 
	 * chance of finding a particular min cut is 1 - 1/200 or 99.5%. 
	 * For reference there are 2^n cuts if we select any cut at random.
	 * Normally iterations = 1000 gets the correct min cut for that problem
	 * size 99% of the time. 
	 */
	public int getMinCut(int numIterations, String filename) {
		int minCut = maxMinCut;
		
		for( int iter = 0; iter < numIterations; iter++) {
			this.importGraph( filename );
			while( this.nodeList.size() > 2 ) {
				int startNode = getRandomNode(this.nodeList); // [1]
				
				int endNode = getRandomNode( // [2]
						this.adjacencyList.get( startNode-1 ).subList( 1, this.adjacencyList.get(startNode-1).size()) );
				
				this.mergeNodes(startNode, endNode);
			}
			
			int cut = this.adjacencyList.get(this.nodeList.get(0)-1).size()-1;
			if( cut < minCut ) {
				minCut = cut;
			}
		}
		return minCut;
	}
	
	/*
	 * Printer function
	 */
	public void printer(int minCut, int numberIter) {
		System.out.println("Random Contraction Algorithm");
		System.out.println("Number of iterations: " + numberIter);
		
		System.out.print("Last Array Ran: Nodes { ");
		for (int nodeIdx = 0; nodeIdx < this.nodeList.size(); nodeIdx++) {
		    System.out.print(this.nodeList.get(nodeIdx)+ " ");//print nodeArray
		}
		System.out.println("}. ");
		
		System.out.println("Last Array Ran: Edges");
		for (int sourceNode = 0; sourceNode < this.adjacencyList.size(); sourceNode++) {
		    for (int edge = 0; edge < this.adjacencyList.get(sourceNode).size(); edge++) {	//to length of current sock
		        System.out.print(this.adjacencyList.get(sourceNode).get(edge) + " "); //print each element in sock
		    }
		    if( this.adjacencyList.get(sourceNode).size() != 0 ) {
		    	System.out.print("\n");
		    }
		}
		
		System.out.println("Graph size: " + numNodes + " number of nodes. ");
		System.out.println( "The Min-cut found is: " + minCut );
	}
	
	/*
	 * Constructor - Initialize adjacency and node lists.
	 */
	public RandomContraction( String filename ) {
        this.adjacencyList = new ArrayList<ArrayList<Integer> >();
        this.nodeList = new ArrayList<Integer>();// allocate memory on heap
        
        this.importGraph( filename );
        numNodes = this.nodeList.size();
	}
	
	/*
	 * Main: runs algorithm for number of iterations and filename,
	 * finding the minimum cut.
	 */
    public static void main(String[] args)
    {
        int numberIter = 100;
        String filename = "./src/minCut/nodeGraph.txt";
        
        RandomContraction rndContraction = new RandomContraction( filename );
        int minCut = rndContraction.getMinCut( numberIter, filename );
        
        rndContraction.printer(minCut, numberIter);
    }
    
}
    
    
