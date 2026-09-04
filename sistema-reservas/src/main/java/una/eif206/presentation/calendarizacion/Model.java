package una.eif206.presentation.calendarizacion;

import una.eif206.logic.CategoriaRecurso;
import una.eif206.logic.Recurso;
import una.eif206.presentation.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {

    private String fecha;
    private CategoriaRecurso categoria;
    private List<CategoriaRecurso> categorias;
    private List<Recurso> recursos;
    private String[][] matriz;

    public static final String FECHA      = "fecha";
    public static final String CATEGORIAS = "categorias";
    public static final String MATRIZ     = "matriz";

    public Model() {
        fecha      = "";
        categorias = new ArrayList<>();
        recursos   = new ArrayList<>();
        matriz     = new String[0][0];
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CATEGORIAS);
        firePropertyChange(MATRIZ);
    }

    public String getFecha() { return fecha; }
    public void setFecha(String f) { this.fecha = f; }
    public CategoriaRecurso getCategoria() { return categoria; }
    public void setCategoria(CategoriaRecurso c) { this.categoria = c; }
    public List<CategoriaRecurso> getCategorias() { return categorias; }
    public void setCategorias(List<CategoriaRecurso> c) { this.categorias = c; firePropertyChange(CATEGORIAS); }
    public List<Recurso> getRecursos() { return recursos; }
    public void setRecursos(List<Recurso> r) { this.recursos = r; }
    public String[][] getMatriz() { return matriz; }
    public void setMatriz(String[][] m) { this.matriz = m; firePropertyChange(MATRIZ); }
}
