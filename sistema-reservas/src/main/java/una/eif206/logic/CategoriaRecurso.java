package una.eif206.logic;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlID;

@XmlAccessorType(XmlAccessType.FIELD)
public class CategoriaRecurso {

    @XmlID
    private String id;
    private String nombre;

    public CategoriaRecurso(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public CategoriaRecurso() {
        this("", "");
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() { return nombre; }
}