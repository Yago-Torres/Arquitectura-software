
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
            String serverName = "rmi://localhost:32000/ServerB959";
            //System.setProperty("java.rmi.server.hostname","155.210.154.207");
            Naming.rebind(serverName, servidorA);
            System.out.println("Servidor B listo como " + serverName);

            String broker = "rmi://localhost:32000/Broker959";
            IBroker brokerInterface= (IBroker) Naming.lookup(broker);
            brokerInterface.registrar_servidor("ServerB", serverName);
            System.out.println("Servidor B registrado en el broker ( a mano )");

            Scanner s = new Scanner(System.in);

            // Comienzo de un mini menu que pregunta que hacer, lo mejor para probarlo de forma interactiva y visual
            while (true) {
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
                        
                        while (true) {
                        System.out.print("Ingrese el tipo de parámetro (int, bool, string, char, double) o presione enter para finalizar: ");
                        String tipoParam = s.nextLine().trim();
                        if (tipoParam.isEmpty()) {
                            break;
                        }
                        param.add(tipoParam);
                        }
                        System.out.print("Ingrese el tipo de retorno (int, bool, string, char, double o void): ");
                        String tipoRetorno = s.nextLine();
                        brokerInterface.alta_servicio("ServerB", nomServicio, param, tipoRetorno);
                        System.out.println("Servicio '" + nomServicio + "' dado de alta.");
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
            System.err.println("Error en el servidor B: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
