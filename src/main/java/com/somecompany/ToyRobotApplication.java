package com.somecompany;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.somecompany.model.Command;
import com.somecompany.service.ToyRobotService;
import com.somecompany.service.ValidationService;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class ToyRobotApplication implements CommandLineRunner {

	@Autowired
	private ValidationService validationService;

	@Autowired
	private ToyRobotService toyRobotService;

	public static void main(String[] args) {
		SpringApplication.run(ToyRobotApplication.class, args);
	}

	@Value("${errorMsg.ioException}")
	private String ERROR_MSG_IO_EXCEPTION;

	@Override
	public void run(String... args) {
		// Instruct user to perform input
		System.out.println("Welcome to toy robot application!");
		System.out.println("Below are the possible operations:");
		System.out.println("PLACE <x-coordinate> <y-coordinate> <facing>");
		System.out.println("MOVE");
		System.out.println("LEFT");
		System.out.println("RIGHT");
		System.out.println("REPORT");

		while (true) {
			System.out.println("Please enter your command:");

			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				String usrInput = br.readLine();

				try {
					// Validate user input
					validationService.validateUserInput(usrInput);

					String[] usrInputArr = usrInput.split(" ");

					String command = usrInputArr[0].toUpperCase();

					if (command.equals(Command.PLACE.name())) {
						// PLACE command

						String[] placeParamArr = usrInputArr[1].split(",");

						String XCor = placeParamArr[0];
						String YCor = placeParamArr[1];
						String facing = placeParamArr[2];

						toyRobotService.place(XCor, YCor, facing);
					} else if (command.equals(Command.MOVE.name())) {
						// MOVE command

						toyRobotService.move();
					} else if (command.equals(Command.LEFT.name())) {
						// LEFT command

						toyRobotService.left();
					} else if (command.equals(Command.RIGHT.name())) {
						// RIGHT command

						toyRobotService.right();
					} else if (command.equals(Command.REPORT.name())) {
						// RIGHT command

						System.out.println(toyRobotService.report());
					}
				} catch (IllegalArgumentException exception) {
					System.out.println(exception.getMessage());
				}
			} catch (IOException exception) {

				System.out.println(ERROR_MSG_IO_EXCEPTION);
				log.error(ERROR_MSG_IO_EXCEPTION);
			}
		}
	}
}
