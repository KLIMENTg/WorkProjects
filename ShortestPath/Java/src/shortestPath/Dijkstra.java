package shortestPath;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;



public class Dijkstra{
	
	private ArrayList<ArrayList<Arcs> > adjacencyList;
	ArrayList<Integer> exploredNodes; // Nodes explored
	private int [] shortestDistances; // Distances
	private final int n = 200; // Number of nodes in graph
	private final int INF = 10000000; // Large number used for infinity
	
	
	/*
	 * This function loads the data from the input file.
	 * The file contains a directed graph where the first column
	 * are the nodes (from 1-200). The next columns are the nodes
	 * connected to the node in the first column and the respective
	 * distance to it.
	 * The data is loaded into an Adjacency List which in Java is of
	 * the type ArrayList<ArrayList<Arcs>> with Arcs being a
	 * custom Data Structure written on in's own file.
	 */
	public void importGraph() {
	
			this.adjacencyList = new ArrayList<ArrayList<Arcs>>();
			
			this.exploredNodes = new ArrayList<Integer>();
			
			try {
				File fileHandler = new File("./src/shortestPath/dijkstraData.txt");
				Scanner fileScanner = new Scanner(fileHandler);
			
				int sourceNode = 0;
				while ( fileScanner.hasNextLine() ) {
					String data = fileScanner.nextLine();
				
					this.adjacencyList.add( new ArrayList<Arcs>() );
					String[] nodeArr = data.split("\t");
					
					for( int destinationIdx = 0; destinationIdx < nodeArr.length; destinationIdx++  ) {
						
						String[] nodeInfo = nodeArr[ destinationIdx ].split(",");
						
						Arcs nodeInt;
						if(nodeInfo.length == 1) {
							nodeInt = new Arcs( nodeInfo[0] );
						}
						else {
							nodeInt = new Arcs( nodeInfo[0] , nodeInfo[1] );
						}
						this.adjacencyList.get( sourceNode ).add( nodeInt );
					
					}
					sourceNode++;
					this.exploredNodes.add( sourceNode );
			    }
			    fileScanner.close();
			} catch (FileNotFoundException e) {
			      System.out.println("An error occurred.");
			      e.printStackTrace();
			}
		}
	
	/*
	 * This function removes a node from the Adjacency List.
	 * Once called that node has been explored/seen and has been added
	 * to the 'explored' list.
	 */
	public void removeNode(int nodeToDelete) {
		for (int rootNodeIdx = 0; rootNodeIdx < exploredNodes.size(); rootNodeIdx++) {
			
			ArrayList<Arcs> nodeSock = this.adjacencyList.get(exploredNodes.get( rootNodeIdx )-1);
			for(int destinationNodeIdx = 1; destinationNodeIdx < nodeSock.size(); destinationNodeIdx++) {
				
				if( nodeSock.get( destinationNodeIdx ).getNode() == nodeToDelete ) {
					nodeSock.remove( destinationNodeIdx );
				}
			}
		}
		
	}
	
	/*
	 * This is the Dijkstra's Shortest Path Algorithm
	 * used to find the shortest path within a directed
	 * graph from a chosen source vertex to all other
	 * nodes. This is implementation gets a worst
	 * case run time of O(nm), where n is the number
	 * of nodes and m the number of edges.
	 * 
	 * [1]: -1 due to adjacency list indexes starting at 0, while nodes start at 1.
	 * i.e. exploredNodes.get(0) - returns source node (1), -1 = 0. adjacencyList.get(0) returns
	 * all the connected nodes to the source node.
	 */
	public void dijkstraSP() {
		exploredNodes = new ArrayList<Integer>();
		shortestDistances = new int [ n + 1 ];
		int shortestDist = INF;
		int closestNodeFound = 1;
		
		exploredNodes.add(1); // Source Node (Node 1)
		
		while(exploredNodes.size() < adjacencyList.size()) {
			
			for (int vectorTailIdx = 0; vectorTailIdx < exploredNodes.size(); vectorTailIdx++) {
				
				ArrayList<Arcs> vectorHeadList = this.adjacencyList.get( exploredNodes.get( vectorTailIdx )-1 ); // [1]
				
				for(int destinationIdx = 1; destinationIdx < vectorHeadList.size(); destinationIdx++) {
					
					Arcs destinationPair = vectorHeadList.get( destinationIdx );
					
					if( !checkIfExplored( destinationPair.getNode() ) ) {
						int shortestDistanceContender = shortestDistances[ exploredNodes.get(vectorTailIdx) ] + destinationPair.getDistance();
						if( shortestDistanceContender < shortestDist) {
							shortestDist = shortestDistanceContender;
							closestNodeFound = destinationPair.getNode();
						}
					}
				}
			}
			exploredNodes.add( closestNodeFound );
			removeNode( closestNodeFound );
			shortestDistances[ closestNodeFound ] = shortestDist;
			shortestDist = INF;
		}		
	}
	
	/*
	 * This function checks whether a node has been explored
	 */
	public boolean checkIfExplored(int node) {
		for(int exploredNodeIdx = 0; exploredNodeIdx < exploredNodes.size(); exploredNodeIdx++) {
			if(exploredNodes.get( exploredNodeIdx ) == node) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * This is a printer function
	 */
	public void printShortestDistances() {
		System.out.println("Shortest distances are: ");
		for(int nodeIdx = 1; nodeIdx < shortestDistances.length; nodeIdx++) {
			System.out.println( "Node#: " + nodeIdx + " " + shortestDistances[nodeIdx] );
			
		}
	}
	
	
	public static void main(String[] args)
    {	
		Dijkstra shortestPathObj = new Dijkstra();
		shortestPathObj.importGraph();
		shortestPathObj.dijkstraSP();
		shortestPathObj.printShortestDistances();
		
    }

}
