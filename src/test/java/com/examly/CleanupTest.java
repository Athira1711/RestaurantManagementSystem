package com.examly;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.sql.*;

public class CleanupTest {
    @BeforeAll
    public static void cleanTestCustomer(){
        try{
            String url = "jdbc:mysql://localhost:3306/appdb";
            String username = "root";
            String password = "examly";

            Connection conn = DriverManager.getConnection(url, username, password);
            String deleteQuery = "Delete from customer where email = ?";
            PreparedStatement stmt = conn.prepareStatement(deleteQuery);
            stmt.setString(1, "john.doe@example.com");
            int rowsDeleted = stmt.executeUpdate();

            System.out.println("CleanupTest: Deleted: " + rowsDeleted + " existing John Doe records");
            stmt.close();
            conn.close();
        }catch(Exception e){
            System.out.println("CleanupTest: Error cleaning up test customer - " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void dummyTest(){
        System.out.println("CleanupTest: Cleanup executed before AppTest.");
    }
}
