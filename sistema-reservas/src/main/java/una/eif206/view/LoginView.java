package una.eif206.view;

import una.eif206.ApplicationLogin;
import una.eif206.controller.CambiarClaveController;
import una.eif206.controller.LoginController;
import una.eif206.logic.Service;
import una.eif206.model.CambiarClaveModel;
import una.eif206.model.LoginModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LoginView extends JDialog implements PropertyChangeListener {

    private JTextField idFld;
    private JPasswordField claveFld;
    private JButton ingresarFld;
    private JButton cambiarFld;

    LoginController controller;
    LoginModel model;

    public LoginView() {
        setTitle("Iniciar Sesion");
        setSize(300, 200);
        setModal(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        idFld       = new JTextField(15);
        claveFld    = new JPasswordField(15);
        ingresarFld = new JButton("Ingresar");
        cambiarFld  = new JButton("Cambiar clave");

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.add(new JLabel("Usuario:")); panel.add(idFld);
        panel.add(new JLabel("Clave:"));   panel.add(claveFld);
        panel.add(ingresarFld);            panel.add(cambiarFld);
        add(panel);

        ingresarFld.addActionListener(e -> {
            try {
                controller.login(idFld.getText(),
                        new String(claveFld.getPassword()));
            } catch (Exception ex) {
                idFld.setBackground(ApplicationLogin.BACKGROUND_ERROR);
                claveFld.setBackground(ApplicationLogin.BACKGROUND_ERROR);
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cambiarFld.addActionListener(e -> {
            try {
                una.eif206.logic.Usuario u = Service.instance().login(
                        idFld.getText(), new String(claveFld.getPassword()));
                CambiarClaveView cv = new CambiarClaveView();
                CambiarClaveModel cm = new CambiarClaveModel();
                new CambiarClaveController(cv, cm, u);
                cv.setVisible(true);
            } catch (Exception ex) {
                idFld.setBackground(ApplicationLogin.BACKGROUND_ERROR);
                JOptionPane.showMessageDialog(this,
                        "Ingrese usuario y clave correctos primero",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public void setController(LoginController c) { this.controller = c; }

    public void setModel(LoginModel m) {
        this.model = m;
        model.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {}
}