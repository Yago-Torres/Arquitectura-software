import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

public interface IBroker extends Remote {
    void registrar_servidor(String nombre_servidor, String host_remoto_IP_puerto) throws RemoteException;

    Respuesta ejecutar_servicio(String nom_servicio, Vector<Object> parametros_servicio) throws RemoteException;
}
