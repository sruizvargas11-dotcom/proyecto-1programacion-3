package una.eif206.logic;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import una.eif206.logic.enums.UsuarioRol;

@XmlAccessorType(XmlAccessType.FIELD)
public class Funcionario extends Usuario {

    private String departamento;

    public Funcionario(String id, String nombre, String departamento) {
        super(id, nombre, id, UsuarioRol.FUNCIONARIO);
        this.departamento = departamento;
    }

    public Funcionario() {
        super();
        this.departamento = "";
    }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String d) { this.departamento = d; }

    @Override
    public String toString() { return nombre; }
}