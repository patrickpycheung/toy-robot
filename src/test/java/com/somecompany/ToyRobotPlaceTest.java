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
import com.somecompany.model.Robot;
import com.somecompany.service.ToyRobotService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ToyRobotApplication.class, initializers = ConfigDataApplicationContextInitializer.class)
public class ToyRobotPlaceTest {

	@Autowired
	private Robot robot;

	@Autowired
	private ToyRobotService toyRobotService;

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

	@BeforeEach
	public void init() {
		robot.setLocation(null);
	}

	@Test
	public void shouldBeAbleToPlaceRobot() {
		// Actual result
		toyRobotService.place("1", "2", "NORTH");

		// Assertion
		assertEquals(1, robot.getLocation().getXCor());
		assertEquals(2, robot.getLocation().getYCor());
		assertEquals(Facing.NORTH, robot.getLocation().getFacing());
	}

	@Test
	public void shouldBeAbleToThrowErrorIfXCorCannotBeParsedToInteger() {

		// Assertion
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			toyRobotService.place("a", "2", "NORTH");
		});

		assertEquals(ERROR_MSG_XCOR_NOT_INTEGER, exception.getMessage());
	}

	@Test
	public void shouldBeAbleToThrowErrorIfXCorIsOutOfOfLowerBound() {

		// Assertion
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			toyRobotService.place("-1", "2", "NORTH");
		});

		assertEquals(ERROR_MSG_XCOR_OUT_OF_BOUNCE, exception.getMessage());
	}

	@Test
	public void shouldBeAbleToThrowErrorIfXCorIsOutOfOfUpperBound() {

		// Assertion
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			toyRobotService.place("6", "2", "NORTH");
		});

		assertEquals(ERROR_MSG_XCOR_OUT_OF_BOUNCE, exception.getMessage());
	}

	@Test
	public void shouldBeAbleToThrowErrorIfYCorCannotBeParsedToInteger() {

		// Assertion
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			toyRobotService.place("1", "a", "NORTH");
		});

		assertEquals(ERROR_MSG_YCOR_NOT_INTEGER, exception.getMessage());
	}

	@Test
	public void shouldBeAbleToThrowErrorIfYCorIsOutOfOfLowerBound() {

		// Assertion
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			toyRobotService.place("1", "-1", "NORTH");
		});

		assertEquals(ERROR_MSG_YCOR_OUT_OF_BOUNCE, exception.getMessage());
	}

	@Test
	public void shouldBeAbleToThrowErrorIfYCorIsOutOfOfUpperBound() {

		// Assertion
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			toyRobotService.place("1", "6", "NORTH");
		});

		assertEquals(ERROR_MSG_YCOR_OUT_OF_BOUNCE, exception.getMessage());
	}

	@Test
	public void shouldBeAbleToThrowErrorIfFacingIsInvalid() {

		// Assertion
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			toyRobotService.place("1", "2", "a");
		});

		assertEquals(ERROR_MSG_INVALID_FACING, exception.getMessage());
	}
}
