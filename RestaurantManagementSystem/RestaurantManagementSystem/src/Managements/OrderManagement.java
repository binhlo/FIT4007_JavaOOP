package Managements;

import Models.Customer;
import Models.Food;
import Models.Order;
import Utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrderManagement {
    public void displayOrderManagementMenu() {
        Scanner scanner = new Scanner(System.in);

        boolean exit = false;
        while (!exit) {
            System.out.println("\nQuản lý gọi món");
            System.out.println("1. Thêm đơn gọi món");
            System.out.println("2. Cập nhật đơn gọi món");
            System.out.println("3. Cập nhật trạng thái đơn gọi món");
            System.out.println("4. Xóa đơn gọi món");
            System.out.println("5. Hiển thị danh sách đơn gọi món");
            System.out.println("6. Tìm kiếm đơn gọi món theo ID");
            System.out.println("7. Tìm kiếm đơn gọi món theo ID khách hàng");
            System.out.println("0. Thoát");
            System.out.print("Chọn chức năng: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                choice = -1;
            }

            switch (choice) {
                case 1: // Thêm đơn
                    addOrder();
                    break;

                case 2: // Cập nhật đơn
                    updateOrder();
                    break;

                case 3: // Cập nhật trạng thái
                    updateOrderStatus();
                    break;

                case 4: // Xóa đơn
                    deleteOrder();
                    break;

                case 5: // Hiển thị danh sách đơn
                    displayOrders();
                    break;

                case 6: // Tìm kiếm đơn theo OrderID
                    searchOrderByOrderId();
                    break;

                case 7: // Tìm kiếm đơn theo CustomerID
                    searchOrderByCustomerId();
                    break;

                case 0: // Thoát
                    exit = true;
                    System.out.println("Thoát khỏi chương trình quản lý gọi món.");
                    break;

                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
            }
        }
    }

    private Order getOrderInformationFromKeyboard() {
        try {
            Order order = new Order();

            Scanner scanner = new Scanner(System.in);
            System.out.print("Nhập ngày đặt hàng (yyyy-MM-dd): ");
            String dateInput = scanner.nextLine();
            Date orderDate = Date.valueOf(dateInput);
            order.setOrderDate(orderDate);

            CustomerManagement customerManagement = new CustomerManagement();
            if (customerManagement.isCustomerListEmpty()) {
                System.out.println("Danh sách khách hàng đang trống. Vui lòng thêm một khách hàng mới trước.");
                return null;
            }
            System.out.println("Danh sách khách hàng hiện có:");
            customerManagement.displayCustomers();

            System.out.print("Nhập CustomerID: ");
            int customerID = Integer.parseInt(scanner.nextLine());
            Customer customer = customerManagement.getCustomerById(customerID);
            while (customer == null) {
                System.out.println("Khách hàng này không tồn tại.");
                System.out.print("Nhập CustomerID: ");
                customerID = Integer.parseInt(scanner.nextLine());
                customer = customerManagement.getCustomerById(customerID);
            }
            order.setCustomerID(customerID);

            FoodManagement foodManagement = new FoodManagement();
            if (foodManagement.isFoodListEmpty()) {
                System.out.println("Danh sách món ăn đang trống. Vui lòng thêm một món ăn mới trước.");
                return null;
            }
            System.out.println("Danh sách món ăn hiện có");
            foodManagement.displayFoods();

            System.out.print("Nhập FoodID: ");
            int foodID = Integer.parseInt(scanner.nextLine());
            Food food = foodManagement.getFoodById(foodID);
            while (food == null) {
                System.out.println("Món ăn này không tồn tại");
                System.out.print("Nhập FoodID: ");
                foodID = Integer.parseInt(scanner.nextLine());
                food = foodManagement.getFoodById(foodID);
            }
            order.setFoodID(foodID);

            System.out.print("Nhập số lượng: ");
            int quantity = Integer.parseInt(scanner.nextLine());
            while (quantity <= 0) {
                System.out.println("Số lượng phải lớn hơn 0.");
                System.out.print("Nhập số lượng: ");
                quantity = Integer.parseInt(scanner.nextLine());
            }
            order.setQuantity(quantity);

            System.out.print("Nhập mã bàn: ");
            String tableId = scanner.nextLine();
            while (tableId.isEmpty()) {
                System.out.println("Mã bàn không được để trống");
                System.out.print("Nhập mã bàn: ");
                tableId = scanner.nextLine();
            }
            order.setTableId(tableId);

            System.out.print("Nhập trạng thái: ");
            String status = scanner.nextLine();
            while (status.isEmpty()) {
                System.out.println("Trạng thái không được bỏ trống.");
                System.out.print("Nhập trạng thái: ");
                status = scanner.nextLine();
            }
            order.setStatus(status);

            return order;
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi: Thông tin nhập vào không hợp lệ.");
        }
        return null;
    }

    // Thêm đơn hàng
    public void addOrder() {
        try {
            Order order = getOrderInformationFromKeyboard();
            if (order == null) {
                return;
            }

            Connection connection = DBConnection.makeConnection();
            String insertSQL = "INSERT INTO \"Order\" (OrderID, OrderDate, CustomerID, FoodID, Quantity, TableID, Status) VALUES (?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement stm = connection.prepareStatement(insertSQL);
            stm.setInt(1, getNextOrderId());
            stm.setDate(2, order.getOrderDate());
            stm.setInt(3, order.getCustomerID());
            stm.setInt(4, order.getFoodID());
            stm.setInt(5, order.getQuantity());
            stm.setString(6, order.getTableId());
            stm.setString(7, order.getStatus());

            int affectedRows = stm.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Thêm đơn hàng thành công!");
            } else {
                System.out.println("Thêm đơn hàng thất bại!");
            }

            stm.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Lỗi SQL: " + e.getMessage());
        }
    }

    // Cập nhật đơn hàng
    public void updateOrder() {
        if (isOrderListEmpty()) {
            System.out.println("Danh sách đơn đặt món đang trống. Vui lòng thêm ít nhất 1 đơn để sử dụng tính năng.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Danh sách đơn hiện có");
        displayOrders();

        System.out.print("Nhập ID đơn gọi món cần cập nhật: ");
        int orderID = Integer.parseInt(scanner.nextLine());
        Order order = getOrderById(orderID);
        if (order == null) {
            System.out.println("Đơn gọi món không tồn tại");
            return;
        }

        order = getOrderInformationFromKeyboard();
        if (order == null) {
            return;
        }

        Connection connection = null;
        PreparedStatement stm = null;
        try {
            connection = DBConnection.makeConnection();
            if (connection != null) {
                String updateOrderSQL = "UPDATE \"Order\" SET OrderDate = ?, CustomerID = ?, FoodID = ?, Quantity = ?, TableID = ?, Status = ? WHERE OrderID = ?;";
                stm = connection.prepareStatement(updateOrderSQL);
                stm.setDate(1, order.getOrderDate());
                stm.setInt(2, order.getCustomerID());
                stm.setInt(3, order.getFoodID());
                stm.setInt(4, order.getQuantity());
                stm.setString(5, order.getTableId());
                stm.setString(6, order.getStatus());
                stm.setInt(7, orderID);

                int affectedRows = stm.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Cập nhật thông tin đơn hàng thành công!");
                } else {
                    System.out.println("Cập nhật thất bại!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stm != null) stm.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Cập nhật trạng thái của một order
    public void updateOrderStatus() {
        if (isOrderListEmpty()) {
            System.out.println("Danh sách đơn đặt món đang trống. Vui lòng thêm ít nhất 1 đơn để sử dụng tính năng.");
            return;
        }

        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Danh sách đơn hiện có");
            displayOrders();

            System.out.print("Nhập ID đơn gọi món cần cập nhật: ");
            int orderID = Integer.parseInt(scanner.nextLine());
            Order order = getOrderById(orderID);
            if (order == null) {
                System.out.println("Đơn gọi món không tồn tại");
                return;
            }

            System.out.print("Nhập trạng thái mới: ");
            String status = scanner.nextLine();
            while (status.isEmpty()) {
                System.out.println("Trạng thái không được để trống.");
                System.out.print("Nhập trạng thái mới: ");
                status = scanner.nextLine();
            }

            Connection connection = DBConnection.makeConnection();
            String updateSQL = "UPDATE \"Order\" SET Status = ? WHERE OrderID = ?;";
            PreparedStatement stm = connection.prepareStatement(updateSQL);
            stm.setString(1, status);
            stm.setInt(2, orderID);

            int affectedRows = stm.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Cập nhật đơn hàng thành công!");
            } else {
                System.out.println("Cập nhật thất bại!");
            }

            stm.close();
            connection.close();
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: Thông tin nhập vào không hợp lệ.");
        } catch (SQLException e) {
            System.out.println("Cập nhật thất bại!");
            System.out.println("Lỗi SQL: " + e.getMessage());
        }
    }

    // Xóa đơn hàng
    public void deleteOrder() {
        if (isOrderListEmpty()) {
            System.out.println("Danh sách đơn đặt món đang trống. Vui lòng thêm ít nhất 1 đơn để sử dụng tính năng.");
            return;
        }

        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Danh sách đơn hiện có");
            displayOrders();

            System.out.print("Nhập ID đơn cần xóa: ");
            int orderID = Integer.parseInt(scanner.nextLine());

            Connection connection = DBConnection.makeConnection();
            String deleteSQL = "DELETE FROM \"Order\" WHERE OrderID = ?;";
            PreparedStatement stm = connection.prepareStatement(deleteSQL);
            stm.setInt(1, orderID);

            int affectedRows = stm.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Xóa đơn thành công!");
            } else {
                System.out.println("Không tìm thấy đơn để xóa.");
            }

            stm.close();
            connection.close();
        } catch (NumberFormatException | SQLException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    // Xóa đơn hàng bằng foodId
    public void deleteOrdersByFoodID(int foodID) {
        try {
            Connection connection = DBConnection.makeConnection();
            String deleteSQL = "DELETE FROM \"Order\" WHERE FoodID = ?;";
            PreparedStatement stm = connection.prepareStatement(deleteSQL);
            stm.setInt(1, foodID);

            int affectedRows = stm.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Xóa các đơn chứa món ăn " + foodID + " thành công!");
            }
        } catch (SQLException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    // Xóa đơn hàng bằng customerId
    public void deleteOrdersByCustomerId(int customerId) {
        try {
            Connection connection = DBConnection.makeConnection();
            String deleteSQL = "DELETE FROM \"Order\" WHERE CustomerID = ?;";
            PreparedStatement stm = connection.prepareStatement(deleteSQL);
            stm.setInt(1, customerId);

            int affectedRows = stm.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Xóa các đơn đặt món của khách hàng " + customerId + " thành công!");
            }
        } catch (SQLException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    // Hiển thị danh sách đơn hàng
    public void displayOrders() {
        try {
            Connection connection = DBConnection.makeConnection();
            String selectSQL = "SELECT * FROM \"Order\";";
            PreparedStatement stm = connection.prepareStatement(selectSQL);
            ResultSet rs = stm.executeQuery();

            ArrayList<Order> orders = new ArrayList<>();
            while (rs.next()) {
                orders.add(new Order(rs.getInt("OrderID"),
                        rs.getDate("OrderDate"),
                        rs.getInt("CustomerID"),
                        rs.getInt("FoodID"),
                        rs.getInt("Quantity"),
                        rs.getString("TableID"),
                        rs.getString("Status")));
            }
            printOrderData(orders);

            rs.close();
            stm.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    // Tìm kiếm đơn theo OrderID
    private void searchOrderByOrderId() {
        if (isOrderListEmpty()) {
            System.out.println("Danh sách đơn đặt món đang trống. Vui lòng thêm ít nhất 1 đơn để sử dụng tính năng.");
            return;
        }
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Nhập ID đơn cần tìm: ");
            int orderId = Integer.parseInt(scanner.nextLine());
            Order order = getOrderById(orderId);
            if (order == null) {
                System.out.println("Không tìm thấy đơn.");
            } else {
                List<Order> orders = new ArrayList<>();
                orders.add(order);
                printOrderData(orders);
            }
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: Thông tin nhập vào không hợp lệ");
        }
    }

    // Tìm kiếm đơn theo CustomerID
    public void searchOrderByCustomerId() {
        if (isOrderListEmpty()) {
            System.out.println("Danh sách đơn đặt món đang trống. Vui lòng thêm ít nhất 1 đơn để sử dụng tính năng.");
            return;
        }
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Nhập ID khách hàng cần tìm: ");
            int customerID = Integer.parseInt(scanner.nextLine());

            Connection connection = DBConnection.makeConnection();
            String searchSQL = "SELECT * FROM \"Order\" WHERE CustomerID = ?;";
            PreparedStatement stm = connection.prepareStatement(searchSQL);
            stm.setInt(1, customerID);
            ResultSet rs = stm.executeQuery();

            ArrayList<Order> orders = new ArrayList<>();
            while (rs.next()) {
                orders.add(new Order(rs.getInt("OrderID"),
                        rs.getDate("OrderDate"),
                        rs.getInt("CustomerID"),
                        rs.getInt("FoodID"),
                        rs.getInt("Quantity"),
                        rs.getString("TableID"),
                        rs.getString("Status")));
            }
            printOrderData(orders);

            rs.close();
            stm.close();
            connection.close();
        } catch (NumberFormatException | SQLException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    public Order getOrderById(int orderID) {
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            connection = DBConnection.makeConnection();
            if (connection != null) {
                String getOrderSQL = "SELECT * FROM \"Order\" WHERE OrderID = ?";
                stm = connection.prepareStatement(getOrderSQL);
                stm.setInt(1, orderID);

                rs = stm.executeQuery();
                if (rs.next()) {
                    return new Order(
                            rs.getInt("OrderID"),
                            rs.getDate("OrderDate"),
                            rs.getInt("CustomerID"),
                            rs.getInt("FoodID"),
                            rs.getInt("Quantity"),
                            rs.getString("TableID"),
                            rs.getString("Status")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stm != null) stm.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void printOrderData(List<Order> orders) {
        System.out.println("|  ID  |    Ngày    | Mã KH | Mã món | Số lượng | Mã bàn   |   Trạng thái   |");
        System.out.println("-----------------------------------------------------------------------------");

        if (orders.size() > 0) {
            for (Order order : orders) {
                System.out.printf("| %4d | %10s | %5d | %6d | %9d | %-7s | %-14s |\n",
                        order.getOrderID(),
                        order.getOrderDate(),
                        order.getCustomerID(),
                        order.getFoodID(),
                        order.getQuantity(),
                        order.getTableId(),
                        order.getStatus());
            }
        } else {
            System.out.println("Danh sách đơn đặt món ăn trống.");
        }
    }


    // Lấy ID tiếp theo cho đơn hàng
    public int getNextOrderId() {
        try {
            Connection connection = DBConnection.makeConnection();
            String maxIDSQL = "SELECT MAX(OrderID) AS MaxID FROM \"Order\";";
            PreparedStatement stm = connection.prepareStatement(maxIDSQL);
            ResultSet rs = stm.executeQuery();

            int nextId = 1;
            if (rs.next()) {
                nextId = rs.getInt("MaxID") + 1;
            }

            rs.close();
            stm.close();
            connection.close();

            return nextId;
        } catch (SQLException e) {
            System.out.println("Lỗi: " + e.getMessage());
            return 1;
        }
    }

    // Kiểm tra danh sách đặt món có trống hay không
    public boolean isOrderListEmpty() {
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            connection = DBConnection.makeConnection();
            if (connection != null) {
                String sql = "SELECT COUNT(*) AS Total FROM [Order];";
                stm = connection.prepareStatement(sql);
                rs = stm.executeQuery();

                if (rs.next()) {
                    return rs.getInt("Total") == 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stm != null) stm.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
