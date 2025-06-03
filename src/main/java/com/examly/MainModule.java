package com.examly;

import com.examly.entity.*;
import com.examly.service.*;
import com.examly.exception.*;
import java.util.*;
import java.util.logging.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class MainModule {
    private static final CustomerService customerService = new CustomerServiceImpl();
    private static final RestaurantService restaurantService = new RestaurantServiceImpl();
    private static final MenuService menuService = new MenuServiceImpl();
    private static final OrderService orderService = new OrderServiceImpl();
    private static final PaymentService paymentService = new PaymentServiceImpl();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            displayMenu();
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    registerCustomer();
                    break;
                case 2:
                    createRestaurant();
                    break;
                case 3:
                    createMenuItem();
                    break;
                case 4:
                    viewRestaurants();
                    break;
                case 5:
                    placeOrder();
                    break;
                case 6:
                    viewOrders();
                    break;
                case 7:
                    makePayment();
                    break;
                case 8:
                    System.out.println("Exiting...");
                    System.exit(0);

                default:
                    System.out.println("Choose an option: ");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n=== Welcome to the Online Food Delivery System ===");
        System.out.println("1. Register Customer");
        System.out.println("2. Create Restaurant");
        System.out.println("3. Create Menu Item");
        System.out.println("4. View Restaurants");
        System.out.println("5. Place Order");
        System.out.println("6. View Orders");
        System.out.println("7. Make Payment");
        System.out.println("8. Exit");
    }

    private static void registerCustomer() {
        System.out.print("Enter Customer Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Customer Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Customer Phone Number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Enter Customer Password: ");
        String password = scanner.nextLine();

        int customerId = (int) (Math.random() * 1000) + 1;
        Customer customer = new Customer(customerId, name, email, phoneNumber, password);
        try {
            boolean success = customerService.createCustomer(customer);
            System.out.println("Customer registered successfully!");
        }
        catch (EmailAlreadyRegisteredException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    private static void createRestaurant() {
        System.out.print("Enter Restaurant Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Restaurant Address: ");
        String address = scanner.nextLine();
        System.out.print("Enter Cuisine Type: ");
        String cuisineType = scanner.nextLine();
        System.out.print("Enter Contact Number: ");
        String contactNumber = scanner.nextLine();

        int restaurantId = (int) (Math.random() * 1000 + 1);
        Restaurant restaurant = new Restaurant(restaurantId, name, address, cuisineType, contactNumber);
        boolean success = restaurantService.createRestaurant(restaurant);
        System.out.println("Restaurant Created Successfully!");
    }

    private static void createMenuItem() {
        System.out.print("Enter Restaurant ID to Add Menu Item: ");
        int restaurantId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Menu Item Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Menu Item Price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Menu Item Description: ");
        String description = scanner.nextLine();
        System.out.print("Enter Available Quantity: ");
        int availableQuantity = scanner.nextInt();

        int itemId = (int) (Math.random() * 1000) + 1;
        MenuItem menuItem = new MenuItem(itemId, restaurantId, name, price, description, availableQuantity);
        try {
            boolean success = menuService.createMenuItem(menuItem);
            System.out.println("Menu Item Created Successfully!");
        } 
        catch (RestaurantNotFoundException e) {
            System.out.println("Error: Restaurant ID " + restaurantId + " Not Found.");

        }
    }

    private static void viewRestaurants() {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        if (restaurants.isEmpty()) {
            System.out.println("No Restaurants Available..");
        } 
        else {
            System.out.println("\nList of Restaurants ===");
            for (Restaurant r : restaurants) {
                System.out.println("Restaurant ID: " + r.getRestaurantId());
                System.out.println("Name: " + r.getName());
                System.out.println("Address: " + r.getAddress());
                System.out.println("Cuisine Type: " + r.getCuisineType());
                System.out.println("Contact Number: " + r.getContactNumber());
                System.out.println();

            }
        }
    }

    private static void placeOrder() {
        System.out.print("Enter Customer ID: ");
        int customerId = scanner.nextInt();
        System.out.print("Enter Restaurant ID To Place Order: ");
        int restaurantId = scanner.nextInt();
        scanner.nextLine();

        List<MenuItem> menuItems = menuService.getMenuItemsByRestaurant(restaurantId);
        if (menuItems.isEmpty()) {
            System.out.println("No Menu Items Available For This Restaurant. ");
            return;
        }
        System.out.println("\nMenu Items");
        for (MenuItem item : menuItems) {
            System.out.println("Item ID: " + item.getItemId());
            System.out.println("Name: " + item.getName());
            System.out.println("Price: " + item.getPrice());
            System.out.println("Description: " + item.getDescription());
            System.out.println("Available Quantity: " + item.getAvailableQuantity());
            System.out.println();
        }
        System.out.print("Enter Menu Item ID To Order: ");
        int itemId = scanner.nextInt();
        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Delivery Address: ");
        String deliveryAddress = scanner.nextLine();

        MenuItem selectedItem = menuItems.stream().filter(item -> item.getItemId() == itemId).findFirst().orElse(null);
        if (selectedItem == null || selectedItem.getAvailableQuantity() < quantity) {
            System.out.println("Invalid item ID or insufficient quantity.");
            return;
        }
        int orderId = (int) (Math.random() * 1000) + 1;
        double totalPrice = selectedItem.getPrice() * quantity;
        Order order = new Order(orderId, customerId, restaurantId, "Pending", totalPrice, deliveryAddress);
        List<OrderItem> orderedItems = new ArrayList<>();
        orderedItems.add(new OrderItem(orderId, itemId, quantity));

        boolean success = orderService.createOrder(order, orderedItems);
        System.out.println("Order Placed Successfully");

    }

    private static void viewOrders() {
        System.out.print("Enter Customer ID To View Orders: ");
        int customerId = scanner.nextInt();
        List<Order> orders = orderService.getOrdersByCustomer(customerId);
        if (orders.isEmpty()) {
            System.out.println("No Orders Found For This Customer");
        }
        else {
            System.out.println("\n===List of Orders ===");
            for (Order o : orders) {
                System.out.println("Order ID: " + o.getOrderId());
                System.out.println("Restaurant ID: " + o.getRestaurantId());
                System.out.println("Total Price: " + o.getTotalPrice());
                System.out.println("Order Status: " + o.getOrderStatus());
                System.out.println("Delivery Address: " + o.getDeliveryAddress());
                System.out.println();
            }
        }
    }

    private static void makePayment() {
        System.out.print("Enter Order ID To Make Payment: ");
        int orderId = scanner.nextInt();
        System.out.print("Enter Amount To Pay: ");
        double amountPaid = scanner.nextDouble();

        List<Order> allOrders = orderService.getOrdersByCustomer(0);
        Order order = allOrders.stream().filter(o -> o.getOrderId() == orderId).findFirst().orElse(null);

        if (order == null) {
            System.out.println("Order Not Found.");
            return;
        }
        if (order.getTotalPrice() != amountPaid) {
            System.out.println("Invalid Amount. Expected: " + order.getTotalPrice());
            return;
        }

        int paymentId = (int) (Math.random() * 1000) + 1;
        Payment payment = new Payment(paymentId, orderId, new Date(), "Completed", amountPaid);
        boolean success = paymentService.processPayment(payment);
        if (success) {
            System.out.println("Payment Successful ! Order Is Now Confirmed.");
            order.setOrderStatus("Confirmed");
        } 
        else {
            System.out.println("Payment Failed.");
        }
    }
}


