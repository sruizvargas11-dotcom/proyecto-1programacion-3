package una.eif206.service;

import una.eif206.data.Data;
import una.eif206.model.Funcionario;

import java.util.List;
import java.util.stream.Collectors;

public class FuncionarioService {

    private final Data data;

    public FuncionarioService(Data data) {
        this.data = data;
    }

    public List<Funcionario> findAll() {
        return data.getFuncionarios();
    }

    public List<Funcionario> search(Funcionario e) {
        return data.getFuncionarios().stream()
                .filter(i -> i.getNombre().toLowerCase().contains(e.getNombre().toLowerCase()))
                .collect(Collectors.toList());
    }

    public String create(Funcionario e) {
        if (e.getId() == null || e.getId().trim().isEmpty()
                || e.getNombre() == null || e.getNombre().trim().isEmpty()) {
            return "El ID y el nombre son obligatorios";
        }
        Funcionario result = data.getFuncionarios().stream()
                .filter(i -> i.getId().equals(e.getId()))
                .findFirst().orElse(null);
        if (result != null) return "Funcionario ya existe";
        data.getFuncionarios().add(e);
        return null;
    }

    public String update(Funcionario f) {
        Funcionario result = data.getFuncionarios().stream()
                .filter(i -> i.getId().equals(f.getId()))
                .findFirst().orElse(null);
        if (result == null) return "Funcionario no encontrado";
        result.setNombre(f.getNombre());
        result.setTelefono(f.getTelefono());
        result.setDepartamento(f.getDepartamento());
        return null;
    }

    public String delete(Funcionario e) {
        if (!data.getFuncionarios().remove(e)) return "Funcionario no encontrado";
        return null;
    }

    public Funcionario findById(String id) {
        return data.getFuncionarios().stream()
                .filter(i -> i.getId().equals(id))
                .findFirst().orElse(null);
    }
}
