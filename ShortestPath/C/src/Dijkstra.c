#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>

// Parameters
#define DEBUG 0
#define NUM_NODES_IN_GRAPH 200
#define START_NODE 1

#define TRUE 1
#define INF 10000000
#define MAX_LINE_SIZE 128
#define MAX_OUT_DEGREE_OF_NODE 50
#define MAX_NUM_NODE_AND_DIST_DIGITS 100
#define WRITE_NODE 0
#define WRITE_DISTANCE 1

/*
 * Container for node and distance. Where node is generally the vector head (destination),
 * and only acting as a vector tail (source) in the first column
 */
struct nodeAndDistStruct
{
	int node;
	int distance;
};
typedef struct nodeAndDistStruct nodeAndDist;

char* readLine(FILE *fileHandler);
void createAdjList(
        char* filename,
        nodeAndDist adjacencyList [ NUM_NODES_IN_GRAPH ][ MAX_OUT_DEGREE_OF_NODE ] );

int* djistras(
        nodeAndDist adjacencyList [ NUM_NODES_IN_GRAPH ][ MAX_OUT_DEGREE_OF_NODE ],
        int startingNode);

void printAllNodes( int* shortestDistances );

void getDestinationNodes(nodeAndDist* nodes, nodeAndDist adjList[ NUM_NODES_IN_GRAPH ][ MAX_OUT_DEGREE_OF_NODE ], int sourceNode);
bool checkIfExplored(int node, int exploredNodes[]);
int numDestinationNodes( nodeAndDist* currentSock );
int getArraySize (int arrayToBeSized[]);
void removeNode(int nodeToremove, int exploredNodes[], nodeAndDist adjList [ NUM_NODES_IN_GRAPH ][ MAX_OUT_DEGREE_OF_NODE ]);

struct nodeAndDistStruct djistraData[2];
nodeAndDist currentSock[ MAX_OUT_DEGREE_OF_NODE ] = {0};


int main()
{
	char* filename;
	int startingNode = START_NODE;
	if( DEBUG )
	{
		filename = "./src/dijkstraDataSimpleCvariant.txt";
	}
	else
	{
		filename = "./src/dijkstraData.txt";
	}

	nodeAndDist (*adjacencyList)[ MAX_OUT_DEGREE_OF_NODE ] = malloc( NUM_NODES_IN_GRAPH * MAX_OUT_DEGREE_OF_NODE * sizeof(nodeAndDist) );
    createAdjList( filename, adjacencyList );
    int* shortestDistances = djistras( adjacencyList, startingNode );
    printAllNodes( shortestDistances );

    return 0;
}

/*
 * Prints all the shortest distances respective of the source node
 */
void printAllNodes( int* shortestDistances )
{
    for( int sourceNode = 1; sourceNode <= NUM_NODES_IN_GRAPH; sourceNode++ )
    {
        printf("The shortest since to node %d: %d\n", sourceNode, shortestDistances[sourceNode]);
    }

}

/*
 * This function loads the data from the input file.
 * The file contains a directed graph where the first column
 * are the nodes (from 1-200). The next columns are the nodes
 * connected to the node in the first column and the respective
 * distance to it, also know as the out-degree.
 * The data is loaded into an Adjacency List which in C is of
 * the type "nodeAndDist adjacencyList [ NUM_NODES_IN_GRAPH ][ MAX_OUT_DEGREE_OF_NODE ]" with adjacencyList being a
 * custom Data Structure used to hold node and distance.
 */
void createAdjList(
        char* filename,
        nodeAndDist adjacencyList [ NUM_NODES_IN_GRAPH ][ MAX_OUT_DEGREE_OF_NODE ] )
{

	FILE* fileHandler;
	fileHandler = fopen( filename, "r");

    for (int sourceNodeIdx = 0; sourceNodeIdx < NUM_NODES_IN_GRAPH; sourceNodeIdx++){

        char* allOutGoingNodes = readLine(fileHandler);
        const char tabCharacter[2] = "\t";

        /* get the first token */
        char* sourceNode = strtok( allOutGoingNodes, tabCharacter );
        adjacencyList[sourceNodeIdx][0].node = atoi( sourceNode );

        /* walk through other tokens */
        int edge = 1;

        char* nodeAndDistString = strtok(NULL, tabCharacter);
        while( nodeAndDistString != NULL )
        {
            char distance[ MAX_NUM_NODE_AND_DIST_DIGITS ] = {0};
            char node[ MAX_NUM_NODE_AND_DIST_DIGITS ] = {0};

            int nodeOrDistSelect = WRITE_NODE;
            int characterIdx = 0, writerIdx = 0;
            do{
                // If we reach a comma, we switch from Node to Distance: "24, 324" Node: 24, Dist: 324
                if( nodeAndDistString[characterIdx] == ',')
                {
                   nodeOrDistSelect = WRITE_DISTANCE;
                   writerIdx = 0;
                   continue;
                }

                // Accumulate the digits of "node" or "distance"
                if( nodeOrDistSelect == WRITE_NODE )
                {
                   node[ writerIdx++ ] = nodeAndDistString[characterIdx];
                }
                else if( nodeOrDistSelect == WRITE_DISTANCE )
                {
                   distance[ writerIdx++ ] = nodeAndDistString[characterIdx];
                }

            } while( nodeAndDistString[ ++characterIdx ] != '\0');

            adjacencyList[sourceNodeIdx][edge].node = atoi( node );
            adjacencyList[sourceNodeIdx][edge].distance = atoi( distance );
            edge++;

            nodeAndDistString = strtok( NULL, tabCharacter );	// Get next nodeAndDistString
        }
    }
}

/*
 * This is the Dijkstra's Shortest Path Algorithm
 * used to find the shortest path within a directed
 * graph.
 */
int* djistras(
        nodeAndDist adjList [ NUM_NODES_IN_GRAPH ][ MAX_OUT_DEGREE_OF_NODE ],
        int startingNode )
{
	 int exploredNodes [ NUM_NODES_IN_GRAPH + 1 ] = {0};
	 int shortestDistances [ NUM_NODES_IN_GRAPH + 1 ] = {0}; // Array with the shortest distances to the starting node

	 int shortestDistance = INF;
	 exploredNodes[0] = startingNode;
	 nodeAndDist* allDestNodes = malloc( MAX_OUT_DEGREE_OF_NODE * sizeof(nodeAndDist) );

	 int sourceNode;
	 while( getArraySize( exploredNodes ) <  NUM_NODES_IN_GRAPH )
	 {
		 for (int exploredNode = 0; exploredNode < NUM_NODES_IN_GRAPH; exploredNode++)
		 {
			 if( exploredNodes[exploredNode] != 0 )
			 {
				 getDestinationNodes( allDestNodes, adjList, exploredNodes[exploredNode] );

				 int numDestNodes = numDestinationNodes ( allDestNodes );	//how big is sock?
				 for(int destNodeIdx = 1; destNodeIdx < numDestNodes; destNodeIdx++)
				 {
					 nodeAndDist pair = allDestNodes[destNodeIdx];
					 if ( pair.node != 0 )
					 {
						 if( !checkIfExplored( pair.node, exploredNodes ) )
						 {
						     int shortestDistanceContender = shortestDistances[exploredNodes[exploredNode]] + pair.distance;
							 if( shortestDistanceContender < shortestDistance	)
							 {
								 shortestDistance = shortestDistanceContender;
								 sourceNode = pair.node;
							 }
						 }
					 }
				 }
			 }
		 }
		 exploredNodes[ sourceNode-1 ] = sourceNode; // exploredNodes starts at 0, nodes start at 1
		 removeNode( sourceNode, exploredNodes, adjList );
		 shortestDistances[ sourceNode ] = shortestDistance;
		 shortestDistance = INF;
	 }

	 int* returnDistances = malloc( ( NUM_NODES_IN_GRAPH + 1) * sizeof(int) );	//array with the shortest distances to the starting node
	 memcpy( returnDistances, shortestDistances, ( NUM_NODES_IN_GRAPH + 1 ) * sizeof(int) );
	 return returnDistances;
}


/*
 * This function extracts the data from a single line of the
 * Adjacency List into the data structure nodes. Gets all
 * the destination nodes of a certain sourceNode.
 */
void getDestinationNodes(
        nodeAndDist* nodes,
        nodeAndDist adjList[ NUM_NODES_IN_GRAPH ][ MAX_OUT_DEGREE_OF_NODE ],
        int sourceNode)
{
	 for(int nodeIdx=0; nodeIdx < MAX_OUT_DEGREE_OF_NODE ; nodeIdx++)
	 {
		nodes[ nodeIdx ] = adjList[ sourceNode-1 ][ nodeIdx ]; //copying into sock
	 }
}


/*
 * This function checks whether a node has been explored or not
 */
bool checkIfExplored( int node, int exploredNodes[] )
{
	for(int exploredIdx=0; exploredIdx < NUM_NODES_IN_GRAPH; exploredIdx++)
	{
		if(exploredNodes[exploredIdx] == node)
		{
			return true;
		}
	}
	return false;
}


/*
 * This function removes a node from the Adjacency List
 * once that node has been explored/seen and has been added
 * to the 'explored' list.
 */
void removeNode(
        int nodeToremove,
        int exploredNodes[],
        nodeAndDist adjList [ NUM_NODES_IN_GRAPH ][ MAX_OUT_DEGREE_OF_NODE ])
{

	for (int exploredIdx = 0; exploredIdx < getArraySize( exploredNodes ); exploredIdx++)
	{
		if(exploredNodes[exploredIdx] != 0) // Explored Nodes Only
		{
			nodeAndDist* currentSock = &adjList [ exploredNodes[exploredIdx] - 1 ][0];

			for(int destNode = 1; destNode < numDestinationNodes( currentSock ); destNode++)
			{
				if(currentSock[ destNode ].node == nodeToremove)
				{
					currentSock[ destNode ].distance = 0;
					currentSock[ destNode ].node = 0;
				}
			}
		}
	}
}


/*
 * This function returns the size of
 * an array passed to it. The size
 * is determined by the number of
 * nodes it holds.
 */
int getArraySize ( int arrayToBeSized[] )
{
	int nodeCounter = 0;
	for( int nodeIdx = 0; nodeIdx < NUM_NODES_IN_GRAPH + 1; nodeIdx++ )
	{
		if( arrayToBeSized[ nodeIdx ] != 0 )
		{
			nodeCounter = nodeCounter + 1;
		}
		else
		{
			continue;
		}
	}
	return nodeCounter;
}


/*
 * This function keeps track of where within the
 * custom data structure nodeAndDist we are. In
 * other words, how many out-going edges for this
 * node are there. Or how many nodes can be reached
 * from this source node.
 */
int numDestinationNodes( nodeAndDist* currentSock )
{
	int maxPos = 0;
	for(int position = 0; position< MAX_OUT_DEGREE_OF_NODE ; position++)
	{
		if( currentSock[position].node != 0 )
		{
			maxPos = position + 1;
		}
		else
		{
			continue;
		}
	}
	return maxPos;
}


/*
 * This function reads the data from a single line of the input file,
 * and return the line as a character pointer.
 */
char* readLine( FILE* fileHandler )
{
    if ( fileHandler == NULL )
    {
        printf("Error: file pointer is null.");
        exit(1);
    }

    int maximumLineLength = MAX_LINE_SIZE;
    char* lineBuffer = (char *) malloc(sizeof(char) * maximumLineLength);

    if (lineBuffer == NULL)
    {
        printf("Error allocating memory for line buffer.");
        exit(1);
    }

    char characterFromLine = getc( fileHandler );
    int characterCount = 0;

    while ( ( characterFromLine != '\n' ) && ( characterFromLine != EOF ) )
    {
        if ( characterCount == maximumLineLength )
        {
            maximumLineLength += MAX_LINE_SIZE;
            lineBuffer = realloc( lineBuffer, maximumLineLength );
            if ( lineBuffer == NULL ) {
                printf("Error reallocating space for line buffer.");
                exit(1);
            }
        }
        lineBuffer[ characterCount ] = characterFromLine;
        characterCount++;

        characterFromLine = getc(fileHandler);
    }

    lineBuffer[ characterCount ] = '\0'; // End line with null

    char* line = (char *) malloc( sizeof(char) * (characterCount + 1) );
    strncpy( line, lineBuffer, ( characterCount + 1 ) );
    free( lineBuffer );

    char *constLine = line;
    return constLine;
}

