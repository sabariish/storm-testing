package com.ts;

import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.web.client.RestTemplate;

//@EnableEurekaServer 
@SpringBootApplication
public class RestCountriesApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestCountriesApplication.class, args);
		RestTemplate restTemplate = new RestTemplate();
		final String countryCodeURI = "http://localhost:9000/api/countryCode/";
		final String countryNameURI = "http://localhost:9000/api/countryName/";

		Scanner input = new Scanner(System.in);
		int selection;
		boolean exit = false;
		while (!exit) {
			System.out.println("Menu : ");
			System.out.println("Type any number between 1 and 2");
			System.out.println("1)Enter Country Code");
			System.out.println("2)Enter Country Name");

			selection = input.nextInt();
			switch (selection) {
			case 1:
				System.out.println("Enter Country Code ");
				input = new Scanner(System.in);
				String result = restTemplate.getForObject(countryCodeURI + input.nextLine(), String.class);
				System.out.println(result);
				System.out.println("Do you want to continue? Type Yes / No");
				Scanner sc1 = new Scanner(System.in);

				if (sc1.next().equalsIgnoreCase("no")) {
					input.close();
					exit = true;
				}
				break;
			case 2:
				System.out.println("Enter Country Name ");
				input = new Scanner(System.in);
				String result1 = restTemplate.getForObject(countryNameURI + input.nextLine(), String.class);
				System.out.println(result1);
				System.out.println("Do you want to continue? Type Yes / No");
				Scanner sc11 = new Scanner(System.in);

				if (sc11.next().equalsIgnoreCase("no")) {
					input.close();
					exit = true;
				}
				break;
			}

		}
	}

}
