package una.eif206.logic;

import una.eif206.data.Data;
import una.eif206.data.XMLHelper;
import una.eif206.logic.enums.EstadoReserva;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    // LOGIN
    public Usuario login(String id, String clave) throws Exception {
        for (Admin a : data.getAdmins()) {
            if (a.getId().equals(id) && a.getClave().equals(clave)) return a;
        }
        for (Funcionario f : data.getFuncionarios()) {
            if (f.getId().equals(id) && f.getClave().equals(clave)) return f;
        }
        throw new Exception("Usuario o clave incorrectos");
    }

    // FUNCIONARIOS
    public void createFuncionario(Funcionario e) throws Exception {
        Funcionario result = data.getFuncionarios().stream()
                .filter(i -> i.getId().equals(e.getId()))
                .findFirst().orElse(null);
        if (result == null) data.getFuncionarios().add(e);
        else throw new Exception("Funcionario ya existe");
    }

    public List<Funcionario> findAllFuncionarios() { return data.getFuncionarios(); }

    public List<Funcionario> searchFuncionarios(Funcionario e) {
        return data.getFuncionarios().stream()
                .filter(i -> i.getNombre().toLowerCase().contains(e.getNombre().toLowerCase()))
                .collect(Collectors.toList());
    }

    public void deleteFuncionario(Funcionario e) throws Exception {
        if (!data.getFuncionarios().remove(e))
            throw new Exception("Funcionario no encontrado");
    }

    public void updateFuncionario(Funcionario f) throws Exception {
        Funcionario result = data.getFuncionarios().stream()
                .filter(i -> i.getId().equals(f.getId()))
                .findFirst().orElse(null);
        if (result != null) {
            result.setNombre(f.getNombre());
            result.setTelefono(f.getTelefono());
            result.setDepartamento(f.getDepartamento());
        } else throw new Exception("Funcionario no encontrado");
    }

    public Funcionario findFuncionarioById(String id) throws Exception {
        return data.getFuncionarios().stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new Exception("Funcionario no encontrado"));
    }

    // CATEGORIAS
    public void createCategoria(CategoriaRecurso e) throws Exception {
        e.setId(data.generarIdCategoria());
        data.getCategorias().add(e);
    }

    public List<CategoriaRecurso> findAllCategorias() { return data.getCategorias(); }

    public List<CategoriaRecurso> searchCategorias(CategoriaRecurso e) {
        return data.getCategorias().stream()
                .filter(i -> i.getDescripcion().toLowerCase().contains(e.getDescripcion().toLowerCase()))
                .collect(Collectors.toList());
    }

    public void deleteCategoria(CategoriaRecurso e) throws Exception {
        if (!data.getCategorias().remove(e))
            throw new Exception("Categoria no encontrada");
    }

    public void updateCategoria(CategoriaRecurso c) throws Exception {
        CategoriaRecurso result = data.getCategorias().stream()
                .filter(i -> i.getId().equals(c.getId()))
                .findFirst().orElse(null);
        if (result != null) result.setDescripcion(c.getDescripcion());
        else throw new Exception("Categoria no encontrada");
    }

    // RECURSOS
    public void createRecurso(Recurso e) throws Exception {
        Recurso result = data.getRecursos().stream()
                .filter(i -> i.getId().equals(e.getId()))
                .findFirst().orElse(null);
        if (result == null) data.getRecursos().add(e);
        else throw new Exception("Recurso ya existe");
    }

    public List<Recurso> findAllRecursos() { return data.getRecursos(); }

    public void deleteRecurso(Recurso e) throws Exception {
        if (!data.getRecursos().remove(e))
            throw new Exception("Recurso no encontrado");
    }

    public void updateRecurso(Recurso r) throws Exception {
        Recurso result = data.getRecursos().stream()
                .filter(i -> i.getId().equals(r.getId()))
                .findFirst().orElse(null);
        if (result != null) {
            result.setDescripcion(r.getDescripcion());
            result.setCategoria(r.getCategoria());
        } else throw new Exception("Recurso no encontrado");
    }

    public List<Recurso> findRecursosByCategoria(CategoriaRecurso categoria) {
        return data.getRecursos().stream()
                .filter(r -> r.getCategoria() != null &&
                        r.getCategoria().getId().equals(categoria.getId()))
                .collect(Collectors.toList());
    }

    // RESERVAS
    public void createReserva(Reserva e) throws Exception {
        Reserva result = data.getReservas().stream()
                .filter(i -> i.getId().equals(e.getId()))
                .findFirst().orElse(null);
        if (result == null) data.getReservas().add(e);
        else throw new Exception("Reserva ya existe");
    }

    public List<Reserva> findAllReservas() { return data.getReservas(); }

    public List<Reserva> findReservasByFuncionario(Funcionario f) {
        return data.getReservas().stream()
                .filter(i -> i.getFuncionario() != null &&
                        i.getFuncionario().getId().equals(f.getId()))
                .collect(Collectors.toList());
    }

    public String generarIdReserva() { return data.generarIdReserva(); }

    public void cancelarReserva(Reserva r) throws Exception {
        Reserva result = data.getReservas().stream()
                .filter(i -> i.getId().equals(r.getId()))
                .findFirst().orElse(null);
        if (result == null) throw new Exception("Reserva no encontrada");
        result.setEstado(EstadoReserva.CANCELADA);
    }

    // ESTADISTICAS
    public Map<String, Integer> getEstadisticasRecursos(String desde, String hasta) throws Exception {
        LocalDate d1 = LocalDate.parse(desde);
        LocalDate d2 = LocalDate.parse(hasta);
        Map<String, Integer> resultado = new LinkedHashMap<>();
        for (Reserva r : data.getReservas()) {
            if (r.getEstado() == EstadoReserva.CANCELADA) continue;
            LocalDate fecha = LocalDate.parse(r.getFecha());
            if (fecha.isBefore(d1) || fecha.isAfter(d2)) continue;
            for (Recurso rec : r.getRecursos()) {
                resultado.merge(rec.getDescripcion(), 1, Integer::sum);
            }
        }
        return resultado;
    }

    public Map<String, Integer> getEstadisticasActividades(String desde, String hasta) throws Exception {
        LocalDate d1 = LocalDate.parse(desde);
        LocalDate d2 = LocalDate.parse(hasta);
        Map<String, Integer> resultado = new LinkedHashMap<>();
        for (Reserva r : data.getReservas()) {
            if (r.getEstado() == EstadoReserva.CANCELADA) continue;
            LocalDate fecha = LocalDate.parse(r.getFecha());
            if (fecha.isBefore(d1) || fecha.isAfter(d2)) continue;
            resultado.merge(r.getActividad(), 1, Integer::sum);
        }
        return resultado;
    }

    // ACTIVIDADES
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final String[] NOMBRES_DIAS = {
            "LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO", "DOMINGO"
    };

    public Map<String, Map<String, String>> getActividadesSemana(String fechaReferencia) throws Exception {
        LocalDate referencia = LocalDate.parse(fechaReferencia, FORMATO_FECHA);
        LocalDate lunes = referencia.with(DayOfWeek.MONDAY);
        LocalDate domingo = lunes.plusDays(6);
        Map<String, Map<String, String>> matriz = new LinkedHashMap<>();
        for (Reserva reserva : data.getReservas()) {
            if (reserva.getEstado() == EstadoReserva.CANCELADA) continue;
            LocalDate fecha;
            try { fecha = LocalDate.parse(reserva.getFecha(), FORMATO_FECHA); }
            catch (Exception ex) { continue; }
            if (fecha.isBefore(lunes) || fecha.isAfter(domingo)) continue;
            String dia = NOMBRES_DIAS[fecha.getDayOfWeek().getValue() - 1];
            Map<String, String> fila = matriz.computeIfAbsent(reserva.getHoraInicio(), k -> new LinkedHashMap<>());
            String textoExistente = fila.get(dia);
            String textoNuevo = reserva.getActividad();
            if (textoExistente != null && !textoExistente.isEmpty()) {
                textoNuevo = textoExistente + "; " + textoNuevo;
            }
            fila.put(dia, textoNuevo);
        }
        return matriz;
    }

    // CALENDARIZACION
    public String[][] getCalendario(String fecha, CategoriaRecurso categoria) {
        String[] horas = {"06:00","07:00","08:00","09:00","10:00","11:00",
                "12:00","13:00","14:00","15:00","16:00","17:00",
                "18:00","19:00","20:00","21:00","22:00"};
        List<Recurso> recursos = data.getRecursos().stream()
                .filter(r -> r.getCategoria() != null &&
                        r.getCategoria().getId().equals(categoria.getId()))
                .collect(Collectors.toList());
        String[][] matriz = new String[horas.length][recursos.size() + 1];
        for (int i = 0; i < horas.length; i++) { matriz[i][0] = horas[i]; }
        for (Reserva r : data.getReservas()) {
            if (!r.getFecha().equals(fecha)) continue;
            if (r.getEstado() != EstadoReserva.ACTIVA) continue;
            for (int col = 0; col < recursos.size(); col++) {
                Recurso rec = recursos.get(col);
                boolean usaRecurso = r.getRecursos() != null &&
                        r.getRecursos().stream().anyMatch(rv -> rv.getId().equals(rec.getId()));
                if (!usaRecurso) continue;
                for (int i = 0; i < horas.length; i++) {
                    if (horaEnRango(horas[i], r.getHoraInicio(), r.getHoraFin())) {
                        matriz[i][col + 1] = r.getActividad() + " - " +
                                (r.getFuncionario() != null ? r.getFuncionario().getNombre() : "");
                    }
                }
            }
        }
        return matriz;
    }

    private boolean horaEnRango(String hora, String inicio, String fin) {
        try {
            int h = Integer.parseInt(hora.replace(":00", ""));
            int i = Integer.parseInt(inicio.split(":")[0]);
            int f = Integer.parseInt(fin.split(":")[0]);
            return h >= i && h < f;
        } catch (Exception e) { return false; }
    }

    // CAMBIAR CLAVE
    public void cambiarClave(Usuario u, String claveActual, String claveNueva) throws Exception {
        if (!u.getClave().equals(claveActual)) throw new Exception("Clave actual incorrecta");
        u.setClave(claveNueva);
    }
}
