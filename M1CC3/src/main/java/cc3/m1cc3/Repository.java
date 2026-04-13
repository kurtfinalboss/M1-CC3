package cc3.m1cc3;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;


public class Repository {
    private final String DBURL;

    private Repository(){
        this.DBURL = null;
    }
  
    private Repository(String dbURL){
        this.DBURL = dbURL;
    }
    
    public static String passengerExists(String name, String contact, String emailAddress){
        String url = "jdbc:sqlite:D:\\TrainHubStation.db";

        boolean nameExists = false;
        boolean contactExists = false;
        boolean emailExists = false;

        try(Connection conn = DriverManager.getConnection(url)){
            String sql = "SELECT fullname, contactNumber, emailAddress FROM tbl_train";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                if(rs.getString("fullname").equalsIgnoreCase(name)){
                    nameExists = true;
                }
                if(rs.getString("contactNumber").equals(contact)){
                    contactExists = true;
                }
                if(rs.getString("emailAddress").equalsIgnoreCase(emailAddress)){
                    emailExists = true;
                }
            }

        } catch(Exception e){
            return "Database error: " + e.getMessage();
        }

        // 
        if(nameExists && contactExists && emailExists){
            return "Name, Contact number, and Email address already exist!";
        } else if(nameExists && contactExists){
            return "Name and Contact number already exist!";
        } else if(nameExists && emailExists){
            return "Name and Email address already exist!";
        } else if(contactExists && emailExists){
            return "Contact number and Email address already exist!";
        } else if(nameExists){
            return "Name already exists!";
        } else if(contactExists){
            return "Contact already exists!";
        } else if(emailExists){
            return "Email already exists!";
        }

        return null;
    }
    
    public void savePassenger(Reservation reservation) {

        String sql = "INSERT INTO tbl_train(fullname, contactNumber, emailAddress, PassengerCategory, discountRate, originStation, destinationStation, departureTime, reservationCode) VALUES (?,?, ?, ?, ?, ?, ?,?,?)";

        try (Connection conn = DriverManager.getConnection(DBURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, reservation.getPassenger().getFullname());
            pstmt.setString(2, reservation.getPassenger().getContactNumber());
            pstmt.setString(3, reservation.getPassenger().getEmailAddress());
            pstmt.setString(4, reservation.getPassenger().getPassengerCategory());
            pstmt.setDouble(5, reservation.getPassenger().getDiscountRate());
            pstmt.setString(6, reservation.getRoute().getOriginStation());
            pstmt.setString(7, reservation.getRoute().getDestinationStation());
            pstmt.setString(8, reservation.getRoute().getDepartureTime());
            pstmt.setString(9, reservation.getReservationCode());
            
            
            pstmt.executeUpdate();
            System.out.println("\nPassenger saved successfully!");
            System.out.println("|****************************************************************|");
            System.out.printf("%50s%n", "Thank you for using our service!");
            System.out.println("|****************************************************************|");

        } catch (SQLException e) {
            System.err.println("Failed to save passenger: " + e.getMessage());
        }
    }
    public static class RepositoryBuilder{
        private String path;
        
        public RepositoryBuilder setDatabasePath(){
            this.path = "jdbc:sqlite:D:\\TrainHubStation.db";
            return this;
        }
        public Repository build() {
            if (path == null) throw new IllegalStateException("Database path not set!");
            return new Repository(path);
        }
    }
}
