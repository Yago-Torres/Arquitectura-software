import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IServidorB extends Remote{
    int numeroMagico() throws RemoteException;
}
