package com.somecompany;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import com.somecompany.model.Facing;
import com.somecompany.model.Grid;
import com.somecompany.model.Robot;
import com.somecompany.service.ToyRobotService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ToyRobotPlaceTest {

	@Autowired
	private Robot robot;

	@Autowired
	private Grid grid;

	@Autowired
	private ToyRobotService toyRobotService;

	@Autowired
	private WebTestClient webTestClient;

	@Value("${errorMsg.invalidApiParams}")
	private String ERROR_INVALID_API_PARAMS;

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
		grid.setWidth(5);
		grid.setHeight(5);

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

		String errorMsg = String.format(ERROR_MSG_XCOR_OUT_OF_BOUNCE, 5);

		assertEquals(errorMsg, exception.getMessage());
	}

	@Test
	public void shouldBeAbleToThrowErrorIfXCorIsOutOfOfUpperBound() {

		// Assertion
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			toyRobotService.place("6", "2", "NORTH");
		});

		String errorMsg = String.format(ERROR_MSG_XCOR_OUT_OF_BOUNCE, 5);

		assertEquals(errorMsg, exception.getMessage());
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

		String errorMsg = String.format(ERROR_MSG_YCOR_OUT_OF_BOUNCE, 5);

		assertEquals(errorMsg, exception.getMessage());
	}

	@Test
	public void shouldBeAbleToThrowErrorIfYCorIsOutOfOfUpperBound() {

		// Assertion
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			toyRobotService.place("1", "6", "NORTH");
		});

		String errorMsg = String.format(ERROR_MSG_YCOR_OUT_OF_BOUNCE, 5);

		assertEquals(errorMsg, exception.getMessage());
	}

	@Test
	public void shouldBeAbleToThrowErrorIfFacingIsInvalid() {

		// Assertion
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			toyRobotService.place("1", "2", "a");
		});

		assertEquals(ERROR_MSG_INVALID_FACING, exception.getMessage());
	}

	@Test
	public void shouldBeAbleToPlaceRobotOnAPICall() {

		String body = "{\n" + "\"facing\":\"NORTH\",\n" + "\"xcor\":1,\n" + "\"ycor\":2\n" + "}";

		webTestClient.put().uri("/api/toyrobot/place").contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(body)).exchange().expectStatus().isOk().expectBody(String.class)
				.value(result -> {
					// Assertion
					assertEquals(1, robot.getLocation().getXCor());
					assertEquals(2, robot.getLocation().getYCor());
					assertEquals(Facing.NORTH, robot.getLocation().getFacing());
				});
	}

	@Test
	public void shouldBeAbleToThrowErrorIfXCorCannotBeParsedToIntegerOnAPICall() {

		String body = "{\n" + "\"facing\":\"NORTH\",\n" + "\"xcor\":a,\n" + "\"ycor\":2\n" + "}";

		webTestClient.put().uri("/api/toyrobot/place").contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(body)).exchange().expectStatus().isBadRequest().expectBody(String.class)
				.value(result -> {
					// Assertion
					assertEquals(ERROR_INVALID_API_PARAMS, result);
				});
	}

	@Test
	public void shouldBeAbleToThrowErrorIfXCorIsOutOfOfLowerBoundOnAPICall() {

		String body = "{\n" + "\"facing\":\"NORTH\",\n" + "\"xcor\":-1,\n" + "\"ycor\":2\n" + "}";

		webTestClient.put().uri("/api/toyrobot/place").contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(body)).exchange().expectStatus().isBadRequest().expectBody(String.class)
				.value(result -> {
					// Assertion
					String errorMsg = String.format(ERROR_MSG_XCOR_OUT_OF_BOUNCE, 5);
					assertEquals(errorMsg, result);
				});
	}

	@Test
	public void shouldBeAbleToThrowErrorIfXCorIsOutOfOfUpperBoundOnAPICall() {

		String body = "{\n" + "\"facing\":\"NORTH\",\n" + "\"xcor\":6,\n" + "\"ycor\":2\n" + "}";

		webTestClient.put().uri("/api/toyrobot/place").contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(body)).exchange().expectStatus().isBadRequest().expectBody(String.class)
				.value(result -> {
					// Assertion
					String errorMsg = String.format(ERROR_MSG_XCOR_OUT_OF_BOUNCE, 5);
					assertEquals(errorMsg, result);
				});
	}

	@Test
	public void shouldBeAbleToThrowErrorIfYCorCannotBeParsedToIntegerOnAPICall() {

		String body = "{\n" + "\"facing\":\"NORTH\",\n" + "\"xcor\":1,\n" + "\"ycor\":a\n" + "}";

		webTestClient.put().uri("/api/toyrobot/place").contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(body)).exchange().expectStatus().isBadRequest().expectBody(String.class)
				.value(result -> {
					// Assertion
					assertEquals(ERROR_INVALID_API_PARAMS, result);
				});
	}

	@Test
	public void shouldBeAbleToThrowErrorIfYCorIsOutOfOfLowerBoundOnAPICall() {

		String body = "{\n" + "\"facing\":\"NORTH\",\n" + "\"xcor\":1,\n" + "\"ycor\":-1\n" + "}";

		webTestClient.put().uri("/api/toyrobot/place").contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(body)).exchange().expectStatus().isBadRequest().expectBody(String.class)
				.value(result -> {
					// Assertion
					String errorMsg = String.format(ERROR_MSG_YCOR_OUT_OF_BOUNCE, 5);
					assertEquals(errorMsg, result);
				});
	}

	@Test
	public void shouldBeAbleToThrowErrorIfYCorIsOutOfOfUpperBoundOnAPICall() {

		String body = "{\n" + "\"facing\":\"NORTH\",\n" + "\"xcor\":1,\n" + "\"ycor\":6\n" + "}";

		webTestClient.put().uri("/api/toyrobot/place").contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(body)).exchange().expectStatus().isBadRequest().expectBody(String.class)
				.value(result -> {
					// Assertion
					String errorMsg = String.format(ERROR_MSG_YCOR_OUT_OF_BOUNCE, 5);
					assertEquals(errorMsg, result);
				});
	}

	@Test
	public void shouldBeAbleToThrowErrorIfFacingIsInvalidOnAPICall() {

		String body = "{\n" + "\"facing\":\"a\",\n" + "\"xcor\":1,\n" + "\"ycor\":2\n" + "}";

		webTestClient.put().uri("/api/toyrobot/place").contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(body)).exchange().expectStatus().isBadRequest().expectBody(String.class)
				.value(result -> {
					// Assertion
					assertEquals(ERROR_INVALID_API_PARAMS, result);
				});
	}
}
