/*
 ============================================================================
 Name        : ComparisonSortA.c
 Author      : KLIMENTg
 Version     :
 Copyright   : Your copyright notice
 Description : Time Benchmark program computes the time C takes to count number
               of inversions in an unsorted array of one million entries,
  	  	  	   in order to compare to Java Benchmark.
 	 	 	   Can be modified to count the number of inversions as it's Java counterpart.
 ============================================================================
 */

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>

#define INPUTSIZE 100000

int* fileLoader( char* filename );
int* divAndConqSort1(int* inplaceArray, int n);

unsigned long inversions = 0;

/*
 * Initializes memory for an array and reads and loads data from InputFile into that array
 */

int* fileLoader( char* filename )
{
	FILE *fileHandler;
	fileHandler = fopen(filename, "r");

	//read file into array
	int* loadInput = (int*) malloc( INPUTSIZE * sizeof(int) );
	int rowNumber;

	for (rowNumber = 0; rowNumber < INPUTSIZE; rowNumber++)
	{
	    if( fscanf(fileHandler, "%d", &loadInput[ rowNumber ]) != 1 )
	    {
	        printf("Input file '%s' has an invalid format: Must have only one number per line.\n", filename);
	        exit(EXIT_FAILURE);
	    }
	}
	fclose(fileHandler);

	return loadInput;
}

/*
 * Note: Timing is not direct CPU clock cycles and depends on the computer load.
 */
int main()
{
	int n = INPUTSIZE;
	char* filename = "./src/InputFile.txt";
    int* inputArr = fileLoader( filename );

	clock_t start_t, end_t;
	start_t = clock();
	int* sortedArr = divAndConqSort1(inputArr, n);
	end_t = clock();
	double total_t = (double)(end_t - start_t) / CLOCKS_PER_SEC;

	printf("Total time taken by CPU: %f\n", total_t  );
	printf("Array size: %u\n", INPUTSIZE);
	printf("Number of inversions: %lu \n", inversions );
	printf("First 2 and last 2 elements (sorted check): %d, %d, ... , %d, %d",
	        sortedArr[ 0 ], sortedArr[ 1 ], sortedArr[ n-2 ], sortedArr[ n-1 ] );

	return EXIT_SUCCESS;
}


/*
 * This function sorts the input array and uses the merge step of
 * mergesort to piggy-back on and determine the number of
 * inversions.
 *
 * @param [in/out] inplaceArray - input array which gets sorted in place
 * @param [in] arrLength - length of the array
 */
int* divAndConqSort1(int* inplaceArray, int arrLength)
{
	int* mergeArr = (int*) malloc( arrLength * sizeof(int) );
	int mid, rLen, lLen;

	//Base Case
	if (arrLength==1){
		return inplaceArray;
	}

	// Recursive Stage
	if (arrLength % 2 == 0)
	{
		mid = arrLength/2;
		rLen = mid;
		lLen = mid;
	}
	else
	{
		mid = arrLength/2+1;
		lLen = mid;
		rLen = mid -1;
	}
	int* L = divAndConqSort1(inplaceArray, lLen);
	int* R = divAndConqSort1(inplaceArray + mid, rLen);

	// Merge Stage
	int leftIdx = 0, rightIdx = 0, mergedArrayIdx = 0;
	for( mergedArrayIdx = 0; mergedArrayIdx < arrLength; mergedArrayIdx++ )
	{
		if(leftIdx == lLen)
		{
			mergeArr[ mergedArrayIdx ] = R[ rightIdx ];
			rightIdx++;
			continue;
		}
		else if(rightIdx == rLen)
		{
			mergeArr[ mergedArrayIdx ] = L[ leftIdx ];
			leftIdx++;
			continue;
		}

		if(L[ leftIdx ] < R[ rightIdx ])
		{
			mergeArr[ mergedArrayIdx ] = L[ leftIdx ];
			leftIdx++;
		}
		else
		{
			mergeArr[ mergedArrayIdx ] = R[ rightIdx ];
			rightIdx++;
			inversions = inversions + ( lLen - leftIdx );
		}
	}

	memcpy( inplaceArray, mergeArr,  arrLength * sizeof(int) );
	free( mergeArr );
	return inplaceArray;
}
