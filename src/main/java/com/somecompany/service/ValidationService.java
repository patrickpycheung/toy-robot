package com.somecompany.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.somecompany.model.Command;
import com.somecompany.model.Facing;
import com.somecompany.model.Robot;

import lombok.extern.slf4j.Slf4j;

/**
 * Backend services for validating the user input.
 * 
 * @author N/A
 */
@Service
@Slf4j
public class ValidationService {

	@Autowired
	@Qualifier("robot")
	private Robot robot;

	@Value("${errorMsg.nullOrEmptyUserInput}")
	private String ERROR_MSG_NULL_OR_EMPTY_USER_INPUT;

	@Value("${errorMsg.invalidCommand}")
	private String ERROR_MSG_INVALID_COMMAND;

	@Value("${errorMsg.invalidPlaceCommandFormat}")
	private String ERROR_MSG_INVALID_PLACE_COMMAND_FORMAT;

	@Value("${errorMsg.invalidNonPlaceCommandFormat}")
	private String ERROR_MSG_INVALID_NON_PLACE_COMMAND_FORMAT;

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

	@Value("${errorMsg.robotFallOff}")
	private String ERROR_MSG_ROBOT_FALL_OFF;

	/**
	 * Validate the user input from the console.
	 * 
	 * @param Raw user input from the console
	 * @throws IllegalArgumentException
	 */
	public void validateUserInput(String usrInput) throws IllegalArgumentException {
		if (usrInput == null || usrInput.equals("")) {
			// Null or empty input

			log.error(ERROR_MSG_NULL_OR_EMPTY_USER_INPUT);
			throw new IllegalArgumentException(ERROR_MSG_NULL_OR_EMPTY_USER_INPUT);
		}

		String[] usrInputArr = usrInput.split(" ");

		String command = usrInputArr[0].toUpperCase();

		if (command.equals(Command.PLACE.name())) {
			// PLACE command

			validatePlaceUserInput(usrInputArr);
		} else if (command.equals(Command.MOVE.name()) || command.equals(Command.LEFT.name())
				|| command.equals(Command.RIGHT.name()) || command.equals(Command.REPORT.name())) {
			// MOVE, LEFT, RIGHT and REPORT commands

			validateNonPlaceUserInput(usrInputArr);
		} else {
			// Invalid command

			log.error(ERROR_MSG_INVALID_COMMAND);
			throw new IllegalArgumentException(ERROR_MSG_INVALID_COMMAND);
		}
	}

	/**
	 * Validate the PLACE command.
	 * 
	 * @param Raw user input from the console
	 * @throws IllegalArgumentException
	 */
	public void validatePlaceUserInput(String[] usrInputArr) throws IllegalArgumentException {

		// PLACE command should have 1 part of params, e.g. "1,2,NORTH"
		if (usrInputArr.length != 2) {
			// Invalid PLACE command format

			log.error(ERROR_MSG_INVALID_PLACE_COMMAND_FORMAT);
			throw new IllegalArgumentException(ERROR_MSG_INVALID_PLACE_COMMAND_FORMAT);
		}

		String[] placeParamArr = usrInputArr[1].split(",");

		// The param part should have 3 parts, e.g. "1","2" and "NORTH"
		if (placeParamArr.length != 3) {
			// Invalid PLACE command format

			log.error(ERROR_MSG_INVALID_PLACE_COMMAND_FORMAT);
			throw new IllegalArgumentException(ERROR_MSG_INVALID_PLACE_COMMAND_FORMAT);
		}
	}

	/**
	 * Validate commands other than PLACE, i.e. MOVE/LEFT/RIGHT/REPORT.
	 * 
	 * @param Raw user input from the console
	 * @throws IllegalArgumentException
	 */
	public void validateNonPlaceUserInput(String[] usrInputArr) throws IllegalArgumentException {
		// MOVE, LEFT, RIGHT and REPORT commands should have 1 part only
		if (usrInputArr.length != 1) {
			// Invalid MOVE/LEFT/RIGHT/REPORT command format

			String errorMsg = String.format(ERROR_MSG_INVALID_NON_PLACE_COMMAND_FORMAT, usrInputArr[0].toUpperCase());

			log.error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}
	}

	/**
	 * Validate the robot's location.
	 * 
	 * @throws IllegalArgumentException
	 */
	public void validateRobotLocation() throws IllegalArgumentException {
		if (robot.getLocation() == null) {
			// Robot has no location

			log.error(ERROR_MSG_NO_LOCATION);
			throw new IllegalArgumentException(ERROR_MSG_NO_LOCATION);
		}
	}

	/**
	 * Validate on whether the parameters of PLACE command will cause the robot to be placed in an invalid location.
	 * 
	 * @param x-coordinate
	 * @param y-coordinate
	 * @param facing
	 * @throws IllegalArgumentException
	 */
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
			if (f.name().equals(facing.toUpperCase())) {
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

	/**
	 * Validate on whether the MOVE command will cause the robot to fall off.
	 * 
	 * @throws IllegalArgumentException
	 */
	public void validateMove() throws IllegalArgumentException {

		// Validate robot location
		validateRobotLocation();

		int XCor = robot.getLocation().getXCor();
		int YCor = robot.getLocation().getYCor();
		Facing facing = robot.getLocation().getFacing();

		if (facing.equals(Facing.EAST)) {
			if (XCor + 1 > 5) {
				// x-Coordinate distance out of bounce

				log.error(ERROR_MSG_ROBOT_FALL_OFF);
				throw new IllegalArgumentException(ERROR_MSG_ROBOT_FALL_OFF);
			}
		} else if (facing.equals(Facing.SOUTH)) {
			if (YCor - 1 < 0) {
				// y-Coordinate distance out of bounce

				log.error(ERROR_MSG_ROBOT_FALL_OFF);
				throw new IllegalArgumentException(ERROR_MSG_ROBOT_FALL_OFF);
			}
		} else if (facing.equals(Facing.WEST)) {
			if (XCor - 1 < 0) {
				// x-Coordinate distance out of bounce

				log.error(ERROR_MSG_ROBOT_FALL_OFF);
				throw new IllegalArgumentException(ERROR_MSG_ROBOT_FALL_OFF);
			}
		} else if (facing.equals(Facing.NORTH)) {
			if (YCor + 1 > 5) {
				// x-Coordinate distance out of bounce

				log.error(ERROR_MSG_ROBOT_FALL_OFF);
				throw new IllegalArgumentException(ERROR_MSG_ROBOT_FALL_OFF);
			}
		}
	}
}
