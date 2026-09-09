package una.eif206.controller;

import una.eif206.logic.CategoriaRecurso;
import una.eif206.logic.Funcionario;
import una.eif206.logic.Recurso;
import una.eif206.logic.Reserva;
import una.eif206.logic.Service;
import una.eif206.logic.Usuario;
import una.eif206.logic.enums.EstadoReserva;
import una.eif206.logic.enums.UsuarioRol;
import una.eif206.model.ReservasModel;
import una.eif206.util.ReservaExtraccion;
import una.eif206.util.Sesion;
import una.eif206.view.ReservasView;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReservasController {

    ReservasView view;
    ReservasModel model;

    public ReservasController(ReservasView view, ReservasModel model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
        model.setCategorias(Service.instance().findAllCategorias());
        cargarLista();
    }

    public void cargarLista() {
        Usuario usuario = Sesion.getUsuario();
        if (usuario.getRol() == UsuarioRol.ADMIN) {
            model.setList(Service.instance().findAllReservas());
        } else {
            model.setList(Service.instance().findReservasByFuncionario((Funcionario) usuario));
        }
    }

    public Reserva create(String actividad, String fecha, String horaInicio,
                          String horaFin, List<CategoriaRecurso> categoriasSeleccionadas) throws Exception {
        if (!(Sesion.getUsuario() instanceof Funcionario)) {
            throw new Exception("Solo un funcionario puede crear reservas");
        }
        Funcionario funcionario = (Funcionario) Sesion.getUsuario();

        List<Recurso> recursos = new ArrayList<>();
        for (CategoriaRecurso categoria : categoriasSeleccionadas) {
            Recurso disponible = buscarRecursoDisponible(categoria, fecha, horaInicio, horaFin);
            if (disponible == null) {
                throw new Exception(
                        "No hay recurso disponible de la categoria: " + categoria.getDescripcion());
            }
            recursos.add(disponible);
        }

        Reserva reserva = new Reserva(Service.instance().generarIdReserva(), actividad,
                fecha, horaInicio, horaFin, funcionario);
        reserva.setRecursos(recursos);

        Service.instance().createReserva(reserva);
        model.setCurrent(new Reserva());
        cargarLista();
        return reserva;
    }

    public void cancelar(Reserva r) throws Exception {
        Service.instance().cancelarReserva(r);
        model.setCurrent(new Reserva());
        cargarLista();
    }

    public void clear() {
        model.setCurrent(new Reserva());
    }

    public ReservaExtraccion extraerConIA(String frase) throws Exception {
        return Service.instance().extraerReserva(frase);
    }

    public void edit(int row) {
        model.setCurrent(model.getList().get(row));
    }

    private Recurso buscarRecursoDisponible(CategoriaRecurso categoria, String fecha,
                                            String horaInicio, String horaFin) {
        for (Recurso recurso : Service.instance().findAllRecursos()) {
            if (!categoria.equals(recurso.getCategoria())) {
                continue;
            }
            if (!recurso.isDisponible()) {
                continue;
            }
            boolean ocupado = Service.instance().findAllReservas().stream()
                    .filter(r -> r.getEstado() != EstadoReserva.CANCELADA)
                    .filter(r -> r.getFecha().equals(fecha))
                    .filter(r -> r.getRecursos().contains(recurso))
                    .anyMatch(r -> seSolapan(r.getHoraInicio(), r.getHoraFin(), horaInicio, horaFin));
            if (!ocupado) {
                return recurso;
            }
        }
        return null;
    }

    private boolean seSolapan(String inicioA, String finA, String inicioB, String finB) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("H:mm");
        LocalTime a1 = LocalTime.parse(inicioA, formato);
        LocalTime a2 = LocalTime.parse(finA, formato);
        LocalTime b1 = LocalTime.parse(inicioB, formato);
        LocalTime b2 = LocalTime.parse(finB, formato);
        return a1.isBefore(b2) && b1.isBefore(a2);
    }
}
