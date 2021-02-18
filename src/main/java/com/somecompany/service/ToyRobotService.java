package com.somecompany.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
}
