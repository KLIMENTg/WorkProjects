/*
 ============================================================================
 Name        : NumberOfComparisons.c
 Author      : KLIMENTg
 Version     :
 Copyright   : Your copyright notice
 Description : Count number of comparisons in randomized algorithm.
 	 	 	   Uses a Divide and Conquer Algorithm in order to compute the
			   number of comparisons executed to sort an unordered array of
			   10 000 integers.
			   This can be done in one of FOUR ways in order to benchmark performance.
			   Use First, Last, Median or RANDOMISED element as a pivot element.
 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define INPUTSIZE 10000 // Probably a better way than hardcoding the constant. C isn't very good with file I/O anyway
#define DEBUG 0 // A Debug flag: can be {0, 1}. See below how it is used.

// Function Prototypes
void testCases( char testType[] );
void divAndConquerBFirst( int* array, const int L, const int R);
void divAndConquerBLast( int* array, int L, int R);
void divAndConquerBMiddle( int* array, int L, int R);
void divAndConquerBRandom( int* array, int L, int R);	// TODO: iMPLEMENT RANDOM PIVOT
void swap( int* A, int* B );
void runQSCmps(int* array, int len, char testType[] );
int* fileLoader();
void printList( int* array, int len );

int numberOfCmps = 0; // counts number of comparisons performed



int main(void) {
	printf("Begin NumberOfComparisons\n");

	//To change Algorithm put it's name below: First, Middle, Last
	char toTest[] = "First";
	testCases( toTest );

	return EXIT_SUCCESS;
}

/*-----------------------------------------------------------------------------
|  Function testCases
|
|  Purpose:  Defines all test cases.
|
|  Parameters:
|      testType (IN) -- A string indicating the type of test to do. Values can be
|						"Middle", "First" and "Last"
|
|  Returns:  VOID
*----------------------------------------------------------------------------*/
void testCases( char testType[] )
{
	int input[] = {6,7,4,1,3,9};
	runQSCmps(input, sizeof(input) / sizeof(int), testType );

	int input1[] = {1,2,3,4};
	runQSCmps(input1, sizeof(input1) / sizeof(int), testType );

	int input2[] = {3,2,1,4,5};
	runQSCmps(input2, sizeof(input2) / sizeof(int), testType );

	int input3[] = {5};
	runQSCmps(input3, sizeof(input3) / sizeof(int), testType );

	int input4[] = {4,5,6,7};
	runQSCmps(input4, sizeof(input4) / sizeof(int), testType );

	int input5[] = {8,2,4,5,7,1,12};
	runQSCmps(input5, sizeof(input5) / sizeof(int), testType );

	int input6[] = {3,2,1};
	runQSCmps(input6, sizeof(input6) / sizeof(int), testType );

	int input7[] = {2,1,3};
	runQSCmps(input7, sizeof(input7) / sizeof(int), testType );

	int input8[] = {1,3,2};
	runQSCmps(input8, sizeof(input8) / sizeof(int), testType );

	int* assignmentInput = fileLoader();
	runQSCmps( assignmentInput, INPUTSIZE, testType);
}

/**
* Runs a usual testcase for divAndConquerB. It takes the input array
* with its length and the test type and counts the number of
* comparisons that DivAndConquerB makes.
*
* @param [in] [int*] 	array 	- unsorted array
* @param [in] [int] 	len 	- the length of the array
* @param [in] [char[]] 	qsType 	- the test type. See testCases
*
* @return [void]
*/
void runQSCmps(int* array, int len, char qsType[] )
{
	numberOfCmps = 0;

	if( strcmp( qsType, "First") == 0 ) { divAndConquerBFirst( array, 0, len - 1); }
	else if ( strcmp( qsType, "Last" ) == 0 ) { divAndConquerBLast( array, 0, len - 1); }
	else if ( strcmp( qsType, "Middle" ) == 0 ) { divAndConquerBMiddle( array, 0, len - 1); }

	printf("Number of comparisons: %d, for: ", numberOfCmps);
	if( len < 100 )
	{
		printList( array, len );
	} else {
		printf("list too long to print.");
	}
	printf("\n");
}

/*-----------------------------------------------------------------------------
|  Function divAndConquerB
|
|  Purpose: The DivAndConquerB algorithm with the first element in the subarray used
|			as a pivot. A snapshot of QS in action:

			[ 7 1 2 9 12 18 20 17 3 5 98 ... ]
			  |     |			  |
			Pivot   |			  |
			  smallestRGroup   nextElem

		    The pivot is compared versus the nextElem. If is less, it is swapped with smallestRGroup (putting
		    the the small element in the beginning of the larger group) and incrementing smallestRGroup. If it
		    is larger, we just look at the next element. At the end we swap the Pivot with smallestRGroup - 1,
		    putting the pivot in its final correct position.
|
|  Parameters:
|      array 	(IN/OUT) -- Array to be sorted (or subarray)
|      L 		(IN) 	-- The Left position of the subarray
|      R 		(IN) 	-- The Right position of the subarray
|
|  Returns:  VOID
*----------------------------------------------------------------------------*/
void divAndConquerBFirst( int* const restrict array, const int L, const int R)
{
	// [restrict] tells the compiler that the array is only accessed by this particular pointer

	// Base Case
	if( L == R || L > R) { return; }

	numberOfCmps += R - L;

	// Choose Pivot
	int pivot = array[L];

	// Partition
	int smallestRGroup = L + 1;
	for( int nextElem = L + 1; nextElem <= R; nextElem++ )
	{
		if( pivot > array[nextElem] )
		{
			swap(&array[smallestRGroup], &array[nextElem]);
			smallestRGroup++;
		}
	}
	array[L] = array[smallestRGroup - 1];
	array[smallestRGroup-1] = pivot;

	// Recurse
	divAndConquerBFirst( array, L, smallestRGroup - 2 );
	divAndConquerBFirst( array, smallestRGroup, R);
}

void divAndConquerBLast( int* const restrict array, const int L, const int R)
{
	// Base Case
	if( L == R || L > R) { return; }

	numberOfCmps += R - L;

	// Swap first and last elements
	swap(&array[L], &array[R]);

	// Choose Pivot
	int pivot = array[L];

	// Partition
	int i = L + 1;
	for( int j = L + 1; j <= R; j++ )
	{
		if( pivot > array[j] )
		{
			swap(&array[i], &array[j]);
			i++;
		}
	}
	array[L] = array[i - 1];
	array[i-1] = pivot;

	// Recurse
	divAndConquerBLast( array, L, i - 2 );
	divAndConquerBLast( array, i, R);
}

void divAndConquerBMiddle( int* const restrict array, const int L, const int R)
{
	// Base Case
	if( L == R || L > R) { return; }

	numberOfCmps += R - L;

	int n = R - L + 1;
	int pivot;

	int middlePos = ( n % 2 == 0 ) ? ( n/2 - 1 ) : ( n/2  );
	middlePos += L; // Add offset

	int First = array[L];
	int Last = array[R];
	int Middle = array[middlePos];

	if( ( (First < Middle) && (Middle < Last)) || ((First > Middle) && (Middle > Last))){
		pivot = Middle;
		array[middlePos] = array[L];
		array[L] = pivot;
	}
	else if( ((Middle < First) && (First < Last)) || ((Middle > First) && (First > Last)) ) {
		pivot = First;
	}
	else if( ((Middle < Last) && (Last < First)) || ((Middle > Last) && (Last > First)) ) {
		pivot = Last;
		array[R] = array[L];
		array[L] = pivot;
	}
	else if( n == 2) {
		pivot = First;
	} else {
		printf("SHOULD NEVER BE IN HERE\n");
	}

	if (DEBUG == 1){
		printf("First %d, Middle %d, Last %d: Median: %d. Length: %d and middle element: %d.\n",
			First, Middle, Last, pivot, n, middlePos);
	}

	// Partition
	int i = L + 1;
	for( int j = L + 1; j <= R; j++ )
	{
		if( pivot > array[j] )
		{
			swap(&array[i], &array[j]);
			i++;
		}
	}
	array[L] = array[i - 1];
	array[i-1] = pivot;

	// Recurse
	divAndConquerBMiddle( array, L, i - 2 );
	divAndConquerBMiddle( array, i, R);
}

// Inline function makes the compiler copy this function into the code that calls it. Making it embedded in the
// binary - meaning it will not have to call it during run time, it is embedded or "INLINED"
inline void swap( int* A, int* B )
{
	int swap = *A;
	*A = *B;
	*B = swap;
}

/*
* This function reads the input array from file.
*/
int* fileLoader(){
	int* returnArray = (int*) malloc( INPUTSIZE * sizeof(int) );

	FILE *myFile;
	myFile = fopen("./src/input.txt", "r");

	for (int i = 0; i < INPUTSIZE; i++)
	{
		fscanf(myFile, "%d", &returnArray[i]);
	}
	fclose(myFile);

	return returnArray;
}

/*
* This function prints out the sorted array.
*/
void printList( int* array, int len )
{
	printf("{ ");
	for( int i = 0; i < len; i++)
	{
		printf("%d,", array[i]);
	}
	printf(" }");
}

// A constant integer L (cannot be changed)
const int aConstant;
// A pointer POINTING to a CONSTANT data (data doesn't change)
const int* aConstantDataPtr;
// A pointer that cannot change its value (cannot point to anything else) but the data may change!
int* const aPtrThatCannotBeChanged;
// A pointer that cannot change what its pointing to AND which points to constant data
const int* const AConstDataANDconstPointer;

