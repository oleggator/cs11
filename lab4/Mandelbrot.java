import java.awt.geom.Rectangle2D;

class Mandelbrot extends FractalGenerator {
    public static final int MAX_ITERATIONS = 2000;

    /* Пишет в range начальный диапазон расчитываемых значений */
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = -1.5;
        range.width = 3;
        range.height = 3;
    }

    /* Выводит количество итераций цикла */
    public int numIterations(double x, double y) {
        double mandelbrotX = 0;
        double mandelbrotY = 0;

        int i = 0;
        while (mandelbrotX * mandelbrotX + mandelbrotY * mandelbrotY < 4
            && i < MAX_ITERATIONS) {

            double newMandelbrotX = mandelbrotX * mandelbrotX - mandelbrotY * mandelbrotY + x;
            mandelbrotY = 2 * mandelbrotX * mandelbrotY + y;
            mandelbrotX = newMandelbrotX;

            ++i;
        }

        if (i == MAX_ITERATIONS)
            return -1;

        return i;
    }
}
