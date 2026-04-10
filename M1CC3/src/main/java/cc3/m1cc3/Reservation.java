package cc3.m1cc3;
import java.util.Random;
import java.util.Scanner;

public class Reservation {
    private final Scanner sc = new Scanner(System.in);
    private final String RESERVATION_CODE;
    private final Passenger passenger;
    private final Route route;
    
    private Reservation(ReservationBuilder builder ){
        this.RESERVATION_CODE = generateReservationCode();
        this.passenger = builder.passenger;
        this.route = builder.route;
    }
    
    public void reservationConfirmed(){
        System.out.println("\n|****************************************************************|");
        System.out.printf("%43s%n", "CONFIRMED RESERVATION");
        System.out.println("|****************************************************************|");
        System.out.println("Reservation confirmed! \nYour reference number: " + RESERVATION_CODE);
    }   
    
    public boolean cancelReservation() {
        while (true) {
            System.out.println("\nDo you want to cancel your reservation?");
            System.out.println("[1] Confirm");
            System.out.println("[0] Cancel");
            System.out.print("Enter Choice: ");

            if (sc.hasNextInt()) {
                int choice = sc.nextInt();
                sc.nextLine();

                if (choice == 1) {
                    System.out.println("\n*SUCCESS!* You have cancelled your reservation.");
                    return true;
                } 
                else if (choice == 0) {
                    System.out.println("\nYour reservation is still active.");
                    return false; 
                } 
                else {
                    System.out.println("\n*INVALID INPUT!* Please enter a valid option.");
                }

            } else {
                System.out.println("\n*INVALID INPUT!* Please enter a number only.");
                sc.next();
            }
        }
}
    
    private String generateReservationCode(){
        Random rand = new Random();
        int num = rand.nextInt(1000);
        return String.format("TK-%03d", num);
    }
     
     public void confirmReservation(){
         
        Reservation reserve = new Reservation.ReservationBuilder().setPassenger(passenger).setRoute(route).build();
        System.out.println("\n===============================================");
        System.out.println("#             RESERVATION SUMMARY             #");
        System.out.println("===============================================");
        System.out.printf("%-18s : %s%n", "Full Name", reserve.getPassenger().getFullname());
        System.out.printf("%-18s : %s%n", "Contact Number", reserve.getPassenger().getContactNumber());
        System.out.printf("%-18s : %s%n", "Category", reserve.getPassenger().getPassengerCategory());
        System.out.printf("%-18s : %s%n", "Discount", reserve.getPassenger().getDiscountRate()* 100 + "%");
        System.out.printf("%-18s : %s%n", "Origin", reserve.getRoute().getOriginStation());
        System.out.printf("%-18s : %s%n", "Destination", reserve.getRoute().getDestinationStation());
        System.out.printf("%-18s : %s%n", "Departure Time", reserve.getRoute().getDepartureTime());
        System.out.println("===============================================");
     }
     
     public String getReservationCode(){ return RESERVATION_CODE;}
     public Passenger getPassenger(){ return passenger;}
     public Route getRoute(){ return route;}
    
    public static class ReservationBuilder{
        private String reservationCode;
        private Passenger passenger;
        private Route route;
        
        public ReservationBuilder setPassenger(Passenger passenger){
            this.passenger = passenger;
            return this;
        }
        
        public ReservationBuilder setRoute(Route route){
            this.route = route;
            return this;
        }
        
        public Reservation build(){
            return new Reservation(this);
        }
    }    
}