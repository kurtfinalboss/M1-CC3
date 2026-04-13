package cc3.m1cc3;

import java.util.Scanner;

public class Route {

    private final String ORIGIN_STATION;
    private final String DESTINATION_STATION;
    private final String DEPARTURE_TIME;
    
    private Route(){
        this.DEPARTURE_TIME = null;
        this.DESTINATION_STATION = null;
        this.ORIGIN_STATION = null;
    }

    private Route(RouteBuilder builder) {
        this.ORIGIN_STATION = builder.originStation;
        this.DESTINATION_STATION = builder.destinationStation;
        this.DEPARTURE_TIME = builder.departureTime;
    }

    public String getOriginStation() { return ORIGIN_STATION; }
    public String getDestinationStation() { return DESTINATION_STATION; }
    public String getDepartureTime() { return DEPARTURE_TIME; }
    
    public static Route selectRoute() {
        Scanner scanner = new Scanner(System.in);
        String[] stations = { "Monumento", "5th Avenue", "R. Papa", "Abad Santos", "Blumentritt" };

        while (true) {
            String origin = chooseStation(scanner, stations, "origin", null);
            String destination = chooseStation(scanner, stations, "destination", origin);

            int choice = -1;
            
            System.out.println("\nFetching your selected trip...");
            
            while (choice != 0 && choice != 1) {
                System.out.println("\n===============================");
                System.out.println("         SELECTED TRIP         ");
                System.out.println("===============================");
                System.out.println("ORIGIN: " + origin);
                System.out.println("DESTINATION: " + destination);
                System.out.println("===============================");

                System.out.println("\nTrip successfully selected!");
                System.out.println("\nEnter [1] to proceed to boarding schedule:");
                System.out.println("[1] Confirm");
                System.out.println("[0] Cancel");
                System.out.print("Enter choice: ");

                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine();

                    if (choice != 0 && choice != 1) {
                        System.out.println("\n*INVALID INPUT!* Please select a valid option.");
                    }
                } else {
                    System.out.println("\n*INVALID INPUT!* Please enter a number.");
                    scanner.next();
                }
            }

            if (choice == 1) {
                System.out.println("\nChecking train availability for the selected route and departure time...");
                String departureTime = checkBoardingSchedule(scanner, origin, destination);

                return new RouteBuilder()
                        .setOriginStation(origin)
                        .setDestinationStation(destination)
                        .setDepartureTime(departureTime)
                        .build();
            }

            System.out.println("\nSelection cancelled! Restarting route selection...");
        }
    }

    private static String chooseStation(Scanner scanner, String[] stations, String type, String exclude) {
        while (true) {
            System.out.println("\n=====================");
            System.out.println("#  ROUTE SELECTION  #");
            System.out.println("=====================");

            if (type.equals("origin")) {
                System.out.println("Please select your origin station below.");
            } else {
                System.out.println("Please select your destination station below.");
                System.out.println("ORIGIN: " + exclude);
            }

            for (int i = 0; i < stations.length; i++) {
                System.out.println("[" + (i + 1) + "] " + stations[i]);
            }

            if (type.equals("origin")) {
                System.out.print("Select Origin Station: ");
            } else {
                System.out.print("Select Destination Station: ");
            }

            int choice;

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("\n*INVALID INPUT!* Please enter a number.");
                scanner.next();
                continue;
            }

            if (choice < 1 || choice > stations.length) {
                System.out.println("\n*INVALID INPUT!* Please select a valid option.");
                continue;
            }

            String selectedStation = stations[choice - 1];

            if (exclude != null && selectedStation.equals(exclude)) {
                System.out.println("\n*INVALID SELECTION!* Destination cannot be the same as origin.");
                continue;
            }

            return selectedStation;
        }
    }

    private static String checkBoardingSchedule(Scanner scanner, String origin, String destination) {
        String schedule = "";

        if (origin.equals("Monumento")) {
            if (destination.equals("5th Avenue")) {
                schedule = "6:30 AM, 1:30 PM, 7:30 PM";
            } else if (destination.equals("R. Papa")) {
                schedule = "8:30 AM, 3:00 PM, 8:00 PM";
            } else if (destination.equals("Abad Santos")) {
                schedule = "12:00 PM, 4:30 PM, 7:00 PM";
            } else if (destination.equals("Blumentritt")) {
                schedule = "3:00 PM, 5:00 PM, 9:00 PM";
            }
        } else if (origin.equals("5th Avenue")) {
            if (destination.equals("Monumento")) {
                schedule = "7:30 AM, 12:00 PM, 3:00 PM";
            } else if (destination.equals("R. Papa")) {
                schedule = "10:00 AM, 2:00 PM, 5:30 PM";
            } else if (destination.equals("Abad Santos")) {
                schedule = "1:30 PM, 6:30 PM, 8:30 PM";
            } else if (destination.equals("Blumentritt")) {
                schedule = "4:00 PM, 7:00 PM, 9:30 PM";
            }
        } else if (origin.equals("R. Papa")) {
            if (destination.equals("Monumento")) {
                schedule = "6:00 AM, 11:30 AM, 3:30 PM";
            } else if (destination.equals("5th Avenue")) {
                schedule = "11:00 AM, 2:30 PM, 5:00 PM";
            } else if (destination.equals("Abad Santos")) {
                schedule = "1:00 PM, 4:00 PM, 8:30 PM";
            } else if (destination.equals("Blumentritt")) {
                schedule = "3:30 PM, 6:30 PM, 10:00 PM";
            }
        } else if (origin.equals("Abad Santos")) {
            if (destination.equals("Monumento")) {
                schedule = "7:00 AM, 11:00 AM, 2:30 PM";
            } else if (destination.equals("5th Avenue")) {
                schedule = "10:30 AM, 12:30 PM, 3:00 PM";
            } else if (destination.equals("R. Papa")) {
                schedule = "2:30 PM, 5:30 PM, 8:30 PM";
            } else if (destination.equals("Blumentritt")) {
                schedule = "3:30 PM, 6:00 PM, 9:00 PM";
            }
        } else if (origin.equals("Blumentritt")) {
            if (destination.equals("Monumento")) {
                schedule = "9:30 AM, 12:30 PM, 3:30 PM";
            } else if (destination.equals("5th Avenue")) {
                schedule = "12:30 PM, 2:00 PM, 5:30 PM";
            } else if (destination.equals("R. Papa")) {
                schedule = "2:00 PM, 5:00 PM, 7:30 PM";
            } else if (destination.equals("Abad Santos")) {
                schedule = "4:30 PM, 6:30 PM, 8:00 PM";
            }
        }

        String[] times = schedule.split(",");
        int choice = 0;

        while (choice < 1 || choice > times.length) {
            System.out.println("\n=============================");
            System.out.println("      BOARDING SCHEDULE     ");
            System.out.println("=============================");
            System.out.println("ORIGIN: " + origin);
            System.out.println("DESTINATION: " + destination);
            System.out.println("=============================");

            System.out.println("Select Available Departure Times: ");
            for (int i = 0; i < times.length; i++) {
                System.out.println("[" + (i + 1) + "] " + times[i].trim());
            }

            System.out.print("Select time: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();

                if (choice < 1 || choice > times.length) {
                    System.out.println("\n*INVALID INPUT!* Please select a valid option.");
                }
            } else {
                System.out.println("\n*INVALID INPUT!* Please enter a number only.");
                scanner.next();
            }
        }

        scanner.nextLine();
        return times[choice - 1].trim();
    }

    public static class RouteBuilder {

        private String originStation;
        private String destinationStation;
        private String departureTime;

        public RouteBuilder setOriginStation(String originStation) {
            this.originStation = originStation;
            return this;
        }

        public RouteBuilder setDestinationStation(String destinationStation) {
            this.destinationStation = destinationStation;
            return this;
        }

        public RouteBuilder setDepartureTime(String departureTime) {
            this.departureTime = departureTime;
            return this;
        }

        public Route build() {
            return new Route(this);
        }
    }
}
