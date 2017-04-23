import java.awt.geom.Rectangle2D;

class Tricorn extends FractalGenerator {
    public static final int MAX_ITERATIONS = 2000;

    /* Пишет в range начальный диапазон расчитываемых значений */
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = -2;
        range.width = 4;
        range.height = 4;
    }

    /* Выводит количество итераций цикла */
    public int numIterations(double x, double y) {
        double tricornX = 0;
        double tricornY = 0;

        int i = 0;
        while (tricornX * tricornX + tricornY * tricornY < 4
            && i < MAX_ITERATIONS) {

            double newTricornX = tricornX * tricornX - tricornY * tricornY + x;
            tricornY = -2 * tricornX * tricornY + y;
            tricornX = newTricornX;

            ++i;
        }

        if (i == MAX_ITERATIONS)
            return -1;

        return i;
    }

    @Override
    public String toString() {
        return "Tricorn";
    }
}
