import Managements.CustomerManagement;
import Managements.FoodManagement;
import Managements.OrderManagement;
import Managements.ReservationManagement;
import Utils.DBConnection;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        boolean exit = false;
        while (!exit) {
            System.out.println("\nHỆ THỐNG QUẢN LÝ NHÀ HÀNG");
            System.out.println("1. Quản lý đồ ăn");
            System.out.println("2. Quản lý khách hàng");
            System.out.println("3. Quản lý đặt chỗ");
            System.out.println("4. Quản lý gọi món");
            System.out.println("0. Thoát");
            System.out.print("Hãy chọn chức năng cần thực hiện: ");
            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());

            } catch (NumberFormatException e) {
                choice = -1;
            }

            switch (choice) {
                case 1:
                    FoodManagement foodManagement = new FoodManagement();
                    foodManagement.displayFoodManagementMenu();
                    break;
                case 2:
                    CustomerManagement customerManagement = new CustomerManagement();
                    customerManagement.displayCustomerManagementMenu();
                    break;
                case 3:
                    ReservationManagement reservationManagement = new ReservationManagement();
                    reservationManagement.displayReservationManagementMenu();
                    break;
                case 4:
                    OrderManagement orderManagement = new OrderManagement();
                    orderManagement.displayOrderManagementMenu();
                    break;
                case 0:
                    System.out.println("Thoát khỏi chương trình quản lý nhà hàng.");
                    exit = true;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
                    break;
            }
        }
    }
}