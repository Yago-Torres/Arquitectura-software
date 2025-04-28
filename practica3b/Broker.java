
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
    System.out.println("\n=== [DEBUG] Inicio ejecutar_servicio ===");
    System.out.println("[DEBUG] Servicio solicitado: " + nom_servicio);
    System.out.println("[DEBUG] Parámetros recibidos: " + parametros_servicio);
    
    try {
        System.out.println("\n[DEBUG] Servicios registrados:");
        registeredServices.values().forEach(s -> System.out.println(
            " - " + s.getService() + " (params: " + s.getParams() + ") -> " + s.getServer()));
        
        for (Service s : registeredServices.values()) {
            System.out.println("\n[DEBUG] Comparando con servicio registrado: " + s.getServer());
            System.out.println("[DEBUG] Coincidencia nombre: " + s.getServer().equalsIgnoreCase(nom_servicio));
            System.out.println("[DEBUG] Coincidencia params count: " + (s.getParams().size() == parametros_servicio.size()));
            
            if (s.getServer().trim().equalsIgnoreCase(nom_servicio.trim())
                    && s.getParams().size() == parametros_servicio.size()) {
                
                System.out.println("[DEBUG] ¡Coincidencia inicial encontrada!");
                boolean match = true;
                Vector<Object> convertedParams = new Vector<>();
                
                // Debug de conversión de parámetros
                System.out.println("\n[DEBUG] Procesando parámetros:");
                for (int i = 0; i < s.getParams().size(); i++) {
                    String expected = s.getParams().get(i).toLowerCase();
                    Object rawParam = parametros_servicio.get(i);
                    
                    System.out.println("\n[DEBUG] Parámetro " + i);
                    System.out.println("[DEBUG] Esperado: " + expected);
                    System.out.println("[DEBUG] Recibido: " + rawParam + " (tipo: " + rawParam.getClass().getSimpleName() + ")");
                    
                    Object converted = null;
                    
                    if (rawParam instanceof String) {
                        String str = ((String) rawParam).trim();
                        System.out.println("[DEBUG] Intentando conversión de String: '" + str + "'");
                        
                        try {
                            if (expected.equals("int")) {
                                converted = Integer.parseInt(str);
                                System.out.println("[DEBUG] Conversión exitosa a int: " + converted);
                            } else if (expected.equals("bool") || expected.equals("boolean")) {
                                converted = Boolean.parseBoolean(str);
                                System.out.println("[DEBUG] Conversión exitosa a boolean: " + converted);
                            } else if (expected.equals("double")) {
                                converted = Double.parseDouble(str);
                                System.out.println("[DEBUG] Conversión exitosa a double: " + converted);
                            } else if (expected.equals("char")) {
                                if (str.length() == 1) {
                                    converted = str.charAt(0);
                                    System.out.println("[DEBUG] Conversión exitosa a char: '" + converted + "'");
                                } else {
                                    System.out.println("[DEBUG] ERROR: Longitud incorrecta para char: " + str.length());
                                    match = false;
                                    break;
                                }
                            } else if (expected.equals("string")) {
                                converted = str;
                                System.out.println("[DEBUG] Usando String directamente");
                            } else {
                                converted = str;
                                System.out.println("[DEBUG] Tipo no reconocido, usando como String");
                            }
                        } catch (Exception ex) {
                            System.out.println("[DEBUG] ERROR en conversión: " + ex.getMessage());
                            match = false;
                            break;
                        }
                    } else {
                        System.out.println("[DEBUG] Parámetro NO es String, usando directamente");
                        converted = rawParam;
                    }
                    convertedParams.add(converted);
                }

                if (!match) {
                    System.out.println("[DEBUG] ❌ Conversión de parámetros falló, continuando...");
                    continue;
                }
                
                System.out.println("\n[DEBUG] Parámetros convertidos: " + convertedParams);
                
                // Preparar tipos de parámetros
                Class<?>[] paramTypes = new Class[s.getParams().size()];
                System.out.println("\n[DEBUG] Tipos de parámetros para reflexión:");
                for (int i = 0; i < paramTypes.length; i++) {
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
                    System.out.println(" - Param " + i + ": " + paramTypes[i].getName());
                }

                // Buscar servidor
                String serverName = s.getService();
                System.out.println("\n[DEBUG] Buscando servidor: " + serverName);
                String url = servers.get(serverName);
                if (url == null) {
                    System.out.println("[DEBUG] ❌ Servidor no encontrado en: " + servers);
                    return new Answer("Error: Servidor " + serverName + " no registrado.");
                }
                System.out.println("[DEBUG] URL del servidor: " + url);

                // Buscar método
                System.out.println("\n[DEBUG] Buscando método: " + s.getServer());
                System.out.println("[DEBUG] Tipos de parámetros: " + Arrays.toString(paramTypes));
                
                Object remoteObj = Naming.lookup(url);
                System.out.println("[DEBUG] Objeto remoto obtenido: " + remoteObj.getClass().getName());
                
                Method method = null;
                try {
                    method = remoteObj.getClass().getMethod(s.getServer(), paramTypes);
                    System.out.println("[DEBUG] Método encontrado en clase principal");
                } catch (NoSuchMethodException e) {
                    System.out.println("[DEBUG] ❌ Método no encontrado en clase principal, buscando en interfaces...");
                    for (Class<?> iface : remoteObj.getClass().getInterfaces()) {
                        System.out.println("[DEBUG] Buscando en interfaz: " + iface.getName());
                        try {
                            method = iface.getMethod(s.getService(), paramTypes);
                            if (method != null) {
                                System.out.println("[DEBUG] ✔️ Método encontrado en interfaz: " + iface.getName());
                                break;
                            }
                        } catch (NoSuchMethodException ex) {
                            System.out.println("[DEBUG] ❌ No encontrado en " + iface.getName());
                        }
                    }
                    if (method == null) {
                        System.out.println("[DEBUG] ❌❌ Método no encontrado en ninguna interfaz");
                        throw new NoSuchMethodException(s.getServer() + " con la firma indicada no se encontró.");
                    }
                }

                // Ejecutar método
                System.out.println("\n[DEBUG] Invocando método con parámetros: " + convertedParams);
                Object result = method.invoke(remoteObj, convertedParams.toArray());
                System.out.println("[DEBUG] Resultado obtenido: " + result);

                if (result == null) {
                    System.out.println("[DEBUG] Método void ejecutado exitosamente");
                    return new Answer("El método " + nom_servicio + " se ejecutó correctamente (void).");
                }
                
                System.out.println("[DEBUG] ✔️ Servicio ejecutado con éxito");
                return new Answer("Resultado de " + nom_servicio + ": " + result);
            }
        }
        
        System.out.println("\n[DEBUG] ❌❌ No se encontró servicio compatible");
        return new Answer("Error: Service " + nom_servicio + " no encontrado o parámetros no coinciden.");
        
    } catch (Exception e) {
        System.out.println("\n[DEBUG] ❌❌ EXCEPCIÓN: " + e.getMessage());
        e.printStackTrace();
        return new Answer("Error al ejecutar el servicio: " + e.getMessage());
    } finally {
        System.out.println("=== [DEBUG] Fin ejecutar_servicio ===\n");
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
        
        Service servicio = new Service(nombre_servidor, nom_servicio, lista_param, tipo_retorno);
        registeredServices.put(nom_servicio, servicio);
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
            String brokerName = "rmi://155.210.154.209:32000/Broker959";
            Naming.rebind(brokerName, broker);
            System.out.println("Broker listo como " + brokerName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }   
}