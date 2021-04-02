package com.somecompany;

import com.somecompany.model.Facing;
import com.somecompany.model.Grid;
import com.somecompany.model.Location;
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
public class ToyRobotMoveTest {

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

    @Value("${errorMsg.robotFallOff}")
    private String ERROR_MSG_ROBOT_FALL_OFF;

    @BeforeEach
    public void init() {
        grid.setWidth(5);
        grid.setHeight(5);

        robot.setLocation(null);
    }

    @Test
    public void shouldBeAbleToMoveRobotEast() {

        Location location = new Location();
        location.setXCor(1);
        location.setYCor(2);
        location.setFacing(Facing.EAST);

        robot.setLocation(location);

        // Actual result
        toyRobotService.move();

        // Assertion
        assertEquals(2, robot.getLocation().getXCor());
        assertEquals(2, robot.getLocation().getYCor());
        assertEquals(Facing.EAST, robot.getLocation().getFacing());
    }

    @Test
    public void shouldBeAbleToMoveRobotSouth() {

        Location location = new Location();
        location.setXCor(1);
        location.setYCor(2);
        location.setFacing(Facing.SOUTH);

        robot.setLocation(location);

        // Actual result
        toyRobotService.move();

        // Assertion
        assertEquals(1, robot.getLocation().getXCor());
        assertEquals(1, robot.getLocation().getYCor());
        assertEquals(Facing.SOUTH, robot.getLocation().getFacing());
    }

    @Test
    public void shouldBeAbleToMoveRobotWest() {

        Location location = new Location();
        location.setXCor(1);
        location.setYCor(2);
        location.setFacing(Facing.WEST);

        robot.setLocation(location);

        // Actual result
        toyRobotService.move();

        // Assertion
        assertEquals(0, robot.getLocation().getXCor());
        assertEquals(2, robot.getLocation().getYCor());
        assertEquals(Facing.WEST, robot.getLocation().getFacing());
    }

    @Test
    public void shouldBeAbleToMoveRobotNorth() {

        Location location = new Location();
        location.setXCor(1);
        location.setYCor(2);
        location.setFacing(Facing.NORTH);

        robot.setLocation(location);

        // Actual result
        toyRobotService.move();

        // Assertion
        assertEquals(1, robot.getLocation().getXCor());
        assertEquals(3, robot.getLocation().getYCor());
        assertEquals(Facing.NORTH, robot.getLocation().getFacing());
    }

    @Test
    public void shouldBeAbleToThrowErrorIfRobotDoesNotHaveLocation() {
        // Assertion
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            toyRobotService.move();
        });

        assertEquals(ERROR_MSG_NO_LOCATION, exception.getMessage());
    }

    @Test
    public void shouldBeAbleToThrowErrorIfRobotWillFallOffAfterMoveEast() {

        Location location = new Location();
        location.setXCor(5);
        location.setYCor(2);
        location.setFacing(Facing.EAST);

        robot.setLocation(location);

        // Assertion
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            toyRobotService.move();
        });

        assertEquals(ERROR_MSG_ROBOT_FALL_OFF, exception.getMessage());

        // Location should not have changed
        assertEquals(5, robot.getLocation().getXCor());
        assertEquals(2, robot.getLocation().getYCor());
        assertEquals(Facing.EAST, robot.getLocation().getFacing());
    }

    @Test
    public void shouldBeAbleToThrowErrorIfRobotWillFallOffAfterMoveSouth() {

        Location location = new Location();
        location.setXCor(1);
        location.setYCor(0);
        location.setFacing(Facing.SOUTH);

        robot.setLocation(location);

        // Assertion
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            toyRobotService.move();
        });

        assertEquals(ERROR_MSG_ROBOT_FALL_OFF, exception.getMessage());

        // Location should not have changed
        assertEquals(1, robot.getLocation().getXCor());
        assertEquals(0, robot.getLocation().getYCor());
        assertEquals(Facing.SOUTH, robot.getLocation().getFacing());
    }

    @Test
    public void shouldBeAbleToThrowErrorIfRobotWillFallOffAfterMoveWest() {

        Location location = new Location();
        location.setXCor(0);
        location.setYCor(2);
        location.setFacing(Facing.WEST);

        robot.setLocation(location);

        // Assertion
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            toyRobotService.move();
        });

        assertEquals(ERROR_MSG_ROBOT_FALL_OFF, exception.getMessage());

        // Location should not have changed
        assertEquals(0, robot.getLocation().getXCor());
        assertEquals(2, robot.getLocation().getYCor());
        assertEquals(Facing.WEST, robot.getLocation().getFacing());
    }

    @Test
    public void shouldBeAbleToThrowErrorIfRobotWillFallOffAfterMoveNorth() {

        Location location = new Location();
        location.setXCor(1);
        location.setYCor(5);
        location.setFacing(Facing.NORTH);

        robot.setLocation(location);

        // Assertion
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            toyRobotService.move();
        });

        assertEquals(ERROR_MSG_ROBOT_FALL_OFF, exception.getMessage());

        // Location should not have changed
        assertEquals(1, robot.getLocation().getXCor());
        assertEquals(5, robot.getLocation().getYCor());
        assertEquals(Facing.NORTH, robot.getLocation().getFacing());
    }

    @Test
    public void shouldBeAbleToMoveRobotEastOnAPICall() {
        toyRobotService.place("1", "2", "NORTH");

        webTestClient.post().uri("/api/toyrobot/move").exchange().expectStatus().isOk().expectBody(String.class)
                .value(result -> {
                    // Assertion
                    assertEquals(1, robot.getLocation().getXCor());
                    assertEquals(3, robot.getLocation().getYCor());
                    assertEquals(Facing.NORTH, robot.getLocation().getFacing());
                });
    }

    @Test
    public void shouldBeAbleToMoveRobotSouthOnAPICall() {
        toyRobotService.place("1", "2", "SOUTH");

        webTestClient.post().uri("/api/toyrobot/move").exchange().expectStatus().isOk().expectBody(String.class)
                .value(result -> {
                    // Assertion
                    assertEquals(1, robot.getLocation().getXCor());
                    assertEquals(1, robot.getLocation().getYCor());
                    assertEquals(Facing.SOUTH, robot.getLocation().getFacing());
                });
    }

    @Test
    public void shouldBeAbleToMoveRobotWestOnAPICall() {
        toyRobotService.place("1", "2", "WEST");

        webTestClient.post().uri("/api/toyrobot/move").exchange().expectStatus().isOk().expectBody(String.class)
                .value(result -> {
                    // Assertion
                    assertEquals(0, robot.getLocation().getXCor());
                    assertEquals(2, robot.getLocation().getYCor());
                    assertEquals(Facing.WEST, robot.getLocation().getFacing());
                });
    }

    @Test
    public void shouldBeAbleToMoveRobotNorthOnAPICall() {
        toyRobotService.place("1", "2", "NORTH");

        webTestClient.post().uri("/api/toyrobot/move").exchange().expectStatus().isOk().expectBody(String.class)
                .value(result -> {
                    // Assertion
                    assertEquals(1, robot.getLocation().getXCor());
                    assertEquals(3, robot.getLocation().getYCor());
                    assertEquals(Facing.NORTH, robot.getLocation().getFacing());
                });
    }

    @Test
    public void shouldBeAbleToThrowErrorIfRobotDoesNotHaveLocationOnAPICall() {
        webTestClient.post().uri("/api/toyrobot/move").exchange().expectStatus().isBadRequest().expectBody(String.class)
                .value(result -> {
                    // Assertion
                    assertEquals(ERROR_MSG_NO_LOCATION, result);
                });
    }

    @Test
    public void shouldBeAbleToThrowErrorIfRobotWillFallOffAfterMoveEastOnAPICall() {
        toyRobotService.place("5", "2", "EAST");

        webTestClient.post().uri("/api/toyrobot/move").exchange().expectStatus().isBadRequest().expectBody(String.class)
                .value(result -> {
                    // Assertion
                    // Location should not have changed
                    assertEquals(5, robot.getLocation().getXCor());
                    assertEquals(2, robot.getLocation().getYCor());
                    assertEquals(Facing.EAST, robot.getLocation().getFacing());
                });
    }

    @Test
    public void shouldBeAbleToThrowErrorIfRobotWillFallOffAfterMoveSouthOnAPICall() {
        toyRobotService.place("1", "0", "SOUTH");

        webTestClient.post().uri("/api/toyrobot/move").exchange().expectStatus().isBadRequest().expectBody(String.class)
                .value(result -> {
                    // Assertion
                    // Location should not have changed
                    assertEquals(1, robot.getLocation().getXCor());
                    assertEquals(0, robot.getLocation().getYCor());
                    assertEquals(Facing.SOUTH, robot.getLocation().getFacing());
                });
    }

    @Test
    public void shouldBeAbleToThrowErrorIfRobotWillFallOffAfterMoveWestOnAPICall() {
        toyRobotService.place("0", "2", "WEST");

        webTestClient.post().uri("/api/toyrobot/move").exchange().expectStatus().isBadRequest().expectBody(String.class)
                .value(result -> {
                    // Assertion
                    // Location should not have changed
                    assertEquals(0, robot.getLocation().getXCor());
                    assertEquals(2, robot.getLocation().getYCor());
                    assertEquals(Facing.WEST, robot.getLocation().getFacing());
                });
    }

    @Test
    public void shouldBeAbleToThrowErrorIfRobotWillFallOffAfterMoveNorthOnAPICall() {
        toyRobotService.place("1", "5", "NORTH");

        webTestClient.post().uri("/api/toyrobot/move").exchange().expectStatus().isBadRequest().expectBody(String.class)
                .value(result -> {
                    // Assertion
                    // Location should not have changed
                    assertEquals(1, robot.getLocation().getXCor());
                    assertEquals(5, robot.getLocation().getYCor());
                    assertEquals(Facing.NORTH, robot.getLocation().getFacing());
                });
    }
}
