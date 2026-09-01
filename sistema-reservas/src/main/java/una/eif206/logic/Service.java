package una.eif206.logic;

import una.eif206.data.Data;
import una.eif206.data.XMLHelper;

import java.util.List;
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
        e.setId(data.generarIdCategoria());
        CategoriaRecurso result = data.getCategorias().stream()
                .filter(i -> i.getId().equals(e.getId()))
                .findFirst().orElse(null);
        if (result == null) data.getCategorias().add(e);
        else throw new Exception("Categoría ya existe");
    }

    public List<CategoriaRecurso> findAllCategorias() {
        return data.getCategorias();
    }

    public List<CategoriaRecurso> searchCategorias(CategoriaRecurso e) {
        return data.getCategorias().stream()
                .filter(i -> i.getDescripcion().toLowerCase()
                        .contains(e.getDescripcion().toLowerCase()))
                .collect(Collectors.toList());
    }

    public void deleteCategoria(CategoriaRecurso e) throws Exception {
        if (!data.getCategorias().remove(e))
            throw new Exception("Categoría no encontrada");
    }

    public void updateCategoria(CategoriaRecurso c) throws Exception {
        CategoriaRecurso result = data.getCategorias().stream()
                .filter(i -> i.getId().equals(c.getId()))
                .findFirst().orElse(null);
        if (result != null) {
            result.setDescripcion(c.getDescripcion());
        } else {
            throw new Exception("Categoría no encontrada");
        }
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
        } else {
            throw new Exception("Recurso no encontrado");
        }
    }

    public List<Recurso> findRecursosByCategoria(CategoriaRecurso c) {
        return data.getRecursos().stream()
                .filter(i -> i.getCategoria().getId().equals(c.getId()))
                .collect(Collectors.toList());
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