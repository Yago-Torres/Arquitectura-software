
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


public class Broker extends UnicastRemoteObject implements IBroker {// pq unicastremote? pq yo lo digo

// map of servers, registered services, and pending requests.
// The keys will be as follows:
    private Map<String, String> servers;
    private Map<String, Servicio> registeredServices;
    private Map<String, PendingRequest> requests;

    // Esto pa la version avanzada
    private static class PendingRequest {
        String client;
        Respuesta answer;
        boolean recieved; // Really needed if we have an answer?
    }

    protected Broker() throws RemoteException {
        servers = new HashMap<>();
        registeredServices = new HashMap<>();
        requests = new HashMap<>();
    }

    @Override
    public void registrar_servidor(String nombre_servidor, String host_remoto_IP_puerto) throws RemoteException {
        servers.put(nombre_servidor, host_remoto_IP_puerto);
        System.out.println("Servidor " + nombre_servidor + " registrado en el broker.");
    }

    @Override
    public Respuesta ejecutar_servicio(String service, Vector<Object> service_params) throws RemoteException {
        try {
            for(Serrvicio s : registeredServices.values()){
                if(s.getName().equals(service)){
                    // We have a service, now we need to find the server
                    String server = servers.get(s.getServer());
                    if(server != null){
                        // We have a server, now we need to execute the service
                        IServidorA servidor = (IServidorA) Naming.lookup(server);
                        return servidor.tralalero();
                    } else {
                        System.out.println("Servidor no encontrado.");
                    }
                }
            }
        }
    }

}