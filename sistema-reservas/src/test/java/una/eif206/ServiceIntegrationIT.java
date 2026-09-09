package una.eif206;

import una.eif206.data.Data;
import una.eif206.logic.CategoriaRecurso;
import una.eif206.logic.Funcionario;
import una.eif206.logic.Reserva;
import una.eif206.logic.Service;
import una.eif206.logic.Usuario;
import una.eif206.logic.enums.EstadoReserva;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceIntegrationIT {

    private Service service;

    @BeforeEach
    void resetData() throws Exception {
        service = Service.instance();
        Field dataField = Service.class.getDeclaredField("data");
        dataField.setAccessible(true);
        dataField.set(service, new Data());
    }

    @Test
    void testFlujoCompletoFuncionario() throws Exception {
        service.createFuncionario(new Funcionario("444", "Ana Ramirez", "Ventas", "5555"));

        Funcionario creado = service.findFuncionarioById("444");
        assertEquals("Ana Ramirez", creado.getNombre());

        service.updateFuncionario(new Funcionario("444", "Ana Maria Ramirez", "Ventas", "6666"));
        Funcionario actualizado = service.findFuncionarioById("444");
        assertEquals("Ana Maria Ramirez", actualizado.getNombre());
        assertEquals("6666", actualizado.getTelefono());

        service.deleteFuncionario(actualizado);
        assertThrows(Exception.class, () -> service.findFuncionarioById("444"));
    }

    @Test
    void testFlujoCompletoCategoria() throws Exception {
        CategoriaRecurso categoria = new CategoriaRecurso("", "Proyector");
        service.createCategoria(categoria);

        List<CategoriaRecurso> encontradas = service.searchCategorias(new CategoriaRecurso("", "proyector"));
        assertTrue(encontradas.stream().anyMatch(c -> c.getId().equals(categoria.getId())));

        service.deleteCategoria(categoria);
        assertTrue(service.findAllCategorias().stream().noneMatch(c -> c.getId().equals(categoria.getId())));
    }

    @Test
    void testFlujoCompletoReserva() throws Exception {
        Funcionario funcionario = service.findFuncionarioById("111");
        Reserva reserva = new Reserva(service.generarIdReserva(), "Reunion de equipo",
                "2026-01-15", "10:00", "11:00", funcionario);

        service.createReserva(reserva);
        service.cancelarReserva(reserva);

        assertEquals(EstadoReserva.CANCELADA, reserva.getEstado());
    }

    @Test
    void testLoginYCambiarClave() throws Exception {
        Usuario usuario = service.login("admin", "admin123");
        service.cambiarClave(usuario, "admin123", "nuevaClaveSegura");

        Usuario relogueado = service.login("admin", "nuevaClaveSegura");
        assertNotNull(relogueado);

        assertThrows(Exception.class, () -> service.login("admin", "admin123"));
    }
}
