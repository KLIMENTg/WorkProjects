/*
 * Subsets.cpp
 *
 *  Created on: Oct. 5, 2021
 *      Author: KLIMENTg
 */

#include "../includes/Subsets.h"

Subsets::Subsets()
{
	// TODO Auto-generated constructor stub
}

Subsets::~Subsets()
{
	// TODO Auto-generated destructor stub
}

void Subsets::genArrayToSetMap( int numCities )
{
	std::cout << "Setting up HashMap ... ";

	int Aidx = 0;
	for( int setCardinality = 0; setCardinality < numCities - STARTING_CITY; setCardinality++)
	{
		std::vector<std::vector<int>>  cardinalSets = subsets.at( setCardinality );

		for (auto set : cardinalSets)
		{
			 std::string hashCode = getHash( set );
			 arrToSubsetMap.insert( std::pair<std::string, int>(hashCode, Aidx++) );
		}
	}
	std::cout << "Done.\n";
}

std::string Subsets::getHash( std::vector<int> set )
{
	std::string hash = "";
	for( int element: set )
	{
		hash += std::to_string( element ) + ",";
	}

	return hash;
}

std::string Subsets::getHashWithExclusion( std::vector<int> set, int exclude )
{
	std::vector<int>::iterator it = std::find(set.begin(), set.end(), exclude);
	set.erase(it);
	std::string hash = getHash( set );
	return hash;
}

int Subsets::getAindexByHash( std::string hashCode )
{
	int Aindex = arrToSubsetMap.at( hashCode );
	return Aindex;
}

void Subsets::genSets( int numOfCities )
{
	std::cout << "Generating sets for " << numOfCities << " number of cities ...";

	for( int choose = 1; choose < numOfCities; choose++)
	{
		//std::cout << "Done C(" << numOfCities << ", " << choose << ")." << std::endl;
		std::cout << ".";
		std::vector<std::vector<int>> set = combinationalSets( numOfCities, choose );
		subsets.push_back( set );
	}

	std::cout << " done.\n";
}

int Subsets::getNumOfSubsets()
{
	int numOfSets = arrToSubsetMap.size();
	return numOfSets;
}

int Subsets::getNumOfSetsByCardinality( int cardinality )
{
	int size = subsets.at(cardinality).size();
	return size;
}

std::vector<int> Subsets::getSet( int cardinality, int setNumber )
{
	std::vector<int> setReturn = subsets.at( cardinality ).at( setNumber );
	return setReturn;
}

std::vector<std::vector<int>> Subsets::combinationalSets( int n, int r )
		{
    std::vector<std::vector<int>> combinations(0);
    std::vector<int> data(r);
    recursiveSetGenerator( combinations, data, 2, n, 0 );
    return combinations;
}

/*
 * C(n,r) = C(n-1, r-1) [choose 1st item] + C(n-1, r) [discard 1st item]
 * 		Choose 1st item: and then append the rest of the sets
 * 		Discard 1st item: and append the rest of the sets
 */
void Subsets::recursiveSetGenerator( std::vector<std::vector<int>>& combinations, std::vector<int>& data, int start, int end, long unsigned int index )
{
	if (index == data.size())
	{
		combinations.push_back( data );
	}
	else if (start <= end)
	{
		data[index] = start;
		recursiveSetGenerator(combinations, data, start + 1, end, index + 1);
		recursiveSetGenerator(combinations, data, start + 1, end, index);
	}
}
