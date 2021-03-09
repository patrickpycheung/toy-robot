package com.somecompany;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import com.somecompany.model.Command;
import com.somecompany.model.Grid;
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

	@Autowired
	@Qualifier("grid")
	private Grid grid;

	public static void main(String[] args) {
		SpringApplication.run(ToyRobotApplication.class, args);
	}

	@Autowired
	private Environment env;

	@Value("${inputFile.Path}")
	private String inputFilePath;

	@Value("${inputFile.Renamed.folder}")
	private String inputFileRenamedFolder;

	@Value("${inputFile.Renamed.suffix}")
	private String inputFileRenamedSuffix;

	@Value("${errorMsg.ioException}")
	private String ERROR_MSG_IO_EXCEPTION;

	@Override
	public void run(String... args) {

		if (env.getActiveProfiles().length == 0 || !env.getActiveProfiles()[0].equals("test")) {
			// Using non-test Spring profile

			/* Set grid size */

			while (true) {
				System.out.println("Please enter the grid size (widthxheight, e.g. 5x5):");
				System.out.println("Leave this field empty to set grid size as default value (i.e. 5x5)");

				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
					String gridSizeInput = br.readLine();

					try {
						// Set default if there is no input specified
						if (gridSizeInput.equals("")) {
							grid.setWidth(5);
							grid.setHeight(5);
							break;
						}

						// Validate the grid size input
						validationService.validateGridSizeInput(gridSizeInput);

						// Set the grid size

						String[] gridSizeInputArr = gridSizeInput.split("x");

						grid.setWidth(Integer.parseInt(gridSizeInputArr[0]));
						grid.setHeight(Integer.parseInt(gridSizeInputArr[1]));

						break;
					} catch (IllegalArgumentException exception) {
						System.out.println(exception.getMessage());
					}

				} catch (IOException exception) {

					System.out.println(ERROR_MSG_IO_EXCEPTION);
					log.error(ERROR_MSG_IO_EXCEPTION);
				}
			}

			/* Handle file input */

			System.out.println("Begin handling file input...");

			try {
				File inputFile = new File(inputFilePath);

				BufferedReader fileBR = new BufferedReader(new FileReader(inputFile));

				try {
					String usrInput;
					while ((usrInput = fileBR.readLine()) != null) {
						// Handle one line of command
						handleUserInput(usrInput);
					}

					// Rename file after processing

					String inputFileName = inputFile.getName();
					Instant instant = Instant.now();

					// Append current timestamp to file, and move to processed folder
					File renamedFile = new File(
							inputFileRenamedFolder + inputFileName + inputFileRenamedSuffix + instant);

					inputFile.renameTo(renamedFile);
				} catch (IOException e) {

					System.out.println(ERROR_MSG_IO_EXCEPTION);
					log.error(ERROR_MSG_IO_EXCEPTION);
				}
			} catch (FileNotFoundException e) {
				// No file input, just proceed
			}

			System.out.println("Finished handling file input.");

			/* Handle manual input on console */

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

					handleUserInput(usrInput);
				} catch (IOException exception) {

					System.out.println(ERROR_MSG_IO_EXCEPTION);
					log.error(ERROR_MSG_IO_EXCEPTION);
				}
			}
		}
	}

	/**
	 * Handle the user input. This may be a command from file or by manual input at command line.
	 * 
	 * @param usrInput
	 */
	private void handleUserInput(String usrInput) {
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
				// REPORT command

				System.out.println(toyRobotService.report());
			}
		} catch (IllegalArgumentException exception) {
			System.out.println(exception.getMessage());
		}
	}
}
