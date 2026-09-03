package una.eif206.presentation.estadisticas;

import una.eif206.presentation.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.LinkedHashMap;
import java.util.Map;

public class Model extends AbstractModel {

    Map<String, Integer> datosRecursos;
    Map<String, Integer> datosActividades;

    public static final String DATOS_RECURSOS    = "datosRecursos";
    public static final String DATOS_ACTIVIDADES = "datosActividades";

    public Model() {
        datosRecursos    = new LinkedHashMap<>();
        datosActividades = new LinkedHashMap<>();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(DATOS_RECURSOS);
        firePropertyChange(DATOS_ACTIVIDADES);
    }

    public Map<String, Integer> getDatosRecursos() { return datosRecursos; }
    public void setDatosRecursos(Map<String, Integer> d) {
        this.datosRecursos = d;
        firePropertyChange(DATOS_RECURSOS);
    }

    public Map<String, Integer> getDatosActividades() { return datosActividades; }
    public void setDatosActividades(Map<String, Integer> d) {
        this.datosActividades = d;
        firePropertyChange(DATOS_ACTIVIDADES);
    }
}