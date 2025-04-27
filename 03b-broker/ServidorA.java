import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServidorA extends UnicastRemoteObject implements IServidorA{
    
    public ServidorA() throws RemoteException {
        super();
    }
    
    @Override
    public String tralalero() {
        return "tralala";
    }

    public static void main(String[] args) {
        try {
            IServidorA servidorA = new ServidorA();
            String serverName = "rmi://155.210.154.210/ServidorA959";
            Naming.rebind(serverName, servidorA);
            System.out.println("Servidor A listo como " + serverName);

            String broker = "rmi://155.210.154.209/Broker959";
        } catch (Exception e) {
            System.err.println("Error en el servidor A: " + e.getMessage());
        }
    }
}
