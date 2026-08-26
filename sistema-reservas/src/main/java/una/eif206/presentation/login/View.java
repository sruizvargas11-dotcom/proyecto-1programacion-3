package una.eif206.presentation.login;

import una.eif206.ApplicationLogin;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class View extends JDialog implements PropertyChangeListener {

    private JTextField idFld;
    private JPasswordField claveFld;
    private JButton ingresarFld;

    Controller controller;
    Model model;

    public View() {
        setTitle("Iniciar Sesión");
        setSize(300, 180);
        setModal(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        idFld       = new JTextField(15);
        claveFld    = new JPasswordField(15);
        ingresarFld = new JButton("Ingresar");

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Usuario:")); panel.add(idFld);
        panel.add(new JLabel("Clave:"));   panel.add(claveFld);
        panel.add(new JLabel(""));         panel.add(ingresarFld);
        add(panel);

        ingresarFld.addActionListener(e -> {
            try {
                controller.login(
                        idFld.getText(),
                        new String(claveFld.getPassword())
                );
            } catch (Exception ex) {
                idFld.setBackground(ApplicationLogin.BACKGROUND_ERROR);
                claveFld.setBackground(ApplicationLogin.BACKGROUND_ERROR);
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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