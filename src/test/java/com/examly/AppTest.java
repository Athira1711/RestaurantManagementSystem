package com.examly;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppTest {



	private static Connection connection;
    private static Object customerDAO; // Using Object to store CustomerDAO instance

    @BeforeAll
    public static void setUp() throws Exception {
        // Initialize the database connection and customerDAO instance
        Class<?> jdbcUtilsClass = Class.forName("com.examly.util.DBConnectionUtil");
        connection = (Connection) jdbcUtilsClass.getMethod("getConnection").invoke(null);
        
        Class<?> daoClass = Class.forName("com.examly.service.CustomerServiceImpl");
        customerDAO = daoClass.getDeclaredConstructor().newInstance();
    }

    // @AfterAll
    // public static void tearDown() throws SQLException {
    //     clearDatabase();
    //     System.out.println("Database cleared after all tests.");
    // }

	// private static void clearDatabase() throws SQLException {
    //     try (Statement stmt = connection.createStatement()) {
    //         stmt.executeUpdate("DELETE FROM customer");
    //     }
    // }


    // Method to get row count of customers from the database
    private int getCustomerRowCount() throws SQLException {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT COUNT(*) FROM customer");
                ResultSet rs = pstmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

   


    @Test
	@Order(1)
	public void testEntityFolder() {
		String directoryPath = "src/main/java/com/examly/entity";
		File directory = new File(directoryPath);
		assertTrue(directory.exists() && directory.isDirectory());
	}

    @Test
	@Order(2)
	public void testExceptionFolder() {
		String directoryPath = "src/main/java/com/examly/exception";
		File directory = new File(directoryPath);
		assertTrue(directory.exists() && directory.isDirectory());
	}


    
    @Test
	@Order(3)
	public void testServiceFolder() {
		String directoryPath = "src/main/java/com/examly/service";
		File directory = new File(directoryPath);
		assertTrue(directory.exists() && directory.isDirectory());
	}


    @Test
	@Order(4)
    void testCustomerInsertQueryExists() throws IOException {
        // Read the service implementation file as a string
        String filePath = "src/main/java/com/examly/service/CustomerServiceImpl.java";  // Update the path if needed
        String fileContent = new String(Files.readAllBytes(Paths.get(filePath)));

        // Define the expected query
        String expectedQuery = "INSERT INTO customer (name, email, phoneNumber, password) VALUES (?, ?, ?, ?)";

        // Assert that the query exists in the file
        assertTrue(fileContent.contains(expectedQuery), "Expected SQL insert query is missing in CustomerServiceImpl");
    }


    @Test
	@Order(5)
    void testRestaurantInsertQueryExists() throws IOException {
        // Read the service implementation file as a string
        String filePath = "src/main/java/com/examly/service/RestaurantServiceImpl.java";  // Update the path if needed
        String fileContent = new String(Files.readAllBytes(Paths.get(filePath)));

        // Define the expected query
        String expectedQuery = "INSERT INTO restaurant (name, address, cuisineType, contactNumber) VALUES (?, ?, ?, ?)";

        // Assert that the query exists in the file
        assertTrue(fileContent.contains(expectedQuery), "Expected SQL insert query is missing in RestaurantServiceImpl");
    }



    @Test
	@Order(6)
    void testMenuInsertQueryExists() throws IOException {
        // Read the service implementation file as a string
        String filePath = "src/main/java/com/examly/service/MenuServiceImpl.java";  // Update the path if needed
        String fileContent = new String(Files.readAllBytes(Paths.get(filePath)));

        // Define the expected query
        String expectedQuery = "INSERT INTO menuItem (restaurantId, name, price, description, availableQuantity) VALUES (?, ?, ?, ?, ?)";

        // Assert that the query exists in the file
        assertTrue(fileContent.contains(expectedQuery), "Expected SQL insert query is missing in RestaurantServiceImpl");
    }


    @Test
	@Order(7)
    void testOrderInsertQueryExists() throws IOException {
        // Read the service implementation file as a string
        String filePath = "src/main/java/com/examly/service/OrderServiceImpl.java";  // Update the path if needed
        String fileContent = new String(Files.readAllBytes(Paths.get(filePath)));

        // Define the expected query
        String expectedQuery = "INSERT INTO orderItem (orderId, itemId, quantity) VALUES (?, ?, ?)";

        // Assert that the query exists in the file
        assertTrue(fileContent.contains(expectedQuery), "Expected SQL insert query is missing in OrderServiceImpl");
    }



    
    @Test
	@Order(8)
    public void testCustomerFileExists() {
		String filePath = "src/main/java/com/examly/entity/Customer.java";
		File file = new File(filePath);
		assertTrue(file.exists() && file.isFile());
	}

    @Test
	@Order(9)
    public void testMenuFileExists() {
		String filePath = "src/main/java/com/examly/entity/MenuItem.java";
		File file = new File(filePath);
		assertTrue(file.exists() && file.isFile());
	}


    @Test
	@Order(10)
    public void testOrderFileExists() {
		String filePath = "src/main/java/com/examly/entity/Order.java";
		File file = new File(filePath);
		assertTrue(file.exists() && file.isFile());
	}


    
    @Test
	@Order(11)
    public void testOrderItemFileExists() {
		String filePath = "src/main/java/com/examly/entity/OrderItem.java";
		File file = new File(filePath);
		assertTrue(file.exists() && file.isFile());
	}

    @Test
	@Order(12)
    public void testPaymentFileExists() {
		String filePath = "src/main/java/com/examly/entity/Payment.java";
		File file = new File(filePath);
		assertTrue(file.exists() && file.isFile());
	}

    @Test
	@Order(13)
    public void testRestaurantFileExists() {
		String filePath = "src/main/java/com/examly/entity/Restaurant.java";
		File file = new File(filePath);
		assertTrue(file.exists() && file.isFile());
	}

    @Test
	@Order(14)
    public void testEmaliAlreadyRegisteredExceptionFile() {
		String filePath = "src/main/java/com/examly/exception/EmailAlreadyRegisteredException.java";
		File file = new File(filePath);
		assertTrue(file.exists() && file.isFile());
	}

    @Test
	@Order(15)
    public void testRestaurantNotFoundExceptionFile() {
		String filePath = "src/main/java/com/examly/exception/RestaurantNotFoundException.java";
		File file = new File(filePath);
		assertTrue(file.exists() && file.isFile());
	}
    @Test
	@Order(16)
	public void testCustomerServiceFile() {
		String filePath = "src/main/java/com/examly/service/CustomerService.java";
		File file = new File(filePath);
		assertTrue(file.exists() && file.isFile());
	}


    @Test
	@Order(17)
	public void testCustomerServiceImplFile() {
		String filePath = "src/main/java/com/examly/service/CustomerServiceImpl.java";
		File file = new File(filePath);
		assertTrue(file.exists() && file.isFile());
	}



	@Test
	@Order(18)
	public void testRestaurantServiceFile() {
		String filePath = "src/main/java/com/examly/service/RestaurantService.java";
		File file = new File(filePath);
		assertTrue(file.exists() && file.isFile());
	}

    @Test
	@Order(19)
    public void testRestaurantServiceImplFile() {
		String filePath = "src/main/java/com/examly/service/RestaurantServiceImpl.java";
		File file = new File(filePath);
		assertTrue(file.exists() && file.isFile());
	}

    @Test
	@Order(20)
    public void testMenuServiceFile() {
		String filePath = "src/main/java/com/examly/service/MenuService.java";
		File file = new File(filePath);
		assertTrue(file.exists() && file.isFile());
	}

    @Test
	@Order(21) 
	public void testMenuServiceImplFile() {
		String filePath = "src/main/java/com/examly/service/MenuServiceImpl.java";
		File file = new File(filePath);
		assertTrue(file.exists() && file.isFile());
	}

    @Test
	@Order(22)
    public void testOrderServiceFile() {
		String filePath = "src/main/java/com/examly/service/OrderService.java";
		File file = new File(filePath);
		assertTrue(file.exists() && file.isFile());
	}


    @Test
	@Order(23)
    public void testOrderServiceImplFile() {
		String filePath = "src/main/java/com/examly/service/OrderServiceImpl.java";
		File file = new File(filePath);
		assertTrue(file.exists() && file.isFile());
	}


    @Test
	@Order(24)
    public void testPaymentServiceFile() {
		String filePath = "src/main/java/com/examly/service/PaymentService.java";
		File file = new File(filePath);
		assertTrue(file.exists() && file.isFile());
	}
    
    @Test
	@Order(25)
    public void testPaymentServiceImplFile() {
		String filePath = "src/main/java/com/examly/service/PaymentServiceImpl.java";
		File file = new File(filePath);
		assertTrue(file.exists() && file.isFile());
	}


	@Test
	@Order(26)
    public void testCustomerRegistration() throws Exception {
        int rowCountBefore = getCustomerRowCount();
        System.out.println("Row count before: " + rowCountBefore);

        // Use reflection to create a new Customer object
        Class<?> customerClass = Class.forName("com.examly.entity.Customer");
        Constructor<?> constructor = customerClass.getConstructor(int.class, String.class, String.class, String.class,String.class);
        
        // Create a customer instance
        Object customer = constructor.newInstance(1, "John Doe", "john.doe@example.com", "12345","67585");

        // Invoke createCustomer() method from CustomerDAO
        Method createCustomerMethod = customerDAO.getClass().getMethod("createCustomer", customerClass);
        createCustomerMethod.invoke(customerDAO, customer);

        int rowCountAfter = getCustomerRowCount();
        System.out.println("Row count after: " + rowCountAfter);
        
        // Assert that a new customer was added
        assertEquals(rowCountBefore + 1, rowCountAfter, "One customer should be added.");
    }


}
