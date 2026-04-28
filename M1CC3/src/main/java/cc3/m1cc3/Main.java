package cc3.m1cc3;
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
        Fare f = new Fare.FareBuilder().build();

        while (true) {
            System.out.println("\n========================================");
            System.out.println("#       TRAIN RESERVATION SYSTEM       #");
            System.out.println("========================================");
            System.out.println("[1] Register");
            System.out.println("[2] Login");
            System.out.println("[0] Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

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
                            System.out.println("[0] Logout");
                            System.out.print("Enter choice: ");

                            int status = sc.nextInt();
                            sc.nextLine();

                            switch (status) {
                                case 1 -> service.viewReservations(p);
                                case 2 -> service.reservePassenger(p); // ✅ PASS USER
                                case 0 -> {
                                    System.out.println("\n*SUCCESS!* Logging out...");
                                    break dashboard;
                                }
                                default -> System.out.println("*INVALID INPUT!*");
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
