package una.eif206.model;

import una.eif206.logic.Funcionario;
import una.eif206.util.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class FuncionariosModel extends AbstractModel {

    Funcionario current;
    List<Funcionario> list;

    public static final String CURRENT = "current";
    public static final String LIST    = "list";

    public FuncionariosModel() {
        current = new Funcionario();
        list = new ArrayList<>();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
        firePropertyChange(LIST);
    }

    public Funcionario getCurrent() { return current; }
    public void setCurrent(Funcionario f) {
        this.current = f;
        firePropertyChange(CURRENT);
    }

    public List<Funcionario> getList() { return list; }
    public void setList(List<Funcionario> l) {
        this.list = l;
        firePropertyChange(LIST);
    }
}