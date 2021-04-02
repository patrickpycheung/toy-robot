package com.somecompany.configuration;

import com.somecompany.model.Grid;
import com.somecompany.model.Obstacle;
import com.somecompany.model.Robot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    @Bean("obstacle")
    public Obstacle getObstacle() {
        Obstacle obstacle = new Obstacle();
        return obstacle;
    }

    /**
     * Grid bean.
     *
     * @return Grid bean
     */
    @Bean("grid")
    public Grid getGrid() {
        Grid grid = new Grid();
        return grid;
    }
}
