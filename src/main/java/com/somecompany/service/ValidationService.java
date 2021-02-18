package com.somecompany.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.somecompany.model.Robot;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ValidationService {

	@Autowired
	@Qualifier("robot")
	private Robot robot;

	@Value("${errorMsg.noLocation}")
	private String ERROR_MSG_NO_LOCATION;

	public void validateRobotLocation() throws IllegalArgumentException {
		if (robot.getLocation() == null) {
			// Robot has no location

			log.error(ERROR_MSG_NO_LOCATION);
			throw new IllegalArgumentException(ERROR_MSG_NO_LOCATION);
		}
	}
}
