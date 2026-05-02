import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Services {
    private final Scanner SC;
    private final Repository REPO;
    
    private Services(ServicesBuilder builder){
        this.SC = builder.sc;
        this.REPO = builder.repo;
    }
    
    public Passenger registerPassenger() {
    String name = "", pass = "", contact = "", emailAddress = "";

    while (true) {
        System.out.println("\n==========================");
        System.out.println("#  FILL-UP REGISTRATION  #");
        System.out.println("==========================");

        System.out.print("Create Username        : ");
        name = SC.nextLine();

        if (!name.matches("[a-zA-Z\\s.]+")) {
            System.out.println("\n*INVALID INPUT!* Letters only.");
            continue;
        }

        System.out.print("Create Password        : ");
            if (pass.isEmpty()) {
                pass = SC.nextLine();
            } else {
                System.out.println(pass);
            }

        System.out.print("Contact Number         : ");
        contact = SC.nextLine();

        if (!contact.matches("\\d{11}")) {
            System.out.println("\n*INVALID INPUT!* Must be 11 digits.");
            continue;
        }

        System.out.print("Email Address          : ");
        emailAddress = SC.nextLine();

        if (!emailAddress.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            System.out.println("\n*INVALID INPUT!* Invalid email.");
            continue;
        }

        String error = REPO.passengerExists(name, pass, contact, emailAddress);
        if (error != null) {
            System.out.println("\n*INVALID INPUT!* " + error);
            continue;
        }

        System.out.println("\nWould you like to save this?");
        System.out.println("[1] Save ");
        System.out.println("[0] Cancel ");
        int choice = numberAuthenticator(0,1);
        
            if (choice == 1) {

                Passenger p = new Passenger.PassengerBuilder()
                        .setFullname(name)
                        .setPassword(pass)
                        .setContactNumber(contact)
                        .setEmailAddress(emailAddress)
                        .build();

                REPO.savePassenger(p); // ✅ FIXED
                return p;

            } else {
                System.out.println("\n*REGISTRATION CANCELLED!*");
                return null;
            }
         
    }
}
    
    public void setupPayment(Passenger p) {
        
        while (true) {
        
        System.out.println("\n=======================");
        System.out.println("#    PAYMENT SETUP    #");
        System.out.println("=======================");
        System.out.println("[1] GCash");
        System.out.println("[2] Card");
        System.out.println("[0] Back");
        
        int choice = numberAuthenticator(0,2);
        
        switch (choice) {
            case 1 -> setupGCash(p);
            case 2 -> setupCard(p);
            case 0 -> { return; }
            default -> System.out.println("\n*INVALID CHOICE!*");
        }
    }
}
    
    public Passenger loginPassenger() {
        System.out.println("\n=== LOGIN ===");

        System.out.print("Username: ");
        String name = SC.nextLine();

        System.out.print("Password: ");
        String password = SC.nextLine();

        Passenger p = REPO.login(name, password);

        if (p != null) {
            System.out.println("\n*LOGIN SUCCESSFUL!*");
        }else {
            System.out.println("\n*INVALID INPUT!* No existing account.");
        }

        return p;
    }
    
   public void cancelReservation(Passenger p) {
       List<String[]> reservations = REPO.getReservations(p.getFullname());
   

        if (reservations.isEmpty()) {
            System.out.println("\nNo reservations to cancel. Kindly reserve first.");
            return;
        }

        int count = 1;
        System.out.println("\n==========================================================================================");
        System.out.print("RESERVATION SUMMARY");
        System.out.printf("%-5s | %-10s | %-12s | %-25s | %-10s | %-8s%n",
                "\nNo. ", " Ticket ", " Seat Number ", " Origin -> Destination ", " Date", "Time ");
        System.out.println("------------------------------------------------------------------------------------------");
        for (String[] r : reservations) {
            System.out.printf("%-5s | %-10s | %-12s | %-25s | %-10s | %-8s%n",
                    count,
                    r[0],//ticket
                    r[8],//seat
                    r[4] + " -> " + r[5], //origin -> destination
                    r[7],//date
                    r[6]);//time
            count++;
        }
        System.out.println("==========================================================================================");
        
        int choice = numberAuthenticator(1, reservations.size());

        String[] selected = reservations.get(choice - 1);
        System.out.println("\n=======================================================");
        System.out.println("Reservation: " + count );
        System.out.println("Ticket: " + selected[0]);
        System.out.println("Seat: " + selected[8]);
        System.out.println("Stations: "+ selected[4] + " -> " + selected[5]);
        System.out.println("Date: "+ selected [7]);
        System.out.println("Time: "+ selected [6]);
        System.out.println("=======================================================");
        
        System.out.println("\nWould you like to confirm?");
        System.out.println("[1] Confirm ");
        System.out.println("[0] Cancel");

        int confirm = numberAuthenticator(0, 1);

        if (confirm == 1) {
            REPO.deleteReservation(selected[0]);
            System.out.println("\n*RESERVATION CANCELLED SUCCESSFULLY!*");
        } else {
            System.out.println("\nCancellation aborted.");
        }
    }
    
    public Fare checkStatus() {
        
        while (true) {
            System.out.println("\n==============================");
            System.out.println("#  PASSENGER CLASSIFICATION  #");
            System.out.println("==============================");
            System.out.println("[1] Regular");
            System.out.println("[2] Student (20%)");
            System.out.println("[3] Senior (30%)");
            System.out.println("[4] PWD (25%)");

            int choice = numberAuthenticator(1, 4);

            String category = "";
            double discount = 0.0;

            switch (choice) {
                case 1:
                    category = "Regular";
                    discount = 0.0;
                    break;

                case 2:
                    category = "Student";
                    discount = 0.20;
                    break;

                case 3:
                    category = "Senior";
                    discount = 0.30;
                    break;

                case 4:
                    category = "PWD";
                    discount = 0.25;
                    break;
            }

            Fare f = new Fare.FareBuilder()
                    .setPassengerCategory(category)
                    .setDiscountRate(discount)
                    .build();

            System.out.println("\nCLASSIFICATION VERIFIED! Discount applied.");
            System.out.println("\nWould you like to confirm?");
            System.out.println("[1] Confirm");
            System.out.println("[0] Cancel");

            int confirm = numberAuthenticator(0, 1);

            if (confirm == 1) {
                return f; 
            } else {
                System.out.println("\nRestarting classification...\n");
              
            }
        }
    }
    
    private final String[] stations = {
        "Monumento", "5th Avenue", "R. Papa", "Abad Santos", "Blumentritt"
    };
    
    private final Map<String, Double> stationFare = Map.of(
        "Monumento", 25.0,
        "5th Avenue", 25.0,
        "R. Papa", 25.0,
        "Abad Santos", 30.0,
        "Blumentritt", 30.0
    );
    
    public Route selectRoute() {
        while (true) {

        String origin = chooseStation("origin", null);
        String destination = chooseStation("destination", origin);

        System.out.println("\n===============================");
        System.out.println("         SELECTED TRIP         ");
        System.out.println("===============================");
        System.out.println("ORIGIN      : " + origin);
        System.out.println("DESTINATION : " + destination);
        System.out.println("FARE        : P" + stationFare.get(destination));
        
        System.out.println("\nWould you like to confirm?");
        System.out.println("[1] Confirm");
        System.out.println("[0] Cancel");
        
        int choice = numberAuthenticator(0,1);

                    if (choice == 1) {
                        String departureTime = checkBoardingSchedule(origin, destination);
                        String reservationDate = getReservationDate();
                        
                        return new Route.RouteBuilder()
                                .setOriginStation(origin)
                                .setDestinationStation(destination)
                                .setDepartureTime(departureTime)
                                .setReservationDate(reservationDate)
                                .build(); 
                    } else {
                        System.out.println("\n*INVALID INPUT!* Please select a valid option");
                    }
                
        System.out.println("\nSelection cancelled. Restarting...");
    }
}

    private String chooseStation(String type, String origin) {
        
        while (true) {
            System.out.println("\n=====================");
            System.out.println("#  ROUTE SELECTION  #");
            System.out.println("=====================");

            for (int i = 0; i < stations.length; i++) {

                String label = stations[i];
                
                if (type.equals("destination")) {
                    label += " (P" + stationFare.get(stations[i]) + ")";
                }

                System.out.println("[" + (i + 1) + "] " + label);
            }

            int choice = numberAuthenticator(1, stations.length);

            String selected = stations[choice - 1];

            if (origin != null && selected.equals(origin)) {
                System.out.println("\n*INVALID!* Destination cannot be the same as origin.");
                continue;
            }

            return selected;
        }
    }
    
    private String checkBoardingSchedule(String origin, String destination) {
        String[] times = {
            "6:30 AM", "1:30 PM", "7:30 PM"
        };
        
        System.out.println("\n=============================");
        System.out.println("      BOARDING SCHEDULE     ");
        System.out.println("=============================");
        System.out.println("ORIGIN      : " + origin);
        System.out.println("DESTINATION : " + destination);

        for (int i = 0; i < times.length; i++) {
            System.out.println("[" + (i + 1) + "] " + times[i]);
        }

        while (true) {
            System.out.print("Select time: ");

            int choice = numberAuthenticator(1,times.length);

                if (choice >= 1 && choice <= times.length) {
                    return times[choice - 1];
                }

            System.out.println("\n*INVALID INPUT! Please choose valid option.*");
        }
    }
    
    private String getSeatLabel(int seatNumber) {
       return (seatNumber % 2 == 1) ? "LS" + seatNumber : "RS" + seatNumber;
    }
    
    private int selectSeat() {
        
        System.out.println("\n===================");
        System.out.println("#  SEAT SELECTION #");
        System.out.println("===================");

        while (true) {

            for (int i = 1; i <= 20; i++) {

                String label = getSeatLabel(i);

                if (REPO.isSeatBooked(i)) {
                    System.out.print("[X] ");
                } else {
                    System.out.print("[" + label + "] ");
                }

                if (i % 2 == 0) {
                    System.out.println();
                }

                if (i % 4 == 0) {
                    System.out.println();
                }
            }

            System.out.println("(X = Booked)");

            int seat = numberAuthenticator(1, 20);

            if (REPO.isSeatBooked(seat)) {
                System.out.println("\n*SEAT ALREADY BOOKED!* Kindly choose another seat.\n");
                continue;
            }

            System.out.println("Selected Seat: " + getSeatLabel(seat));

            System.out.println("\nWould you like to confirm?");
            System.out.println("[1] Confirm");
            System.out.println("[0] Cancel");

            int choice = numberAuthenticator(0, 1);

            if (choice == 1) {
                return seat;
            } else {
                System.out.println("\nSelection cancelled.\n");
            }
        }
    }
    
    public void viewReservations(Passenger p){
        
        System.out.println("\n=======================");
        System.out.println("#  YOUR RESERVATIONS  #");
        System.out.println("=======================");

        var reservations = REPO.getReservations(p.getFullname());

        if (reservations.isEmpty()) {
            System.out.println("\nYou have no reservations yet. Kindly reserve first!");
            return;
        }

        int count = 1;

        for (String[] r : reservations) {
            int seatNumber = Integer.parseInt(r[8]);
            String seatLabel = getSeatLabel(seatNumber);
             
            System.out.println("\n===============================================");
            System.out.println("Reservation #" + count++ + "     Date: " + r[7]);
            System.out.println("-----------------------------------------------");
            System.out.println("Reference Code   : " + r[0]);
            System.out.println("Name             : " + r[1]);
            System.out.println("Category         : " + r[2]);
            System.out.println("Discount         : " + (Double.parseDouble(r[3]) * 100) + "%");
            System.out.println("Route            : " + r[4] + " -> " + r[5]);
            System.out.println("Departure Time   : " + r[6]);
            System.out.println("Seat Number      : " + seatLabel);
            System.out.println("Total Fare       : " + "P" + r[9]);
            System.out.println("===============================================");
            
        }
    }
    
   private String getReservationDate(){ 
           
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
       
       while (true) {
           System.out.print("\nEnter reservation date (MM-DD-YYYY): ");
           String input = SC.nextLine().trim();

           try {
               LocalDate date = LocalDate.parse(input, formatter);
               return date.format(formatter);
           }catch (DateTimeParseException e) {
               System.out.println("\n*INVALID DATE!* Please enter a real date (e.g., 05-12-2026).");
           }
           
           System.out.println("\nWould you like to confirm?");
           System.out.println("[1] Confirm");
           System.out.println("[0] Cancel");
        
       }
   }
   
    private double calculateFare(Route route, Fare fare) {
        
        Map<String, Double> stationFare = new HashMap<>();
        stationFare.put("Monumento", 25.0);
        stationFare.put("5th Avenue", 25.0);
        stationFare.put("R. Papa", 25.0);
        stationFare.put("Abad Santos", 30.0);
        stationFare.put("Blumentritt", 30.0);

        return stationFare.get(route.getDestinationStation());

        }
    
    public PaymentFramework selectPayment(double amount, double discount, Reservation r) {
        
        while (true) {
            System.out.println("\n=====================");
            System.out.println("#  ONLINE PAYMENT   #");
            System.out.println("=====================");
            System.out.println("[1] Gcash");
            System.out.println("[2] Card");
            System.out.println("[0] Back");

            int choice = numberAuthenticator(0, 2);

            switch (choice) {

                case 1:
                    return new GCashPayment(amount, discount, r, REPO);

                case 2:
                    return new CardPayment(amount, discount, r, REPO);

                case 0:
                    System.out.println("\nReturning to previous menu...\n");
                    return null; 
            }
        }
    }
    
    
  private void setupGCash(Passenger p) {
      
        String number, pin;
        double passengerBalance;
        double balanceLimit = 1000;

        if (REPO.hasGCash(p.getFullname())) {
            System.out.println("\n*GCASH ALREADY EXIST!*");
            return;
        }


        while (true) {
            System.out.print("Enter GCash Number (11 digits): ");
            number = SC.nextLine();

            if (!number.matches("\\d{11}")) {
                System.out.println("*INVALID NUMBER!*");
            } else {
                break;
            }
        }

        while (true) {
            System.out.print("Create 4-digit PIN: ");
            pin = SC.nextLine();

            if (!pin.matches("\\d{4}")) {
                System.out.println("*INVALID PIN!*");
            } else {
                break;
            }
        }

        while (true) {
            System.out.print("Enter balance: ");

            if (!SC.hasNextDouble()) {
                System.out.println("*INVALID BALANCE INPUT!*");
                SC.nextLine();
                continue;
            }

            passengerBalance = SC.nextDouble();
            SC.nextLine(); // consume newline

            if (passengerBalance > balanceLimit) {
                System.out.println("*BALANCE EXCEEDS LIMIT (P1000)!*");
                continue;
            }

            break; // valid balance
        }

        REPO.saveGCash(p.getFullname(), number, pin, passengerBalance);
        System.out.println("\n*GCASH SETUP SUCCESSFUL!*");
    }

    private void setupCard(Passenger p) {
        String pin;
        double passengerBalance;
        double balanceLimit = 1000;

        if (REPO.hasCard(p.getFullname())) {
            System.out.println("\n*CREDIT CARD ALREADY EXISTS!*");
            return;
        }

        
        while (true) {
            System.out.print("Create 4-digit Card PIN: ");
            pin = SC.nextLine();

            if (!pin.matches("\\d{4}")) {
                System.out.println("*INVALID PIN! Please try again.*");
            } else {
                break;
            }
        }

        while (true) {
            System.out.print("Enter card balance: ");

            if (!SC.hasNextDouble()) {
                System.out.println("*INVALID BALANCE INPUT!*");
                SC.nextLine();
                continue;
            }

            passengerBalance = SC.nextDouble();
            SC.nextLine();

            if (passengerBalance > balanceLimit) {
                System.out.println("*BALANCE EXCEEDS LIMIT (P1000)!*");
                continue;
            }

            break;
        }

        REPO.saveCard(p.getFullname(), pin, passengerBalance);

        System.out.println("\n*CARD SETUP COMPLETE!*");
    }
    
    public void reservePassenger(Passenger p) {
        
        Fare fare = checkStatus();
        Route route = selectRoute();
        int seat = selectSeat();

        double baseFare = calculateFare(route, fare);
        
        double discount = baseFare * fare.getDiscountRate();
        
        Reservation r = new Reservation.ReservationBuilder()
                .setPassenger(p)
                .setFare(fare)
                .setRoute(route)
                .setSeatNumber(seat)
                .setTotalFare(baseFare)
                .build();
        
        PaymentFramework payment;
        double finalTotal;
        
        while (true) {

            payment = selectPayment(baseFare, discount, r);

            finalTotal = payment.processInvoice();

            if (finalTotal != -1) {
                r = new Reservation.ReservationBuilder()
                        .setPassenger(p)
                        .setFare(fare)
                        .setRoute(route)
                        .setSeatNumber(seat)
                        .setTotalFare(finalTotal)
                        .setPaymentType(payment.getClass().getSimpleName())
                        .build();

                break;
            }
            
            System.out.println("\n[1] Try Again");
            System.out.println("[0] Cancel Reservation");
            System.out.print("Enter choice: ");

            String choice = SC.nextLine();

            if (choice.equals("0")) {
                System.out.println("\n*RESERVATION CANCELLED!*");
                return; 
            }
        }
        
          r = new Reservation.ReservationBuilder()
                .setPassenger(p)
                .setFare(fare)
                .setRoute(route)
                .setSeatNumber(seat)
                .setTotalFare(finalTotal)
                .setPaymentType(payment.getClass().getSimpleName())
                .build();

              r.reservationConfirmed();
              REPO.saveReservation(r);
        
    }
    
     public int numberAuthenticator(int min, int max) {
        while (true) {
            System.out.print("Enter choice (" + min + "-" + max + "): ");

            if (SC.hasNextInt()) {
                int choice = SC.nextInt();
                SC.nextLine(); // clear buffer

                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    System.out.println("\n*INVALID!* Choose between " + min + " and " + max + ".\n");
                }

            } else {
                System.out.println("\n*INVALID INPUT!* Numbers only.\n");
                SC.next(); // discard invalid input
            }
        }
    }
    
    public static class ServicesBuilder {
        private Scanner sc;
        private Repository repo;

        public ServicesBuilder setScanner(Scanner sc) {
            this.sc = sc;
            return this;
        }

        public ServicesBuilder setRepository(Repository repo) {
            this.repo = repo;
            return this;
        }

        public Services build() {
            if (sc == null || repo == null) {
                throw new IllegalStateException("Missing dependencies!");
            }
            return new Services(this);
        }
    }
}

