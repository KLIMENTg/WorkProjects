/*
 * This program implements the Two Sum Algorithm and makes use
 * of the data structure Hash Table, which provides fast lookup
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
	
	int[] tarray;
	HashSet <Long> intSet;
	
	/*
	 * This function imports the data from an input file which contains
	 * 1 million integers, both positive and negative. It then loads it
	 * into a Hash Table called "intSet", which in Java is of type
	 * HashSet <Long>
	 */
	public void importDataIntoHashSet() {
		intSet = new HashSet<Long>();
		try {
			 File myObj = new File("./src/TwoSum/data.txt");
		     Scanner myReader = new Scanner(myObj);
		     while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        long a = Long.parseLong(data);
		        intSet.add(a);
		      }
		      myReader.close();
		} catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		}
	}
	
	/*
	 * This function generates the array "t"
	 * which has a range [-10 000,10 000]
	 */
	public void populateSumArr() {
		tarray = new int[20001];
		for(int i = -10000; i<=10000; i++) {
			tarray[i+10000] = i;
		}
	}
	
	/*
	 * This is the 2-SUM algorithm
	 */
	public void twoSumAlgorithm() {
		
        Iterator<Long> itr=this.intSet.iterator();    
        long lookup = 0;
        int totalOccurences = 0;
        
        for(int i=0; i<20001; i++) {

	        while(itr.hasNext()){
	        	long x = itr.next();
	        	lookup = tarray[i] - x;
	        	if(intSet.contains(lookup) && lookup != x) {
	        		totalOccurences++;
	        		break;
	        	}
	        }
	        itr = this.intSet.iterator();
	        System.out.println("Running Total: " + totalOccurences + " for T: " + i);
        }
        System.out.print("Pairs x&y that sum up to t: " + totalOccurences + " \n");
	}
	
	public static void main(String[] args) {
		
		TwoSumAlgo obj = new TwoSumAlgo();
		
		obj.importDataIntoHashSet();
		obj.populateSumArr();
		obj.twoSumAlgorithm();

	}
	
	
}

