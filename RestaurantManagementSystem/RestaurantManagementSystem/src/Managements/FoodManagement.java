package Managements;

import Models.Customer;
import Models.Food;
import Utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class FoodManagement {

    public void displayFoodManagementMenu() {
        Scanner scanner = new Scanner(System.in);
        try {
            boolean exit = false;
            while (!exit) {
                System.out.println("\nQuản lý món ăn");
                System.out.println("1. Thêm món ăn");
                System.out.println("2. Cập nhật món ăn");
                System.out.println("3. Xóa món ăn");
                System.out.println("4. Hiển thị danh sách món ăn");
                System.out.println("5. Tìm kiếm món ăn theo tên");
                System.out.println("0. Thoát");
                System.out.print("Chọn chức năng: ");
                int choice;
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    choice = -1;
                }

                switch (choice) {
                    case 1: // Thêm món ăn
                        System.out.print("Nhập tên món ăn: ");
                        String name = scanner.nextLine();
                        System.out.print("Nhập giá món ăn: ");
                        float price = Float.parseFloat(scanner.nextLine());
                        addFood(name, price);
                        break;

                    case 2: // Cập nhật món ăn
                        System.out.println("Danh sách món ăn hiện có");
                        displayFoods();
                        System.out.print("Nhập ID món ăn cần cập nhật: ");
                        int idToUpdate = scanner.nextInt();
                        scanner.nextLine(); // Clear buffer

                        Food thisFood = getFoodById(idToUpdate);
                        if (thisFood == null) {
                            System.out.println("Không tìm thấy món ăn với ID: " + idToUpdate);
                        } else {
                            System.out.print("Nhập tên mới: ");
                            name = scanner.nextLine();
                            System.out.print("Nhập giá mới: ");
                            price = Float.parseFloat(scanner.nextLine());
                            updateFood(idToUpdate, name, price);
                        }
                        break;

                    case 3: // Xóa món ăn
                        System.out.println("Danh sách món ăn hiện có");
                        displayFoods();
                        System.out.print("Nhập ID món ăn cần xóa: ");
                        int idToDelete = scanner.nextInt();
                        scanner.nextLine(); // Clear buffer
                        OrderManagement orderManagement = new OrderManagement();
                        orderManagement.deleteOrdersByFoodID(idToDelete);
                        deleteFood(idToDelete);
                        break;

                    case 4: // Hiển thị danh sách món ăn
                        displayFoods();
                        break;

                    case 5: // Tìm kiếm món ăn theo tên
                        System.out.print("Nhập tên món ăn cần tìm: ");
                        String searchName = scanner.nextLine();
                        searchFoodByName(searchName);
                        break;
                    case 0: // Thoát
                        exit = true;
                        System.out.println("Thoát khỏi chương trình quản lý món ăn.");
                        break;

                    default:
                        System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
                }
            }
        } catch (IllegalArgumentException e) {
            scanner.nextLine();
            System.out.println("Thông tin nhập vào không hợp lệ.");
        }
    }

    // Thêm món ăn mới
    public void addFood(String name, float price) {
        Connection connection = null;
        PreparedStatement stm = null;

        try {
            connection = DBConnection.makeConnection();
            if (connection != null) {
                String addFoodSQL = "INSERT INTO Food (FoodID, Name, Price) VALUES (?, ?, ?);";
                stm = connection.prepareStatement(addFoodSQL);
                stm.setInt(1, getNextFoodId());
                stm.setString(2, name);
                stm.setFloat(3, price);

                int affectedRows = stm.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Thêm món ăn thành công!");
                } else {
                    System.out.println("Thêm món ăn thất bại!");
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

    // Cập nhật thông tin món ăn
    public void updateFood(int id, String name, float price) {
        Connection connection = null;
        PreparedStatement stm = null;

        try {
            connection = DBConnection.makeConnection();
            if (connection != null) {
                String updateFoodSQL = "UPDATE Food SET Name = ?, Price = ? WHERE FoodID = ?;";
                stm = connection.prepareStatement(updateFoodSQL);
                stm.setString(1, name);
                stm.setFloat(2, price);
                stm.setInt(3, id);

                int affectedRows = stm.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Cập nhật món ăn thành công!");
                } else {
                    System.out.println("Cập nhật món ăn thất bại!");
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

    // Xóa món ăn
    public void deleteFood(int id) {
        Connection connection = null;
        PreparedStatement stm = null;

        try {
            connection = DBConnection.makeConnection();
            if (connection != null) {
                String deleteFoodSQL = "DELETE FROM Food WHERE FoodID = ?;";
                stm = connection.prepareStatement(deleteFoodSQL);
                stm.setInt(1, id);

                int affectedRows = stm.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Xóa món ăn thành công!");
                } else {
                    System.out.println("Xóa món ăn thất bại!");
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

    // Hiển thị danh sách món ăn
    public void displayFoods() {
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            connection = DBConnection.makeConnection();
            if (connection != null) {
                String getAllFoodSQL = "SELECT * FROM Food;";
                stm = connection.prepareStatement(getAllFoodSQL);
                rs = stm.executeQuery();

                if (rs.isBeforeFirst()) {
                    // Print table header
                    System.out.println("| ID   | Tên                | Giá        |");
                    System.out.println("------------------------------------------");

                    while (rs.next()) {
                        // Format output for each food item
                        System.out.printf("| %-4d | %-18s | %-10.2f |\n",
                                rs.getInt("FoodID"),
                                rs.getString("Name"),
                                rs.getFloat("Price"));
                    }
                } else {
                    System.out.println("Không có món ăn nào.");
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

    // Tìm kiếm món ăn theo tên
    public void searchFoodByName(String name) {
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            connection = DBConnection.makeConnection();
            if (connection != null) {
                String getFoodByNameSQL = "SELECT * FROM Food WHERE Name LIKE ?;";
                stm = connection.prepareStatement(getFoodByNameSQL);
                stm.setString(1, "%" + name + "%");
                rs = stm.executeQuery();

                if (rs.isBeforeFirst()) {
                    // Print table header
                    System.out.println("| ID   | Tên                | Giá        |");
                    System.out.println("------------------------------------------");

                    while (rs.next()) {
                        // Format output for each food item
                        System.out.printf("| %-4d | %-18s | %-10.2f |\n",
                                rs.getInt("FoodID"),
                                rs.getString("Name"),
                                rs.getFloat("Price"));
                    }
                } else {
                    System.out.println("Không tìm thấy món ăn nào với tên: " + name);
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

    public Food getFoodById(int id) {
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            connection = DBConnection.makeConnection();
            if (connection != null) {
                String getFoodByIdSQL = "SELECT * FROM Food WHERE FoodID = ?;";
                stm = connection.prepareStatement(getFoodByIdSQL);
                stm.setInt(1, id);
                rs = stm.executeQuery();
                if (rs.next()) {
                    return new Food(rs.getInt("FoodID"),
                            rs.getString("Name"),
                            rs.getFloat("Price"));
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

    public int getNextFoodId() {
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            connection = DBConnection.makeConnection();
            if (connection != null) {
                String checkFoodSQL = "SELECT TOP(1) FoodID FROM Food ORDER BY FoodID DESC;";
                stm = connection.prepareStatement(checkFoodSQL);
                rs = stm.executeQuery();
                if (rs.next()) {
                    return rs.getInt("FoodID") + 1;
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

    // Kiểm tra danh sách món ăn có trống hay không
    public boolean isFoodListEmpty() {
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            connection = DBConnection.makeConnection();
            if (connection != null) {
                String sql = "SELECT COUNT(*) AS Total FROM Food;";
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
