package shortestPath;


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

