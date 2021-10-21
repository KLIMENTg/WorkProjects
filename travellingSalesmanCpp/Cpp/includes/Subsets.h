/*
 * Subsets.h
 *
 *  Created on: Oct. 5, 2021
 *      Author: pav
 */

#ifndef SRC_SUBSETS_H_
#define SRC_SUBSETS_H_

#include <iostream>
#include <vector>
#include <array>
#include <unordered_map>
#include <algorithm>

#define STARTING_CITY 1

class Subsets {
private:
	std::vector<std::vector<std::vector<int>>> subsets;
	std::unordered_map<std::string, int> arrToSubsetMap;

	void recursiveSetGenerator( std::vector<std::vector<int>>& combinations, std::vector<int>& data, int start, int end, long unsigned int index );
	std::vector<std::vector<int>> combinationalSets( int n, int r );

public:
	Subsets();
	virtual ~Subsets();
	void genSets( int numOfCities );
	void genArrayToSetMap( int numCities );
	int getNumOfSubsets();
	int getNumOfSetsByCardinality( int cardinality );
	std::vector<int> getSet( int cardinality, int setNumber );

	std::string getHash( std::vector<int> set );
	std::string getHashWithExclusion( std::vector<int> set, int exclude );
	int getAindexByHash( std::string hashCode );
};

#endif /* SRC_SUBSETS_H_ */
