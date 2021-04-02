package com.somecompany.controller;

import com.somecompany.model.Location;
import com.somecompany.service.ToyRobotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/toyrobot")
public class ToyRobotController {

    @Autowired
    private ToyRobotService toyRobotService;

    @Value("${errorMsg.invalidApiParams}")
    private String ERROR_INVALID_API_PARAMS;

    /**
     * API endpoint for "REPORT" function.
     *
     * @return ResponseEntity<String>
     */
    @GetMapping("/report")
    public ResponseEntity<Mono<String>> report() {

        try {
            return ResponseEntity.status(HttpStatus.OK).body(Mono.just(toyRobotService.report()));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Mono.just(exception.getMessage()));
        }
    }

    /**
     * API endpoint for "PLACE" function.
     *
     * @param location
     * @return ResponseEntity<String>
     */
    @PutMapping("/place")
    public ResponseEntity<Mono<String>> place(@RequestBody Location location) {

        try {
            toyRobotService.place(String.valueOf(location.getXCor()), String.valueOf(location.getYCor()),
                    location.getFacing().name());
            return ResponseEntity.status(HttpStatus.OK).body(Mono.just("Successfully placed robot on grid."));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Mono.just(exception.getMessage()));
        }

    }

    /**
     * API endpoint for "MOVE" function.
     *
     * @return ResponseEntity<Mono < String>>
     */
    @PostMapping("/move")
    public ResponseEntity<Mono<String>> move() {

        try {
            toyRobotService.move();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Mono.just("Successfully moved robot, new location is " + toyRobotService.report()));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Mono.just(exception.getMessage()));
        }
    }

    /**
     * API endpoint for "LEFT" function.
     *
     * @return ResponseEntity<Mono < String>>
     */
    @PostMapping("/left")
    public ResponseEntity<Mono<String>> left() {

        try {
            toyRobotService.left();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Mono.just("Successfully turned robot to the left, new location is " + toyRobotService.report()));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Mono.just(exception.getMessage()));
        }
    }

    /**
     * API endpoint for "RIGHT" function.
     *
     * @return ResponseEntity<Mono < String>>
     */
    @PostMapping("/right")
    public ResponseEntity<Mono<String>> right() {

        try {
            toyRobotService.right();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Mono.just("Successfully turned robot to the left, new location is " + toyRobotService.report()));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Mono.just(exception.getMessage()));
        }
    }

    /**
     * Handle handleHttpMessageNotReadableException, e.g. cannot parse the parameters.
     *
     * @return ResponseEntity<String>
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException() {

        return ResponseEntity.badRequest().body(ERROR_INVALID_API_PARAMS);
    }
}