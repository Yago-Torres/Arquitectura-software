import java.rmi.Naming;
import java.util.Vector;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        try{
            String brokerName = "rmi://IPponeraquui";
            IBroker broker = (IBroker) Naming.lookup(brokerName); // CAMBIAR POR EL TIPO BROKER Q HAGAMOS
            Scanner sc = new Scanner(System.in);

            while (true) { // Bucle que hace todo lo del cliente, ya hemos comenzado la conexión con el broker, probaremos las distintas opciones implementadas
            
            
            
            }
        } catch (Exception e) {
            System.out.println("Error en el Cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }
}