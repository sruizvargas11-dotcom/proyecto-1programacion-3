package una.eif206;

import una.eif206.data.Data;
import una.eif206.logic.Admin;
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

    // ===================== RESERVAS =====================

    @Test
    void testCreateReserva() throws Exception {
        Funcionario funcionario = service.findFuncionarioById("111");
        Reserva reserva = new Reserva(service.generarIdReserva(), "Reunion de proyecto",
                "2026-02-10", "09:00", "10:00", funcionario);

        service.createReserva(reserva);

        assertTrue(service.findAllReservas().stream()
                .anyMatch(r -> r.getId().equals(reserva.getId())));
    }

    @Test
    void testCreateReservaDuplicada() throws Exception {
        Funcionario funcionario = service.findFuncionarioById("111");
        String id = service.generarIdReserva();
        Reserva reserva = new Reserva(id, "Reunion", "2026-02-10", "09:00", "10:00", funcionario);
        service.createReserva(reserva);

        Reserva duplicada = new Reserva(id, "Otra reunion", "2026-02-11", "11:00", "12:00", funcionario);
        assertThrows(Exception.class, () -> service.createReserva(duplicada));
    }

    @Test
    void testCancelarReserva() throws Exception {
        Funcionario funcionario = service.findFuncionarioById("111");
        Reserva reserva = new Reserva(service.generarIdReserva(), "Reunion", "2026-02-10",
                "09:00", "10:00", funcionario);
        service.createReserva(reserva);

        service.cancelarReserva(reserva);

        assertEquals(EstadoReserva.CANCELADA, reserva.getEstado());
    }

    @Test
    void testFindReservasByFuncionario() throws Exception {
        Funcionario f1 = service.findFuncionarioById("111");
        Funcionario f2 = service.findFuncionarioById("222");

        Reserva r1 = new Reserva(service.generarIdReserva(), "Reunion A", "2026-02-10", "09:00", "10:00", f1);
        Reserva r2 = new Reserva(service.generarIdReserva(), "Reunion B", "2026-02-10", "11:00", "12:00", f2);
        service.createReserva(r1);
        service.createReserva(r2);

        List<Reserva> reservasF1 = service.findReservasByFuncionario(f1);
        List<Reserva> reservasF2 = service.findReservasByFuncionario(f2);

        assertTrue(reservasF1.stream().anyMatch(r -> r.getId().equals(r1.getId())));
        assertTrue(reservasF1.stream().noneMatch(r -> r.getId().equals(r2.getId())));
        assertTrue(reservasF2.stream().anyMatch(r -> r.getId().equals(r2.getId())));
        assertTrue(reservasF2.stream().noneMatch(r -> r.getId().equals(r1.getId())));
    }

    @Test
    void testSolapamientoMismoRecurso() throws Exception {
        Funcionario funcionario = service.findFuncionarioById("111");
        CategoriaRecurso categoria = service.findAllCategorias().stream()
                .filter(c -> c.getId().equals("CAT-000002")).findFirst().orElseThrow();
        Recurso recurso = service.findRecursosByCategoria(categoria).get(0);

        Reserva r1 = new Reserva(service.generarIdReserva(), "Reunion A", "2026-02-10", "09:00", "11:00", funcionario);
        r1.setRecursos(List.of(recurso));
        service.createReserva(r1);

        Reserva r2 = new Reserva(service.generarIdReserva(), "Reunion B", "2026-02-10", "10:00", "12:00", funcionario);
        r2.setRecursos(List.of(recurso));

        assertThrows(Exception.class, () -> service.createReserva(r2));
    }

    @Test
    void testSinSolapamientoHoraLibre() throws Exception {
        Funcionario funcionario = service.findFuncionarioById("111");
        CategoriaRecurso categoria = service.findAllCategorias().stream()
                .filter(c -> c.getId().equals("CAT-000002")).findFirst().orElseThrow();
        Recurso recurso = service.findRecursosByCategoria(categoria).get(0);

        Reserva r1 = new Reserva(service.generarIdReserva(), "Reunion A", "2026-02-10", "09:00", "11:00", funcionario);
        r1.setRecursos(List.of(recurso));
        service.createReserva(r1);

        Reserva r2 = new Reserva(service.generarIdReserva(), "Reunion B", "2026-02-10", "11:00", "13:00", funcionario);
        r2.setRecursos(List.of(recurso));

        service.createReserva(r2);

        assertTrue(service.findAllReservas().stream().anyMatch(r -> r.getId().equals(r2.getId())));
    }

    @Test
    void testGenerarIdReserva() {
        String id = service.generarIdReserva();
        assertNotNull(id);
        assertFalse(id.isEmpty());
    }

    // ===================== CATEGORIAS =====================

    @Test
    void testUpdateCategoria() throws Exception {
        CategoriaRecurso categoria = new CategoriaRecurso("", "Sala VIP");
        service.createCategoria(categoria);

        service.updateCategoria(new CategoriaRecurso(categoria.getId(), "Sala VIP Actualizada"));

        CategoriaRecurso actualizada = service.findAllCategorias().stream()
                .filter(c -> c.getId().equals(categoria.getId())).findFirst().orElseThrow();
        assertEquals("Sala VIP Actualizada", actualizada.getDescripcion());
    }

    @Test
    void testSearchCategorias() throws Exception {
        service.createCategoria(new CategoriaRecurso("", "Sala de Conferencias"));
        service.createCategoria(new CategoriaRecurso("", "Sala de Descanso"));

        List<CategoriaRecurso> resultado = service.searchCategorias(new CategoriaRecurso("", "conferencias"));

        assertTrue(resultado.stream().anyMatch(c -> c.getDescripcion().equals("Sala de Conferencias")));
        assertTrue(resultado.stream().noneMatch(c -> c.getDescripcion().equals("Sala de Descanso")));
    }

    @Test
    void testDeleteCategoria() throws Exception {
        CategoriaRecurso categoria = new CategoriaRecurso("", "Categoria Temporal");
        service.createCategoria(categoria);

        service.deleteCategoria(categoria);

        assertTrue(service.findAllCategorias().stream().noneMatch(c -> c.getId().equals(categoria.getId())));
    }

    // ===================== RECURSOS =====================

    @Test
    void testUpdateRecurso() throws Exception {
        CategoriaRecurso categoria = service.findAllCategorias().get(0);
        Recurso recurso = new Recurso("R-500", "Recurso Original", categoria);
        service.createRecurso(recurso);

        service.updateRecurso(new Recurso("R-500", "Recurso Modificado", categoria));

        Recurso actualizado = service.findAllRecursos().stream()
                .filter(r -> r.getId().equals("R-500")).findFirst().orElseThrow();
        assertEquals("Recurso Modificado", actualizado.getDescripcion());
    }

    @Test
    void testFindRecursosByCategoria() throws Exception {
        CategoriaRecurso categoriaA = new CategoriaRecurso("", "Categoria A");
        CategoriaRecurso categoriaB = new CategoriaRecurso("", "Categoria B");
        service.createCategoria(categoriaA);
        service.createCategoria(categoriaB);

        service.createRecurso(new Recurso("R-A1", "Recurso A1", categoriaA));
        service.createRecurso(new Recurso("R-B1", "Recurso B1", categoriaB));

        List<Recurso> recursosA = service.findRecursosByCategoria(categoriaA);

        assertTrue(recursosA.stream().anyMatch(r -> r.getId().equals("R-A1")));
        assertTrue(recursosA.stream().noneMatch(r -> r.getId().equals("R-B1")));
    }

    // ===================== ESTADISTICAS =====================

    @Test
    void testGetEstadisticasRecursos() throws Exception {
        Funcionario funcionario = service.findFuncionarioById("111");
        CategoriaRecurso categoria = service.findAllCategorias().get(0);
        Recurso recurso = service.findRecursosByCategoria(categoria).get(0);

        Reserva reserva = new Reserva(service.generarIdReserva(), "Uso de laptop", "2026-03-01",
                "09:00", "10:00", funcionario);
        reserva.setRecursos(List.of(recurso));
        service.createReserva(reserva);

        Map<String, Integer> estadisticas = service.getEstadisticasRecursos("2026-03-01", "2026-03-01");

        assertTrue(estadisticas.getOrDefault(recurso.getDescripcion(), 0) > 0);
    }

    @Test
    void testGetEstadisticasActividades() throws Exception {
        Funcionario funcionario = service.findFuncionarioById("111");
        Reserva reserva = new Reserva(service.generarIdReserva(), "Capacitacion", "2026-03-05",
                "09:00", "10:00", funcionario);
        service.createReserva(reserva);

        Map<String, Integer> estadisticas = service.getEstadisticasActividades("2026-03-01", "2026-03-10");

        assertTrue(estadisticas.containsKey("Capacitacion"));
        assertEquals(1, estadisticas.get("Capacitacion"));
    }

    // ===================== CALENDARIZACION =====================

    @Test
    void testGetCalendario() throws Exception {
        Funcionario funcionario = service.findFuncionarioById("111");
        CategoriaRecurso categoria = service.findAllCategorias().stream()
                .filter(c -> c.getId().equals("CAT-000002")).findFirst().orElseThrow();
        Recurso recurso = service.findRecursosByCategoria(categoria).get(0);

        Reserva reserva = new Reserva(service.generarIdReserva(), "Reunion Directiva", "2026-03-10",
                "09:00", "10:00", funcionario);
        reserva.setRecursos(List.of(recurso));
        service.createReserva(reserva);

        String[][] matriz = service.getCalendario("2026-03-10", categoria);

        assertTrue(matriz.length > 0);
        boolean contieneActividad = false;
        for (String[] fila : matriz) {
            for (String celda : fila) {
                if (celda != null && celda.contains("Reunion Directiva")) {
                    contieneActividad = true;
                }
            }
        }
        assertTrue(contieneActividad);
    }
}
