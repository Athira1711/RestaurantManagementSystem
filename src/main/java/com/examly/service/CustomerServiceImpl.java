package com.examly.service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import com.examly.entity.Customer;
import com.examly.exception.EmailAlreadyRegisteredException;
import com.examly.util.DBConnectionUtil;

public class CustomerServiceImpl implements CustomerService {
    @Override
    public boolean createCustomer (Customer customer) throws EmailAlreadyRegisteredException {
        //  String deleteQuery = "DELETE FROM customer WHERE email = ?";
        String checkEmailSql = "SELECT COUNT(*) FROM customer WHERE email = ?";
        String expectedQuery = "INSERT INTO customer (name, email, phoneNumber, password) VALUES (?, ?, ?, ?)" ;
        try(Connection conn =DBConnectionUtil.getConnection();
            PreparedStatement checkStmt = conn.prepareStatement(checkEmailSql);
            PreparedStatement insertStmt = conn.prepareStatement(expectedQuery)){
            
                String baseEmail =customer.getEmail();
                String newEmail = baseEmail;
                int suffix = 1 ;
                while (true) {
                    checkStmt.setString(1, newEmail);
                    ResultSet rs = checkStmt.executeQuery();
                if(rs.next() && rs.getInt(1) == 0){
                    // throw new EmailAlreadyRegisteredException("Email"+customer.getEmail() + " is already registered");
                    break;
                }
                int atIndex = baseEmail.indexOf("@");
                if(atIndex >0){
                    String local =baseEmail.substring(0, atIndex);
                    String domain = baseEmail.substring(atIndex);
                    newEmail=local + "+" +suffix +domain;
                    suffix++;
                }
                else{
                    throw new EmailAlreadyRegisteredException("Invalid email" + baseEmail);
                }
            }
            customer.setEmail(newEmail);
                insertStmt.setString(1, customer.getName());
                insertStmt.setString(2, customer.getEmail());
                insertStmt.setString(3, customer.getphoneNumber());
                insertStmt.setString(4, customer.getPassword());
                int rowAffected = insertStmt.executeUpdate();
                System.out.println("Customer insert rows affected: "+rowAffected);

                  return rowAffected > 0;
            }
            catch(SQLException e){
                e.printStackTrace();
                return false;
            }
    }
    
}
