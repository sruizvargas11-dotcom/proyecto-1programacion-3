package una.eif206;

import una.eif206.data.Data;
import una.eif206.logic.CategoriaRecurso;
import una.eif206.logic.Funcionario;
import una.eif206.logic.Recurso;
import una.eif206.logic.Reserva;
import una.eif206.logic.Service;
import una.eif206.logic.Usuario;
import una.eif206.logic.enums.EstadoReserva;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

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

    @Test
    void testFlujoCompletoReservaConSolapamiento() throws Exception {
        Funcionario funcionario = service.findFuncionarioById("111");
        CategoriaRecurso categoria = service.findAllCategorias().stream()
                .filter(c -> c.getId().equals("CAT-000002")).findFirst().orElseThrow();
        Recurso recurso = service.findRecursosByCategoria(categoria).get(0);

        Reserva primera = new Reserva(service.generarIdReserva(), "Reunion 1", "2026-04-01",
                "09:00", "11:00", funcionario);
        primera.setRecursos(List.of(recurso));
        service.createReserva(primera);

        Reserva solapada = new Reserva(service.generarIdReserva(), "Reunion 2", "2026-04-01",
                "10:00", "12:00", funcionario);
        solapada.setRecursos(List.of(recurso));
        assertThrows(Exception.class, () -> service.createReserva(solapada));

        Reserva sinSolape = new Reserva(service.generarIdReserva(), "Reunion 3", "2026-04-01",
                "11:00", "13:00", funcionario);
        sinSolape.setRecursos(List.of(recurso));
        service.createReserva(sinSolape);
        assertTrue(service.findAllReservas().stream().anyMatch(r -> r.getId().equals(sinSolape.getId())));

        service.cancelarReserva(primera);
        assertEquals(EstadoReserva.CANCELADA, primera.getEstado());
    }

    @Test
    void testFlujoEstadisticas() throws Exception {
        Funcionario funcionario = service.findFuncionarioById("111");
        CategoriaRecurso categoria = service.findAllCategorias().get(0);
        Recurso recurso = service.findRecursosByCategoria(categoria).get(0);

        Reserva r1 = new Reserva(service.generarIdReserva(), "Capacitacion", "2026-05-01", "09:00", "10:00", funcionario);
        r1.setRecursos(List.of(recurso));
        service.createReserva(r1);

        Reserva r2 = new Reserva(service.generarIdReserva(), "Capacitacion", "2026-05-02", "09:00", "10:00", funcionario);
        r2.setRecursos(List.of(recurso));
        service.createReserva(r2);

        Reserva r3 = new Reserva(service.generarIdReserva(), "Revision de proyecto", "2026-05-03", "09:00", "10:00", funcionario);
        service.createReserva(r3);

        Map<String, Integer> estadisticasRecursos = service.getEstadisticasRecursos("2026-05-01", "2026-05-03");
        assertEquals(2, estadisticasRecursos.getOrDefault(recurso.getDescripcion(), 0));

        Map<String, Integer> estadisticasActividades = service.getEstadisticasActividades("2026-05-01", "2026-05-03");
        assertEquals(2, estadisticasActividades.getOrDefault("Capacitacion", 0));
        assertEquals(1, estadisticasActividades.getOrDefault("Revision de proyecto", 0));
    }

    @Test
    void testFlujoCalendarizacion() throws Exception {
        Funcionario funcionario = service.findFuncionarioById("111");
        CategoriaRecurso categoria = service.findAllCategorias().stream()
                .filter(c -> c.getId().equals("CAT-000002")).findFirst().orElseThrow();
        Recurso recurso = service.findRecursosByCategoria(categoria).get(0);

        Reserva reserva = new Reserva(service.generarIdReserva(), "Sesion de Planificacion", "2026-06-01",
                "10:00", "11:00", funcionario);
        reserva.setRecursos(List.of(recurso));
        service.createReserva(reserva);

        String[][] matriz = service.getCalendario("2026-06-01", categoria);

        int filaDiezHoras = -1;
        for (int i = 0; i < matriz.length; i++) {
            if ("10:00".equals(matriz[i][0])) {
                filaDiezHoras = i;
                break;
            }
        }
        assertTrue(filaDiezHoras >= 0);
        assertTrue(matriz[filaDiezHoras][1] != null && matriz[filaDiezHoras][1].contains("Sesion de Planificacion"));
    }
}
