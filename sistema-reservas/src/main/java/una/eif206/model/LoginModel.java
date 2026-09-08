package una.eif206.model;

import una.eif206.logic.Usuario;
import una.eif206.util.AbstractModel;

import java.beans.PropertyChangeListener;

public class LoginModel extends AbstractModel {

    private Usuario usuario;
    public static final String CURRENT = "current";

    public LoginModel() {
        usuario = new Usuario();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
    }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario u) {
        this.usuario = u;
        firePropertyChange(CURRENT);
    }
}