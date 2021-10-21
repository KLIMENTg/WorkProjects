/*
 * CartesianCoordinates.cpp
 *
 *  Created on: Oct. 5, 2021
 *      Author: KLIMENTg
 */

#include "../includes/CartesianCoordinates.h"

CartesianCoordinates::CartesianCoordinates( float x, float y )
{
	this->X = x;
	this->Y = y;
}

CartesianCoordinates::~CartesianCoordinates() {
	// TODO Auto-generated destructor stub
}

float CartesianCoordinates::getDistance( CartesianCoordinates A, CartesianCoordinates B )
{
	float deltaX = std::pow( A.getX() - B.getX(), 2);
	float deltaY = std::pow( A.getY() - B.getY(), 2);
	float eucledianDistance = std::sqrt( deltaX + deltaY );
	return eucledianDistance;
}
