package cmpSortA;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class cmpSortA {
	long inversions = 0;
	
	public void test() {
		int Array1[] = {1,4,3,2};
		long answer = this.naiveInsertCount(Array1);
		System.out.println(answer);
		
		int Array2[] = {1,3,5,2,4,6};
		answer = this.naiveInsertCount(Array2);
		System.out.println(answer);
		
		int Array3[] = {1,2,3,4,5,6};
		answer = this.naiveInsertCount(Array3);
		System.out.println(answer);
		
		int Array4[] = {6,5,4,3,2,1};
		answer = this.naiveInsertCount(Array4);
		System.out.println(answer);
		
		int[] myIntArray = readInputArray();
		
		long startTime = System.nanoTime();
		answer = this.naiveInsertCount(myIntArray);
		long estimatedTime = System.nanoTime() - startTime;
		
		System.out.println("Inversion count naively  " + answer + " in " + estimatedTime / 1000000000.0 + " s");
        
        inversions = 0;
        
    long beginTime = System.nanoTime();
    List<Integer> hugeArray = convertArrayType( myIntArray );
    List<Integer> hugeSorted = divAndConquer05( hugeArray );
    long totalTime = System.nanoTime() - beginTime;
        
    System.out.println( "Original Array has: " + inversions + " number of INVersions in " + totalTime / 1000000000.0 + " s");
    }
	
	public void testNativeInversions( int Array[] ) {
		long inversions = naiveInsertCount(Array);
		System.out.println( "Naive Inversions: " + inversions + "." );
	}
	
	public void testMergeInversions( int Array[], boolean printArray ) {
		inversions = 0;
		List<Integer> integerList = loadList( Array );
		divAndConquer05( integerList );
        
        if( printArray == true ) {
        	System.out.println( "Merge Inversions: " + inversions + " for array: " + integerList.toString() );	
        } else {
        	System.out.println( "Merge Inversions: " + inversions + "." );
        }
        
	}
	
	public List<Integer> loadList( int Array[] ) {
		List<Integer> intList = new ArrayList<>();
		
		for( int i = 0; i < Array.length; i++ ) {
			intList.add( Array[i] );
		}
		
		return intList;
	}
	
	public List<Integer> convertArrayType( int Array[] ) {
		List<Integer> newArrayType = new ArrayList<>();
		
		for( int i = 0; i < Array.length; i++ ) {
			newArrayType.add( Array[i] );
		}
		
		return newArrayType;
	}
	
	public int[] readInputArray() {
		int[] myIntArray = new int[100000];
		try {
			 File myObj = new File("./src/cmpSortA/InputFile.txt");
		     Scanner myReader = new Scanner(myObj);
		     int k=0;
		     while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        int a = Integer.parseInt(data);
		        myIntArray[k++] = a;	        
		      }
		      myReader.close();
		} catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		      return new int [1];
		}
		return myIntArray;
	}
	
	public long naiveInsertCount(int Array[]) {
		long inv = 0;
		for( int i=0; i < Array.length; i++ ) {
			for( int j=i+1; j < Array.length; j++ ) {
				if( Array[i] > Array[j] ){
					inv++;
				}
			}
		}
		return inv;
	}

	public List<Integer> divAndConquer05( List<Integer> inputArr ) {
		
		// Base Case
		if( inputArr.size() == 1 ) {
			return inputArr;
		}
		
		// Recursive Calls
		int n = inputArr.size();
		List<Integer> L = divAndConquer05( inputArr.subList(0, n/2) );
		List<Integer> R = divAndConquer05( inputArr.subList(n/2, n) );
		
		// Put together
		int leftIndex = 0;
		int rightIndex = 0; 
		List<Integer> sortedArr = new ArrayList<>();
		
		for( int k = 0; k < n; k++ ) {
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
				inversions = inversions + (L.size() - leftIndex);
			}
		}
		return sortedArr;
	}
	
	public static void main(String[] args) {
		cmpSortA myMainObj = new cmpSortA();
		myMainObj.test();
	}
  
}