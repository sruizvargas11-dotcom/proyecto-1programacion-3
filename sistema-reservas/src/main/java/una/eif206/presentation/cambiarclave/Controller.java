package una.eif206.presentation.cambiarclave;

import una.eif206.logic.Service;
import una.eif206.logic.Usuario;
import una.eif206.presentation.Sesion;

public class Controller {

    View view;
    Model model;

    public Controller(View view, Model model, Usuario usuario) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
        if (usuario != null) {
            model.setUsuario(usuario);
        }
    }

    public void cambiarClave(String actual, String nueva) throws Exception {
        Usuario u = model.getUsuario();
        if (u == null) throw new Exception("Debe iniciar sesion primero");
        Service.instance().cambiarClave(u, actual, nueva);
    }
}