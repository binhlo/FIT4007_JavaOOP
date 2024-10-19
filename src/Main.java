public class Main {
    public static void main(String[] args) {
        // Kiểm tra nếu có tham số dòng lệnh
        if (args.length != 1) {
            System.out.println("Vui lòng nhập một số nguyên n.");
            return;
        }

        try {
            // Chuyển đổi tham số dòng lệnh thành số nguyên
            int n = Integer.parseInt(args[0]);

            // Tạo đối tượng SumCalculator và tính tổng
            SumOfNumbers calculator = new SumOfNumbers(n);
            int sum = calculator.SumOfNumbersToN();

            // In kết quả
            System.out.println("Tổng của các số từ 1 đến " + n + " là: " + sum);

        } catch (NumberFormatException e) {
            System.out.println("Tham số dòng lệnh phải là một số nguyên hợp lệ.");
        }
    }
}
