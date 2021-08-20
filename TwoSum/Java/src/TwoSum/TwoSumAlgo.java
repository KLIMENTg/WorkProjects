/*
 * This program implements the Two Sum Algorithm and makes use
 * of the Hash table data structure, which provides fast lookup
 * achieved in constant O(1) time. The goal is to determine how
 * many distinct integers x and y exist within the Hash Table
 * such that, when added together x+y=t, where t is in the
 * interval [-10 000,10 000] (inclusive). 
 * Q: Is there an x and y s.t. x+y=-10 000, and if yes - how many
 * of them are there? Is there an x and y s.t. x+y=-9 999 ...and so on
 * up to +10 000
 */

package TwoSum;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashSet;
import java.util.*;  


public class TwoSumAlgo{
	
	int[] inputSummationRange;
	HashSet <Long> integerSet;
	final int maxSearchRange = 10000;
	final int minSearchRange = -10000;
	
	/*
	 * This function imports the data from an input file which contains
	 * 1 million integers, both positive and negative. It then loads it
	 * into a Hash Table called "integerSet", which in Java is of type
	 * HashSet <Long>. Note some integers need 64 bits, hence Long.
	 */
	public void importDataIntoHashSet() {
		integerSet = new HashSet<Long>();
		try {
			 File fileHandler = new File("./src/TwoSum/data.txt");
		     Scanner fileScanner = new Scanner(fileHandler);
		     while (fileScanner.hasNextLine()) {
		        String data = fileScanner.nextLine();
		        long fileInteger = Long.parseLong(data);
		        integerSet.add(fileInteger);
		      }
		      fileScanner.close();
		} catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		}
	}
	
	/*
	 * This function generates the array "inputSummationRange"
	 * which has a range [-10 000,10 000]
	 */
	public void populateSumArr() {
		inputSummationRange = new int[ 20001 ];
		for(int rangeIdx = minSearchRange; rangeIdx <= maxSearchRange; rangeIdx++) {
			inputSummationRange[ rangeIdx + maxSearchRange ] = rangeIdx;
		}
	}
	
	/*
	 * 2-SUM algorithm. Description top of file.
	 */
	public void twoSumAlgorithm() {
		
        Iterator<Long> hashSetIterator = this.integerSet.iterator();    
        long pairLookup = 0;
        int totalOccurences = 0;
        
        for(int targetIdx = 0; targetIdx < 20001; targetIdx++) {

	        while(hashSetIterator.hasNext()){
	        	long x = hashSetIterator.next();
	        	pairLookup = inputSummationRange[ targetIdx ] - x;
	        	
	        	if( integerSet.contains(pairLookup) && pairLookup != x ) { // x,y found s.t. x+y = TargetValue
	        		totalOccurences++;
	        		break;
	        	}
	        }
	        hashSetIterator = this.integerSet.iterator();
	        System.out.println("Running Total: " + totalOccurences + " for T: " + targetIdx);
        }
        System.out.print("Pairs x&y that sum up to t: " + totalOccurences + " \n");
	}
	
	public static void main(String[] args) {
		
		TwoSumAlgo twoSumObj = new TwoSumAlgo();
		
		twoSumObj.importDataIntoHashSet();
		twoSumObj.populateSumArr();
		twoSumObj.twoSumAlgorithm();

	}
}

