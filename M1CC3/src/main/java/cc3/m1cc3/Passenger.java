package cc3.m1cc3;

import java.util.Scanner;

public class Passenger {
    private final String PASSENGER_CATEGORY, FULLNAME, CONTACT_NUMBER, EMAIL_ADDRESS;
    private final double DISCOUNT_RATE;
    
    private Passenger(PassengerBuilder builder){
        this.PASSENGER_CATEGORY = builder.passengerCategory;
        this.DISCOUNT_RATE = builder.discountRate;
        this.FULLNAME = builder.fullname;
        this.CONTACT_NUMBER = builder.contactNumber;
        this.EMAIL_ADDRESS = builder.emailAddress;
    }

    public String getPassengerCategory(){return PASSENGER_CATEGORY;}
    public double getDiscountRate(){return DISCOUNT_RATE;}
    public String getFullname(){return FULLNAME;} 
    public String getContactNumber(){return CONTACT_NUMBER;}
    public String getEmailAddress(){return EMAIL_ADDRESS;}

    public static Passenger fillUpRegistration() {
        Scanner sc = new Scanner(System.in);
        String name = "", contact = "", emailAddress = "";

        while (true) {
            System.out.println("\n==========================");
            System.out.println("#  FILL-UP REGISTRATION  #");
            System.out.println("==========================");
            System.out.println("Let's get you registered! Please enter your full name and contact details to secure your reservation.");

            System.out.print("Full name        : ");
            if (name.isEmpty()) {
                name = sc.nextLine();
            } else {
                System.out.println(name);
            }

            if (!name.matches("[a-zA-Z\\s.]+")) {
                System.out.println("\n*INVALID INPUT!* Enter a letter only. Other characters are prohibited.");
                name = "";
                continue;
            }

            System.out.print("Contact Number   : ");
            if (contact.isEmpty()) {
                contact = sc.nextLine();
            } else {
                System.out.println(contact);
            }

            if (!contact.matches("\\d{11}")) {
                System.out.println("\n*INVALID INPUT!* Contact must be a number and exactly 11 digits.");
                contact = "";
                continue;
            }

            System.out.print("Email Address    : ");
            if (emailAddress.isEmpty()) {
                emailAddress = sc.nextLine();
            } else {
                System.out.println(emailAddress);
            }

            if (!emailAddress.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                System.out.println("\n*INVALID INPUT!* Wrong email format.");
                emailAddress = "";
                continue;
            }

            String error = Repository.passengerExists(name, contact, emailAddress);
            if (error != null) {
                System.out.println("\n*INVALID INPUT!* " + error);
                System.out.println("Please enter a different details.");

                // Reset only the fields that are wrong (optional but better UX)
                if (error.contains("Name")) name = "";
                if (error.contains("Contact")) contact = "";
                if (error.contains("Email")) emailAddress = "";

                continue;
            }
            
            System.out.println("\n*REGISTRATION SUCCESSFUL!*");

            int choice = -1;
            while (true) {
                System.out.print("\nEnter [1] to continue to passenger selection.\n");
                System.out.print("[1] Continue\n[0] Cancel\nEnter choice: ");

                if (sc.hasNextInt()) {
                    choice = sc.nextInt();
                    sc.nextLine();

                    if (choice == 1 || choice == 0) {
                        break;
                    } else {
                        System.out.println("\n*INVALID INPUT!* Please enter a valid option.");
                    }
                } else {
                    System.out.println("\n*INVALID INPUT!* Please enter a number only.");
                    sc.next();
                }
            }

            if (choice == 1) {
                return new PassengerBuilder()
                        .setFullname(name)
                        .setContactNumber(contact)
                        .setEmailAddress(emailAddress) 
                        .build();
            }

            System.out.println("\n*REGISTRATION CANCELLED!* Restarting registration...*");
            name = "";
            contact = "";
            emailAddress = "";
        }
    }

    public static Passenger checkStatus(Passenger passenger) {

        Scanner sc = new Scanner(System.in);

        while (true) {

            int choice;

            while (true) {
                System.out.println("\n==============================");
                System.out.println("#  PASSENGER CLASSIFICATION  #");
                System.out.println("==============================");
                System.out.println("Welcome! Please select your passenger category to see if you qualify for a fare discount.");
                System.out.println("[1] Regular");
                System.out.println("[2] Student (20%)");
                System.out.println("[3] Senior (30%)");
                System.out.println("[4] PWD (25%)");
                System.out.print("Enter choice: ");

                if (sc.hasNextInt()) {
                    choice = sc.nextInt();
                    sc.nextLine();

                    String category;
                    double discount;

                    switch (choice) {
                        case 1 -> { category = "Regular"; discount = 0.0; }
                        case 2 -> { category = "Student"; discount = 0.20; }
                        case 3 -> { category = "Senior"; discount = 0.30; }
                        case 4 -> { category = "PWD"; discount = 0.25; }
                        default -> {
                            System.out.println("\n*INVALID INPUT!* Please select a valid option.");
                            continue;
                        }
                    }

                    passenger = new PassengerBuilder()
                            .setPassengerCategory(category)
                            .setDiscountRate(discount)
                            .setFullname(passenger.getFullname())
                            .setContactNumber(passenger.getContactNumber())
                            .setEmailAddress(passenger.getEmailAddress())
                            .build();

                    break;
                } else {
                    System.out.println("\n*INVALID INPUT!* Please enter a number.");
                    sc.next();
                }
            }

            System.out.println("\nCLASSIFICATION VERIFIED! The fare discount has been applied to your ticket.\n");

            int choice2;
            while (true) {
                System.out.print("Enter [1] to proceed to route selection.\n[1] Confirm\n[0] Cancel\nEnter choice: ");

                if (sc.hasNextInt()) {
                    choice2 = sc.nextInt();
                    sc.nextLine();

                    if (choice2 == 1) {
                        return passenger;
                    } else if (choice2 == 0) {
                        System.out.println("\nCLASSIFICATION CANCELLED! Restarting classification...");
                        break;
                    } else {
                        System.out.println("\n*INVALID INPUT!* Please enter a valid option.\n");
                    }
                } else {
                    System.out.println("\n*INVALID INPUT!* Please enter a number only.\n");
                    sc.next();
                }
            }
        }
    }
 
    public static class PassengerBuilder{

        private String passengerCategory, fullname, contactNumber, emailAddress;
        private double discountRate;

        public PassengerBuilder setPassengerCategory(String passengerCategory){
            this.passengerCategory = passengerCategory;
            return this;
        }

        public PassengerBuilder setDiscountRate(double discountRate){
            this.discountRate = discountRate;
            return this;
        }
        
        public PassengerBuilder setFullname(String fullname){
            this.fullname = fullname;
            return this;
        }
        
        public PassengerBuilder setContactNumber(String contactNumber){
            this.contactNumber = contactNumber;
            return this;
        }
        
        public PassengerBuilder setEmailAddress(String emailAddress){
            this.emailAddress = emailAddress;
            return this;
        }

        public Passenger build(){
            return new Passenger(this);
        }
    }
}

