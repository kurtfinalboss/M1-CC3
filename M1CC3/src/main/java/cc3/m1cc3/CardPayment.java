public class CardPayment extends PaymentFramework {

    private double updatedCredit;
    private final Repository REPO;

    public CardPayment(double amount, double discount, Reservation reservation, Repository repo) {
        super(amount, discount, reservation);
        this.REPO = repo;
    }

    @Override
    public boolean validatePayment(double finalAmount) {

        String fullname = reservation.getPassenger().getFullname();
        String storedPin = REPO.getCardPin(fullname);

        if (storedPin == null) {
            System.out.println("No Card found.");
            return false;
        }

        int attempts = 3;

        while (attempts > 0) {

            System.out.print("Enter Card PIN: ");
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

            double credit = REPO.getCardCredit(fullname);

            System.out.println("\nAvailable Credit: " + credit);

            if (credit < finalAmount) {
                System.out.println("Insufficient credit.");
                return false;
            }

            updatedCredit = credit - finalAmount;

            REPO.updateCardCredit(fullname, updatedCredit);

            System.out.println("Card Payment Successful!");
            return true;
        }

        return false;
    }

    @Override
    public void displayRemaining() {
        System.out.println("\nRemaining Credit: " + updatedCredit);
    }

    @Override
    public String getPaymentType() {
        return "Card";
    }
}
