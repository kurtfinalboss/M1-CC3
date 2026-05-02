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
            
            int choice = service.numberAuthenticator(0,2);

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
                        
                        int status = service.numberAuthenticator(0,4);

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
