package com.somecompany;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.somecompany.model.Facing;
import com.somecompany.model.Location;
import com.somecompany.model.Robot;
import com.somecompany.service.ToyRobotService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ToyRobotApplication.class, initializers = ConfigDataApplicationContextInitializer.class)
public class ToyRobotRightTest {

	@Autowired
	private Robot robot;

	@Autowired
	private ToyRobotService toyRobotService;

	@Value("${errorMsg.noLocation}")
	private String ERROR_MSG_NO_LOCATION;

	@BeforeEach
	public void init() {
		robot.setLocation(null);
	}

	@Test
	public void shouldBeAbleToFaceRobotSouthIfRobotIsFacingEast() {

		Location location = new Location();
		location.setXCor(1);
		location.setYCor(2);
		location.setFacing(Facing.EAST);

		robot.setLocation(location);

		// Actual result
		toyRobotService.right();

		// Assertion
		assertEquals(1, robot.getLocation().getXCor());
		assertEquals(2, robot.getLocation().getYCor());
		assertEquals(Facing.SOUTH, robot.getLocation().getFacing());
	}

	@Test
	public void shouldBeAbleToFaceRobotWestIfRobotIsFacingSouth() {

		Location location = new Location();
		location.setXCor(1);
		location.setYCor(2);
		location.setFacing(Facing.SOUTH);

		robot.setLocation(location);

		// Actual result
		toyRobotService.right();

		// Assertion
		assertEquals(1, robot.getLocation().getXCor());
		assertEquals(2, robot.getLocation().getYCor());
		assertEquals(Facing.WEST, robot.getLocation().getFacing());
	}

	@Test
	public void shouldBeAbleToFaceRobotNorthIfRobotIsFacingWest() {

		Location location = new Location();
		location.setXCor(1);
		location.setYCor(2);
		location.setFacing(Facing.WEST);

		robot.setLocation(location);

		// Actual result
		toyRobotService.right();

		// Assertion
		assertEquals(1, robot.getLocation().getXCor());
		assertEquals(2, robot.getLocation().getYCor());
		assertEquals(Facing.NORTH, robot.getLocation().getFacing());
	}

	@Test
	public void shouldBeAbleToFaceRobotEastIfRobotIsFacingNorth() {

		Location location = new Location();
		location.setXCor(1);
		location.setYCor(2);
		location.setFacing(Facing.NORTH);

		robot.setLocation(location);

		// Actual result
		toyRobotService.right();

		// Assertion
		assertEquals(1, robot.getLocation().getXCor());
		assertEquals(2, robot.getLocation().getYCor());
		assertEquals(Facing.EAST, robot.getLocation().getFacing());
	}

	@Test
	public void shouldBeAbleToThrowErrorIfRobotDoesNotHaveLocation() {
		// Assertion
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			toyRobotService.right();
		});

		assertEquals(ERROR_MSG_NO_LOCATION, exception.getMessage());
	}
}