package com.somecompany;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.somecompany.model.Facing;
import com.somecompany.model.Grid;
import com.somecompany.model.Location;
import com.somecompany.model.Robot;
import com.somecompany.service.ToyRobotService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ToyRobotReportTest {

	@Autowired
	private Robot robot;

	@Autowired
	private Grid grid;

	@Autowired
	private ToyRobotService toyRobotService;

	@Autowired
	private WebTestClient webTestClient;

	@Value("${errorMsg.noLocation}")
	private String ERROR_MSG_NO_LOCATION;

	@BeforeEach
	public void init() {
		grid.setWidth(5);
		grid.setHeight(5);

		robot.setLocation(null);
	}

	@Test
	public void shouldBeAbleToReportLocation() {
		Location location = new Location();
		location.setXCor(1);
		location.setYCor(2);
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

	@Test
	public void shouldBeAbleToReportLocationOnAPICall() throws InterruptedException {
		Location location = new Location();
		location.setXCor(1);
		location.setYCor(2);
		location.setFacing(Facing.NORTH);

		robot.setLocation(location);

		webTestClient.get().uri("/api/toyrobot/report").exchange().expectStatus().isOk().expectBody(String.class)
				.value(result -> assertEquals("1,2,NORTH", result));
	}

	@Test
	public void shouldBeAbleToThrowErrorIfRobotDoesNotHaveLocationOnAPICall() {
		webTestClient.get().uri("/api/toyrobot/report").exchange().expectStatus().isBadRequest()
				.expectBody(String.class).value(result -> assertEquals(ERROR_MSG_NO_LOCATION, result));
	}
}
