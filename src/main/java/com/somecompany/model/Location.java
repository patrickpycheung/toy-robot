package com.somecompany.model;

import lombok.Data;

/**
 * 
 * Model of the Location of a robot.
 * 
 * @author N/A
 */
@Data
public class Location {

	private int XCor;
	private int YCor;
	private Facing facing;
}
