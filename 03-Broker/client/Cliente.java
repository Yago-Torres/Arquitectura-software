package client;
import server.Collection;
import java.rmi.Naming;
// TODO : imports necesarios
public class Cliente {
    public static void main (String[] args) {
    try
    {
        // Paso 1 - Obtener una referencia al objeto servidor creado anteriormente
        // Nombre del host servidor o su IP . Es dónde se buscar á al objeto remoto
        String hostname = "10.1.65.245" ; // se puede usar "IP:puerto "
        Collection server = (Collection)Naming.lookup ("//" + hostname + "/MyCollection" ) ;
        // Paso 2 - Invocar remotamente los metodos del objeto servidor:
        String nombreColeccion = server.name_of_collection();
        int numero_libros = server.number_of_books();
        System.out.println(nombreColeccion + ": " + numero_libros);
        server.name_of_collection("Quemados sus dos libros");
        nombreColeccion = server.name_of_collection();
        System.out.println(nombreColeccion + ": " + numero_libros);
        // TODO : obtener el nombre de la colección y el número de libros
        // TODO : cambiar el nombre de la coleccion y ver que ha funcionado
    }
    catch ( Exception ex ) {
        System.out.println ( ex ) ;
    }
    }
}
