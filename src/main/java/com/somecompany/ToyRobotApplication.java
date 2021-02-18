package com.somecompany;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ToyRobotApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ToyRobotApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
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

			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String usrInput = br.readLine();
		}
	}
}
