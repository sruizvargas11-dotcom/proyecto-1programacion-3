package una.eif206.presentation.reservas;

import una.eif206.logic.CategoriaRecurso;
import una.eif206.logic.Funcionario;
import una.eif206.logic.Recurso;
import una.eif206.logic.Reserva;
import una.eif206.logic.Service;
import una.eif206.logic.Usuario;
import una.eif206.logic.enums.EstadoReserva;
import una.eif206.logic.enums.UsuarioRol;
import una.eif206.presentation.Sesion;

import java.util.ArrayList;
import java.util.List;

public class Controller {

    View view;
    Model model;

    public Controller(View view, Model model) {
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
        return inicioA.compareTo(finB) < 0 && inicioB.compareTo(finA) < 0;
    }
}
