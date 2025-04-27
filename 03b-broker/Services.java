import java.io.Serializable;
import java.util.Vector;

// Serializable para que pueda convertirse en bytes directamente por java, sirve para poder mandarlo a traves de la red
public class Services implements Serializable {
    private static final long serialVersionUID = 1L;

    private Vector<Service> services;

    public Services() {
        this.services = new Vector<>();
    }

    public void addService(Service service) {
        this.services.add(service);
    }

    public void removeService(Service service) {
        this.services.remove(service);
    }

    public Vector<Service> getServices() {
        return this.services;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Service s : services) {
            sb.append(s.toString() + "\n");
        }
        return sb.toString();
    }
}
