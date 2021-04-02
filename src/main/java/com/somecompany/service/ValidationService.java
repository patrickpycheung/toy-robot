package com.somecompany.service;

import com.somecompany.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    @Autowired
    @Qualifier("obstacle")
    private Obstacle obstacle;

    @Autowired
    @Qualifier("grid")
    private Grid grid;

    @Value("${grid.width.minAllowedSize}")
    private int gridWidthMinAllowedSize;

    @Value("${grid.width.maxAllowedSize}")
    private int gridWidthMaxAllowedSize;

    @Value("${grid.height.minAllowedSize}")
    private int gridHeightMinAllowedSize;

    @Value("${grid.height.maxAllowedSize}")
    private int gridHeightMaxAllowedSize;

    @Value("${errorMsg.invalidGridSizeInput}")
    private String ERROR_MSG_INVALID_GRID_SIZE_INPUT;

    @Value("${errorMsg.widthOutOfBounce}")
    private String ERROR_MSG_WIDTH_OUT_OF_BOUNCE;

    @Value("${errorMsg.heightOutOfBounce}")
    private String ERROR_MSG_HEIGHT_OUT_OF_BOUNCE;

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

    @Value("${errorMsg.placeObstacleAtNonExistentLocation}")
    private String ERROR_MSG_PLACE_ROBOT_AT_NON_EXISTENT_LOCATION;

    @Value("${errorMsg.robotHitObstacle}")
    private String ERROR_MSG_ROBOT_HIT_OBSTACLE;

    /**
     * Validate the grid size input from the console.
     *
     * @param Raw grid size input from the console
     * @throws IllegalArgumentException
     */
    public void validateGridSizeInput(String gridSizeInput) throws IllegalArgumentException {
        String[] gridSizeInputArr = gridSizeInput.split("x");

        // The params should be 2 integers
        if (gridSizeInputArr.length != 2) {
            log.error(ERROR_MSG_INVALID_GRID_SIZE_INPUT);
            throw new IllegalArgumentException(ERROR_MSG_INVALID_GRID_SIZE_INPUT);
        }

        try {

            // Validate width

            Integer width = Integer.parseInt(gridSizeInputArr[0]);

            if (width < gridWidthMinAllowedSize || width > gridWidthMaxAllowedSize) {
                // Width out of bounce

                String errorMsg = String.format(ERROR_MSG_WIDTH_OUT_OF_BOUNCE, gridWidthMinAllowedSize,
                        gridWidthMaxAllowedSize);

                log.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }

            // Validate height

            Integer height = Integer.parseInt(gridSizeInputArr[1]);

            if (height < gridHeightMinAllowedSize || height > gridHeightMaxAllowedSize) {
                // Height out of bounce

                String errorMsg = String.format(ERROR_MSG_HEIGHT_OUT_OF_BOUNCE, gridHeightMinAllowedSize,
                        gridHeightMaxAllowedSize);

                log.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }
        } catch (NumberFormatException exception) {
            // Cannot parse width and/or height to integer

            log.error(ERROR_MSG_INVALID_GRID_SIZE_INPUT);
            throw new IllegalArgumentException(ERROR_MSG_INVALID_GRID_SIZE_INPUT);
        }
    }

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
                || command.equals(Command.RIGHT.name()) || command.equals(Command.REPORT.name())
                || command.equals(Command.PLACE_OBSTACLE.name())) {
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

        int xCorLimit = grid.getWidth();
        int yCorLimit = grid.getHeight();

        // Validate x-Coordinate
        try {
            int XCor = Integer.valueOf(XCorStr);

            if (XCor < 0 || XCor > xCorLimit) {
                // x-Coordinate Distance out of bounce

                String errorMsg = String.format(ERROR_MSG_XCOR_OUT_OF_BOUNCE, xCorLimit);

                log.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }
        } catch (NumberFormatException exception) {
            // Cannot parse x-Coordinate to integer

            log.error(ERROR_MSG_XCOR_NOT_INTEGER);
            throw new IllegalArgumentException(ERROR_MSG_XCOR_NOT_INTEGER);
        }

        // Validate y-Coordinate
        try {
            int YCor = Integer.valueOf(YCorStr);

            if (YCor < 0 || YCor > yCorLimit) {
                // y-Coordinate Distance out of bounce

                String errorMsg = String.format(ERROR_MSG_YCOR_OUT_OF_BOUNCE, yCorLimit);

                log.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
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
     * Validate on whether the PLACE_OBSTACLE command will place the obstacle at an non-existent location.
     *
     * @throws IllegalArgumentException
     */
    public void validatePlaceObstacle() throws IllegalArgumentException {
        // Validate robot location
        validateRobotLocation();

        int XCor = robot.getLocation().getXCor();
        int YCor = robot.getLocation().getYCor();
        Facing facing = robot.getLocation().getFacing();

        if (facing.equals(Facing.EAST)) {
            if (XCor + 1 > 5) {
                // Robot as most eastward location

                log.error(ERROR_MSG_PLACE_ROBOT_AT_NON_EXISTENT_LOCATION);
                throw new IllegalArgumentException(ERROR_MSG_PLACE_ROBOT_AT_NON_EXISTENT_LOCATION);
            }
        } else if (facing.equals(Facing.SOUTH)) {
            if (YCor - 1 < 0) {
                // Robot as most southward location

                log.error(ERROR_MSG_PLACE_ROBOT_AT_NON_EXISTENT_LOCATION);
                throw new IllegalArgumentException(ERROR_MSG_PLACE_ROBOT_AT_NON_EXISTENT_LOCATION);
            }
        } else if (facing.equals(Facing.WEST)) {
            if (XCor - 1 < 0) {
                // Robot as most westward location

                log.error(ERROR_MSG_PLACE_ROBOT_AT_NON_EXISTENT_LOCATION);
                throw new IllegalArgumentException(ERROR_MSG_PLACE_ROBOT_AT_NON_EXISTENT_LOCATION);
            }
        } else if (facing.equals(Facing.NORTH)) {
            if (YCor + 1 > 5) {
                // Robot as most morthhward location

                log.error(ERROR_MSG_PLACE_ROBOT_AT_NON_EXISTENT_LOCATION);
                throw new IllegalArgumentException(ERROR_MSG_PLACE_ROBOT_AT_NON_EXISTENT_LOCATION);
            }
        }
    }

    /**
     * Validate on whether the MOVE command will cause the robot to fall off.
     *
     * @throws IllegalArgumentException
     */
    public void validateMove() throws IllegalArgumentException {

        int xCorLimit = grid.getWidth();
        int yCorLimit = grid.getHeight();

        // Validate robot location
        validateRobotLocation();

        int XCor = robot.getLocation().getXCor();
        int YCor = robot.getLocation().getYCor();
        Facing facing = robot.getLocation().getFacing();

        int obstacleXCor = 0;
        int obstacleYCor = 0;

        if (obstacle.getLocation() != null) {
            // Obstacle has been placed

            obstacleXCor = obstacle.getLocation().getXCor();
            obstacleYCor = obstacle.getLocation().getYCor();
        }


        if (facing.equals(Facing.EAST)) {
            if (XCor + 1 > xCorLimit) {
                // x-Coordinate distance out of bounce

                log.error(ERROR_MSG_ROBOT_FALL_OFF);
                throw new IllegalArgumentException(ERROR_MSG_ROBOT_FALL_OFF);
            }

            if (obstacle.getLocation() != null && obstacleXCor == XCor + 1 && obstacleYCor == YCor) {
                // Obstacle exists in the direction the robot is facing

                log.error(ERROR_MSG_ROBOT_HIT_OBSTACLE);
                throw new IllegalArgumentException(ERROR_MSG_ROBOT_HIT_OBSTACLE);
            }
        } else if (facing.equals(Facing.SOUTH)) {
            if (YCor - 1 < 0) {
                // y-Coordinate distance out of bounce

                log.error(ERROR_MSG_ROBOT_FALL_OFF);
                throw new IllegalArgumentException(ERROR_MSG_ROBOT_FALL_OFF);
            }

            if (obstacle.getLocation() != null && obstacleXCor == XCor && obstacleYCor == YCor - 1) {
                // Obstacle exists in the direction the robot is facing

                log.error(ERROR_MSG_ROBOT_HIT_OBSTACLE);
                throw new IllegalArgumentException(ERROR_MSG_ROBOT_HIT_OBSTACLE);
            }
        } else if (facing.equals(Facing.WEST)) {
            if (XCor - 1 < 0) {
                // x-Coordinate distance out of bounce

                log.error(ERROR_MSG_ROBOT_FALL_OFF);
                throw new IllegalArgumentException(ERROR_MSG_ROBOT_FALL_OFF);
            }

            if (obstacle.getLocation() != null && obstacleXCor == XCor - 1 && obstacleYCor == YCor) {
                // Obstacle exists in the direction the robot is facing

                log.error(ERROR_MSG_ROBOT_HIT_OBSTACLE);
                throw new IllegalArgumentException(ERROR_MSG_ROBOT_HIT_OBSTACLE);
            }
        } else if (facing.equals(Facing.NORTH)) {
            if (YCor + 1 > yCorLimit) {
                // x-Coordinate distance out of bounce

                log.error(ERROR_MSG_ROBOT_FALL_OFF);
                throw new IllegalArgumentException(ERROR_MSG_ROBOT_FALL_OFF);
            }

            if (obstacle.getLocation() != null && obstacleXCor == XCor && obstacleYCor == YCor + 1) {
                // Obstacle exists in the direction the robot is facing

                log.error(ERROR_MSG_ROBOT_HIT_OBSTACLE);
                throw new IllegalArgumentException(ERROR_MSG_ROBOT_HIT_OBSTACLE);
            }
        }
    }
}
