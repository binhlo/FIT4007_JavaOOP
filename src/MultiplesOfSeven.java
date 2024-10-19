/*Bài 12. Viết chương trình xuất ra
tổng các số là bội số của 7 (từ 1 đến 100) */

public class MultiplesOfSeven {
    private float sum;

    public MultiplesOfSeven() {
        this.sum = 0;
    }


    public float sumOfMultiplesOfSeven() {
        for (int i=7; i<100; i+=7) {
            sum += i;
        }
        return sum;
    }

    public void printSumOfMultiplesOfSeven() {
        System.out.println("Tong cua boi cac so chia het cho 7 tu 1-99 la: " +sum);
    }
}
