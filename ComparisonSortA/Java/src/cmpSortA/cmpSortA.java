/*
 * This program computes the number of inversions among an unordered set of integers.
 * There are 100 000 integers from 1 to 100 000 (inclusive) in random order.
 * 
 * For example: the set of integers [1, 2, 3, 4] contains no inversions as they are in order,
 * but the set [1, 3, 2, 4] contains one inversion namely - the 3 comes before the 2.
 */

package cmpSortA;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class cmpSortA {
	final int inputArraySize = 100000;
	final double ns = 1000000000.0;
	long inversions = 0;
	
	
	/*
	 * Simple Test-Vectors generation and testing.
	 * Versus a naive O(n^2) implementation vs O(n log(n)).
	 */
	public void test() {
		int Array1[] = {1,4,3,2};
		this.testNativeInversions(Array1);
		
		int Array2[] = {1,3,5,2,4,6};
		this.testNativeInversions(Array2);
		
		int Array3[] = {1,2,3,4,5,6};
		this.testNativeInversions(Array3);
		
		int Array4[] = {6,5,4,3,2,1};
		this.testNativeInversions(Array4);
		
		int[] inputInts = readInputArray();
		
		long startTime = System.nanoTime();
		long answer = this.naiveInsertCount(inputInts);
		long estimatedTime = System.nanoTime() - startTime;
		
		System.out.println("Inversion count naively " + answer + " in " + estimatedTime / ns + " s");
        
	    List<Integer> inputList = convertArrayType( inputInts );
	    
	    inversions = 0;
	    long beginTime = System.nanoTime();
	    divAndConquer05( inputList );
	    long totalTime = System.nanoTime() - beginTime;
	    
	    System.out.println( "Original Array has: " + inversions + " number of Inversions in " + totalTime / ns + " s");
    }
	
	/*
	 * Count of array inversions for testing purpose.
	 */
	public void testNativeInversions( int Array[] ) {
		naiveInsertCount(Array);
		System.out.println( "Naive Inversions: " + inversions + " For: " + Arrays.toString( Array ) + "." );
	}
	
	/*
	 * This function converts the original input array type
	 * from primitive integer array into the
	 * non-primitive data structure ArrayList<Integer>
	 */
	public List<Integer> convertArrayType( int input[] ) {
		List<Integer> integerList = new ArrayList<Integer>();
		
		for( int intIdx = 0; intIdx < input.length; intIdx++ ) {
			integerList.add( input[ intIdx ] );
		}
		
		return integerList;
	}
	
	/*
	 * This function reads the input array from file. Stores
	 * the list of integers in a primitive int array.
	 */
	public int[] readInputArray() {
		int[] inputArray = new int[ inputArraySize ];
		try {
			 File fileHandler = new File("./src/cmpSortA/InputFile.txt");
		     Scanner fileScanner = new Scanner(fileHandler);
		     
		     int integerIdx = 0;
		     while ( fileScanner.hasNextLine() ) {
		        String fileLine = fileScanner.nextLine();
		        int inputInt = Integer.parseInt( fileLine );
		        inputArray[ integerIdx++ ] = inputInt;	        
		      }
		      fileScanner.close();
		      
		} catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		      return new int [1];
		}
		return inputArray;
	}
	
	/*
	 * This function counts the number of inversions. In
	 * a nested for-loop, by the definition of an inversion
	 * a left element is greater than an element to the right.
	 * RunTime O(n^2).
	 */
	public long naiveInsertCount(int Array[]) {
		long localInversions = 0; // Improved speed, versus loop-modification of class variable
		
		for( int leftPos = 0; leftPos < Array.length; leftPos++ ) {
			for( int rightPos = leftPos + 1; rightPos < Array.length; rightPos++ ) {
				if( Array[ leftPos ] > Array[ rightPos ] ){
					localInversions++;
				}
			}
		}
		inversions = localInversions;
		return inversions;
	}
	
	/*
	 * This function counts the number of inversions,
	 * while sorting the input array. A piggy-back
	 * onto the respective merge step of mergesort
	 * with an near-optimal run time of O(n log(n)). 
	 */
	public List<Integer> divAndConquer05( List<Integer> inputArr ) {
		long localInversions = 0; // Local inversions count for optimization
		
		// Base Case
		if( inputArr.size() == 1 ) {
			return inputArr;
		}
		
		// Recursive Calls
		int n = inputArr.size();
		List<Integer> L = divAndConquer05( inputArr.subList(0, n/2) );
		List<Integer> R = divAndConquer05( inputArr.subList(n/2, n) );
		
		// Merge
		int leftIndex = 0;
		int rightIndex = 0; 
		List<Integer> sortedArr = new ArrayList<Integer>();
		
		for( int sortedArrayIdx = 0; sortedArrayIdx < n; sortedArrayIdx++ ) {
			if( L.size() == leftIndex ) {
				sortedArr.add( R.get(rightIndex) );
				rightIndex++;
				continue;
			} else if( R.size() == rightIndex ) {
				sortedArr.add( L.get(leftIndex) );
				leftIndex++;
				continue;
			}
			
			if( L.get(leftIndex) < R.get(rightIndex) ) {
				sortedArr.add( L.get(leftIndex) );
				leftIndex++;
			} else {
				sortedArr.add( R.get(rightIndex) );
				rightIndex++;
				localInversions = localInversions + (L.size() - leftIndex);
			}
		}
		inversions += localInversions;
		return sortedArr;
	}
	
	public static void main(String[] args) {
		cmpSortA inversionObj = new cmpSortA();
		inversionObj.test();
	}
  
}
