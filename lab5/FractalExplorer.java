import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileFilter;
import javax.swing.JOptionPane;

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
    private JImageDisplay imageDisplay;
    private FractalGenerator fractalGenerator;
    private Rectangle2D.Double range;

    public FractalExplorer(int initialDisplaySize) {
        displaySize = initialDisplaySize;
        range = new Rectangle2D.Double();
    }

    /* Создание и отображение графического интерфейса */
    private void createAndShowGUI() {
        JFrame frame = new JFrame("Fractal Explorer");
        frame.setLayout(new BorderLayout());

        JLabel label = new JLabel("Fractal:");

        JComboBox<FractalGenerator> comboBox = new JComboBox<>();
        comboBox.addItem(new Mandelbrot());
        comboBox.addItem(new Tricorn());
        comboBox.addItem(new BurningShip());

        JPanel northPanel = new JPanel();
        northPanel.add(label);
        northPanel.add(comboBox);

        frame.add(northPanel, BorderLayout.NORTH);

        imageDisplay = new JImageDisplay(displaySize, displaySize);
        frame.add(imageDisplay, BorderLayout.CENTER);

        JButton saveImageButton = new JButton("Save Image");
        JButton resetButton = new JButton("Reset Display");

        JPanel southPanel = new JPanel();
        southPanel.add(saveImageButton);
        southPanel.add(resetButton);

        frame.add(southPanel, BorderLayout.SOUTH);

        /* Обработчик мыши */
        class MouseHandler extends MouseAdapter {
            @Override
            public void mouseClicked(MouseEvent e) {
                double x = fractalGenerator.getCoord(range.x, range.x + range.width, displaySize, e.getX());
                double y = fractalGenerator.getCoord(range.y, range.y + range.width, displaySize, e.getY());

                fractalGenerator.recenterAndZoomRange(range, x, y, 0.5);
                drawFractal();
            }
        }

        MouseHandler mouseHandler = new MouseHandler();
        imageDisplay.addMouseListener(mouseHandler);

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

        SaveHandler saveHandler = new SaveHandler();
        saveImageButton.addActionListener(saveHandler);

        /* Обработчик сброса */
        class ResetHandler implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                fractalGenerator.getInitialRange(range);
                drawFractal();
            }
        }

        ResetHandler resetHandler = new ResetHandler();
        resetButton.addActionListener(resetHandler); 

        /* Обработчик combobox */
        class ComboBoxHandler implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                JComboBox comboBox = (JComboBox)e.getSource();
                fractalGenerator = (FractalGenerator)comboBox.getSelectedItem();

                fractalGenerator.getInitialRange(range);
                drawFractal();
            }
        }

        ComboBoxHandler comboBoxHandler = new ComboBoxHandler();
        comboBox.addActionListener(comboBoxHandler); 

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);

        fractalGenerator = (FractalGenerator)comboBox.getSelectedItem();
        fractalGenerator.getInitialRange(range);
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

    public static void main(String[] args) {
        FractalExplorer fractalExplorer = new FractalExplorer(800);
        fractalExplorer.createAndShowGUI();
        fractalExplorer.drawFractal();
    }
}
