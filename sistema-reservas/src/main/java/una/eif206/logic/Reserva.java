package una.eif206.logic;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlIDREF;
import una.eif206.logic.enums.EstadoReserva;

@XmlAccessorType(XmlAccessType.FIELD)
public class Reserva {

    private String id;
    @XmlIDREF
    private Funcionario funcionario;
    @XmlIDREF
    private Recurso recurso;
    private String fechaInicio;
    private String fechaFin;
    private EstadoReserva estado;

    public Reserva(String id, Funcionario funcionario, Recurso recurso,
                   String fechaInicio, String fechaFin) {
        this.id = id;
        this.funcionario = funcionario;
        this.recurso = recurso;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = EstadoReserva.ACTIVA;
    }

    public Reserva() {
        this.id = "";
        this.estado = EstadoReserva.ACTIVA;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Funcionario getFuncionario() { return funcionario; }
    public void setFuncionario(Funcionario f) { this.funcionario = f; }
    public Recurso getRecurso() { return recurso; }
    public void setRecurso(Recurso r) { this.recurso = r; }
    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String f) { this.fechaInicio = f; }
    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String f) { this.fechaFin = f; }
    public EstadoReserva getEstado() { return estado; }
    public void setEstado(EstadoReserva e) { this.estado = e; }
}