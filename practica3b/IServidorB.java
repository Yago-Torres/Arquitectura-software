import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IServidorB extends Remote{
    int numeroMagico(int a) throws RemoteException;
    int multiplicacion(int a, int b) throws RemoteException;
}
