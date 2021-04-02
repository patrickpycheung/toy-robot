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
public class ToyRobotRightTest {

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

    @Test
    public void shouldBeAbleToFaceRobotSouthIfRobotIsFacingEastOnAPICall() {
        toyRobotService.place("1", "2", "EAST");

        webTestClient.post().uri("/api/toyrobot/right").exchange().expectStatus().isOk().expectBody(String.class)
                .value(result -> {
                    // Assertion
                    assertEquals(1, robot.getLocation().getXCor());
                    assertEquals(2, robot.getLocation().getYCor());
                    assertEquals(Facing.SOUTH, robot.getLocation().getFacing());
                });
    }

    @Test
    public void shouldBeAbleToFaceRobotWestIfRobotIsFacingSouthOnAPICall() {
        toyRobotService.place("1", "2", "SOUTH");

        webTestClient.post().uri("/api/toyrobot/right").exchange().expectStatus().isOk().expectBody(String.class)
                .value(result -> {
                    // Assertion
                    assertEquals(1, robot.getLocation().getXCor());
                    assertEquals(2, robot.getLocation().getYCor());
                    assertEquals(Facing.WEST, robot.getLocation().getFacing());
                });
    }

    @Test
    public void shouldBeAbleToFaceRobotNorthIfRobotIsFacingWestOnAPICall() {
        toyRobotService.place("1", "2", "WEST");

        webTestClient.post().uri("/api/toyrobot/right").exchange().expectStatus().isOk().expectBody(String.class)
                .value(result -> {
                    // Assertion
                    assertEquals(1, robot.getLocation().getXCor());
                    assertEquals(2, robot.getLocation().getYCor());
                    assertEquals(Facing.NORTH, robot.getLocation().getFacing());
                });
    }

    @Test
    public void shouldBeAbleToFaceRobotEastIfRobotIsFacingNorthOnAPICall() {
        toyRobotService.place("1", "2", "NORTH");

        webTestClient.post().uri("/api/toyrobot/right").exchange().expectStatus().isOk().expectBody(String.class)
                .value(result -> {
                    // Assertion
                    assertEquals(1, robot.getLocation().getXCor());
                    assertEquals(2, robot.getLocation().getYCor());
                    assertEquals(Facing.EAST, robot.getLocation().getFacing());
                });
    }

    @Test
    public void shouldBeAbleToThrowErrorIfRobotDoesNotHaveLocationOnAPICall() {
        webTestClient.post().uri("/api/toyrobot/right").exchange().expectStatus().isBadRequest().expectBody(String.class)
                .value(result -> {
                    // Assertion
                    assertEquals(ERROR_MSG_NO_LOCATION, result);
                });
    }
}
