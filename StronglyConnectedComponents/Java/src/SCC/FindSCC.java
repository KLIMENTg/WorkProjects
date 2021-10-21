package SCC;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


import java.util.*;

public class FindSCC {
	
    private boolean DEBUG;
    private String filename;
	public int[] [] graphArray;
	public int[] [] graphReverseArray;
	public ArrayList<ArrayList<Integer>> graph;
	public ArrayList<ArrayList<Integer>> graphReverse;
	public int[] explored;
	public int[] leader;
	public int[] leaderCount;
	public int[] finishingTime;
	public int[] resultingLeaders;
	int t, s;
	
	/*
	 * This function reads the input from file and loads it
	 * into two 2D arrays. The second being the reverse of the first
	 * (i.e. first and second column are reversed). Making giving
	 * Graph and Reverse Graph.
	 */
	public void importGraph() {
		final int nodeToNodeConection = 2;
	    
	    int numEdges = 0;
	    try {
	        numEdges = (int) Files.lines( Paths.get(filename) ).count();    
	    } catch( Exception e ) {
	        e.getStackTrace();
	    }
		
		this.graphArray = new int [ numEdges ] [ nodeToNodeConection ];
		this.graphReverseArray = new int [ numEdges ] [ nodeToNodeConection ];
		
		try {
			File fileHandler = new File( filename );
			Scanner fileScanner = new Scanner( fileHandler );
		
			int nodeIdx = 0;
			while ( fileScanner.hasNextLine() ) {
				String lineText = fileScanner.nextLine();
			
				int writeIndex = 0;
				for (String nodeString: lineText.split(" ")) {
					Integer node = Integer.parseInt(nodeString);
					
					graphArray[ nodeIdx ][ writeIndex ] = node;
					graphReverseArray[ nodeIdx ][ reverseWrite( writeIndex ) ] = node;
					
					writeIndex++;
				}
				nodeIdx++;
		    }
		    fileScanner.close();
		    
		} catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		}
	}
	
	/*
	 * Used to reverse the arc direction. For example
	 * node 1 -> 2, into node 2 -> 1. Used when importing
	 * the Graph and Reverse Graph
	 */
	private static int reverseWrite( int writePosition ) {
	    int toggleWritePosition = writePosition ^ 1; 
	    return toggleWritePosition;
	}
	
	/*
	 * This function calls for the input to be
	 * transferred from 2D arrays
	 * into two Adjacency Lists.
	 */
	public void createAdjacencyLists() {
	    graph = createAdjList( graphArray );
	    graphReverse = createAdjList( graphReverseArray );
	}
	
	/*
	 * This function transfers the input from a 2D array
	 * into an Adjacency List sorted by first column
	 */
	public ArrayList<ArrayList<Integer>> createAdjList (int [] [] edgeList){
		
	    ArrayList<ArrayList<Integer>> adjList = new ArrayList<ArrayList<Integer>>();
	    
		Sort2DArrayBasedOnColumnNumber( edgeList , 1 );
		
		int currentArrayListEntry = 0;
		int maxNode = 0;
		
		for (int edge = 0; edge < edgeList.length; edge++) {
			
		    // Keep track of max node number
			if( maxNode < edgeList[edge][1] ) {
				maxNode = edgeList[edge][1];
			}
			
			// Check for sink vertices and add self loop.
			// A sink vertex has only incoming edges.
			if( edgeList[edge][0] - currentArrayListEntry > 1  ) {
				while( edgeList[edge][0] - currentArrayListEntry > 1 ) {
					adjList.add( new ArrayList<Integer>() );
					
					adjList.get( currentArrayListEntry ).add( currentArrayListEntry + 1 );
					adjList.get( currentArrayListEntry ).add( currentArrayListEntry + 1 );
					
					currentArrayListEntry++;
				}
			}
			
			// Create source-node, out-going node (1st node in a row of an
			// adjacency list)
			if( edgeList[edge][0] != currentArrayListEntry ) {
				adjList.add( new ArrayList<Integer>() );
				adjList.get( currentArrayListEntry ).add( edgeList[edge][0] );			
				currentArrayListEntry = edgeList[edge][0];
			}
			
			// Adding an edge to the destination-node, from current source-node.
			adjList.get( currentArrayListEntry-1 ).add( edgeList[edge][1] );
		}
		
		// Add remaining sink nodes with self-loops at the end
		// If the largest outgoing edge was 520, and we only have entries 
		// for 505 nodes, fill in 15 sink nodes to 520.
		if(maxNode > adjList.size()) {
			while(maxNode > adjList.size()) {
				adjList.add( new ArrayList<Integer>() );
				adjList.get(currentArrayListEntry).add(currentArrayListEntry);
				adjList.get(currentArrayListEntry).add(currentArrayListEntry);
				currentArrayListEntry++;
			}
		}
		return adjList;
	}
	
	/*
	 * This function sorts a 2D array by first
	 * column numbers in ascending order.
	 */
    public static  void Sort2DArrayBasedOnColumnNumber (int[][] array, final int columnNumber){
        Arrays.sort(array, new Comparator<int[]>() {
            @Override
            public int compare(int[] first, int[] second) {
               if(first[ columnNumber-1 ] > second[ columnNumber-1 ]) {
            	   return 1;
               } 
               else if(first[ columnNumber-1 ] < second[ columnNumber-1 ]) {
            	   return -1;
               }
               else {
            	   return 0;
               }
            }
        });
    }
	
    /*
     * This is the algorithm for finding Strongly Connected Components
     * and return an array of Leader Nodes
     * 
     * Run the Depth-First-Search (DFS) on the reverse graph: finds the 
     * finishing times required
     * 
     * Use finishing times to remap the original graph's adjacency list: 
     * determines the visiting order of the next DFS pass
     * 
     * Run DFS again with the finishing times order, to determine
     * the leaders of the nodes. The leaders determine the
     * Strongly Connected Components (SCCs).
     * 
     * Return the array of the node leaders
     */
    public int[] kojurajuTwoPassAlgorithm () {
    	System.out.println("Start calc finish times.");
    	
    	DFS_Loop( graphReverse );
    	
		injectFinishingTimeInlistG();
		
		System.out.println("Start calc leader nodes.");
		createAdjacencyLists();
		
		DFS_Loop(graph);
		
		return leader;
    }
    
    /*
     * This is the Depth First Search Loop used to explore or discover nodes
     * in a directed graph starting from one node in order to determine
     * the Finishing Times for that graph. 
     * (i.e. the last node to be discovered will be given value of 1 etc.)
     * Finishing times found guarantee that running DFS will not improperly 
     * discover the SCCs, proof omitted.
     */
	public void DFS_Loop( ArrayList<ArrayList<Integer>> G ) {
		int N = G.size();
		t = 0; // finishing time
		s = 0; // leader starting node
		explored = new int [N + 1]; // explored node list
		leader = new int [N + 1]; // leader array
		finishingTime = new int [N + 1]; // finishing times array
				
		for (int i = N; i >= 1; i--) {
			if(explored[i] == 0) {
				s = i;
				DFS(G, i);
			}
		}
		
		
	}
	
	/*
     * This is the Depth First Search Algorithm used for
     * further exploring a directed graph. With a piggy-back
     * of assigning finishing times and leader nodes depending on
     * the group they fall within. All nodes having the 
     * same leader node are said to be Strongly Connected.
     * 
     * @param i [int] - source node
     * @param G [adjList] - graph
     * 
     * [1] - Destination node
     */
	public void DFS(ArrayList<ArrayList<Integer>> G, int i) {
		explored[i] = i;
		leader[i]= s;
		
		ArrayList<Integer> arcs = G.get( i-1 );
		for( int arcIndex = 1; arcIndex < arcs.size(); arcIndex++ ) {
			int j = arcs.get(arcIndex); // [1]
			if( explored[j] == 0 ) {
				DFS(G, j);
			}
		}
		t++;
		finishingTime[i] = t;
	}
	
	/*
	 * This function swaps the nodes in listG with
	 * the finishing times obtained from the 
	 * Depth First Search we ran on listGrev
	 * RunTime: O(n)
	 */
	public void injectFinishingTimeInlistG() {
		for (int i = 0; i < graphArray.length; i++) {
			for( int j=0; j < 2; j++ ) {
				graphArray[i][j] = finishingTime[ graphArray[i][j] ];
			}
		}
	}

	/*
	 * This function counts the number of nodes,
	 * which have the same leader node, thereby meaning
	 * that those nodes all belong to the same group - 
	 * making them Strongly Connected. 
	 * 
	 * Generates the group sizes and sorts them.
	 * Answer is printed at the end of the program - 
	 * there are 434821 nodes in the largest group
	 * followed by 968 nodes in the second largest etc.
	 */
	public void leaderCount() {
		leaderCount = new int [ resultingLeaders.length ];
		
		for (int count=0; count<resultingLeaders.length; count++) {
			leaderCount[resultingLeaders[count]]++;
		}
		Arrays.sort(leaderCount);
	}
	
	/*
	 * This function is for debugging purposes
	 */
	public void debug() {
		
		System.out.println(" the size o fgraphGrev_adjList graph " + this.graphReverse.size() );
		
	    try {
	        FileWriter myWriter = new FileWriter("filename.txt");
	        
	        for (int i = 0; i < graphReverse.size(); i++) {
	            for (int j = 0; j < graphReverse.get(i).size(); j++) {
	                myWriter.write( graphReverse.get(i).get(j) + " ");
	            }
	            myWriter.write("\n");
	        }
	        
	        myWriter.close();
	        System.out.println("Successfully wrote to the file.");
	    } catch (IOException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
	    }
		
	}
	
	/*
	 * Constructor: Initialize the debug state and the input file
	 * accordingly.
	 */
	public FindSCC( boolean debugFlag ) {
	    DEBUG = debugFlag;
	    
        if ( DEBUG ) {
            filename = "./src/SCC/TestInput.txt";
        } else {
            filename = "./src/SCC/SCC.txt";
        }
	}
	
	/*
	 * - Import the graph
	 * - Generate and adjacency list representation
	 * - Run Kujuraju 2-Pass Algorithm
	 * - Group nodes by common leaders
	 * - Print the 5 biggest groups
	 */
	public static void main(String[] args)
    {   
        FindSCC scc = new FindSCC( false );
        scc.importGraph();
		
		System.out.println("Done Import File.");
		scc.createAdjacencyLists();
		System.out.println("Done Adjacency List creation.");
		
		if( scc.DEBUG ) { scc.debug(); }
		
		scc.resultingLeaders = scc.kojurajuTwoPassAlgorithm();
		System.out.println("Done Kojuraju's 2-Pass Algorithm.");
		scc.leaderCount();
				
		int groupNumber = 1;
		int numGroupsToPrint = 5;
		for( 
		    int arcIndex = scc.leaderCount.length-1; 
		    arcIndex > scc.leaderCount.length-numGroupsToPrint-1; 
		    arcIndex-- ) 
		{
				System.out.print("SCC Group #: " + groupNumber++ + " has reachability to: " 
				        + scc.leaderCount[arcIndex] + " nodes.\n");
		}
    }

}
