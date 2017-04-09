import java.awt.geom.Rectangle2D;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class FractalExplorer {

    private int displaySize;
    private JImageDisplay imageDisplay;
    private FractalGenerator fractalGenerator;
    private Rectangle2D.Double range;

    public FractalExplorer(int initialDisplaySize) {
        displaySize = initialDisplaySize;

        fractalGenerator = new Mandelbrot();

        range = new Rectangle2D.Double();
        fractalGenerator.getInitialRange(range);
    }

    /* Создание и отображение графического интерфейса */
    private void createAndShowGUI() {
        JFrame frame = new JFrame("Fractal Explorer");
        frame.setLayout(new BorderLayout());

        imageDisplay = new JImageDisplay(displaySize, displaySize);
        frame.add(imageDisplay, BorderLayout.CENTER);

        MouseHandler mouseHandler = new MouseHandler();
        imageDisplay.addMouseListener(mouseHandler);

        JButton resetButton = new JButton("Reset Display");
        frame.add(resetButton, BorderLayout.SOUTH);

        ResetHandler resetHandler = new ResetHandler();
        resetButton.addActionListener(resetHandler); 

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    /* Нарисовать фрактал */
    private void drawFractal() {
        for (int i = 0; i < displaySize; ++i)
            for (int j = 0; j < displaySize; ++j) {
                double x = fractalGenerator.getCoord(range.x, range.x + range.width, displaySize, i);
                double y = fractalGenerator.getCoord(range.y, range.y + range.width, displaySize, j);

                int iterationsCount = fractalGenerator.numIterations(x, y);

                if (iterationsCount == -1) {
                    imageDisplay.drawPixel(i, j, 0);
                } else {
                    float hue = 0.7f + (float) iterationsCount / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);

                    imageDisplay.drawPixel(i, j, rgbColor);
                }
            }

        imageDisplay.repaint();
    }

    /* Обработчик сброса */
    private class ResetHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            fractalGenerator.getInitialRange(range);
            drawFractal();
        }
    }

    /* Обработчик мыши */
    private class MouseHandler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            double x = fractalGenerator.getCoord(range.x, range.x + range.width, displaySize, e.getX());
            double y = fractalGenerator.getCoord(range.y, range.y + range.width, displaySize, e.getY());

            fractalGenerator.recenterAndZoomRange(range, x, y, 0.5);
            drawFractal();
        }
    }

    public static void main(String[] args) {
        FractalExplorer fractalExplorer = new FractalExplorer(800);
        fractalExplorer.createAndShowGUI();
        fractalExplorer.drawFractal();
    }
}
