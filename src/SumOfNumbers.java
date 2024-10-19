/*Bài 13. Viết chương trình in ra tổng 1+2+3….+n
với n được nhập từ tham số command line*/

public class SumOfNumbers {
    private int number;


    public SumOfNumbers(int number){
        this.number = number;
    }

    public int SumOfNumbersToN() {
        int sum = 0;
        for (int i=1;i<=number;i++) {
            sum += i;
        }
        return sum;
    }

}

