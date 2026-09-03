package una.eif206.presentation.login;

import una.eif206.logic.Service;
import una.eif206.logic.Usuario;
import una.eif206.presentation.Sesion;

public class Controller {

    View view;
    Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
    }

    public void login(String id, String clave) throws Exception {
        Usuario u = Service.instance().login(id, clave);
        Sesion.setUsuario(u);
        view.dispose();
    }
}