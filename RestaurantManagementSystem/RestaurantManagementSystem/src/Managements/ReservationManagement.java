package Managements;

import Models.Customer;
import Models.Reservation;
import Utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.Scanner;

public class ReservationManagement {
    public void displayReservationManagementMenu() {
        Scanner scanner = new Scanner(System.in);

        boolean exit = false;
        while (!exit) {
            System.out.println("\nQuản lý đặt chỗ");
            System.out.println("1. Thêm đặt chỗ");
            System.out.println("2. Cập nhật đặt chỗ");
            System.out.println("3. Hủy đặt chỗ");
            System.out.println("4. Hiển thị danh sách đặt chỗ");
            System.out.println("5. Tìm kiếm đặt chỗ theo CustomerID");
            System.out.println("0. Thoát");
            System.out.print("Chọn chức năng: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                choice = -1;
            }

            switch (choice) {
                case 1: // Thêm đặt chỗ
                    addReservation(scanner);
                    break;

                case 2: // Cập nhật đặt chỗ
                    updateReservation(scanner);
                    break;

                case 3: // Hủy đặt chỗ
                    cancelReservation(scanner);
                    break;

                case 4: // Hiển thị danh sách đặt chỗ
                    displayReservations();
                    break;

                case 5: // Tìm kiếm đặt chỗ theo CustomerID
                    searchReservationByCustomerId(scanner);
                    break;

                case 0: // Thoát
                    exit = true;
                    System.out.println("Thoát khỏi chương trình quản lý đơn đặt chỗ.");
                    break;

                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
            }
        }
    }

    // Thêm đặt chỗ
    public void addReservation(Scanner scanner) {
        try {
            CustomerManagement customerManagement = new CustomerManagement();
            if (customerManagement.isCustomerListEmpty()) {
                System.out.println("Danh sách khách hàng đang trống. Vui lòng thêm một khách hàng mới trước.");
                return;
            }
            System.out.println("Danh sách khách hàng hiện có:");
            customerManagement.displayCustomers();

            System.out.print("Nhập CustomerID: ");
            int customerID = Integer.parseInt(scanner.nextLine());
            Customer customer = customerManagement.getCustomerById(customerID);
            if (customer == null) {
                System.out.println("Khách hàng này không tồn tại!");
                return;
            }

            System.out.print("Nhập ngày đặt chỗ (yyyy-MM-dd): ");
            String dateInput = scanner.nextLine();
            Date reservationDate = Date.valueOf(dateInput);
            System.out.print("Nhập số bàn: ");
            int tableNumber = Integer.parseInt(scanner.nextLine());
            System.out.print("Nhập số lượng khách: ");
            int numberOfGuests = Integer.parseInt(scanner.nextLine());

            if (customerID <= 0 || tableNumber <= 0 || numberOfGuests <= 0) {
                System.out.println("Thông tin không hợp lệ. Vui lòng kiểm tra lại.");
                return;
            }

            Connection connection = DBConnection.makeConnection();
            String insertSQL = "INSERT INTO Reservation (ReservationID, ReservationDate, CustomerID, TableNumber, NumberOfGuests, Cancelled) VALUES (?, ?, ?, ?, ?, ?);";
            PreparedStatement stm = connection.prepareStatement(insertSQL);
            stm.setInt(1, getNextReservationId());
            stm.setDate(2, reservationDate);
            stm.setInt(3, customerID);
            stm.setInt(4, tableNumber);
            stm.setInt(5, numberOfGuests);
            stm.setBoolean(6, false);

            int affectedRows = stm.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Thêm đặt chỗ thành công!");
            } else {
                System.out.println("Thêm đặt chỗ thất bại!");
            }

            stm.close();
            connection.close();
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    // Cập nhật đặt chỗ
    public void updateReservation(Scanner scanner) {
        if (isReservationListEmpty()) {
            System.out.println("Danh sách đơn đặt chỗ đang trống. Vui lòng thêm 1 đơn để sử dụng tính năng.");
        }
        try {
            System.out.println("Danh sách các đơn đặt chỗ hiện có");
            displayReservations();

            System.out.print("Nhập ID đặt chỗ cần cập nhật: ");
            int reservationID = Integer.parseInt(scanner.nextLine());
            Reservation reservation = getReservationById(reservationID);
            if (reservation == null) {
                System.out.println("Không tìm thấy ");
                return;
            }

            System.out.print("Nhập ngày mới (yyyy-MM-dd): ");
            String dateInput = scanner.nextLine();
            Date reservationDate = Date.valueOf(dateInput);
            System.out.print("Nhập số bàn mới: ");
            int tableNumber = Integer.parseInt(scanner.nextLine());
            System.out.print("Nhập số lượng khách mới: ");
            int numberOfGuests = Integer.parseInt(scanner.nextLine());

            if (tableNumber <= 0 || numberOfGuests <= 0) {
                System.out.println("Thông tin không hợp lệ. Vui lòng kiểm tra lại.");
                return;
            }

            Connection connection = DBConnection.makeConnection();
            String updateSQL = "UPDATE Reservation SET ReservationDate = ?, TableNumber = ?, NumberOfGuests = ? WHERE ReservationID = ?;";
            PreparedStatement stm = connection.prepareStatement(updateSQL);
            stm.setDate(1, reservationDate);
            stm.setInt(2, tableNumber);
            stm.setInt(3, numberOfGuests);
            stm.setInt(4, reservationID);

            int affectedRows = stm.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Cập nhật đặt chỗ thành công!");
            } else {
                System.out.println("Không tìm thấy đặt chỗ để cập nhật.");
            }

            stm.close();
            connection.close();
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    // Hủy đặt chỗ
    public void cancelReservation(Scanner scanner) {
        if (isReservationListEmpty()) {
            System.out.println("Danh sách đơn đặt chỗ đang trống. Vui lòng thêm 1 đơn để sử dụng tính năng.");
        }
        try {
            System.out.println("Danh sách các đơn đặt chỗ hiện có");
            displayReservations();

            System.out.print("Nhập ID đặt chỗ cần hủy: ");
            int reservationID = Integer.parseInt(scanner.nextLine());

            Connection connection = DBConnection.makeConnection();
            String cancelSQL = "UPDATE Reservation SET Cancelled = ? WHERE ReservationID = ?;";
            PreparedStatement stm = connection.prepareStatement(cancelSQL);
            stm.setBoolean(1, true);
            stm.setInt(2, reservationID);

            int affectedRows = stm.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Hủy đặt chỗ thành công!");
            } else {
                System.out.println("Không tìm thấy đặt chỗ để hủy.");
            }

            stm.close();
            connection.close();
        } catch (NumberFormatException | SQLException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    // Xóa đặt chỗ bằng customerId
    public void deleteReservationsByCustomerId(int customerId) {
        try {
            Connection connection = DBConnection.makeConnection();
            String deleteSQL = "DELETE FROM \"Reservation\" WHERE CustomerID = ?;";
            PreparedStatement stm = connection.prepareStatement(deleteSQL);
            stm.setInt(1, customerId);

            int affectedRows = stm.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Xóa các đơn đặt chỗ của khách hàng " + customerId + " thành công!");
            }
        } catch (SQLException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    // Hiển thị danh sách đặt chỗ
    public void displayReservations() {
        try {
            Connection connection = DBConnection.makeConnection();
            String selectSQL = "SELECT * FROM Reservation WHERE Cancelled = 'false';";
            PreparedStatement stm = connection.prepareStatement(selectSQL);
            ResultSet rs = stm.executeQuery();

            System.out.println("| ID   | Ngày       | Mã KH      | Bàn   | Số người |");
            System.out.println("-----------------------------------------------------");

            while (rs.next()) {
                System.out.printf("| %-4d | %-10s | %-10d | %-5d | %-8d |\n",
                        rs.getInt("ReservationID"),
                        rs.getDate("ReservationDate"),
                        rs.getInt("CustomerID"),
                        rs.getInt("TableNumber"),
                        rs.getInt("NumberOfGuests"));
            }

            rs.close();
            stm.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    // Tìm kiếm đặt chỗ theo CustomerID
    public void searchReservationByCustomerId(Scanner scanner) {
        if (isReservationListEmpty()) {
            System.out.println("Danh sách đơn đặt chỗ đang trống. Vui lòng thêm 1 đơn để sử dụng tính năng.");
        }
        try {
            System.out.print("Nhập mã khách hàng cần tìm: ");
            int customerID = Integer.parseInt(scanner.nextLine());

            Connection connection = DBConnection.makeConnection();
            String searchSQL = "SELECT * FROM Reservation WHERE CustomerID = ?;";
            PreparedStatement stm = connection.prepareStatement(searchSQL);
            stm.setInt(1, customerID);
            ResultSet rs = stm.executeQuery();

            System.out.println("| ID   | Ngày       | Mã KH      | Bàn   | Số người |");
            System.out.println("-----------------------------------------------------");

            if (!rs.isBeforeFirst()) {
                System.out.println("Không có đặt chỗ nào cho CustomerID này.");
            }

            while (rs.next()) {
                System.out.printf("| %-4d | %-10s | %-10d | %-5d | %-8d |\n",
                        rs.getInt("ReservationID"),
                        rs.getDate("ReservationDate"),
                        rs.getInt("CustomerID"),
                        rs.getInt("TableNumber"),
                        rs.getInt("NumberOfGuests"));
            }

            rs.close();
            stm.close();
            connection.close();
        } catch (NumberFormatException | SQLException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    // Lấy ID tiếp theo cho đặt chỗ
    public int getNextReservationId() {
        try {
            Connection connection = DBConnection.makeConnection();
            String maxIDSQL = "SELECT MAX(ReservationID) AS MaxID FROM Reservation;";
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

    public Reservation getReservationById(int reservationId) {
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            connection = DBConnection.makeConnection();
            if (connection != null) {
                String getReservationByIdSQL = "SELECT * FROM Reservation WHERE ReservationID = ?";
                stm = connection.prepareStatement(getReservationByIdSQL);
                stm.setInt(1, reservationId);
                rs = stm.executeQuery();

                if (rs.next()) {
                    return new Reservation(
                            rs.getInt("ReservationID"),
                            rs.getInt("CustomerID"),
                            rs.getDate("ReservationDate"),
                            rs.getInt("TableNumber"),
                            rs.getInt("NumberOfGuests"),
                            rs.getBoolean("Cancelled")
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

    // Kiểm tra danh sách đặt chỗ có trống hay không
    public boolean isReservationListEmpty() {
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            connection = DBConnection.makeConnection();
            if (connection != null) {
                String sql = "SELECT COUNT(*) AS Total FROM Reservation;";
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
