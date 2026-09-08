package una.eif206.controller;

import una.eif206.logic.Service;
import una.eif206.logic.Usuario;
import una.eif206.model.LoginModel;
import una.eif206.util.Sesion;
import una.eif206.view.LoginView;

public class LoginController {

    LoginView view;
    LoginModel model;

    public LoginController(LoginView view, LoginModel model) {
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