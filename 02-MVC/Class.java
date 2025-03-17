import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
//otros import

public class Class {
	
	//Atributos
  
private String name;
private int x, y;
private List<String> attributes;
private List<String> methods;
private boolean selected;


	
public Class(String name, int x, int y) {
    this.name = name;
    this.x = x;
    this.y = y;
    this.attributes = new ArrayList<>();
    this.methods = new ArrayList<>();
    this.selected = false;
}

public void addAttribute(String attribute) {
    attributes.add(attribute);
}

public void addMethod(String method) {
    methods.add(method);
}

public int getX() {
    return this.x;
}

public int getY() {
    return this.y;
}

public int getmidX() {
    return this.x + 50; 
}

public int getmidY() {
    return this.y + 50; 
}


public void setX(int x) {
    this.x = x;
}

public void setY(int y) {
    this.y = y;
}

public void toggleSelection() {
    selected = !selected;
}

public boolean isSelected() {
    return selected;
}

public void draw(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;

    int width = 100;
    int height = 100;

    // Calcula la altura de las secciones
    int headerHeight = 20;
    int attributesHeight = 40; // espacio para atributos
    int methodsHeight = height - headerHeight - attributesHeight;

    // Color de fondo
    if (selected) {
        g2d.setColor(Color.CYAN);
    } else {
        g2d.setColor(Color.WHITE);
    }
    g2d.fillRect(x, y, width, height);

    // Borde general
    g2d.setColor(Color.BLACK);
    g2d.drawRect(x, y, width, height);

    // Línea divisoria entre nombre y atributos
    g2d.drawLine(x, y + headerHeight, x + width, y + headerHeight);

    // Línea divisoria entre atributos y métodos
    g2d.drawLine(x, y + headerHeight + attributesHeight, x + width, y + headerHeight + attributesHeight);

    // Dibujar nombre de la clase
    g2d.drawString(name, x + 10, y + 15);

    // Dibujar atributos
    int attrY = y + headerHeight + 15;
    if (attributes.isEmpty()) {
        g2d.drawString("atributos", x + 10, attrY);
    } else {
        for (String attribute : attributes) {
            g2d.drawString(attribute, x + 10, attrY);
            attrY += 15;
        }
    }

    // Dibujar métodos
    int methY = y + headerHeight + attributesHeight + 15;
    if (methods.isEmpty()) {
        g2d.drawString("métodos", x + 10, methY);
    } else {
        for (String method : methods) {
            g2d.drawString(method, x + 10, methY);
            methY += 15;
        }
    }
}


	
}
