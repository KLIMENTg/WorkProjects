package shortestPath;

/*
 * Container for node and distance. Where node is generally the vector head (destination),
 * and only acting as a vector tail (source) in the first column 
 */
public class Arcs{
	
	private int node;
	private int distance;
	
	int getNode(){
		return this.node;
	}
	int getDistance(){
		return this.distance;
	}
	
	public String toString() {
		String returnString = "( no:" + node + ", dist: " + distance + ")";
		return returnString;
	}
	
	Arcs(String node, String distance){	//Constructor
		this.node = Integer.parseInt( node );
		this.distance = Integer.parseInt( distance );
	}
	
	Arcs(String node){	//Constructor
		this.node = Integer.parseInt( node );
	}
}

