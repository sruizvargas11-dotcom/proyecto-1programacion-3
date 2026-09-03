package una.eif206.presentation.login;

import una.eif206.ApplicationLogin;
import una.eif206.logic.Service;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class View extends JDialog implements PropertyChangeListener {

    private JTextField idFld;
    private JPasswordField claveFld;
    private JButton ingresarFld;
    private JButton cambiarFld;

    Controller controller;
    Model model;

    public View() {
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
                una.eif206.presentation.cambiarclave.View cv =
                        new una.eif206.presentation.cambiarclave.View();
                una.eif206.presentation.cambiarclave.Model cm =
                        new una.eif206.presentation.cambiarclave.Model();
                new una.eif206.presentation.cambiarclave.Controller(cv, cm, u);
                cv.setVisible(true);
            } catch (Exception ex) {
                idFld.setBackground(ApplicationLogin.BACKGROUND_ERROR);
                JOptionPane.showMessageDialog(this,
                        "Ingrese usuario y clave correctos primero",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public void setController(Controller c) { this.controller = c; }

    public void setModel(Model m) {
        this.model = m;
        model.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {}
}