import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

public interface IBroker extends Remote {
    void registrar_servidor(String nombre_servidor, String host_remoto_IP_puerto) throws RemoteException;

    Answer ejecutar_servicio(String nom_servicio, Vector<Object> parametros_servicio) throws RemoteException;

    Services lista_services() throws RemoteException;

    void alta_servicio(String nombre_servidor, String nom_servicio, Vector<String> parametros_servicio, String tipo_retorno) throws RemoteException;

    void baja_servicio(String nombre_servidor, String nom_servicio) throws RemoteException;

    void ejecutar_servicio_async(String nom_servicio, Vector<Object> parametros_servicio) throws RemoteException;
    Answer obtener_respuesta_async(String nom_servicio) throws RemoteException;
}
