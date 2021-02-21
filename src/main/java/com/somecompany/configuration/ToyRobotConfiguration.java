package com.somecompany.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.somecompany.model.Robot;

/**
 * Configurations for the Toy Robot application.
 * 
 * @author N/A
 */
@Configuration
public class ToyRobotConfiguration {

	/**
	 * Robot bean.
	 * 
	 * @return Robot bean
	 */
	@Bean("robot")
	public Robot getRobot() {
		Robot robot = new Robot();
		return robot;
	}
}
