package una.eif206.presentation.recursos;

import una.eif206.logic.CategoriaRecurso;
import una.eif206.logic.Recurso;
import una.eif206.presentation.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {

    Recurso current;
    List<Recurso> list;
    List<CategoriaRecurso> categorias;

    public static final String CURRENT    = "current";
    public static final String LIST       = "list";
    public static final String CATEGORIAS = "categorias";

    public Model() {
        current = new Recurso();
        list = new ArrayList<>();
        categorias = new ArrayList<>();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
        firePropertyChange(LIST);
        firePropertyChange(CATEGORIAS);
    }

    public Recurso getCurrent() { return current; }
    public void setCurrent(Recurso r) {
        this.current = r;
        firePropertyChange(CURRENT);
    }

    public List<Recurso> getList() { return list; }
    public void setList(List<Recurso> l) {
        this.list = l;
        firePropertyChange(LIST);
    }

    public List<CategoriaRecurso> getCategorias() { return categorias; }
    public void setCategorias(List<CategoriaRecurso> c) {
        this.categorias = c;
        firePropertyChange(CATEGORIAS);
    }
}
