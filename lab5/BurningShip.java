import java.awt.geom.Rectangle2D;

class BurningShip extends FractalGenerator {
    public static final int MAX_ITERATIONS = 2000;

    /* Пишет в range начальный диапазон расчитываемых значений */
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = -2.5;
        range.width = 4;
        range.height = 4;
    }

    /* Выводит количество итераций цикла */
    public int numIterations(double x, double y) {
        double burningShipX = 0;
        double burningShipY = 0;

        int i = 0;
        while (burningShipX * burningShipX + burningShipY * burningShipY < 4
            && i < MAX_ITERATIONS) {

            double newBurningShipX = burningShipX * burningShipX - burningShipY * burningShipY + x;
            burningShipY = 2 * Math.abs(burningShipX) * Math.abs(burningShipY) + y;
            burningShipX = newBurningShipX;

            ++i;
        }

        if (i == MAX_ITERATIONS)
            return -1;

        return i;
    }

    @Override
    public String toString() {
        return "Burning Ship";
    }
}
