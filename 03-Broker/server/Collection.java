package server;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Collection extends Remote
{
// M é todos de la interfaz
int number_of_books() throws RemoteException;
String name_of_collection() throws RemoteException ;
void name_of_collection(String _new_value) throws RemoteException ;
}
