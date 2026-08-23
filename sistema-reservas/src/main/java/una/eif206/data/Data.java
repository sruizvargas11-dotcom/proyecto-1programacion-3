package una.eif206.data;

import jakarta.xml.bind.annotation.*;
import una.eif206.logic.Admin;
import una.eif206.logic.Funcionario;
import una.eif206.logic.CategoriaRecurso;
import una.eif206.logic.Recurso;
import una.eif206.logic.Reserva;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
public class Data {

    @XmlElementWrapper(name = "admins")
    @XmlElement(name = "admin")
    private List<Admin> admins;

    @XmlElementWrapper(name = "funcionarios")
    @XmlElement(name = "funcionario")
    private List<Funcionario> funcionarios;

    @XmlElementWrapper(name = "categorias")
    @XmlElement(name = "categoria")
    private List<CategoriaRecurso> categorias;

    @XmlElementWrapper(name = "recursos")
    @XmlElement(name = "recurso")
    private List<Recurso> recursos;

    @XmlElementWrapper(name = "reservas")
    @XmlElement(name = "reserva")
    private List<Reserva> reservas;

    public Data() {
        admins = new ArrayList<>();
        admins.add(new Admin("ADM001", "admin123"));

        funcionarios = new ArrayList<>();
        funcionarios.add(new Funcionario("F001", "Juan Pérez", "TI"));
        funcionarios.add(new Funcionario("F002", "María López", "RRHH"));

        categorias = new ArrayList<>();
        recursos = new ArrayList<>();
        reservas = new ArrayList<>();
    }

    public List<Admin> getAdmins() { return admins; }
    public List<Funcionario> getFuncionarios() { return funcionarios; }
    public List<CategoriaRecurso> getCategorias() { return categorias; }
    public List<Recurso> getRecursos() { return recursos; }
    public List<Reserva> getReservas() { return reservas; }
}