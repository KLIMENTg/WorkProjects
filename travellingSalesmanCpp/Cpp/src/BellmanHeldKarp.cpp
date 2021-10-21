/*
 * BellmanHeldKarp.cpp
 *
 *  Created on: Oct. 5, 2021
 *      Author: KLIMENTg
 */

#include "../includes/BellmanHeldKarp.h"

BellmanHeldKarp::BellmanHeldKarp( std::string fileName )
{
	loadCities( fileName );
	subsets.genSets( numOfCities );
	subsets.genArrayToSetMap( numOfCities );

	S1_initialConditions();
	S2_algorithm();
	S3_minCostTour();
}

void BellmanHeldKarp::loadCities( std::string fileName )
{
	std::string line;
	std::ifstream myfile;
	myfile.open( fileName );

	if(!myfile.is_open())
	{
		perror("Error open");
		exit(EXIT_FAILURE);
	}

	getline(myfile, line);
	numOfCities = std::stoi( line );

	std::cout << "Reading input file: " + fileName << " ... ";
	while(getline(myfile, line))
	{
		std::regex regexp("[0-9]+.[0-9]+");

		std::regex_iterator<std::string::iterator> it (line.begin(), line.end(), regexp);
		std::regex_iterator<std::string::iterator> end;

		float X = std::stof( it->str() );
		it++;
		float Y = std::stof( it->str() );

		cities.push_back( CartesianCoordinates(X,Y) );

	}
	std::cout << "Cities vector loaded." << std::endl;
}

void BellmanHeldKarp::S1_initialConditions()
{
	std::cout << "Setting distance matrix A's initial conditions...";
	costsA.resize( subsets.getNumOfSubsets(), std::vector<float>( numOfCities + STARTING_CITY ));

	for( int destination = 2; destination <= numOfCities; destination++) {

		int setIdx = destination - STARTING_CITY - LIST_OFFSET;

		CartesianCoordinates startCity = cities.at( STARTING_CITY - LIST_OFFSET );
		CartesianCoordinates nextHop = cities.at( destination - LIST_OFFSET );

		costsA[ setIdx ][ destination ] = CartesianCoordinates::getDistance(startCity, nextHop);

	}

	std::cout << "set." << std::endl;
}

void BellmanHeldKarp::S2_algorithm()
{
	int j,k, Aidx;
	double minCost, cost_kj, cost;
	std::string HashCode;

	for( int m = 1; m < numOfCities - LIST_OFFSET; m++ )
	{
		std::cout << "Cardinality size: " << (m + 2) << std::endl;
		for( int setNum = 0; setNum < subsets.getNumOfSetsByCardinality(m); setNum++ )
		{
			std::vector<int> set = subsets.getSet(m, setNum);
			for( long unsigned int jIdx = 0; jIdx < set.size(); jIdx++ )
			{
				j = set[ jIdx ];
				minCost = INFINITY;

				for( long unsigned int kIdx = 0; kIdx < set.size(); kIdx++ )
				{
					k = set[ kIdx ];

					if( k != j )
					{
						cost_kj = CartesianCoordinates::getDistance(
								cities.at(k - LIST_OFFSET),
								cities.at(j - LIST_OFFSET) );

						HashCode = subsets.getHashWithExclusion(set, j);
						Aidx = subsets.getAindexByHash( HashCode );
						cost = costsA[ Aidx ][ k ] + cost_kj;

						if( cost < minCost )
						{
							minCost = cost;
						}
					}
				}
				HashCode = subsets.getHash(set);
				Aidx = subsets.getAindexByHash( HashCode );
				costsA[ Aidx ][ j ] = minCost;
			}
		}
	}
}

void BellmanHeldKarp::S3_minCostTour()
{
	std::vector<int> fullCitySet = subsets.getSet( numOfCities - LIST_OFFSET - STARTING_CITY, 0);
	std::string HashCode = subsets.getHash( fullCitySet );
	int AfullSetIndex = subsets.getAindexByHash( HashCode );

	minTourCost = INFINITY;
	for( int destination = 2; destination <= numOfCities; destination++)
	{
		double cost_j1 = CartesianCoordinates::getDistance(
				cities.at(destination - LIST_OFFSET),
				cities.at(1 - LIST_OFFSET) );

		double tour = costsA[ AfullSetIndex ][ destination ] + cost_j1;
		if( tour < minTourCost )
		{
			minTourCost = tour;
		}
	}
}

void BellmanHeldKarp::printMinTourCostFloor()
{
	std::cout << "The cost for the minimum tour is: " << (int)minTourCost;
}

BellmanHeldKarp::~BellmanHeldKarp()
{
}

