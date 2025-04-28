import java.io.Serializable;
import java.util.Vector;


// Serializable para que pueda convertirse en bytes directamente por java, sirve para poder mandarlo a traves de la red
public class Service implements Serializable {
    private String nombre;
    private String server;
    private Vector<String> params;
    private String tipoDevuelto;

    public Service(String nombre, String server, Vector<String> params, String tipoDevuelto) {
        this.nombre = nombre;
        this.server = server;
        this.params = params;
        this.tipoDevuelto = tipoDevuelto;
    }

    public String getService() {
        return nombre;
    }

    public String getServer() {
        return server;
    }

    
    public Vector<String> getParams() {
        return params;
    }

    public String getTipoDevuelto() {
        return tipoDevuelto;
    }
    

    @Override
    public String toString() {
        return "Servidor: " + server + ", Servicio: " + nombre + ", Parámetros: " + params + ", Tipo de Retorno: " + tipoDevuelto;
    }
  }
