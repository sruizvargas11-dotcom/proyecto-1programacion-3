package una.eif206.logic;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlID;
import jakarta.xml.bind.annotation.XmlIDREF;

@XmlAccessorType(XmlAccessType.FIELD)
public class Recurso {

    @XmlID
    private String id;
    private String nombre;
    @XmlIDREF
    private CategoriaRecurso categoria;

    public Recurso(String id, String nombre, CategoriaRecurso categoria) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
    }

    public Recurso() {
        this.id = "";
        this.nombre = "";
        this.categoria = null;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public CategoriaRecurso getCategoria() { return categoria; }
    public void setCategoria(CategoriaRecurso c) { this.categoria = c; }

    @Override
    public String toString() { return nombre; }
}