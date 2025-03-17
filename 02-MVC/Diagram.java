import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Diagram 
		extends JPanel 
		implements MouseListener, 
			   MouseMotionListener, 
			   KeyListener {
	
	//atributos
	private Window window;//Ventana en la que está el diagrama
	public Class clase; 
	
	private Vector<Class> classes = new Vector<Class>(); //las clases que crea el usuario
	private Vector<Association> associations = new Vector<>(); // las asociaciones que crea el usuario

	private int mouseX, mouseY;  // Variables to store the mouse position when a class is selected
	private Class lastSelectedClass = null; // To store the last selected class

	private boolean isDraggingAssociation = false;
	private int dragX, dragY; // Where the user is dragging the line
	private Class associationSource = null; // Class where the drag starts

	private Class targetHover = null; // Clase sobre la que pasamos durante la asociación



	//metodos
	public Diagram(Window theWindow) {
		window = theWindow;
		
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		setFocusable(true);
		setBorder(BorderFactory.createLineBorder(Color.black));
	}
	
	public void addClass() {
		Class newClass = new Class("New Class", 100, 100);  // You can modify this to allow user input for name and position
    	classes.add(newClass);  // Add the new Class to the list of classes

 	   	// Optionally, update the label showing the number of classes
	    window.updateNClasses(this);

	    // Repaint the diagram to show the new class
    	repaint();
	}
	
	public int getNClasses(){
		//Devuelve el número de clases
		return classes.size();
	}
	
	public int getNAssociations(){
		return associations.size();
	}

@Override
public void paint(Graphics g) {
    super.paint(g);

    // Dibujar asociaciones
    for (Association assoc : associations) {
        assoc.draw(g);
    }

    // Dibujar línea temporal mientras arrastramos asociación
    if (isDraggingAssociation && associationSource != null) {
        g.setColor(Color.GREEN);
        g.drawLine(associationSource.getX() + 50, associationSource.getY() + 50, dragX, dragY);
    }

    // Dibujar las clases
    for (Class c : classes) {
        if (c == targetHover) {
            // Borde verde si es la clase sobre la que pasamos
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.GREEN);
            g2.setStroke(new BasicStroke(3));
            g2.drawRect(c.getX(), c.getY(), 100, 100);
            g2.setStroke(new BasicStroke(1)); // Reset stroke
        }
        c.draw(g);
    }
}

	/********************************************/
	/** MÈtodos de MouseListener               **/
	/********************************************/

	public void mousePressed(MouseEvent e) {
    if (e.getButton() == MouseEvent.BUTTON1) {

		boolean clickedOnClass = false;

		for (Class c : classes) {
        if (e.getX() > c.getX() && e.getX() < c.getX() + 100 &&
                e.getY() > c.getY() && e.getY() < c.getY() + 100) {

                clickedOnClass = true;

                if (c.isSelected()) {
                    // START ASSOCIATION DRAG
                    associationSource = c;
                    isDraggingAssociation = true;
                    dragX = e.getX();
                    dragY = e.getY();
                } else {
                    // START CLASS MOVE
                    clase = c;
                    mouseX = e.getX();
                    mouseY = e.getY();
                }
                break; // Stop after finding first matching class
            }
    		}

		if (!clickedOnClass) {
            // Clicked on empty space, reset dragging state if needed
            clase = null;
            associationSource = null;
            isDraggingAssociation = false;
        }

        
    } else if (e.getButton() == MouseEvent.BUTTON3) {
    // Right click - Delete the class
    Iterator<Class> classIter = classes.iterator();
    while (classIter.hasNext()) {
        Class c = classIter.next();
        if (e.getX() > c.getX() && e.getX() < c.getX() + 100 &&
            e.getY() > c.getY() && e.getY() < c.getY() + 100) {

            // Borrar asociaciones que tienen a 'c'
            associations.removeIf(a -> a.from == c || a.to == c);

            // Eliminar la clase
            classIter.remove();

            window.updateNClasses(this);
            window.updateNAssociations(this);

            repaint();
            break;
        }
    }
}
}

		
   	
    
@Override
public void mouseReleased(MouseEvent e) {
    if (isDraggingAssociation && associationSource != null) {
        for (Class target : classes) {
            if (e.getX() > target.getX() && e.getX() < target.getX() + 100 &&
                e.getY() > target.getY() && e.getY() < target.getY() + 100) {
                associations.add(new Association(associationSource, target));
                window.updateNAssociations(this);
                break;
            }
        }
        isDraggingAssociation = false;
    	associationSource.toggleSelection(); 
    	lastSelectedClass = null; 
    	associationSource = null;
    	targetHover = null;
    	repaint();
    }
    clase = null;
}


	    public void mouseEntered(MouseEvent e) {
    	}
    
	public void mouseExited(MouseEvent e) {
    	}
    
	public void mouseClicked(MouseEvent e) {
    	}

	/********************************************/
	/** MÈtodos de MouseMotionListener         **/
	/********************************************/    
    	public void mouseMoved(MouseEvent e) {
		//…
	}

@Override
public void mouseDragged(MouseEvent e) {
    if (isDraggingAssociation && associationSource != null) {
        dragX = e.getX();
        dragY = e.getY();

        // Detectar si estamos sobre otra clase mientras arrastramos
        targetHover = null;
        for (Class c : classes) {
            if (c != associationSource &&
                e.getX() > c.getX() && e.getX() < c.getX() + 100 &&
                e.getY() > c.getY() && e.getY() < c.getY() + 100) {
                targetHover = c;
                break;
            }
        }
        repaint();
    } 
    else if (clase != null) {
        // Drag/move la clase
        int dx = e.getX() - mouseX;
        int dy = e.getY() - mouseY;
        clase.setX(clase.getX() + dx);
        clase.setY(clase.getY() + dy);
        mouseX = e.getX();
        mouseY = e.getY();
        repaint();
    }
}


    
	/********************************************/
	/** MÈtodos de KeyListener                 **/
	/********************************************/

	public void keyTyped(KeyEvent e) {
    	}
    
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_S) {
            boolean found = false;
            for (Class c : classes) {
                if (mouseX > c.getX() && mouseX < c.getX() + 100 &&
                    mouseY > c.getY() && mouseY < c.getY() + 100) {
                    if (c.isSelected()) {
                        // Deseleccionar la clase
                        c.toggleSelection();
                        lastSelectedClass = null;  // Si se deselecciona, no hay última clase seleccionada
                    } else {
                        // Si no está seleccionada, seleccionamos esta clase
                        if (lastSelectedClass != null) {
                            lastSelectedClass.toggleSelection(); // Deseleccionamos la clase previamente seleccionada
                        }
                        c.toggleSelection(); // Seleccionamos la nueva clase
                        lastSelectedClass = c; // Guardamos la clase seleccionada
                    }
                    found = true;
                    repaint();
                    break;
                }
            }
            if (!found && lastSelectedClass != null) {
                // Si no estamos sobre ninguna clase, deseleccionamos la última clase
                lastSelectedClass.toggleSelection();
                lastSelectedClass = null;
                repaint();
            }
        }
		//…
	}
    
    	public void keyReleased(KeyEvent e) {
    	}
}
