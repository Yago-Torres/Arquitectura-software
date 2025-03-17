import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.QuadCurve2D;

public class Association {
    // Referencias a las clases
    Class from;
    Class to;

    public Association(Class from, Class to) {
        this.from = from;
        this.to = to;
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        
        // Activa Antialiasing para mejorar la calidad del dibujo
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.black);

        if (from == to) {
            System.out.println("Dibujando bucle de autorrelación para la clase: " + from);

            int x = from.getX();
            int y = from.getY();
            int size = 80; // Tamaño del bucle

            // Ajustamos la posición del bucle para que sea más visible
            int offsetX = 50;
            int offsetY = 30;

            // Dibujamos un arco como una curva con un punto de control
            QuadCurve2D loop = new QuadCurve2D.Double(
                x, y,                     // Punto de inicio (centro de la clase)
                x + offsetX, y - offsetY - size, // Punto de control para la curva
                x + 2 * offsetX, y        // Punto final (de vuelta a la clase)
            );

            g2.draw(loop);
        } else {
            g2.drawLine(from.getmidX(), from.getmidY(), to.getmidX(), to.getmidY());
        }
    }
}
