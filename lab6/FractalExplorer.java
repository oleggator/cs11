import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileFilter;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import java.awt.geom.Rectangle2D;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import java.io.File;
import java.io.IOException;

class FractalExplorer {
    private int displaySize;

    private JFrame frame;
    private JImageDisplay imageDisplay;
    private JLabel label;
    private JComboBox<FractalGenerator> comboBox;
    private JPanel northPanel;
    private JButton saveImageButton;
    private JButton resetButton;
    private JPanel southPanel;

    private FractalGenerator fractalGenerator;
    private Rectangle2D.Double range;
    private int remainingLinesCount = 0;

    private class FractalWorker extends SwingWorker<Object, Object> {

        int lineNumber;
        int[] colorsOfPoints;

        public FractalWorker(int lineNumber) {
            this.lineNumber = lineNumber;
        }

        /* Выполняется в фоне */
        @Override
        protected Object doInBackground() {
            colorsOfPoints = new int[displaySize];

            for (int i = 0; i < displaySize; ++i) {
                double x = fractalGenerator.getCoord(range.x, range.x + range.width, displaySize, i);
                double y = fractalGenerator.getCoord(range.y, range.y + range.width, displaySize, lineNumber);

                int iterationsCount = fractalGenerator.numIterations(x, y);

                if (iterationsCount == -1) {
                    colorsOfPoints[i] = 0;
                } else {
                    float hue = 0.7f + (float) iterationsCount / 200f;
                    colorsOfPoints[i] = Color.HSBtoRGB(hue, 1f, 1f);
                }
            }

            return null;
        }

        /* Выполняетя после выполение doInBackground */
        @Override
        protected void done() {
            for (int i = 0; i < displaySize; ++i) {
                imageDisplay.drawPixel(i, lineNumber, colorsOfPoints[i]);
            }

            imageDisplay.repaint(0, 0, lineNumber, displaySize, 1);
            --remainingLinesCount;

            if (remainingLinesCount == 0)
                enableUI(true);
            
        }

    }

    public FractalExplorer(int initialDisplaySize) {
        displaySize = initialDisplaySize;
        range = new Rectangle2D.Double();
    }

    /* Создание и отображение графического интерфейса */
    private void createAndShowGUI() {
        frame = new JFrame("Fractal Explorer");
        frame.setLayout(new BorderLayout());

        label = new JLabel("Fractal:");

        comboBox = new JComboBox<>();
        comboBox.addItem(new Mandelbrot());
        comboBox.addItem(new Tricorn());
        comboBox.addItem(new BurningShip());

        northPanel = new JPanel();
        northPanel.add(label);
        northPanel.add(comboBox);

        frame.add(northPanel, BorderLayout.NORTH);

        imageDisplay = new JImageDisplay(displaySize, displaySize);
        frame.add(imageDisplay, BorderLayout.CENTER);

        saveImageButton = new JButton("Save Image");
        resetButton = new JButton("Reset Display");

        southPanel = new JPanel();
        southPanel.add(saveImageButton);
        southPanel.add(resetButton);

        frame.add(southPanel, BorderLayout.SOUTH);

        MouseHandler mouseHandler = new MouseHandler();
        imageDisplay.addMouseListener(mouseHandler);

        SaveHandler saveHandler = new SaveHandler();
        saveImageButton.addActionListener(saveHandler);

        ResetHandler resetHandler = new ResetHandler();
        resetButton.addActionListener(resetHandler); 

        ComboBoxHandler comboBoxHandler = new ComboBoxHandler();
        comboBox.addActionListener(comboBoxHandler); 

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);

        fractalGenerator = (FractalGenerator)comboBox.getSelectedItem();
        fractalGenerator.getInitialRange(range);
    }

    /* Отрисовка фрактала */
    private void drawFractal() {
        enableUI(false);
        remainingLinesCount = displaySize;

        for (int i = 0; i < displaySize; ++i) {
            FractalWorker worker = new FractalWorker(i);
            worker.execute();
        }
    }

    /* Активация/деактивация элементов управления */
    private void enableUI(boolean val) {
        saveImageButton.setEnabled(val);
        resetButton.setEnabled(val);
        comboBox.setEnabled(val);
    }

    /* Обработчик мыши */
    class MouseHandler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (remainingLinesCount == 0) {
                double x = fractalGenerator.getCoord(range.x, range.x + range.width, displaySize, e.getX());
                double y = fractalGenerator.getCoord(range.y, range.y + range.width, displaySize, e.getY());

                fractalGenerator.recenterAndZoomRange(range, x, y, 0.5);
                drawFractal();
            }
        }
    }

    /* Обработчик сохранения */
    class SaveHandler implements ActionListener {
        public void actionPerformed(ActionEvent action) {
            JFileChooser chooser = new JFileChooser();
            FileFilter filter = new FileNameExtensionFilter("PNG Images", "png");
            chooser.setFileFilter(filter);
            chooser.setAcceptAllFileFilterUsed(false);

            if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                try {
                    ImageOutputStream stream = null;

                    stream = ImageIO.createImageOutputStream(chooser.getSelectedFile());
                    if (stream != null) {
                        ImageIO.write(imageDisplay.bufferedImage, "png", stream);
                    } else {
                        IOException e = new IOException("File writing error");
                        throw e;
                    }

                } catch (IOException e) {
                    JOptionPane.showMessageDialog(frame, e.getMessage(), "Cannot save image",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /* Обработчик combobox */
    class ComboBoxHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JComboBox comboBox = (JComboBox)e.getSource();
            fractalGenerator = (FractalGenerator)comboBox.getSelectedItem();

            fractalGenerator.getInitialRange(range);
            drawFractal();
        }
    }

    /* Обработчик сброса */
    class ResetHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            fractalGenerator.getInitialRange(range);
            drawFractal();
        }
    }

    public static void main(String[] args) {
        FractalExplorer fractalExplorer = new FractalExplorer(800);
        fractalExplorer.createAndShowGUI();
        fractalExplorer.drawFractal();
    }
}
