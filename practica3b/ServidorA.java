import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.Vector;

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
            String serverName = "rmi://localhost:32000/ServidorA959";
            //System.setProperty("java.rmi.server.hostname","155.210.154.207");
            Naming.rebind(serverName, servidorA);
            System.out.println("Servidor A listo como " + serverName);

            String broker = "rmi://localhost:32000/Broker959";
            IBroker brokerInterface= (IBroker) Naming.lookup(broker);
            brokerInterface.registrar_servidor("ServerA", serverName);
            System.out.println("Servidor A registrado en el broker ( a mano )");

            Scanner s = new Scanner(System.in);

            // Comienzo de un mini menu que pregunta que hacer, lo mejor para probarlo de forma interactiva y visual
            while (true) {
                // Gestionar le menu aquí sin q se parezca en exceso al de mis colegones
                System.out.println("Menú ServidorA");
                System.out.println("1. Dar de alta un servicio");
                System.out.println("2. Dar de baja un servicio");
                System.out.println("3. Salir");
                System.out.print("Introduzca una opción: ");

                int opcion = s.nextInt();
                s.nextLine();
                switch (opcion) {
                    case 1: // Añadir servicio
                        System.out.print("Introduzca el servicio a añadir: ");
                        String nomServicio = s.nextLine();
                        Vector<String> param = new Vector<>();
                        

                        System.out.print("Introduzca parametros de entrada (int, bool, string, char, double). Enter para terminar:");
                        // Lee parametros hasta q hay uno vacío
                        String tipoParam;
                        while (!(tipoParam = s.nextLine().trim()).isEmpty()) {
                            param.add(tipoParam);
                            System.out.println("Siga introduciendo tipos de parámetros (int, bool, string, char, double). Enter para terminar:");
                        }
                        System.out.print("Introduzca tipo de retorno de la función (int, bool, string, char, double o void)");
                        String tipoRetorno = s.nextLine().trim();
                        brokerInterface.alta_servicio("ServerA", nomServicio, param, tipoRetorno);
                        System.out.println("Servicio '" + nomServicio + "' añadido con exito.");
                        break;
                    case 2: // Eliminar servicio ya registrado
                        System.out.print("Introduzca el nombre del servicio a eliminar: ");
                        String Servicio = s.nextLine();
                        brokerInterface.baja_servicio("ServerA", Servicio);
                        System.out.println("Servicio '" + Servicio + "'dado de baja.");
                        break;
                    case 3: // Saliendo
                        System.out.print("Saliendo del menú de ServerA");
                        s.close();
                        break;
                    default:
                        System.out.println("Opción inválida, por favor escoja según el menú");
                }
            }

        } catch (Exception e) {
            System.err.println("Error en el servidor A: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
