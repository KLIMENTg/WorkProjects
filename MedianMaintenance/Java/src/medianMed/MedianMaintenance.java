/*
 * This is program calculates the median given a set of numbers that
 * come in as a stream i.e. in real time. It uses the Heap Data Structure
 * to calculate a median of a set of numbers.
 * By having two heaps called Hlow and Hhigh we can always have a median
 * ready and calculated as the integers are becoming available one
 * by one. This is a way to always have a median in real time.
 */

package medianMed;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.*;

public class MedianMaintenance{
	
	final int maxFileSize = 10000;
	final int error = -1;
	int median = 0;
	int[] medianArr = new int[maxFileSize];
	int[] inputArray;
	
	/*
	 * This function imports the data from an input file which contains
	 * the integers 1 to 10 000 in unsorted order. It then loads it
	 * into an array called inputArray.
	 * 
	 * @param None
	 *
	 * @return [int[]] - inputArray
	 */
	public int[] readInputArray() {
		
		int[] inputArray = new int[ maxFileSize ];
		try {
			 File fileHandler = new File( "./src/medianMed/median.txt" );
		     Scanner fileScanner = new Scanner( fileHandler );
		     
		     int lineNumber = 0;
		     while (fileScanner.hasNextLine()) {
		        String data = fileScanner.nextLine();
		        int integerFromFile = Integer.parseInt( data );
		        inputArray[lineNumber++] = integerFromFile;        
		      }
		      fileScanner.close();
		} catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		      return new int [ error ];
		}
		
		return inputArray;
	}
	
	/*
	 * This function finds the median among the
	 * integers of the array that is passed to it.
	 * 
	 * @param [int[]] inputArray
	 * 
	 * @return median
	 */
	int getMedian(int[] inputArray) {
		
		if( (inputArray.length) % 2 != 0) {
			median = inputArray[ ( (inputArray.length) + 1 ) / 2 -1 ]; // r+1=k is the number of elements x1, x2, ..., xk
		}
		else {
			median = inputArray[ ( (inputArray.length) ) / 2 -1 ];
		}
		return median;
	}
	
	
	/*
	 * This is the Median Maintenance Algorithm, that makes use of
	 * the Heap Data Structure (Java: PriorityQueue<Integer>) to calculate 
	 * a median of a set of numbers.
	 * 
	 * By having two heaps called heapLow and heapHigh we can determine the
	 * median of a set of numbers as they come in as a stream (i.e. one
	 * by one). This is a way to always have a median in real time,
	 * in other words the algorithm maintains the current median available
	 * for O(1), constant time look up.
	 * 
	 * [0] - Add To Heap: If element is greater than the Max of the Low-Heap, place in High-Heap,
	 * otherwise it goes into the Low-Heap.
	 * 
	 * [1] - Heaps Rebalance: If the High-Heap is bigger than the Low-Heap by 1,
	 * move element from High->Low, and if Low-Heap is bigger than Low-Heap, move
	 * element from Low->High.
	 * 
	 * [2] - Fill Median Array: If Heaps are equal, take the max of the Low-Heap (by design: 2 3 4 5: 3 ). 
	 * Otherwise take the top-element of the larger Heap (min or max respectively).
	 */
	void calculateMedianMaintenance() {
		PriorityQueue<Integer> heapHigh = new PriorityQueue<Integer>(); // Declare Min-Heap
		PriorityQueue<Integer> heapLow = new PriorityQueue<>( (x, y) -> Integer.compare(y, x) ); // Declare Max-Heap
		
		// First element is the median
		heapLow.add(inputArray[0]);
		medianArr[0] = inputArray[0];
		
		for(int inputIdx = 1; inputIdx < inputArray.length; inputIdx++) {
			
			// Find Correct Heap [0]
			if(inputArray[ inputIdx ] > heapLow.peek()) {
				heapHigh.add(inputArray[inputIdx]);
			}
			
			else {
				heapLow.add(inputArray[inputIdx]);
			}
			
			// Now Re-balance[1]
			if(	(heapHigh.size() - heapLow.size()	) > 1) {
				heapLow.add(heapHigh.poll());
			}
			else if(	(heapLow.size() - heapHigh.size()	)	>	1) {
				heapHigh.add(heapLow.poll());
			}
			
			// Fill median array [2]
			if(	(heapLow.size() + heapHigh.size()	) % 2	==	0) {
				medianArr[inputIdx] = heapLow.peek();
			}
			else {
				if(	heapHigh.size()	>	heapLow.size()	) {
					medianArr[inputIdx]	=	heapHigh.peek();
				}
				else {
					medianArr[inputIdx]	=	heapLow.peek();
				}
			}
			
		}
		
		
	}

	public static void main(String args[]) {
		
		MedianMaintenance medMain = new MedianMaintenance();
		medMain.inputArray = medMain.readInputArray();
		medMain.calculateMedianMaintenance();
		
		int medianCounter = 0;
		for( int medianIdx = 0; medianIdx < medMain.medianArr.length; medianIdx++) {
			medianCounter = medianCounter + medMain.medianArr[medianIdx];
		}
		System.out.print("Median total is " + medianCounter + '.');
		
	}
}


	
	