package una.eif206;

import una.eif206.data.Data;
import una.eif206.logic.Admin;
import una.eif206.logic.CategoriaRecurso;
import una.eif206.logic.Funcionario;
import una.eif206.logic.Recurso;
import una.eif206.logic.Service;
import una.eif206.logic.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {

    private Service service;

    @BeforeEach
    void resetData() throws Exception {
        service = Service.instance();
        Field dataField = Service.class.getDeclaredField("data");
        dataField.setAccessible(true);
        dataField.set(service, new Data());
    }

    @Test
    void testLoginExitoso() throws Exception {
        Usuario usuario = service.login("admin", "admin123");
        assertTrue(usuario instanceof Admin);
    }

    @Test
    void testLoginFallido() {
        assertThrows(Exception.class, () -> service.login("admin", "wrongpass"));
    }

    @Test
    void testLoginFuncionario() throws Exception {
        Usuario usuario = service.login("111", "111");
        assertTrue(usuario instanceof Funcionario);
    }

    @Test
    void testCambiarClaveExitosa() throws Exception {
        Usuario usuario = service.login("admin", "admin123");
        service.cambiarClave(usuario, "admin123", "nuevaClave");
        assertEquals("nuevaClave", usuario.getClave());
    }

    @Test
    void testCambiarClaveIncorrecta() throws Exception {
        Usuario usuario = service.login("admin", "admin123");
        assertThrows(Exception.class, () -> service.cambiarClave(usuario, "claveMala", "nuevaClave"));
    }

    @Test
    void testCreateFuncionario() throws Exception {
        int before = service.findAllFuncionarios().size();
        service.createFuncionario(new Funcionario("333", "Nuevo Empleado", "TI", "1234"));
        assertEquals(before + 1, service.findAllFuncionarios().size());
    }

    @Test
    void testCreateFuncionarioDuplicado() throws Exception {
        Funcionario duplicado = new Funcionario("111", "Otro Nombre", "TI", "0000");
        assertThrows(Exception.class, () -> service.createFuncionario(duplicado));
    }

    @Test
    void testDeleteFuncionario() throws Exception {
        Funcionario existente = service.findFuncionarioById("111");
        service.deleteFuncionario(existente);
        assertThrows(Exception.class, () -> service.findFuncionarioById("111"));
    }

    @Test
    void testUpdateFuncionario() throws Exception {
        service.updateFuncionario(new Funcionario("111", "Nombre Actualizado", "Ventas", "9999"));
        Funcionario actualizado = service.findFuncionarioById("111");
        assertEquals("Nombre Actualizado", actualizado.getNombre());
        assertEquals("9999", actualizado.getTelefono());
    }

    @Test
    void testSearchFuncionarios() {
        List<Funcionario> resultado = service.searchFuncionarios(new Funcionario("", "juan", "", ""));
        assertFalse(resultado.isEmpty());
        assertTrue(resultado.stream().anyMatch(f -> f.getId().equals("111")));
    }

    @Test
    void testCreateCategoria() throws Exception {
        int before = service.findAllCategorias().size();
        CategoriaRecurso categoria = new CategoriaRecurso("", "Categoria Nueva");
        service.createCategoria(categoria);
        assertEquals(before + 1, service.findAllCategorias().size());
        assertTrue(categoria.getId().matches("CAT-\\d{6}"));
    }

    @Test
    void testCreateRecurso() throws Exception {
        int before = service.findAllRecursos().size();
        CategoriaRecurso categoria = service.findAllCategorias().get(0);
        service.createRecurso(new Recurso("999", "Recurso de prueba", categoria));
        assertEquals(before + 1, service.findAllRecursos().size());
    }

    @Test
    void testGenerarIdCategoria() {
        Data data = new Data();
        assertTrue(data.generarIdCategoria().matches("CAT-\\d{6}"));
    }
}
