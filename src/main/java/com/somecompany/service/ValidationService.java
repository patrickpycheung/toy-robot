package com.somecompany.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.somecompany.model.Facing;
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

	@Value("${errorMsg.xCorNotInteger}")
	private String ERROR_MSG_XCOR_NOT_INTEGER;

	@Value("${errorMsg.xCorOutOfBounce}")
	private String ERROR_MSG_XCOR_OUT_OF_BOUNCE;

	@Value("${errorMsg.yCorNotInteger}")
	private String ERROR_MSG_YCOR_NOT_INTEGER;

	@Value("${errorMsg.yCorOutOfBounce}")
	private String ERROR_MSG_YCOR_OUT_OF_BOUNCE;

	@Value("${errorMsg.invalidFacing}")
	private String ERROR_MSG_INVALID_FACING;

	public void validateRobotLocation() throws IllegalArgumentException {
		if (robot.getLocation() == null) {
			// Robot has no location

			log.error(ERROR_MSG_NO_LOCATION);
			throw new IllegalArgumentException(ERROR_MSG_NO_LOCATION);
		}
	}

	public void validatePlaceParams(String XCorStr, String YCorStr, String facing) throws IllegalArgumentException {

		// Validate x-Coordinate
		try {
			int XCor = Integer.valueOf(XCorStr);

			if (XCor < 0 || XCor > 5) {
				// x-Coordinate Distance out of bounce

				log.error(ERROR_MSG_XCOR_OUT_OF_BOUNCE);
				throw new IllegalArgumentException(ERROR_MSG_XCOR_OUT_OF_BOUNCE);
			}
		} catch (NumberFormatException exception) {
			// Cannot parse x-Coordinate to integer

			log.error(ERROR_MSG_XCOR_NOT_INTEGER);
			throw new IllegalArgumentException(ERROR_MSG_XCOR_NOT_INTEGER);
		}

		// Validate y-Coordinate
		try {
			int YCor = Integer.valueOf(YCorStr);

			if (YCor < 0 || YCor > 5) {
				// y-Coordinate Distance out of bounce

				log.error(ERROR_MSG_YCOR_OUT_OF_BOUNCE);
				throw new IllegalArgumentException(ERROR_MSG_YCOR_OUT_OF_BOUNCE);
			}
		} catch (NumberFormatException exception) {
			// Cannot parse y-Coordinate to integer

			log.error(ERROR_MSG_YCOR_NOT_INTEGER);
			throw new IllegalArgumentException(ERROR_MSG_YCOR_NOT_INTEGER);
		}

		// Validate facing
		boolean isValidFacing = false;
		for (Facing f : Facing.values()) {
			if (f.name().equals(facing)) {
				// Is a valid facing
				isValidFacing = true;
				break;
			}
		}

		if (!isValidFacing) {
			// Facing is invalid

			log.error(ERROR_MSG_INVALID_FACING);
			throw new IllegalArgumentException(ERROR_MSG_INVALID_FACING);
		}
	}
}
