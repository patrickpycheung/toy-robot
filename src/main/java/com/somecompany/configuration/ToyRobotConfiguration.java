package com.somecompany.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.somecompany.model.Robot;

@Configuration
public class ToyRobotConfiguration {

	@Bean("robot")
	public Robot getRobot() {
		Robot robot = new Robot();
		return robot;
	}
}
