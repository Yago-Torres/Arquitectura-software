
import java.rmi.Naming;
import java.util.Vector;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        try{
            String brokerName = "rmi://155.207.154.209:32000/Broker959";
            IBroker broker = (IBroker) Naming.lookup(brokerName); // CAMBIAR POR EL TIPO BROKER Q HAGAMOS
            Scanner s = new Scanner(System.in);

            while (true) { // Bucle que hace todo lo del cliente, ya hemos comenzado la conexión con el broker, probaremos las distintas opciones implementadas
                 System.out.println("Menú Cliente");
                System.out.println("1. Listar servicios");
                System.out.println("2. Ejecutar servicio (síncrono)");
                System.out.println("3. Ejecutar servicio (asíncrono)");
                System.out.println("4. Obtener respuesta asíncrona");
                System.out.println("5. Salir");
                System.out.print("Introduzca una opción: ");
                
                int opcion = s.nextInt();
                s.nextLine(); // Limpiar el buffer
            
                Vector<Object> param = new Vector<>();
                switch (opcion) {
                    case 1:
                        Services services = broker.lista_services();
                        System.out.print("Servicios en el broker: ");
                        System.out.println(services);
                
                    case 2:
                        System.out.print("Introduzca nombre del servicio a ejecutar: ");
                        String service = s.nextLine();
                        param = new Vector<>();
                        System.out.print("Introduzca los parametros (enter para finalizar): ");
                        while (true) {
                            System.out.print("Parameter: ");
                            String entrada = s.nextLine().trim();
                            if (entrada.isEmpty()) {
                                break;
                            }
                            param.add(entrada);
                        }
                        Answer answer = broker.ejecutar_servicio(service, param);
                        System.out.println("Respuesta del servicio: " + answer);

                    case 3:
                        System.out.print("Introduzca nombre del servicio a ejecutar (async): ");
                        String servicio = s.nextLine();
                        param = new Vector<>();
                        System.out.print("Introduzca los parametros (enter para no introducir más): ");
                        while (true) {
                            System.out.print("Parameter: ");
                            String entrada = s.nextLine().trim();
                            if (entrada.isEmpty()) {
                                break;
                            }
                            param.add(entrada);
                        }
                        try {
                            // broker.ejecutar_servicio_async(service, param); // PARTE EXTRAAAAAAAAAAAA
                            // System.out.println("Se ha enviado solicitud async para " + service + ".");
                        } catch (Exception e) {
                            System.out.println("Error al enviar la solicitud: " + e.getMessage());
                        }
                    case 4:
                        System.out.print("Introduzca nombre del s1rvicio del que obtener respuesta: ");
                        //String service = s.nextLine();
                        // AVANZADO RESPUESTAS ASÍNCRONAS - Answer answer = broker.respuesta_async(service);
                        System.out.print("Aún no imlementado sry");
                    case 5: // Salir del sistema
                        System.out.print("Saliendo del cliente");
                        s.close();
                        break;

                    default:
                        System.out.print("Opción inválida, siga las del menú.");
                }
            }
            
        } catch (Exception e) {
            System.out.println("Error en el Cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }
}