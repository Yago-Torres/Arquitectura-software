
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.Vector;

public class ServidorB extends UnicastRemoteObject implements IServidorB{
    
    public ServidorB() throws RemoteException {
        super();
    }
    
    @Override
    public int multiplicacion(int a, int b) throws RemoteException {
        return a * b;
    }

    @Override
    public int numeroMagico(int a) throws RemoteException {
        return a * 42;
    }

    public static void main(String[] args) {
        try {
            IServidorB servidorA = new ServidorB();
            String serverName = "rmi://155.210.154.207:32000/ServerB959";
            //System.setProperty("java.rmi.server.hostname","155.210.154.207");
            Naming.rebind(serverName, servidorA);
            System.out.println("Servidor A listo como " + serverName);

            String broker = "rmi://155.210.154.209:32000/Broker959";
            IBroker brokerInterface= (IBroker) Naming.lookup(broker);
            brokerInterface.registrar_servidor("ServerB", serverName);
            System.out.println("Servidor B registrado en el broker ( a mano )");

            Scanner s = new Scanner(System.in);

            // Comienzo de un mini menu que pregunta que hacer, lo mejor para probarlo de forma interactiva y visual
            while (true) {
                // Gestionar le menu aquí sin q se parezca en exceso al de mis colegones
                System.out.println("Menú ServerB");
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
                            System.out.print("Siga con parámetros de entrada (int, bool, string, char, double). Enter para terminar:");
                        }
                        System.out.print("Introduzca tipo de retorno de la función (int, bool, string, char, double o void)");
                        String tipoRetorno = s.nextLine().trim();
                        brokerInterface.alta_servicio("ServerB", nomServicio, param, tipoRetorno);
                        System.out.println("Servicio '" + nomServicio + "' añadido con exito.");
                        break;
                    case 2: // Eliminar servicio ya registrado
                        System.out.print("Introduzca el nombre del servicio a eliminar: ");
                        String Servicio = s.nextLine();
                        brokerInterface.baja_servicio("ServerB", Servicio);
                        System.out.println("Servicio '" + Servicio + "'dado de baja.");
                        break;
                    case 3: // Saliendo
                        System.out.print("Saliendo del menú de ServerB");
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
