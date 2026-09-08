package una.eif206.model;

import una.eif206.logic.CategoriaRecurso;
import una.eif206.util.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class CategoriasModel extends AbstractModel {
    CategoriaRecurso current;
    List<CategoriaRecurso> list;

    public static final String CURRENT = "current";
    public static final String LIST    = "list";

    public CategoriasModel() {
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