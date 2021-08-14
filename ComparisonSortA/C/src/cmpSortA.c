/*
 ============================================================================
 Name        : ComparisonSortA.c
 Author      : KLIMENTg
 Version     :
 Copyright   : Your copyright notice
 Description : Comparison Sorting in C, Ansi-style
 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#define INPUTSIZE 100000

int* fileLoader();
int* divAndConqSort1(int* inArray, int n);

/*
 * Initializes memory for an array and reads and loads data from InputFile into that array
 */

int* fileLoader(){
	FILE *myFile;
	myFile = fopen("./src/InputFile.txt", "r");

	//read file into array
	int* tempArr = (int*) malloc( INPUTSIZE * sizeof(int) );
	int rowNumber;

	for (rowNumber = 0; rowNumber < INPUTSIZE; rowNumber++)
	{
		fscanf(myFile, "%d", &tempArr[rowNumber]);
	}

	fclose(myFile);

	return tempArr;
}

int main(void)
{
    int* tempArr = fileLoader();
	int n = INPUTSIZE;
	int* sortedArr;
	int* inputArr = (int*) malloc( n * sizeof(int) );
	//int tempArr[] = {3, 2, 1, 4};
	memcpy(inputArr, tempArr, n * sizeof(int));

	clock_t start_t, end_t;
	start_t = clock();
	sortedArr = divAndConqSort1(inputArr, n);
	end_t = clock();
	double total_t = (double)(end_t - start_t) / CLOCKS_PER_SEC;
	printf("Total time taken by CPU: %f\n", total_t  );
	// time: 0.029776 s

	//testPrinter( sortedArr, n );

	return EXIT_SUCCESS;
}

int* divAndConqSort1(int* inArray, int arrLength)
{
	int* mergeArr = (int*) malloc( arrLength * sizeof(int) );
	int mid, rLen, lLen;
	//Base Case
	if (arrLength==1){
		return inArray;
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
	int* L = divAndConqSort1(inArray, lLen);
	int* R = divAndConqSort1(inArray + mid, rLen);

	// Merge Stage
	int i, j, k;
	i=0, j=0, k=0;
	for(k=0; k<arrLength; k++)
	{
		if(i == lLen){
			mergeArr[k] = R[j];
			j++;
			continue;
		}
		else if(j == rLen){
			mergeArr[k] = L[i];
			i++;
			continue;
		}

		if(L[i] < R[j])
		{
			mergeArr[k] = L[i];
			i++;
		}
		else
		{
			mergeArr[k] = R[j];
			j++;
		}
	}

	memcpy(inArray, mergeArr,  arrLength * sizeof(int));
	free( mergeArr );
	return inArray;
}











