package una.eif206.presentation.actividades;

import una.eif206.presentation.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.LinkedHashMap;
import java.util.Map;

public class Model extends AbstractModel {

    String fechaReferencia;

    Map<String, Map<String, String>> matriz;

    public static final String FECHA_REFERENCIA = "fechaReferencia";
    public static final String MATRIZ           = "matriz";

    public Model() {
        fechaReferencia = "";
        matriz = new LinkedHashMap<>();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(FECHA_REFERENCIA);
        firePropertyChange(MATRIZ);
    }

    public String getFechaReferencia() { return fechaReferencia; }
    public void setFechaReferencia(String f) {
        this.fechaReferencia = f;
        firePropertyChange(FECHA_REFERENCIA);
    }

    public Map<String, Map<String, String>> getMatriz() { return matriz; }
    public void setMatriz(Map<String, Map<String, String>> m) {
        this.matriz = m;
        firePropertyChange(MATRIZ);
    }
}
