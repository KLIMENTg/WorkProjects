/*
 * This uses a Divide and Conquer Algorithm in order to compute the
 * number of comparisons executed to sort an unordered array of 
 * 10 000 integers. 
 * This can be done in one of three ways in order to benchmark performance.
 * Use First, Last or Median element as a pivot element.
 */

package NumberOfComparisons;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Main
{
	int comparisons; // comparisons GLOBAL definition
	
	/*
	 * This function reads the input array from file.
	 */
	public int[] readInputArray() {
		int[] unsortedInput = new int[10000];
		try {
			 File fileHandle = new File("./src/NumberOfComparisons/InputFile.txt");
		     Scanner fileReader = new Scanner(fileHandle);
		     
		     int lineNumber = 0;
		     while (fileReader.hasNextLine()) {
		        String fileLine = fileReader.nextLine();
		        int arrayEntry = Integer.parseInt( fileLine );
		        unsortedInput[ lineNumber++ ] = arrayEntry;		        
		      }
		      
		     fileReader.close();
		} catch (FileNotFoundException e) {
		      System.out.println("Error occurred during reading of the input file.");
		      e.printStackTrace();
		      return new int [1];
		}
		return unsortedInput;
	}
	
	/*
	 * This function is the Divide and Conquer Algorithm
	 * which uses the First element of the input array as pivot.
	 */
	void divAndConquerFirst (int[] inplaceUnsorted, int L, int R ) {
		
		// Base Case: 1-element or empty-array
		if( L == R | L > R ) {
			return;
		}
		
		// Pivot
		this.comparisons =  this.comparisons + ( R - L ) ;
		
		int i = L + 1;
		int j = L + 1;
		int pivot = inplaceUnsorted[L];
		int swapMemory;
		
		for( ; j <= R; j++ ) {
			if( inplaceUnsorted[j] < pivot) {
				swapMemory = inplaceUnsorted[ j ];
				inplaceUnsorted[ j ] = inplaceUnsorted[ i ];
				inplaceUnsorted[ i ] = swapMemory;
				i++;
			}
		}
		
		// Place pivot in it's rightful place
		swapMemory = pivot;
		inplaceUnsorted[ L ] = inplaceUnsorted[ i - 1 ];
		inplaceUnsorted[ i - 1 ] = swapMemory;
		
		// Recurse
		divAndConquerFirst( inplaceUnsorted,  L, i - 2 );
		divAndConquerFirst( inplaceUnsorted,  i, R );

		return;
	}
	
	/*
	 * This function finds the Median Element
	 */
	void divAndConquerObtainMedian (int[] inplaceUnsorted, int L, int R ) {
		
		// Base Case: 1-element or empty-array
		if( L == R | L > R ) {
			return;
		}
		
		// Comparisons
		int i = L + 1;
		int j = L + 1;
		int pivot = inplaceUnsorted[L];
		int swapMemory;
		for( ; j <= R; j++ ) {
			if( inplaceUnsorted[j] < pivot) {
				swapMemory = inplaceUnsorted[ j ];
				inplaceUnsorted[ j ] = inplaceUnsorted[ i ];
				inplaceUnsorted[ i ] = swapMemory;
				i++;
			}
		}
		swapMemory = pivot;
		inplaceUnsorted[ L ] = inplaceUnsorted[ i-1 ];
		inplaceUnsorted[ i-1 ] = swapMemory;
		
		// Recurse
		divAndConquerObtainMedian( inplaceUnsorted,  L, i-2 );
		divAndConquerObtainMedian( inplaceUnsorted,  i, R );

		return;
	}

	/*
	 * This function is the Divide and Conquer Algorithm
	 * which uses the Last element of the input array as pivot.
	 */
	void divAndConquerLast (int[] inplaceUnsorted, int L, int R ) {
		
		// Base Case: 1-element or empty-array
		if( L == R | L > R ) {
			return;
		}
		
		int swapMemory = 0;
		swapMemory = inplaceUnsorted[ R ];
		inplaceUnsorted[ R ] = inplaceUnsorted[ L ];
		inplaceUnsorted[ L ] = swapMemory;
		
		this.comparisons = this.comparisons + ( R - L ) ;
		int i = L + 1;
		int j = L + 1;
		int pivot = inplaceUnsorted[ L ];

		for( ; j <= R; j++ ) {
			if( inplaceUnsorted[j] < pivot) {
				swapMemory = inplaceUnsorted[j];
				inplaceUnsorted[ j ] = inplaceUnsorted[ i ];
				inplaceUnsorted[ i ] = swapMemory;
				i++;
			}
		}
		
		swapMemory = pivot;
		inplaceUnsorted[ L ] = inplaceUnsorted[ i-1 ];
		inplaceUnsorted[ i-1 ] = swapMemory;
		
		divAndConquerLast( inplaceUnsorted,  L, i-2 );
		divAndConquerLast( inplaceUnsorted,  i, R );

		return;
		
	}
	
	/*
	 * This function is the Divide and Conquer Algorithm
	 * which uses the Median element of the input array as pivot.
	 */
	void divAndConquerMed (int[] inplaceUnsorted, int L, int R ) {
		
		// Base Case: 1-element or empty-array
		if( L == R | L > R ) {
			return;
		}
		
		// Median position
		int midElement, midPos;
		if( ( R - L + 1 ) % 2 == 0 ){
			midPos =  L + ( R - L + 1 )/2 - 1;
			midElement = inplaceUnsorted[ midPos ];
		}
		else {
			midPos = L + ( R - L )/2;
			midElement = inplaceUnsorted[ midPos ];
		}
		
		//check if A is 3 or more members
		int[] arrToDetMedian = { inplaceUnsorted[L], midElement, inplaceUnsorted[R] };
		this.divAndConquerObtainMedian( arrToDetMedian, 0, arrToDetMedian.length - 1);
		int pivotM = arrToDetMedian[1];
		
		int pivot;
		if ( R-L == 1) {
			pivot = inplaceUnsorted[L];
		}
		else if( pivotM == inplaceUnsorted[L] ) {
			pivot = inplaceUnsorted[L];
		}
		else if( pivotM == midElement ) {
			pivot = midElement;
			int swap = 0;
			swap = midElement;
			inplaceUnsorted[midPos] = inplaceUnsorted[L];
			inplaceUnsorted[L] = swap;
		}
		else {
			pivot = inplaceUnsorted[R];
			int swap = 0;
			swap = inplaceUnsorted[R];
			inplaceUnsorted[R] = inplaceUnsorted[L];
			inplaceUnsorted[L] = swap;
		}
		
		this.comparisons =  this.comparisons + ( R - L ) ;
		
		int i = L + 1;
		int j = L + 1;
		int swapMemory;
		
		for( ; j <= R; j++ ) {
			if( inplaceUnsorted[j] < pivot) {
				swapMemory = inplaceUnsorted[ j ];
				inplaceUnsorted[ j ] = inplaceUnsorted[ i ];
				inplaceUnsorted[ i ] = swapMemory;
				i++;
			}
		}
		swapMemory = pivot;
		inplaceUnsorted[ L ] = inplaceUnsorted[ i-1 ];
		inplaceUnsorted[ i-1 ] = swapMemory;
		
		divAndConquerMed( inplaceUnsorted,  L, i-2 );
		divAndConquerMed( inplaceUnsorted,  i, R );

		return;
	}
	
	
	public static void main(String args[])
	{
		System.out.print("Starting Divide and Conquer Algorithm\n");

		Main numCmpsCalculator = new Main();
		numCmpsCalculator.comparisons = 0; 
		int[] inputUnsortedArray = numCmpsCalculator.readInputArray();
		
		//To change Algorithm put it's name below: divAndConquerFirst, divAndConquerMed, divAndConquerLast
		numCmpsCalculator.divAndConquerFirst( inputUnsortedArray, 0, inputUnsortedArray.length - 1 );
		
		System.out.print("Comparisons: " + numCmpsCalculator.comparisons + "\n\n");
		System.out.print("Done Sorting Array\n");
	}
}