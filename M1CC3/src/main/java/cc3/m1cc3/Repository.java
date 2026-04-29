import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class Repository {
    private final String DBURL;

    private Repository(){
        this.DBURL = null;
    }
    private Repository(String dbURL){
        this.DBURL = dbURL;
    }
    
   
   public void savePassenger(Passenger p) {
        String sql = "INSERT INTO tbl_passenger(fullname, password, contactNumber, emailAddress) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DBURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, p.getFullname());
            pstmt.setString(2, p.getPassword());
            pstmt.setString(3, p.getContactNumber());
            pstmt.setString(4, p.getEmailAddress());

            pstmt.executeUpdate();

            System.out.println("\nPassenger registered successfully!");

        } catch (SQLException e) {
            System.err.println("Failed to save passenger: " + e.getMessage());
        }
   }
   
   public void saveReservation(Reservation r) {
       String sql = "INSERT INTO tbl_reservation(reservationCode, fullname, passengerCategory, discountRate, originStation, destinationStation, departureTime, reservationDate, seatNumber, totalFare) VALUES (?,?,?,?,?,?,?,?,?,?)";
       try (Connection conn = DriverManager.getConnection(DBURL);
               PreparedStatement ps = conn.prepareStatement(sql)) {
           
           ps.setString(1, r.getReservationCode());
           ps.setString(2, r.getPassenger().getFullname());
           ps.setString(3, r.getFare().getPassengerCategory());
           ps.setDouble(4, r.getFare().getDiscountRate());
           ps.setString(5, r.getRoute().getOriginStation());
           ps.setString(6, r.getRoute().getDestinationStation());
           ps.setString(7, r.getRoute().getDepartureTime());
           ps.setString(8, r.getRoute().getReservationDate());
           ps.setInt(9, r.getSeatNumber());
           ps.setDouble(10, r.getTotalFare());
           
           ps.executeUpdate();
       } catch (Exception e) {
           System.out.println(e.getMessage());
       }
   }
   
  public void deleteReservation(String reservationCode) {
    String sql = "DELETE FROM tbl_reservation WHERE reservationCode = ?";

    try (Connection conn = DriverManager.getConnection(DBURL);
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, reservationCode);

        int rows = pstmt.executeUpdate();

        if (rows > 0) {
            System.out.println("Reservation cancelled successfully.");
        } else {
            System.out.println("Reservation not found.");
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
}
   
   public List<String[]> getReservations(String name) {
       
       List<String[]> list = new ArrayList<>();
       
       String sql = "SELECT * FROM tbl_reservation WHERE fullname=?";
       
       try (Connection conn = DriverManager.getConnection(DBURL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] data = {
                    rs.getString("reservationCode"),//0
                    rs.getString("fullname"),//1
                    rs.getString("passengerCategory"),//2
                    String.valueOf(rs.getDouble("discountRate")),//3
                    rs.getString("originStation"),//4
                    rs.getString("destinationStation"),//5
                    rs.getString("departureTime"),//6
                    rs.getString("reservationDate"),//7
                    String.valueOf(rs.getInt("seatNumber")),//8
                    String.valueOf(rs.getDouble("totalFare")),//9 
            };
                list.add(data);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return list;
    }
    
    public Passenger login(String name, String password) {
        String sql = "SELECT * FROM tbl_passenger WHERE fullname=? AND password=?";

        try (Connection conn = DriverManager.getConnection(DBURL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Passenger.PassengerBuilder()
                        .setFullname(rs.getString("fullname"))
                        .setPassword(rs.getString("password"))
                        .setContactNumber(rs.getString("contactNumber"))
                        .setEmailAddress(rs.getString("emailAddress"))
                        .build();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
   
    public static String passengerExists(String name, String password , String contact, String emailAddress){
        String url = "jdbc:sqlite:D:\\TrainHubStation.db";

        boolean nameExists = false;
        boolean passExists = false;
        boolean contactExists = false;
        boolean emailExists = false;
        
        try(Connection conn = DriverManager.getConnection(url)){
            String sql = "SELECT fullname, password, contactNumber, emailAddress FROM tbl_passenger";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                if (name.equalsIgnoreCase(rs.getString("fullname"))) {
                    nameExists = true;
                }
                
                if (name.equalsIgnoreCase(rs.getString("password"))) {
                    passExists = true;
                }
                
                if (name.equalsIgnoreCase(rs.getString("contactNumber"))) {
                    contactExists = true;
                }
                
                if (name.equalsIgnoreCase(rs.getString("emailAddress"))) {
                    emailExists = true;
                }
            }
        } catch(Exception e){
            return "Database error: " + e.getMessage();
        }
        
        if(nameExists && passExists && contactExists && emailExists)return "Name, Password, Contact number, and Email address already exist!";
        if(nameExists && passExists && contactExists) return "Name, Password, and Contact number already exist!";
        if(nameExists && passExists && emailExists) return "Name, Password, and Email address already exist!";
        if(nameExists && contactExists && emailExists)return "Name, Contact number, and Email address already exist!";
        if(passExists && contactExists && emailExists) return "Password, Contact number, and Email address already exist!";
        if(nameExists && passExists) return "Name and Password already exist!";
        if(nameExists && contactExists) return "Name and Contact number already exist!";
        if(nameExists && emailExists) return "Name and Email address already exist!";
        if(passExists && contactExists) return "Password and Contact number already exist!";
        if(passExists && emailExists) return "Password and Email address already exist!";
        if(contactExists && emailExists) return "Contact number and Email address already exist!";
        if(nameExists) return "Name already exists!";
        if(passExists) return "Password already exists!";
        if(contactExists) return "Contact already exists!";
        if(emailExists) return "Email already exists!";

        return null;
    }
    
    public boolean isSeatBooked(int seat) {
        String sql = "SELECT * FROM tbl_reservation WHERE seatNumber=?";
        
        try (Connection conn = DriverManager.getConnection(DBURL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, seat);
            ResultSet rs = ps.executeQuery();

            return rs.next(); // true if exists

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
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
