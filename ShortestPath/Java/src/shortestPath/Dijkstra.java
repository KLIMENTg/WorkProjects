package shortestPath;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;



public class Dijkstra{
	
	private ArrayList<ArrayList<Arcs> > dijkstraAdjList;
	ArrayList<Integer> X; // Nodes explored
	int [] A; // Distances
	
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
	
			this.dijkstraAdjList = new ArrayList<ArrayList<Arcs>>();
			this.X = new ArrayList<Integer>();
			
			try {
				File myObj = new File("./src/shortestPath/dijkstraData.txt");
				Scanner myReader = new Scanner(myObj);
			
				int nodeIdx = 0;
				while (myReader.hasNextLine()) {
					String data = myReader.nextLine();
				
					this.dijkstraAdjList.add( new ArrayList<Arcs>() );
//					for (String node: data.split("\t")) {
					String[] nodeArr = data.split("\t");
					
					for( int i=0; i<nodeArr.length; i++  ) {
						
						
						String[] inside = nodeArr[i].split(",");
						
						Arcs nodeInt;
						if(inside.length == 1) {
							nodeInt = new Arcs(inside[0]);
						}
						else {
							nodeInt = new Arcs(inside[0] , inside[1]);
						}
						// = Integer.parseInt(node);
						this.dijkstraAdjList.get(nodeIdx).add( nodeInt );
					
					}
					nodeIdx++;
					this.X.add(nodeIdx);
			    }
			    myReader.close();
			} catch (FileNotFoundException e) {
			      System.out.println("An error occurred.");
			      e.printStackTrace();
			}
		}
	
	/*
	 * This function removes a node from the Adjacency List
	 * once that node has been explored/seen and has been added
	 * to the 'explored' list.
	 */
	public void removeNode(int nodetodelete) {
		for (int i=0; i<X.size(); i++) {
			ArrayList<Arcs> sock = this.dijkstraAdjList.get(X.get(i)-1);
			for(int j=1; j<sock.size(); j++) {
				if(sock.get(j).getNode()==nodetodelete) {
					sock.remove(j);
				}
			}
		}
		
	}
	
	/*
	 * This is the Dijkstra's Shortest Path Algorithm
	 * used to find the shortest path within a directed
	 * graph.
	 */
	public void dJIKSTRA() {
		X = new ArrayList<Integer>();
		A = new int [201];
		int shortestDist = 10000000;
		int atNode = 1;
		
		X.add(1);
		while(X.size() < dijkstraAdjList.size()) {
			
			for (int i=0; i<X.size(); i++) {
				ArrayList<Arcs> currentSock = this.dijkstraAdjList.get(X.get(i)-1);
				for(int j=1; j<currentSock.size(); j++) {
					Arcs returnPair = currentSock.get(j);
					if(!checkIfExplored(returnPair.getNode())) {
						if(A[ X.get(i) ] + returnPair.getDistance() < shortestDist) {
							shortestDist = A[ X.get(i) ] + returnPair.getDistance();
							atNode = returnPair.getNode();
						}
					}
				}
			}
			X.add(atNode);
			removeNode(atNode);
			A[atNode] = shortestDist;
			shortestDist = 10000000;
		}
		
		
		
	}
	
	/*
	 * This function checks whether a node has been explored
	 */
	public boolean checkIfExplored(int node) {
		for(int i=0; i<X.size(); i++) {
			if(X.get(i)==node) {
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
		for(int i=1; i<A.length; i++) {
			System.out.println( "Node#: " + i + " " + A[i] );
			
		}
	}
	
	
	public static void main(String[] args)
	
    {	
		Dijkstra obj = new Dijkstra();
		obj.importGraph();
		obj.dJIKSTRA();
		obj.printShortestDistances();
		
    }
	

}
