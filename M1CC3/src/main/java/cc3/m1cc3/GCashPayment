public class GCashPayment extends PaymentFramework {

    private double updatedBalance;
    private final Repository REPO;

    public GCashPayment(double amount, double discount, Reservation reservation, Repository repo) {
        super(amount, discount, reservation);
        this.REPO = repo;
    }

    @Override
    public boolean validatePayment(double finalAmount) {

        String fullname = reservation.getPassenger().getFullname();
        String storedPin = REPO.getGCashPin(fullname);

        if (storedPin == null) {
            System.out.println("No GCash account found.");
            return false;
        }

        int attempts = 3;

        while (attempts > 0) {

            System.out.print("Enter GCash PIN: ");
            String input = new java.util.Scanner(System.in).nextLine();

            if (!input.matches("\\d{4}")) {
                System.out.println("*INVALID INPUT!*");
                continue;
            }

            if (!input.equals(storedPin)) {
                attempts--;
                System.out.println("Incorrect PIN. Attempts left: " + attempts);
                continue;
            }

            double currentBalance = REPO.getGCashBalance(fullname);

            System.out.println("\nCurrent Balance: " + currentBalance);

            if (currentBalance < finalAmount) {
                System.out.println("Insufficient balance.");
                return false;
            }

            updatedBalance = currentBalance - finalAmount;

            REPO.updateGCashBalance(fullname, updatedBalance);

            System.out.println("GCash Payment Successful!");
            return true;
        }

        return false;
    }

    @Override
    public void displayRemaining() {
        System.out.println("\nRemaining Balance: " + updatedBalance);
    }

    @Override
    public String getPaymentType() {
        return "GCash";
    }
}
