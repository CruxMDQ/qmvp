package callisto.quotermvp.firebase.model;

/**
 * Created by emiliano.desantis on 31/01/2017.
 */
public class Chamber {

    private String identifier;
    private Double surface;
    private String name;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Double getSurface() {
        return surface;
    }

    public void setSurface(Double surface) {
        this.surface = surface;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Chamber() { }

    public Chamber(Double surface, String name) {
        this.surface = surface;
        this.name = name;
    }

    public Chamber(String identifier, Double surface, String name) {
        this.identifier = identifier;
        this.surface = surface;
        this.name = name;
    }
}
