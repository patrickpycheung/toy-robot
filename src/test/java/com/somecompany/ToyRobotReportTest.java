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
public class ToyRobotReportTest {

	@Autowired
	private Robot robot;

	@Autowired
	private ToyRobotService toyRobotService;

	@BeforeEach
	public void init() {
		robot.setLocation(null);
	}

	@Value("${errorMsg.noLocation}")
	private String ERROR_MSG_NO_LOCATION;

	@Test
	public void shouldBeAbleToReportLocation() {
		Location location = new Location();
		location.setXCor("1");
		location.setYCor("2");
		location.setFacing(Facing.NORTH);

		robot.setLocation(location);

		// Actual result
		String result = toyRobotService.report();

		// Assertion
		assertEquals("1,2,NORTH", result);
	}

	@Test
	public void shouldBeAbleToThrowErrorIfRobotDoesNotHaveLocation() {
		// Assertion
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			toyRobotService.report();
		});

		assertEquals(ERROR_MSG_NO_LOCATION, exception.getMessage());
	}
}
