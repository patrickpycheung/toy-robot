package com.somecompany;

import com.somecompany.model.*;
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
public class ToyRobotReportTest {

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
    public void shouldBeAbleToReportLocationOfRobot() {
        Location location = new Location();
        location.setXCor(1);
        location.setYCor(2);
        location.setFacing(Facing.NORTH);

        robot.setLocation(location);

        // Actual result
        String result = toyRobotService.report();

        // Assertion
        assertEquals("Robot is at 1,2,NORTH", result);
    }

    @Test
    public void shouldBeAbleToReportLocationOfRobotAndObstacle() {
        Location robotLocation = new Location();
        robotLocation.setXCor(1);
        robotLocation.setYCor(2);
        robotLocation.setFacing(Facing.NORTH);

        robot.setLocation(robotLocation);

        Location obstacleLocation = new Location();
        obstacleLocation.setXCor(1);
        obstacleLocation.setYCor(3);

        obstacle.setLocation(obstacleLocation);

        // Actual result
        String result = toyRobotService.report();

        // Assertion
        assertEquals("Robot is at 1,2,NORTH; Obstacle is at 1,3", result);
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
    public void shouldBeAbleToReportLocationOfRobotOnAPICall() throws InterruptedException {
        Location location = new Location();
        location.setXCor(1);
        location.setYCor(2);
        location.setFacing(Facing.NORTH);

        robot.setLocation(location);

        webTestClient.get().uri("/api/toyrobot/report").exchange().expectStatus().isOk().expectBody(String.class)
                .value(result -> assertEquals("Robot is at 1,2,NORTH", result));
    }

    @Test
    public void shouldBeAbleToReportLocationOfRobotAndObstacleOnAPICall() throws InterruptedException {
        Location location = new Location();
        location.setXCor(1);
        location.setYCor(2);
        location.setFacing(Facing.NORTH);

        robot.setLocation(location);

        Location obstacleLocation = new Location();
        obstacleLocation.setXCor(1);
        obstacleLocation.setYCor(3);

        obstacle.setLocation(obstacleLocation);

        webTestClient.get().uri("/api/toyrobot/report").exchange().expectStatus().isOk().expectBody(String.class)
                .value(result -> assertEquals("Robot is at 1,2,NORTH; Obstacle is at 1,3", result));
    }

    @Test
    public void shouldBeAbleToThrowErrorIfRobotDoesNotHaveLocationOnAPICall() {
        webTestClient.get().uri("/api/toyrobot/report").exchange().expectStatus().isBadRequest()
                .expectBody(String.class).value(result -> assertEquals(ERROR_MSG_NO_LOCATION, result));
    }
}
