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
	
	private Vector classes = new Vector(); //las clases que crea el usuario
	private Vector associations = new Vector(); // las asociaciones que crea el usuario
	
	// ... (otros posibles atributos)


	//metodos
	public Diagram(Window theWindow) {
		window = theWindow;
		
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		
		setBorder(BorderFactory.createLineBorder(Color.black));
	}
	
	public void addClass() {
		//Añade una clase al diagrama
	}
	
	public int getNClasses(){
		//Devuelve el número de clases
	}
	
	public int getNAssociations(){
		//Devuelve el numero de asociaciones
	}

	public void paint(Graphics g) {
		//Dibuja el diagrama de clases
	}
	
	/********************************************/
	/** MÈtodos de MouseListener               **/
	/********************************************/

	public void mousePressed(MouseEvent e) {
		//…
   	 }
    
    	public void mouseReleased(MouseEvent e) {
 		//…		
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
    
	public void mouseDragged(MouseEvent e) {
		//…
	}
    
	/********************************************/
	/** MÈtodos de KeyListener                 **/
	/********************************************/

	public void keyTyped(KeyEvent e) {
    	}
    
	public void keyPressed(KeyEvent e) {
		//…
	}
    
    	public void keyReleased(KeyEvent e) {
    	}
}
