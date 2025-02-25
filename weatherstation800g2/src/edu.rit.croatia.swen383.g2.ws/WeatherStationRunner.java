package edu.rit.croatia.swen383.g2.ws;

import edu.rit.croatia.swen383.g2.ws.ui.*;
import java.util.Scanner;

public class WeatherStationRunner {

  public WeatherStationRunner() {
  }

  public static void main(String[] args) {
    WeatherStation weatherStation = new WeatherStation();
    Scanner scanner = new Scanner(System.in);

    System.out.println("\nWeather Station Display Selection");
    System.out.println("1. TextUI");
    System.out.println("2. SwingUI");
    System.out.println("3. JavaFXUI");
    System.out.println("4. Statistics Display");
    System.out.println("5. Forecast Display");
    System.out.println("6. All Displays");
    System.out.println("7. Exit");
    System.out.print("Enter your choice: ");

    try {
      int choice = scanner.nextInt();
      scanner.nextLine(); // Clear the buffer

      switch (choice) {
        case 1:
          weatherStation.attach(new TextUI(weatherStation));
          break;
        case 2:
          weatherStation.attach(new SwingUI(weatherStation));
          break;
        case 3:
          weatherStation.attach(new JavaFXUI(weatherStation));
          break;
        case 4:
          weatherStation.attach(new StatisticsDisplay(weatherStation));
          break;
        case 5:
          weatherStation.attach(new ForcastDisplay(weatherStation));
          break;
        case 6:
          weatherStation.attach(new TextUI(weatherStation));
          weatherStation.attach(new SwingUI(weatherStation));
          weatherStation.attach(new JavaFXUI(weatherStation));
          weatherStation.attach(new StatisticsDisplay(weatherStation));
          weatherStation.attach(new ForcastDisplay(weatherStation));
          break;
        case 7:
          System.out.println("Exiting...");
          scanner.close();
          System.exit(0);
          break;
        default:
          System.out.println("Invalid choice. Exiting...");
          scanner.close();
          System.exit(1);
      }

      scanner.close();
      System.out.println("\nStarting Weather Station...\n");
      weatherStation.run();

    } catch (Exception e) {
      System.out.println("Invalid input. Please enter a number 1-7.");
      scanner.close();
      System.exit(1);
    }
  }
}
