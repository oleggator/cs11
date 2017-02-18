/**
 * Вывод всех простых чисел до 100
 **/
public class Primes {
    /**
     * Точка входа
     **/
    public static void main(String[] args) {

        for (int i = 2; i < 100; ++i)
            if (isPrime(i))
                System.out.println(i);

    }

    /**
     * Определение простого числа
     **/
    public static boolean isPrime(int n) {
        for (int i = 2; i < n; ++i)
            if (n % i == 0)
                return false;

        return true;
    }

}