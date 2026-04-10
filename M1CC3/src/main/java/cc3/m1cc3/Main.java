package cc3.m1cc3;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Reservation reserve = new Reservation.ReservationBuilder().build();
        Repository repo = new Repository.RepositoryBuilder().setDatabasePath().build();
        
        int choice = 0;
        
        while(choice != 1){
            Passenger passenger = Passenger.fillUpRegistration();
            passenger = Passenger.checkStatus(passenger);
            Route route = Route.selectRoute();
            
            reserve = new Reservation.ReservationBuilder().setPassenger(passenger).setRoute(route).build();
            
            System.out.println("\nPreparing your reservation summary...");
            
            while (true) {
                reserve.confirmReservation();
                System.out.println("\nYour reservation details are provided above.");
                System.out.println("\nEnter [1] to confirm reservation:");
                System.out.println("[1] Confirm \n[0] Cancel");
                System.out.print("Enter Choice: ");
                
                if (sc.hasNextInt()) {
                    int status = sc.nextInt();
                    sc.nextLine();
                    
                    if (status == 1) {
                        reserve.reservationConfirmed();
                        repo.savePassenger(reserve);
                        return;
                    } else if (status == 0) {
                        boolean isCancelled = reserve.cancelReservation();
                        if (isCancelled) {
                            break; //restart whole process
                        } else {
                            continue; //back to summary
                        }
                    } else {
                        System.out.println("\n*INVALID INPUT!* Please enter a valid option.");
                    }
                } else {
                    System.out.println("\n*INVALID INPUT!* Please enter a number only.");
                    sc.next();
                }
            } 
        }
       sc.close();
    }
}