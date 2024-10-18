/*Bài 10. Viết chương trình in ra tổng của 10 số chẵn đầu tiên
(sử dụng vòng lặp for hoặc while)
* */

public class Sum {
    private float sum;

    // Constructor
    public Sum() {
        this.sum = 0; // Khởi tạo sum là 0
    }

    public float sumNumber() {
        for (int i = 0; i < 10; i++) {
            sum += i * 2; // Cộng các số chẵn (0, 2, 4, ..., 18)
        }
        return sum;
    }

    public void printSumNumber() {
        System.out.println("Tổng của 10 số chẵn đầu tiên là: " + sum);
    }









}
