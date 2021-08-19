import numpy as np
import math

# File with all integers
file1 = open('/home/kliment/Documents/github/StanfordAlgorithms/Assign6Q2/median.txt', 'r')
Lines = file1.readlines()

# Numpy array to hold the integers
data = np.array( [], dtype=np.int_ )

# Read file and fill data array
for line in Lines:
   number = line.split('\n')[0]
   data = np.append(data,[ np.int_(number) ])

mArray = np.array([])
mTotal = 0
for i in data:
   mArray = np.append( mArray, [ i ] )
   mArray = np.sort( mArray )
   
   numElements = len(mArray) 
   if( numElements % 2 == 0 ): # Even
      m = mArray[ math.floor( numElements / 2 - 1 ) ]
   else: # Odd
      m = mArray[ math.floor( numElements / 2 ) ]
   mTotal += m
   
mTotal = mTotal % 10000

print("The total m value is: ", mTotal )
