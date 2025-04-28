
import java.lang.reflect.Method;
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
    }

    protected Broker() throws RemoteException {
        servers = new HashMap<>();
        registeredServices = new HashMap<>();
        requests = new HashMap<>();
    }

    @Override
    public void registrar_servidor(String nombre_servidor, String hostRemotoIPPuerto) throws RemoteException {
        System.out.println("Registrando servidor: " + nombre_servidor + " en " + hostRemotoIPPuerto);
        servers.put(nombre_servidor, hostRemotoIPPuerto);
    }

    @Override
    public Answer ejecutar_servicio(String nom_servicio, Vector<Object> parametros_servicio) throws RemoteException {
        System.out.println("Cliente solicita servicio: " + nom_servicio);
        try {
            // Buscar un servicio registrado que coincida en nombre y cantidad (y tipos) de parámetros.
            for (Service s : registeredServices.values()) {
                if (s.getService().equalsIgnoreCase(nom_servicio)
                        && s.getParams().size() == parametros_servicio.size()) {
    
                    // Validar que los tipos de parámetros esperados coincidan
                    boolean match = true;
                    Vector<Object> convertedParams = new Vector<>();
                    // Convertir cada parámetro recibido al tipo que se espera
                    for (int i = 0; i < s.getParams().size(); i++) {
                        String expected = s.getParams().get(i).toLowerCase();
                        Object rawParam = parametros_servicio.get(i);
                        Object converted = null;
                        
                        // Si el parámetro viene como cadena, intentamos convertirlo
                        if (rawParam instanceof String) {
                            String str = ((String) rawParam).trim();
                            try {
                                if (expected.equals("int")) {
                                    converted = Integer.parseInt(str);
                                } else if (expected.equals("bool") || expected.equals("boolean")) {
                                    converted = Boolean.parseBoolean(str);
                                } else if (expected.equals("double")) {
                                    converted = Double.parseDouble(str);
                                } else if (expected.equals("char")) {
                                    if (str.length() == 1) {
                                        converted = str.charAt(0);
                                    } else {
                                        match = false;
                                        break;
                                    }
                                } else if (expected.equals("string")) {
                                    converted = str;
                                } else {
                                    // Si se llegara a dar un tipo no permitido (esto ya se valida en alta_servicio)
                                    converted = str;
                                }
                            } catch (Exception ex) {
                                // Falló la conversión
                                match = false;
                                break;
                            }
                        } else {
                            // Si ya es del tipo correcto se usa tal cual
                            converted = rawParam;
                        }
                        convertedParams.add(converted);
                    }
        
                    if (!match) {
                        continue; // Si la conversión falló, se pasa al siguiente candidato
                    }
                    
                    // Preparar el arreglo de clases para la llamada por reflexión
                    int n = s.getParams().size();
                    Class<?>[] paramTypes = new Class[n];
                    for (int i = 0; i < n; i++) {
                        String tipo = s.getParams().get(i).toLowerCase();
                        if (tipo.equals("int")) {
                            paramTypes[i] = int.class;
                        } else if (tipo.equals("bool") || tipo.equals("boolean")) {
                            paramTypes[i] = boolean.class;
                        } else if (tipo.equals("string")) {
                            paramTypes[i] = String.class;
                        } else if (tipo.equals("char")) {
                            paramTypes[i] = char.class;
                        } else if (tipo.equals("double")) {
                            paramTypes[i] = double.class;
                        } else {
                            paramTypes[i] = Object.class;
                        }
                    }
                    
                    // Buscar el método en el objeto remoto, primero intentando en la clase y luego en sus interfaces.
                    String serverName = s.getServer();
                    String url = servers.get(serverName);
                    System.out.println("URL del servidor: " + url);
                    if (url == null) {
                        return new Answer("Error: Servidor " + serverName + " no registrado.");
                    }
                    Object remoteObj = Naming.lookup(url);
                    Method method = null;
                    try {
                        System.out.println("Buscando en la clase: " + remoteObj.getClass().getName());
                        method = remoteObj.getClass().getMethod(s.getService(), paramTypes);
                    } catch (NoSuchMethodException e) {
                        // Buscar en cada interfaz implementada por el proxy
                        for (Class<?> iface : remoteObj.getClass().getInterfaces()) {
                            try {
                                System.out.println("Buscando en la interfaz: " + iface.getName());
                                method = iface.getMethod(s.getService(), paramTypes);
                                if (method != null) break;
                            } catch (NoSuchMethodException ex) {
                                // Continuar buscando
                            }
                        }
                        if (method == null) {
                            throw new NoSuchMethodException(s.getService() + " con la firma indicada no se encontró.");
                        }
                    }
                    
                    // Invocar el método con los parámetros convertidos
                    Object result = method.invoke(remoteObj, convertedParams.toArray());
                    // Para métodos void, result es null. Podemos devolver un mensaje adecuado.
                    if (result == null) {
                        return new Answer("El método " + nom_servicio + " se ejecutó correctamente (void).");
                    }
                    return new Answer("Resultado de " + nom_servicio + ": " + result);
                }
            }
            return new Answer("Error: Servicio " + nom_servicio + " no encontrado o parámetros no coinciden.");
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
    public void alta_servicio(String nombre_servidor, String nom_servicio, Vector<String> lista_param, String tipo_retorno) throws RemoteException {
       
        StringBuilder signature = new StringBuilder();
        for (String param : lista_param) {
            signature.append(param).append(",");
        }
        String key = nombre_servidor + ":" + nom_servicio + ":" + signature.toString();
        Service servicio = new Service(nom_servicio, nombre_servidor, lista_param, tipo_retorno);
        registeredServices.put(key, servicio);
        System.out.println("Alta de servicio: " + servicio);
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
            System.out.println("Service no registrado: " + nom_servicio);
            return;
        }


        Service removed = registeredServices.remove(nom_servicio);
        System.out.println("Baja de servicio: " + removed);
    }

    @Override
    public void ejecutar_servicio_async(String nom_servicio, Vector<Object> parametros_servicio) throws RemoteException {
        String client;
        try {
            client = RemoteServer.getClientHost();
        } catch (ServerNotActiveException e) {
            throw new RemoteException("Error al obtener el host del cliente", e);
        }
        String key = client + "_" + nom_servicio;

        synchronized (requests) {
            if (requests.containsKey(key)) {
                throw new RemoteException("Ya hay una solicitud asíncrona para este servicio.");
            }
            PendingRequest pendingRequest = new PendingRequest();
            pendingRequest.client = client;
            pendingRequest.answer = null;
            requests.put(key, pendingRequest);
        }

        new Thread(() -> {
            try {
                Answer answer = ejecutar_servicio(nom_servicio, parametros_servicio);
                synchronized (requests) {
                    PendingRequest pendingRequest = requests.get(key);
                    if (pendingRequest != null) {
                        pendingRequest.answer = answer;
                    }
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public Answer obtener_respuesta_async(String nom_servicio) throws RemoteException {
        String client;
        try {
            client = RemoteServer.getClientHost();
        } catch (ServerNotActiveException e) {
            throw new RemoteException("Error al obtener el host del cliente", e);
        }
        String key = client + "_" + nom_servicio;

        synchronized (requests) {
            if (!requests.containsKey(key)) {
                System.out.println("No hay Answer asíncrona para el servicio: " + nom_servicio);
                return new Answer("No hay una solicitud asíncrona para este servicio.");
            }
            PendingRequest pendingRequest = requests.get(key);
            if (pendingRequest == null) {
                return new Answer("Aún no hay Answer.");
            }
            requests.remove(key);
            return pendingRequest.answer;
            
        }
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
            String brokerName = "rmi://localhost:32000/Broker959";
            Naming.rebind(brokerName, broker);
            System.out.println("Broker listo como " + brokerName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }   
}