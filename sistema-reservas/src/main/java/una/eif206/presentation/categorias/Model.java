package una.eif206.presentation.categorias;

import una.eif206.logic.CategoriaRecurso;
import una.eif206.presentation.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {
    CategoriaRecurso current;
    List<CategoriaRecurso> list;

    public static final String CURRENT = "current";
    public static final String LIST    = "list";

    public Model() {
        current = new CategoriaRecurso();
        list = new ArrayList<>();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
        firePropertyChange(LIST);
    }

    public CategoriaRecurso getCurrent() { return current; }
    public void setCurrent(CategoriaRecurso c) {
        this.current = c;
        firePropertyChange(CURRENT);
    }

    public List<CategoriaRecurso> getList() { return list; }
    public void setList(List<CategoriaRecurso> l) {
        this.list = l;
        firePropertyChange(LIST);
    }
}