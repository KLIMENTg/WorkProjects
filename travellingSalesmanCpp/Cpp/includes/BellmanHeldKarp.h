/*
 * BellmanHeldKarp.h
 *
 *  Created on: Oct. 5, 2021
 *      Author: pav
 */

#ifndef SRC_BELLMANHELDKARP_H_
#define SRC_BELLMANHELDKARP_H_

#include <iostream>
#include <string>
#include <fstream>
#include <vector>
#include <array>
#include <regex>

#include "CartesianCoordinates.h"
#include "Subsets.h"

#define STARTING_CITY 1
#define LIST_OFFSET 1

class BellmanHeldKarp {

private:
	float minTourCost;
	int numOfCities;
	std::vector<CartesianCoordinates> cities;
	Subsets subsets;
	std::vector<std::vector<float>> costsA;
	//float (*costsA)[20] = new float [262143][20];
	//std::array<std::array<float, 262143>,20> costsA;

	void loadCities( std::string filename );
	void S1_initialConditions();
	void S2_algorithm();
	void S3_minCostTour();

public:
	BellmanHeldKarp( std::string fileName );
	virtual ~BellmanHeldKarp();
	void printMinTourCostFloor();
};

#endif /* SRC_BELLMANHELDKARP_H_ */
