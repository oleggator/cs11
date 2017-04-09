import javax.swing.JComponent;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.awt.Graphics;

public class JImageDisplay extends JComponent {
    private BufferedImage bufferedImage;

    public JImageDisplay(int width, int height) {
        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        setPreferredSize(new Dimension(width, height));
    }

    /* Делает все пиксели черными */
    public void clearImage() {
        for (int i = 0; i < bufferedImage.getHeight(); ++i)
            for (int j = 0; j < bufferedImage.getWidth(); ++j)
                bufferedImage.setRGB(j, i, 0);
    }

    /* Задаёт цвет пикселя */
    public void drawPixel(int x, int y, int rgbColor) {
        bufferedImage.setRGB(x, y, rgbColor);
    }

    /* Рисует изображение */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(bufferedImage, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null);
    }

}
