package SCC;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


import java.util.*;

public class FindSCC{
	
	public int[] [] listG;
	public int[] [] listGrev;
	public ArrayList<ArrayList<Integer>> graphG_adjList;
	public ArrayList<ArrayList<Integer>> graphGrev_adjList;
	public int[] explored;
	public int[] leader;
	public int[] leaderCount;
	public int[] finishingTime;
	public int[] ansArray;
	int t, s;
	static final int inputSize = 5105042 + 1;
//	static final int inputSize = 10 + 1;
	
	/*
	 * This function reads the input from file and loads it
	 * into two 2D arrays the second being the reverse of the first
	 * (i.e. first and second column are reversed)
	 */
	public void importGraph() {
		
		this.listG = new int [inputSize] [2];
		this.listGrev = new int [inputSize] [2];
		
		try {
			//File myObj = new File("./src/SCC/TestInput.txt");
			File myObj = new File("./src/SCC/SCC.txt");
			Scanner myReader = new Scanner(myObj);
		
			int nodeIdx = 0;
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
			
				int writeIndex = 0;
				for (String node: data.split(" ")) {
					Integer nodeInt = Integer.parseInt(node);
					listG[nodeIdx][writeIndex] = nodeInt;
					listGrev[nodeIdx][writeIndex ^ 1] = nodeInt;
					writeIndex++;
				}
				nodeIdx++;
		    }
		    myReader.close();
		    
		} catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		}
	}
	
	/*
	 * This function calls for the input to be
	 * transferred from 2D arrays
	 * into two Adjacency Lists.
	 */
	public void getAdjLists() {
	    graphG_adjList = createAdjList(listG);
	    graphGrev_adjList = createAdjList(listGrev);
	}
	
	/*
	 * This function transfers the input from a 2D array
	 * into an Adjacency List sorted by first column
	 */
	public ArrayList<ArrayList<Integer>> createAdjList (int [] [] edgeList){
		ArrayList<ArrayList<Integer>> adjList = new ArrayList<ArrayList<Integer>>();
				
		Sort2DArrayBasedOnColumnNumber(edgeList,1);
		
		int currentALentry = 0;
		int maxNode = 0;
		
		for (int i = 0; i<edgeList.length; i++) {
			
			if(maxNode<edgeList[i][1]) {
				maxNode = edgeList[i][1];
			}
			
			if( edgeList[i][0] - currentALentry > 1  ) {
				while( edgeList[i][0] - currentALentry > 1 ) {
					adjList.add( new ArrayList<Integer>() );
					
					adjList.get(currentALentry).add( currentALentry + 1 );
					adjList.get(currentALentry).add( currentALentry + 1 );
					
					currentALentry++;
				}
			}
			
			if(edgeList[i][0] != currentALentry) {
				adjList.add( new ArrayList<Integer>() );
				adjList.get(currentALentry).add(edgeList[i][0]);			
				currentALentry = edgeList[i][0];
			}
			adjList.get(currentALentry-1).add(edgeList[i][1]);
		}
		
		if(maxNode > adjList.size()) {
			while(maxNode > adjList.size()) {
				adjList.add( new ArrayList<Integer>() );
				adjList.get(currentALentry).add(currentALentry);
				adjList.get(currentALentry).add(currentALentry);
				currentALentry++;
			}
		}
		//System.out.println("Max Node is; " + maxNode);
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
               if(first[columnNumber-1] > second[columnNumber-1]) {
            	   return 1;
               } 
               else if(first[columnNumber-1] < second[columnNumber-1]) {
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
     */
    public int[] kojuraju () {
    	System.out.println("Start calc finish times.");
    	DFS_Loop(graphGrev_adjList);
    	
		injectFinishingTimeInlistG();
		
		System.out.println("Start calc leader nodes.");
		getAdjLists();
		
		DFS_Loop(graphG_adjList);
		
		return leader;
    }
    
    /*
     * This is the Depth First Search Loop used to explore or discover nodes
     * in a directed graph starting from one node in order to determine
     * the Finishing Times for a that graph.
     * (i.e. the last node to be discovered will be given value of 1 etc.)
     */
	public void DFS_Loop( ArrayList<ArrayList<Integer>> G ) {
		int N = G.size();
		t = 0;
		s = 0; // leader node
		explored = new int [N + 1];
		leader = new int [N + 1];
		finishingTime = new int [N + 1];
				
		for (int i=N; i>=1; i--) {
			if(explored[i] == 0) {
				s = i;
				DFS(G, i);
			}
		}
		
		
	}
	
	/*
     * This is the Depth First Search Algorithm used for
     * further exploring a directed graph by assigning
     * all explored vertices a leader node depending on
     * the group they fall within. All nodes having the 
     * same leader node are said to be Strongly Connected
     */
	public void DFS(ArrayList<ArrayList<Integer>> G,int i) {
		explored[i] = i;
		leader[i]= s;
		
		ArrayList<Integer> arcs = G.get(i-1);
		for( int arcIndex = 1; arcIndex < arcs.size(); arcIndex++ ) {
			int j = arcs.get(arcIndex);
			if(explored[j] == 0) {
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
	 */
	public void injectFinishingTimeInlistG() {
		for (int i=0; i<listG.length; i++) {
			for (int j=0; j<2; j++) {
				//graphG_adjList.get(i).set(j, finishingTime[ graphG_adjList.get(i).get(j) ] );
				listG[i][j] = finishingTime[listG[i][j]];
			
			}
		}
		
		
	}

	/*
	 * This function counts the number of nodes,
	 * which have the same leader node, thereby meaning
	 * that those nodes all belong to the same group - 
	 * making them Strongly Connected. 
	 * Generates of group sizes and sorts it.
	 * Answer is printed at the end of the program - 
	 * there are 434821 nodes in the largest group
	 * followed by 968 nodes in the second largest etc.
	 */
	public void leaderCount() {
		leaderCount = new int [ansArray.length];
		
		for (int count=0; count<ansArray.length; count++) {
			leaderCount[ansArray[count]]++;
		}
		Arrays.sort(leaderCount);
	}
	
	/*
	 * This function is for debugging purposes
	 */
	public void debug() {
		
		System.out.println(" the size o fgraphGrev_adjList graph " + this.graphGrev_adjList.size() );
		
	    try {
	        FileWriter myWriter = new FileWriter("filename.txt");
	        
	        for (int i = 0; i < graphGrev_adjList.size(); i++) {
	            for (int j = 0; j < graphGrev_adjList.get(i).size(); j++) {
	                myWriter.write( graphGrev_adjList.get(i).get(j) + " ");
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
	
	public static void main(String[] args)
    {
		FindSCC obj = new FindSCC();
		obj.importGraph();
		System.out.println("Done import");
		obj.getAdjLists();
		System.out.println("Done adj list creation");
		obj.debug();
		obj.ansArray = obj.kojuraju();
		System.out.println("Done Kojuraju");
		obj.leaderCount();
				
		for( int arcIndex = obj.leaderCount.length -1; arcIndex > obj.leaderCount.length -6 ; arcIndex-- ) {
				System.out.print("Node " + arcIndex + " " + obj.leaderCount[arcIndex] + "\n");
		}
    }

}
