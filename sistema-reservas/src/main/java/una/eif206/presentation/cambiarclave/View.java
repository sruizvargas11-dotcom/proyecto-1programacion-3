package una.eif206.presentation.cambiarclave;

import una.eif206.ApplicationLogin;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class View extends JDialog implements PropertyChangeListener {

    private JPasswordField claveActualFld;
    private JPasswordField claveNuevaFld;
    private JPasswordField claveNueva2Fld;
    private JButton okFld;
    private JButton cancelarFld;
    Controller controller;
    Model model;

    public View() {
        setTitle("Cambiar Clave");
        setSize(320, 200);
        setModal(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        claveActualFld = new JPasswordField(15);
        claveNuevaFld  = new JPasswordField(15);
        claveNueva2Fld = new JPasswordField(15);
        okFld          = new JButton("OK");
        cancelarFld    = new JButton("Cancelar");

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.add(new JLabel("Clave Actual:"));  panel.add(claveActualFld);
        panel.add(new JLabel("Clave Nueva:"));   panel.add(claveNuevaFld);
        panel.add(new JLabel("Repetir Clave:")); panel.add(claveNueva2Fld);
        panel.add(okFld);                        panel.add(cancelarFld);
        add(panel);

        okFld.addActionListener(e -> {
            try {
                String actual = new String(claveActualFld.getPassword());
                String nueva  = new String(claveNuevaFld.getPassword());
                String nueva2 = new String(claveNueva2Fld.getPassword());
                if (!nueva.equals(nueva2)) {
                    claveNuevaFld.setBackground(ApplicationLogin.BACKGROUND_ERROR);
                    claveNueva2Fld.setBackground(ApplicationLogin.BACKGROUND_ERROR);
                    JOptionPane.showMessageDialog(this, "Las claves nuevas no coinciden", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                controller.cambiarClave(actual, nueva);
                JOptionPane.showMessageDialog(this, "Clave cambiada exitosamente");
                dispose();
            } catch (Exception ex) {
                claveActualFld.setBackground(ApplicationLogin.BACKGROUND_ERROR);
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelarFld.addActionListener(e -> dispose());
    }

    public void setController(Controller c) { this.controller = c; }
    public void setModel(Model m) { this.model = m; model.addPropertyChangeListener(this); }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {}
}
