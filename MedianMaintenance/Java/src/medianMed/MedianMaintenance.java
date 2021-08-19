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
	
	int median = 0;
	int[] medianArr = new int[10000];
	int[] myIntArray;
	
	
	/*
	 * This function imports the data from an input file which contains
	 * the integers 1 to 10 000 in unsorted order. It then loads it
	 * into an array called myIntArray.
	 */
	public int[] readInputArray() {
		int[] myIntArray = new int[10000];
		try {
			 File myObj = new File("./src/medianMed/median.txt");
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

	
	/*
	 * This function transfers the contents of 
	 * myIntArray into a new tempArr
	 */
	void arrCopy(int[] tempArr,int[] myIntArray) {
		for (int i=0; i<tempArr.length; i++) {
			tempArr[i] = myIntArray[i];
		}	
	}
	
	
	/*
	 * This function finds the median among the
	 * integers of an array that is passed to it
	 */
	int getMedian(int[] tempArr) {
		//logic to find the median
		if( (tempArr.length)%2 != 0) {
			median = tempArr[ ( (tempArr.length) + 1 ) / 2 -1 ]; //r+1=k is the number of elements x1, x2, ..., xk
		}
		else {
			median = tempArr[ ( (tempArr.length) ) / 2 -1 ];
		}
		return median;
	}
	
	
	/*
	 * This is the Median Maintenance Algorithm, that makes use of
	 * the Heap Data Structure to calculate a median of a set of numbers.
	 * By having two heaps called Hlow and Hhigh we can determine the
	 * median of a set of numbers as they come in as a stream, or one
	 * by one. This is a way to always have a median in real time.
	 */
	void calculateMedianMaintenance() {
		PriorityQueue<Integer> Hhigh = new PriorityQueue<Integer>();
		PriorityQueue<Integer> Hlow =new PriorityQueue<>((x, y) -> Integer.compare(y, x));
		
		//First element is the median
		Hlow.add(myIntArray[0]);
		medianArr[0] = myIntArray[0];
		
		for(int i=1; i<myIntArray.length; i++) {
			
			if(myIntArray[i] > Hlow.peek()) {
				Hhigh.add(myIntArray[i]);
			}
			
			else {
				Hlow.add(myIntArray[i]);
			}
			
			//Now Rebalance
			if(	(Hhigh.size() - Hlow.size()	) > 1) {
				Hlow.add(Hhigh.poll());
			}
			else if(	(Hlow.size() - Hhigh.size()	)	>	1) {
				Hhigh.add(Hlow.poll());
			}
			//Rebalance done
			
			//Fill median array
			if(	(Hlow.size() + Hhigh.size()	)	%2	==	0) {
				medianArr[i] = Hlow.peek();
			}
			else {
				if(	Hhigh.size()	>	Hlow.size()	) {
					medianArr[i]	=	Hhigh.peek();
				}
				else {
					medianArr[i]	=	Hlow.peek();
				}
			}
			
		}
		
		
	}

		
	public static void main(String args[]) {
		
		MedianMaintenance obj = new MedianMaintenance();
		obj.myIntArray = obj.readInputArray();
		obj.calculateMedianMaintenance();
		
		int medianCounter = 0;
		for( int i = 0; i<obj.medianArr.length; i++) {
			medianCounter = medianCounter + obj.medianArr[i];
			}
		System.out.print("Median total is " + medianCounter);//%10000 );
		
	}
	
	
	
}


	
	