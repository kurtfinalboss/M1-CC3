package m1.m11;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Repository repo = new Repository.RepositoryBuilder()
                .setDatabasePath()
                .build();

        Services service = new Services.ServicesBuilder()
                .setScanner(sc)
                .setRepository(repo)
                .build();

        while (true) {
            System.out.println("\n========================================");
            System.out.println("#       TRAIN RESERVATION SYSTEM       #");
            System.out.println("========================================");
            System.out.println("[1] Register");
            System.out.println("[2] Login");
            System.out.println("[0] Exit");
            System.out.print("Enter choice: ");

            if (!sc.hasNextInt()) {
                System.out.println("\n*INVALID INPUT!* Please enter a number only.");
                sc.next(); // clear invalid input
                continue;
            }

            int choice = sc.nextInt();
            sc.nextLine();

            if (choice < 0 || choice > 2) {
                System.out.println("\n*INVALID INPUT!* Please select a valid option.");
                continue;
            }

            switch (choice) {
                case 1 -> service.registerPassenger();
                
                case 2 -> {
                Passenger p = service.loginPassenger();

                if (p != null) {
                    dashboard:
                    while (true) {
                        System.out.println("\n===============");
                        System.out.println("#  DASHBOARD  #");
                        System.out.println("===============");
                        System.out.println("[1] View Reservation Summary");
                        System.out.println("[2] Reserve");
                        System.out.println("[3] Cancel Reservation");
                        System.out.println("[4] Setup Payment");
                        System.out.println("[0] Logout");
                        System.out.print("Enter choice: ");

                        if (!sc.hasNextInt()) {
                            System.out.println("\n*INVALID INPUT!* Please enter a number only.");
                            sc.next();
                            continue;
                        }

                        int status = sc.nextInt();
                        sc.nextLine();

                        if (status < 0 || status > 4) {
                            System.out.println("\n*INVALID INPUT!* Please select a valid option.");
                            continue;
                        }

                        switch (status) {
                            case 1 -> service.viewReservations(p);
                            case 2 -> service.reservePassenger(p);
                            case 3 -> service.cancelReservation(p);
                            case 4 -> service.setupPayment(p);
                            case 0 -> {
                                System.out.println("\n*SUCCESS!* Logging out...");
                                break dashboard;
                            }
                        }
                    }
                }
            }
                case 0 -> {
                    System.out.println("\nTHANK YOU FOR USING OUR SYSTEM!");
                    return;
                }
            }
        }
    }
}
