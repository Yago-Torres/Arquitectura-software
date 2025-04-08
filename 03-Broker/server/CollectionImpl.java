package server;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.Naming;

public class CollectionImpl extends UnicastRemoteObject implements Collection {
    // Private member variables
    private int m_number_of_books;
    private String m_name_of_collection;

    // Constructor
    public CollectionImpl() throws RemoteException {
        super(); // Llama al constructor de UnicastRemoteObject
        m_number_of_books = 2;
        m_name_of_collection = "Biblioteca de Totti";
    }

    public int number_of_books() throws RemoteException {
        return m_number_of_books;
    }

    public String name_of_collection() throws RemoteException {
        return m_name_of_collection;
    }

    public void name_of_collection(String _new_value) {
        m_name_of_collection = _new_value;
    }

    public static void main(String args[]) {
        // Nombre o IP del host donde reside el objeto servidor
        String hostName = "10.1.65.245"; // se puede usar "IPhostremoto : puerto "
        // Por defecto , RMI usa el puerto 1099
        try {
            // Crear objeto remoto
            CollectionImpl obj = new CollectionImpl();
            System.out.println("Creado!");
            // Registrar el objeto remoto
            Naming.rebind("//" + hostName + "/MyCollection", obj);
            System.out.println("Estoy registrado!");
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}