package com.somecompany.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.somecompany.service.ToyRobotService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/toyrobot")
public class ToyRobotController {

	@Autowired
	private ToyRobotService toyRobotService;

	@GetMapping("/report")
	public ResponseEntity<Mono<String>> report() {

		try {
			return ResponseEntity.status(HttpStatus.OK).body(Mono.just(toyRobotService.report()));
		} catch (IllegalArgumentException exception) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Mono.just(exception.getMessage()));
		}
	}
}