#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>

struct nodeAndDistStruct
{
	int node;
	int distance;
};
typedef struct nodeAndDistStruct nodeAndDist;

char *readLine(FILE *file);
void createAdjList( nodeAndDist adjList [200][50] );
int* djistras( nodeAndDist adjList [200][50] );
void fillSock(nodeAndDist* currentSock, nodeAndDist adjList[200][50], int node);
bool checkIfExplored(int node, int X[]);
void removeNode(int nodeToremove, int X[], nodeAndDist adjList [200][50]);

struct nodeAndDistStruct djistraData[2];
nodeAndDist currentSock[50] = {0};


int main()
{

	nodeAndDist* adjList = malloc( 200 * 50 * sizeof(nodeAndDist) );
    createAdjList( adjList );
    int* shortestDistances = djistras(adjList);


    return 0;

}

/*
 * This function loads the data from the input file.
 * The file contains a directed graph where the first column
 * are the nodes (from 1-200). The next columns are the nodes
 * connected to the node in the first column and the respective
 * distance to it.
 * The data is loaded into an Adjacency List which in C is of
 * the type "nodeAndDist adjList [200][50]" with adjList being a
 * custom Data Structure used to hold node and distance.
 */
void createAdjList( nodeAndDist adjList [200][50] ) {

	FILE *myFile;
	myFile = fopen("/home/kliment/Documents/github/StanfordAlgorithms/Assign5/dijkstraData.txt", "r");
	const int fileSize = 200;

	for (int count=0; count < fileSize; count++){

	//TO DO: put this in a loop
		char* input = readLine(myFile);

		char *token;
		const char search[2] = "\t";
		char* firstNode;
	/* get the first token */
		firstNode = strtok(input, search);
		adjList[count][0].node = atoi( firstNode );

	/* walk through other tokens */
		int edge = 1;

		token = firstNode; //strtok(input, search);//initialize token to first part of string before '\t'
		while( token != NULL ) {

		   token = strtok(NULL, search);	//get next token
		   if (token == NULL)
		   {
			   break;
		   }

		   char distance[100] = {0};
		   char node[100] = {0};
		   int whichNumber = 0;
		   int i = 0, idx = 0;
		   do{
			   if( token[i] == ','){
				   whichNumber = 1;
				   idx = 0;
				   continue;
			   }

			   if( whichNumber == 0 ){
				   node[idx++] = token[i];
			   } else {
				   distance[idx++] = token[i];
			   }

		   } while( token[++i] != '\0');


		   adjList[count][edge].node = atoi( node );
		   adjList[count][edge].distance = atoi( distance );
	       edge++;
		}
	}
}

/*
 * This is the Dijkstra's Shortest Path Algorithm
 * used to find the shortest path within a directed
 * graph.
 */
int* djistras( nodeAndDist adjList [200][50] ){

	 int X [201] = {0};
	 int A [201] = {0};	//array with the shortest distances to the starting node


	 int shortestDistance = 10000000;
	 int atNode = 1;
	 X[0] = atNode;
	 nodeAndDist* currentSock = malloc( 50 * sizeof(nodeAndDist) );


	 while(arrCounter(X) < 200){
		 for (int i = 0; i < 200; i++){
			 if( X[i] != 0 ){
				 fillSock( currentSock, adjList, X[i]);

				 int currSockSize = structCounter ( currentSock );	//how big is sock?
				 for(int j=1; j < currSockSize; j++){
					 nodeAndDist pair = currentSock[j];
					 if (pair.node != 0){
						 if(!checkIfExplored(pair.node, X)){
							 if(	A[X[i]] + pair.distance < shortestDistance	){
								 shortestDistance=(A[ X[i] ] + pair.distance );
								 atNode= pair.node;

								 if(atNode>200){
									 int xyz=2;
								 }

							 }
						 }
					 }
				 }
			 }
		 }
		 X[atNode-1]=atNode;
		 removeNode(atNode, X, adjList);
		 A[atNode] = shortestDistance;
		 shortestDistance = 10000000;
	 }

	 int* shortestDistances = malloc( 201 * sizeof(int) );	//array with the shortest distances to the starting node
	 memcpy( shortestDistances, A, 201 * sizeof(int) );
	 return shortestDistances;
}


/*
 * This function transfers the data from a single line of the
 * Adjacency List into the data structure currentSock.
 */
void fillSock(nodeAndDist* currentSock, nodeAndDist adjList[200][50], int node){
	 for(int sockCount=0; sockCount<50; sockCount++){
		currentSock[sockCount] = adjList[node-1][sockCount]; //copying into sock
		//printf("AdjList members %d\n",currentSock[sockCount].distance);
	 }
}


/*
 * This function checks whether a node has been explored
 */
bool checkIfExplored(int node, int X[]){
	for(int i=0; i<200; i++){
		if(X[i]==node){
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
void removeNode(int nodeToremove, int X[], nodeAndDist adjList [200][50]){

	for (int i=0; i<arrCounter(X); i++){
		if(X[i] != 0){
			nodeAndDist* currentSock = &adjList [ X[i] - 1 ][0];

			for(int j=1; j<structCounter(currentSock); j++) {
				if(currentSock[j].node == nodeToremove) {
					currentSock[j].distance=0;
					currentSock[j].node=0;
				}
			}
		}
	}
}


/*
 * This function returns the size of
 * an array passed to it.
 */
int arrCounter (int X[]){
	int nodeCounter = 0;
	for(int i=0; i<201; i++){
		if(X[i]!=0){
			nodeCounter = nodeCounter + 1;
		}
		else{
			continue;
		}
	}
	return nodeCounter;
}


/*
 * This function keeps track of where within the
 * custom data structure nodeAndDist we are.
 */
int structCounter (nodeAndDist* currentSock[]){
	int maxPos = 0;
	for(int i=0; i<50; i++){
		if(currentSock[i]!=0){
			maxPos = i + 1;
		}
		else{
			continue;
		}
	}
	return maxPos;
}


/*
 * This function reads the data from a single line of the input file.
 */
char *readLine(FILE *file) {

    if (file == NULL) {
        printf("Error: file pointer is null.");
        exit(1);
    }

    int maximumLineLength = 128;
    char *lineBuffer = (char *)malloc(sizeof(char) * maximumLineLength);

    if (lineBuffer == NULL) {
        printf("Error allocating memory for line buffer.");
        exit(1);
    }

    char ch = getc(file);
    int count = 0;

    while ((ch != '\n') && (ch != EOF)) {
        if (count == maximumLineLength) {
            maximumLineLength += 128;
            lineBuffer = realloc(lineBuffer, maximumLineLength);
            if (lineBuffer == NULL) {
                printf("Error reallocating space for line buffer.");
                exit(1);
            }
        }
        lineBuffer[count] = ch;
        count++;

        ch = getc(file);
    }

    lineBuffer[count] = '\0';
    //char line[count + 1];
    char* line = (char *)malloc(sizeof(char) * (count + 1) );
    strncpy(line, lineBuffer, (count + 1));
    free(lineBuffer);
    char *constLine = line;
    return constLine;
}

