import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;

/**
 * На вход принимает координаты вершин треугольника
 * и выводит в консоль его площадь
 *
 * @author Уткин Олег
 **/
public class Lab1 {

    /**
     * Точка входа
     **/
    public static void main(String[] args) {
        Point3d[] points = new Point3d[3];

        Scanner in = new Scanner(System.in);
        for (int i = 0; i < 3; ++i) {
            System.out.printf("Точка %d: ", i + 1);
            points[i] = new Point3d(getDouble(in), getDouble(in), getDouble(in));
        }


        if (points[0].equals(points[1]) ||
            points[0].equals(points[2]) ||
            points[1].equals(points[2])
        ) {

            System.out.println("Точки совпадают");
            System.exit(1);

        } else {

            System.out.println(computeArea(points[0], points[1], points[2]));
        }

    }


    /**
     * Расчитывает площадь треугольника
     **/
    public static double computeArea(Point3d point1, Point3d point2, Point3d point3) {
        double semiperimeter = (    point1.distanceTo(point2)
                                    + point1.distanceTo(point3)
                                    + point2.distanceTo(point3)
                               ) / 2;

        return Math.sqrt(semiperimeter *
                        (semiperimeter - point1.distanceTo(point2)) *
                        (semiperimeter - point1.distanceTo(point3)) *
                        (semiperimeter - point2.distanceTo(point3)) );
    }

    /**
     * Обрабатывает double из стандартного ввода
     *
     * @param scanner принимает Scanner
     *
     * @return 0 при ошибке
     **/
    public static double getDouble(Scanner scanner) {

        try {
            double d = scanner.nextDouble();
            return d;

        } catch (InputMismatchException e) {

            return 0.0;
        }

    }
}