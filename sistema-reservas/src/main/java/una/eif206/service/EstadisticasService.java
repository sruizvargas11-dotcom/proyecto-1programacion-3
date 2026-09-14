package una.eif206.service;

import una.eif206.data.Data;
import una.eif206.model.Recurso;
import una.eif206.model.Reserva;
import una.eif206.model.enums.EstadoReserva;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class EstadisticasService {

    private final Data data;

    public EstadisticasService(Data data) {
        this.data = data;
    }

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
}
