/*
 * CartesianCoordinates.h
 *
 *  Created on: Oct. 5, 2021
 *      Author: pav
 */

#ifndef SRC_CARTESIANCOORDINATES_H_
#define SRC_CARTESIANCOORDINATES_H_

#include <cmath>

class CartesianCoordinates {
private:
	float X;
	float Y;

public:
	CartesianCoordinates( float x, float y );
	virtual ~CartesianCoordinates();

	static float getDistance( CartesianCoordinates A, CartesianCoordinates B );

	float getX() const {
		return X;
	}

	void setX(float x) {
		X = x;
	}

	float getY() const {
		return Y;
	}

	void setY(float y) {
		Y = y;
	}

};

#endif /* SRC_CARTESIANCOORDINATES_H_ */
