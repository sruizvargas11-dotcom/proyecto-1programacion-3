package una.eif206.logic;

import una.eif206.data.Data;
import una.eif206.data.XMLHelper;

import java.util.List;
import java.util.stream.Collectors;

import una.eif206.logic.enums.EstadoReserva;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

public class Service {

    private static Service theInstance;

    public static Service instance() {
        if (theInstance == null) theInstance = new Service();
        return theInstance;
    }

    private Data data;

    private Service() {
        try {
            data = XMLHelper.instance().load();
        } catch (Exception e) {
            data = new Data();
        }
    }

    public void stop() {
        try {
            XMLHelper.instance().store(data);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // ── LOGIN ──
    public Usuario login(String id, String clave) throws Exception {
        for (Admin a : data.getAdmins()) {
            if (a.getId().equals(id) && a.getClave().equals(clave)) return a;
        }
        for (Funcionario f : data.getFuncionarios()) {
            if (f.getId().equals(id) && f.getClave().equals(clave)) return f;
        }
        throw new Exception("Usuario o clave incorrectos");
    }

    // ── FUNCIONARIOS ──
    public void createFuncionario(Funcionario e) throws Exception {
        Funcionario result = data.getFuncionarios().stream()
                .filter(i -> i.getId().equals(e.getId()))
                .findFirst().orElse(null);
        if (result == null) data.getFuncionarios().add(e);
        else throw new Exception("Funcionario ya existe");
    }

    public List<Funcionario> findAllFuncionarios() {
        return data.getFuncionarios();
    }

    public List<Funcionario> searchFuncionarios(Funcionario e) {
        return data.getFuncionarios().stream()
                .filter(i -> i.getNombre().toLowerCase()
                        .contains(e.getNombre().toLowerCase()))
                .collect(Collectors.toList());
    }

    public void deleteFuncionario(Funcionario e) throws Exception {
        if (!data.getFuncionarios().remove(e))
            throw new Exception("Funcionario no encontrado");
    }

    // ── CATEGORIAS ──
    public void createCategoria(CategoriaRecurso e) throws Exception {
        CategoriaRecurso result = data.getCategorias().stream()
                .filter(i -> i.getId().equals(e.getId()))
                .findFirst().orElse(null);
        if (result == null) data.getCategorias().add(e);
        else throw new Exception("Categoría ya existe");
    }

    public List<CategoriaRecurso> findAllCategorias() {
        return data.getCategorias();
    }

    // ── RECURSOS ──
    public void createRecurso(Recurso e) throws Exception {
        Recurso result = data.getRecursos().stream()
                .filter(i -> i.getId().equals(e.getId()))
                .findFirst().orElse(null);
        if (result == null) data.getRecursos().add(e);
        else throw new Exception("Recurso ya existe");
    }

    public List<Recurso> findAllRecursos() {
        return data.getRecursos();
    }

    // ── RESERVAS ──
    public void createReserva(Reserva e) throws Exception {
        Reserva result = data.getReservas().stream()
                .filter(i -> i.getId().equals(e.getId()))
                .findFirst().orElse(null);
        if (result == null) data.getReservas().add(e);
        else throw new Exception("Reserva ya existe");
    }

    public List<Reserva> findAllReservas() {
        return data.getReservas();
    }

    public List<Reserva> findReservasByFuncionario(Funcionario f) {
        return data.getReservas().stream()
                .filter(i -> i.getFuncionario().getId().equals(f.getId()))
                .collect(Collectors.toList());
    }
    public String generarIdReserva() {
        return data.generarIdReserva();
    }

    public void cancelarReserva(Reserva r) throws Exception {
        Reserva result = data.getReservas().stream()
                .filter(i -> i.getId().equals(r.getId()))
                .findFirst().orElse(null);
        if (result == null) throw new Exception("Reserva no encontrada");
        result.setEstado(EstadoReserva.CANCELADA);
    }


    // ── ACTIVIDADES (matriz semanal: hora -> dia -> texto) ──
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final String[] NOMBRES_DIAS = {
            "LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO", "DOMINGO"
    };

    public Map<String, Map<String, String>> getActividadesSemana(String fechaReferencia) throws Exception {
        LocalDate referencia;
        try {
            referencia = LocalDate.parse(fechaReferencia, FORMATO_FECHA);
        } catch (Exception ex) {
            throw new Exception("Fecha invalida, use el formato yyyy-MM-dd");
        }

        LocalDate lunes = referencia.with(DayOfWeek.MONDAY);
        LocalDate domingo = lunes.plusDays(6);

        Map<String, Map<String, String>> matriz = new LinkedHashMap<>();

        for (Reserva reserva : data.getReservas()) {
            if (reserva.getEstado() == EstadoReserva.CANCELADA) {
                continue;
            }

            LocalDate fecha;
            try {
                fecha = LocalDate.parse(reserva.getFecha(), FORMATO_FECHA);
            } catch (Exception ex) {
                continue; // fecha con formato invalido, se ignora
            }

            if (fecha.isBefore(lunes) || fecha.isAfter(domingo)) {
                continue; // no cae en la semana consultada
            }

            String dia = NOMBRES_DIAS[fecha.getDayOfWeek().getValue() - 1];
            Map<String, String> fila = matriz.computeIfAbsent(
                    reserva.getHoraInicio(), k -> new LinkedHashMap<>());

            String textoExistente = fila.get(dia);
            String textoNuevo = reserva.getActividad();
            if (textoExistente != null && !textoExistente.isEmpty()) {
                textoNuevo = textoExistente + "; " + textoNuevo;
            }
            fila.put(dia, textoNuevo);
        }

        return matriz;
    }
    // ── CAMBIAR CLAVE ──
    public void cambiarClave(Usuario u, String claveActual, String claveNueva)
            throws Exception {
        if (!u.getClave().equals(claveActual)) {
            throw new Exception("Clave actual incorrecta");
        }
        u.setClave(claveNueva);
    }

    // ── UPDATE FUNCIONARIO ──
    public void updateFuncionario(Funcionario f) throws Exception {
        Funcionario result = data.getFuncionarios().stream()
                .filter(i -> i.getId().equals(f.getId()))
                .findFirst().orElse(null);
        if (result != null) {
            result.setNombre(f.getNombre());
            result.setTelefono(f.getTelefono());
            result.setDepartamento(f.getDepartamento());
        } else {
            throw new Exception("Funcionario no encontrado");
        }
    }

    // ── FIND FUNCIONARIO BY ID ──
    public Funcionario findFuncionarioById(String id) throws Exception {
        return data.getFuncionarios().stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new Exception("Funcionario no encontrado"));
    }
}