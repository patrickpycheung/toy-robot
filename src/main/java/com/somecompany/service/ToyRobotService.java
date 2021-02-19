package com.somecompany.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.somecompany.model.Facing;
import com.somecompany.model.Location;
import com.somecompany.model.Robot;

@Service
public class ToyRobotService {

	@Autowired
	@Qualifier("robot")
	private Robot robot;

	@Autowired
	private ValidationService validationService;

	public String report() throws IllegalArgumentException {

		// Validate robot location
		validationService.validateRobotLocation();

		return robot.getLocation().getXCor() + "," + robot.getLocation().getYCor() + ","
				+ robot.getLocation().getFacing();
	}

	public void place(String XCor, String YCor, String facing) throws IllegalArgumentException {

		// Validate the PLACE command params
		validationService.validatePlaceParams(XCor, YCor, facing);

		Location location = new Location();
		location.setXCor(Integer.valueOf(XCor));
		location.setYCor(Integer.valueOf(YCor));
		location.setFacing(Facing.valueOf(facing));

		robot.setLocation(location);
	}
}
