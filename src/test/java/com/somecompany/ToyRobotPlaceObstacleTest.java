package com.somecompany;

import com.somecompany.model.Grid;
import com.somecompany.model.Obstacle;
import com.somecompany.model.Robot;
import com.somecompany.service.ToyRobotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ToyRobotPlaceObstacleTest {

    @Autowired
    private Robot robot;

    @Autowired
    private Obstacle obstacle;

    @Autowired
    private Grid grid;

    @Autowired
    private ToyRobotService toyRobotService;

    @Autowired
    private WebTestClient webTestClient;

    @Value("${errorMsg.placeObstacleAtNonExistentLocation}")
    private String ERROR_MSG_PLACE_ROBOT_AT_NON_EXISTENT_LOCATION;

    @Value("${errorMsg.noLocation}")
    private String ERROR_MSG_NO_LOCATION;

    @BeforeEach
    public void init() {
        grid.setWidth(5);
        grid.setHeight(5);

        robot.setLocation(null);
        obstacle.setLocation(null);
    }

    @Test
    public void shouldBeAbleToPlaceObstacle() {
        toyRobotService.place("1", "2", "EAST");
        toyRobotService.placeObstacle();

        assertEquals(2, obstacle.getLocation().getXCor());
        assertEquals(2, obstacle.getLocation().getYCor());
    }

    @Test
    public void shouldBeAbleToThrowErrorIfAttemptToPlaceObstacleButRobotDoesNotHaveLocation() {
        // Assertion
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            toyRobotService.placeObstacle();
        });

        assertEquals(ERROR_MSG_NO_LOCATION, exception.getMessage());
    }

    @Test
    public void shouldBeAbleToThrowErrorIfAttemptToPlaceObstacleWhenRobotIsAtEastFringe() {
        toyRobotService.place("5", "2", "EAST");

        // Assertion
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            toyRobotService.placeObstacle();
        });

        assertEquals(ERROR_MSG_PLACE_ROBOT_AT_NON_EXISTENT_LOCATION, exception.getMessage());
    }

    @Test
    public void shouldBeAbleToThrowErrorIfAttemptToPlaceObstacleWhenRobotIsAtSouthFringe() {
        toyRobotService.place("1", "0", "SOUTH");

        // Assertion
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            toyRobotService.placeObstacle();
        });

        assertEquals(ERROR_MSG_PLACE_ROBOT_AT_NON_EXISTENT_LOCATION, exception.getMessage());
    }

    @Test
    public void shouldBeAbleToThrowErrorIfAttemptToPlaceObstacleWhenRobotIsAtWestFringe() {
        toyRobotService.place("0", "2", "WEST");

        // Assertion
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            toyRobotService.placeObstacle();
        });

        assertEquals(ERROR_MSG_PLACE_ROBOT_AT_NON_EXISTENT_LOCATION, exception.getMessage());
    }

    @Test
    public void shouldBeAbleToThrowErrorIfAttemptToPlaceObstacleWhenRobotIsAtNorthFringe() {
        toyRobotService.place("1", "5", "NORTH");

        // Assertion
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            toyRobotService.placeObstacle();
        });

        assertEquals(ERROR_MSG_PLACE_ROBOT_AT_NON_EXISTENT_LOCATION, exception.getMessage());
    }

    @Test
    public void shouldBeAbleToPlaceObstacleOnAPICall() throws InterruptedException {
        toyRobotService.place("1", "2", "EAST");
        toyRobotService.placeObstacle();

        webTestClient.put().uri("/api/toyrobot/placeObstacle").exchange().expectStatus().isOk().expectBody(String.class)
                .value(result -> {
                    assertEquals(2, obstacle.getLocation().getXCor());
                    assertEquals(2, obstacle.getLocation().getYCor());
                });
    }

    @Test
    public void shouldBeAbleToThrowErrorIfAttemptToPlaceObstacleButRobotDoesNotHaveLocationOnAPICall() {
        webTestClient.put().uri("/api/toyrobot/placeObstacle").exchange().expectStatus().isBadRequest().expectBody(String.class)
                .value(result -> {
                    // Assertion
                    assertEquals(ERROR_MSG_NO_LOCATION, result);
                });
    }

    @Test
    public void shouldBeAbleToThrowErrorIfAttemptToPlaceObstacleWhenRobotIsAtEastFringeOnAPICall() {
        toyRobotService.place("5", "2", "EAST");

        webTestClient.put().uri("/api/toyrobot/placeObstacle").exchange().expectStatus().isBadRequest().expectBody(String.class)
                .value(result -> {
                    // Assertion
                    assertEquals(ERROR_MSG_PLACE_ROBOT_AT_NON_EXISTENT_LOCATION, result);
                });
    }

    @Test
    public void shouldBeAbleToThrowErrorIfAttemptToPlaceObstacleWhenRobotIsAtSouthFringeOnAPICall() {
        toyRobotService.place("1", "0", "SOUTH");

        webTestClient.put().uri("/api/toyrobot/placeObstacle").exchange().expectStatus().isBadRequest().expectBody(String.class)
                .value(result -> {
                    // Assertion
                    assertEquals(ERROR_MSG_PLACE_ROBOT_AT_NON_EXISTENT_LOCATION, result);
                });
    }

    @Test
    public void shouldBeAbleToThrowErrorIfAttemptToPlaceObstacleWhenRobotIsAtWestFringeOnAPICall() {
        toyRobotService.place("0", "2", "WEST");

        webTestClient.put().uri("/api/toyrobot/placeObstacle").exchange().expectStatus().isBadRequest().expectBody(String.class)
                .value(result -> {
                    // Assertion
                    assertEquals(ERROR_MSG_PLACE_ROBOT_AT_NON_EXISTENT_LOCATION, result);
                });
    }

    @Test
    public void shouldBeAbleToThrowErrorIfAttemptToPlaceObstacleWhenRobotIsAtNorthFringeOnAPICall() {
        toyRobotService.place("1", "5", "NORTH");

        webTestClient.put().uri("/api/toyrobot/placeObstacle").exchange().expectStatus().isBadRequest().expectBody(String.class)
                .value(result -> {
                    // Assertion
                    assertEquals(ERROR_MSG_PLACE_ROBOT_AT_NON_EXISTENT_LOCATION, result);
                });
    }
}
