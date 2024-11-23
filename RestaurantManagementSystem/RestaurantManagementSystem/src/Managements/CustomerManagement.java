package Managements;

import Models.Customer;
import Models.Reservation;
import Utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class CustomerManagement {

    public void displayCustomerManagementMenu() {
        Scanner scanner = new Scanner(System.in);

        boolean exit = false;
        while (!exit) {
            System.out.println("\nQuản lý khách hàng");
            System.out.println("1. Thêm khách hàng");
            System.out.println("2. Cập nhật khách hàng");
            System.out.println("3. Xóa khách hàng");
            System.out.println("4. Hiển thị danh sách khách hàng");
            System.out.println("5. Tìm kiếm khách hàng theo tên");
            System.out.println("0. Thoát");
            System.out.print("Chọn chức năng: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());

            } catch (NumberFormatException e) {
                choice = -1;
            }

            switch (choice) {
                case 1: // Thêm khách hàng
                    System.out.print("Nhập tên khách hàng: ");
                    String name = scanner.nextLine();
                    System.out.print("Nhập số điện thoại: ");
                    String phone = scanner.nextLine();
                    System.out.print("Nhập email: ");
                    String email = scanner.nextLine();
                    addCustomer(name, phone, email);
                    break;

                case 2: // Cập nhật khách hàng
                    System.out.println("Danh sách khách hàng hiện có");
                    displayCustomers();
                    System.out.print("Nhập ID khách hàng cần cập nhật: ");
                    int idToUpdate = scanner.nextInt();
                    scanner.nextLine(); // Clear buffer

                    Customer thisCustomer = getCustomerById(idToUpdate);
                    if (thisCustomer == null) {
                        System.out.println("Không tìm thấy khách hàng với ID: " + idToUpdate);
                    } else {
                        System.out.print("Nhập tên mới: ");
                        name = scanner.nextLine();
                        System.out.print("Nhập số điện thoại mới: ");
                        phone = scanner.nextLine();
                        System.out.print("Nhập email mới: ");
                        email = scanner.nextLine();
                        updateCustomer(idToUpdate, name, phone, email);
                    }
                    break;

                case 3: // Xóa khách hàng
                    System.out.println("Danh sách khách hàng hiện có");
                    displayCustomers();
                    System.out.print("Nhập ID khách hàng cần xóa: ");
                    int idToDelete = scanner.nextInt();
                    scanner.nextLine(); // Clear buffer

                    // Delete reservation
                    ReservationManagement reservationManagement = new ReservationManagement();
                    reservationManagement.deleteReservationsByCustomerId(idToDelete);

                    // Delete order
                    OrderManagement orderManagement = new OrderManagement();
                    orderManagement.deleteOrdersByCustomerId(idToDelete);

                    // Delete customer
                    deleteCustomer(idToDelete);
                    break;

                case 4: // Hiển thị danh sách khách hàng
                    displayCustomers();
                    break;

                case 5: // Tìm kiếm khách hàng theo tên
                    System.out.print("Nhập tên khách hàng cần tìm: ");
                    String searchName = scanner.nextLine();
                    searchCustomerByName(searchName);
                    break;
                case 0: // Thoát
                    exit = true;
                    System.out.println("Thoát khỏi chương trình quản lý khách hàng.");
                    break;

                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
            }
        }
    }

    // Thêm khách hàng mới
    public void addCustomer(String name, String phone, String email) {
        Connection connection = null;
        PreparedStatement stm = null;

        try {
            connection = DBConnection.makeConnection();
            if (connection != null) {
                String addCustomerSQL = "INSERT INTO Customer (CustomerID, Name, Phone, Email) VALUES (?, ?, ?, ?);";
                stm = connection.prepareStatement(addCustomerSQL);
                stm.setInt(1, getNextCustomerId());
                stm.setString(2, name);
                stm.setString(3, phone);
                stm.setString(4, email);

                int affectedRows = stm.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Thêm khách hàng thành công!");
                } else {
                    System.out.println("Thêm khách hàng thất bại!");
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


    // Cập nhật thông tin khách hàng
    public void updateCustomer(int id, String name, String phone, String email) {
        Connection connection = null;
        PreparedStatement stm = null;

        try {
            connection = DBConnection.makeConnection();
            if (connection != null) {
                String updateCustomerSQL = "UPDATE Customer SET Name = ?, Email = ?, Phone = ? WHERE CustomerID = ?;";
                stm = connection.prepareStatement(updateCustomerSQL);
                stm.setString(1, name);
                stm.setString(2, email);
                stm.setString(3, phone);
                stm.setInt(4, id);

                int affectedRows = stm.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Cập nhật khách hàng thành công!");
                } else {
                    System.out.println("Cập nhật khách hàng thất bại!");
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

    // Xóa khách hàng
    public void deleteCustomer(int id) {
        Connection connection = null;
        PreparedStatement stm = null;

        try {
            connection = DBConnection.makeConnection();
            if (connection != null) {
                String deleteCustomerSQL = "DELETE FROM Customer WHERE CustomerID = ?;";
                stm = connection.prepareStatement(deleteCustomerSQL);
                stm.setInt(1, id);

                int affectedRows = stm.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Xóa khách hàng thành công!");
                } else {
                    System.out.println("Xóa khách hàng thất bại!");
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

    // Hiển thị danh sách khách hàng
    public void displayCustomers() {
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            connection = DBConnection.makeConnection();
            if (connection != null) {
                String getAllCustomerSQL = "SELECT * FROM Customer;";
                stm = connection.prepareStatement(getAllCustomerSQL);
                rs = stm.executeQuery();

                if (rs.isBeforeFirst()) {
                    // Print table header
                    System.out.println("| ID   | Tên                  | SĐT                  | Email                |");
                    System.out.println("-----------------------------------------------------------------------------");

                    while (rs.next()) {
                        // Format output for each customer
                        System.out.printf("| %-4d | %-20s | %-20s | %-20s |\n",
                                rs.getInt("CustomerID"),
                                rs.getString("Name"),
                                rs.getString("Phone"),
                                rs.getString("Email"));
                    }
                } else {
                    System.out.println("Không có khách hàng nào.");
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
    }
    // Tìm kiếm khách hàng theo tên
    public void searchCustomerByName(String name) {
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            connection = DBConnection.makeConnection();
            if (connection != null) {
                String getCustomerByNameSQL = "SELECT * FROM Customer WHERE Name LIKE ?";
                stm = connection.prepareStatement(getCustomerByNameSQL);
                // Add wildcards inside the prepared statement parameter.
                stm.setString(1, "%" + name + "%");
                rs = stm.executeQuery();
                if (rs.isBeforeFirst()) {
                    // Print table header
                    System.out.println("| ID   | Tên                  | SĐT                  | Email                |");
                    System.out.println("-----------------------------------------------------------------------------");

                    while (rs.next()) {
                        // Format output for each customer
                        System.out.printf("| %-4d | %-20s | %-20s | %-20s |\n",
                                rs.getInt("CustomerID"),
                                rs.getString("Name"),
                                rs.getString("Phone"),
                                rs.getString("Email"));
                    }
                } else {
                    System.out.println("Không tìm thấy khách hàng nào với tên: " + name);
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
    }
    public Customer getCustomerById(int id) {
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            connection = DBConnection.makeConnection();
            if (connection != null) {
                String getCustomerByIdSQL = "SELECT * FROM Customer WHERE CustomerID = ?";
                stm = connection.prepareStatement(getCustomerByIdSQL);
                stm.setInt(1, id);
                rs = stm.executeQuery();
                if (rs.next()) {
                    return new Customer(rs.getInt("CustomerID"),
                            rs.getString("Name"),
                            rs.getString("Phone"),
                            rs.getString("Email"));
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

    public int getNextCustomerId() {
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            connection = DBConnection.makeConnection();
            if (connection != null) {
                String checkCustomerSQL = "SELECT TOP(1) CustomerID FROM Customer ORDER BY CustomerID DESC";
                stm = connection.prepareStatement(checkCustomerSQL);
                rs = stm.executeQuery();
                if (rs.next()) {
                    return rs.getInt("CustomerID") + 1;
                } else {
                    return 1;
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
        return 1;
    }

    // Kiểm tra danh sách khách hàng có trống hay không
    public boolean isCustomerListEmpty() {
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            connection = DBConnection.makeConnection();
            if (connection != null) {
                String sql = "SELECT COUNT(*) AS Total FROM Customer;";
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
