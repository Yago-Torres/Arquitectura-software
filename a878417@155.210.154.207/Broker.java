
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;


public class Broker extends UnicastRemoteObject implements IBroker {

// map of servers, registered services, and pending requests.
// The keys will be as follows:
    private Map<String, String> servers;
    private Map<String, Service> registeredServices;
    private Map<String, PendingRequest> requests;

    // Esto pa la version avanzada
    private static class PendingRequest {
        String client;
        Answer answer;
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
    public Answer ejecutar_servicio(String service, Vector<Object> service_params) throws RemoteException {
        try {
            for(Service s : registeredServices.values()){
                if(s.getService().equals(service)){
                    // We have a service, now we need to find the server
                    String server = servers.get(s.getServer());
                    if(server != null){
                        // We have a server, now we need to execute the service
                        IServidorA servidor = (IServidorA) Naming.lookup(server);
                        return new Answer("Reslutado de " + server + ": " + servidor.tralalero());
                    } else {
                        return new Answer("Servidor no encontrado.");
                    }
                }
            }
            return new Answer("Servicio no encontrado: " + service);
        } catch (Exception e) {
            e.printStackTrace();
            return new Answer("Error al ejecutar el servicio: " + e.getMessage());
        }
    }

    @Override
    public Services lista_services() throws RemoteException {
        Services res = new Services();
        for (Service s : registeredServices.values()) {
            res.addService(s);
        }
        return res;
    }


    @Override
    public void alta_servicio(String nombre_servidor, String nom_servicio, Vector<String> parametros_servicio, String tipo_retorno) throws RemoteException {
        // Comprobamos que el servidor está registrado
        if (!servers.containsKey(nombre_servidor)) {
            System.out.println("Servidor no registrado: " + nombre_servidor);
            return;
        }

        // Comprobamos que los tipos de los parámetros son válidos
        for (Object param : parametros_servicio) {
            if (!isAllowedType(param.toString(), false)) {
                System.out.println("Tipo de parámetro no válido: " + param);
                return;
            }
        }

        // Comprobamos que el tipo de retorno es válido
        if (!isAllowedType(tipo_retorno, true)) {
            System.out.println("Tipo de retorno no válido: " + tipo_retorno);
            return;
        }

        StringBuilder params = new StringBuilder();
        for (String paramString : parametros_servicio) {
            params.append(paramString).append(", ");
        }

        String serviceSignature = nom_servicio + "(" + params.toString() + ") -> " + tipo_retorno;

        // Registramos el servicio
        Service service = new Service(nom_servicio, nombre_servidor, parametros_servicio, tipo_retorno);
        registeredServices.put(nom_servicio, service);
        System.out.println(registeredServices);
        System.out.println("Servicio " + nom_servicio + " registrado en el broker.");
    }

    @Override
    public void baja_servicio(String nombre_servidor, String nom_servicio) throws RemoteException {

        // Comprobamos que el servidor está registrado
        if (!servers.containsKey(nombre_servidor)) {
            System.out.println("Servidor no registrado: " + nombre_servidor);
            return;
        }

        // Comprobamos que el servicio no está ya registrado
        if (!registeredServices.containsKey(nom_servicio)) {
            System.out.println("Servicio no registrado: " + nom_servicio);
            return;
        }


        Service removed = registeredServices.remove(nom_servicio);
        System.out.println("Baja de servicio: " + removed);
    }


    // Valida si un tipo es válido, como es distinto como param y como return value usamos un boolean para diferenciar casos
    private boolean isAllowedType(String tipo, boolean esRetorno) {
        if (tipo == null) {
            return false;
        }
        Set<String> allowedTypes = new HashSet<>(Arrays.asList(
            "int", "bool", "boolean", "string", "char", "double"
        ));
        if (esRetorno) {
            allowedTypes.add("void");
        }
        return allowedTypes.contains(tipo.toLowerCase());
    }

    public static void main(String[] args) {
        try {
            Broker broker = new Broker();
            String brokerName = "rmi://155.207.154.209:32000/Broker959";
            Naming.rebind(brokerName, broker);
            System.out.println("Broker listo como " + brokerName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }   
}