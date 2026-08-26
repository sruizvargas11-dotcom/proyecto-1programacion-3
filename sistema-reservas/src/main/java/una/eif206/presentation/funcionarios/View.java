package una.eif206.presentation.funcionarios;

import una.eif206.ApplicationLogin;
import una.eif206.logic.Funcionario;
import una.eif206.presentation.Highlighter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class View implements PropertyChangeListener {

    private JPanel panel;
    private JTextField idFld;
    private JTextField nombreFld;
    private JTextField departamentoFld;
    private JTextField busquedaFld;
    private JButton guardarFld;
    private JButton cancelarFld;
    private JButton buscarFld;
    private JButton borrarFld;
    private JTable tabla;

    Controller controller;
    Model model;

    public View() {
        panel           = new JPanel(new BorderLayout(5, 5));
        idFld           = new JTextField(10);
        nombreFld       = new JTextField(20);
        departamentoFld = new JTextField(15);
        busquedaFld     = new JTextField(15);
        guardarFld      = new JButton("Guardar");
        cancelarFld     = new JButton("Limpiar");
        buscarFld       = new JButton("Buscar");
        borrarFld       = new JButton("Borrar");
        tabla           = new JTable();

        JPanel form = new JPanel(new GridLayout(5, 2, 5, 5));
        form.add(new JLabel("ID:"));           form.add(idFld);
        form.add(new JLabel("Nombre:"));       form.add(nombreFld);
        form.add(new JLabel("Departamento:")); form.add(departamentoFld);
        form.add(new JLabel("Buscar:"));       form.add(busquedaFld);

        JPanel botones = new JPanel();
        botones.add(guardarFld);
        botones.add(cancelarFld);
        botones.add(buscarFld);
        botones.add(borrarFld);
        form.add(new JLabel(""));
        form.add(botones);

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        guardarFld.addActionListener(e -> {
            if (validate()) {
                try {
                    controller.create(take());
                    JOptionPane.showMessageDialog(panel, "REGISTRO APLICADO");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelarFld.addActionListener(e -> controller.clear());

        buscarFld.addActionListener(e ->
                controller.search(busquedaFld.getText()));

        borrarFld.addActionListener(e -> {
            int op = JOptionPane.showConfirmDialog(panel, "Confirma borrar?");
            if (op == JOptionPane.YES_OPTION) {
                try {
                    controller.delete(model.getCurrent());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage());
                }
            }
        });

        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tabla.getSelectedRow();
                if (row >= 0) controller.edit(row);
            }
        });

        Highlighter h = new Highlighter(Color.green);
        idFld.addMouseListener(h);
        nombreFld.addMouseListener(h);
        departamentoFld.addMouseListener(h);
    }

    public JPanel getPanel() { return panel; }

    public void setController(Controller c) { this.controller = c; }

    public void setModel(Model m) {
        this.model = m;
        model.addPropertyChangeListener(this);
    }

    public Funcionario take() {
        Funcionario f = new Funcionario();
        f.setId(idFld.getText());
        f.setNombre(nombreFld.getText());
        f.setDepartamento(departamentoFld.getText());
        return f;
    }

    private boolean validate() {
        boolean valid = true;
        if (idFld.getText().isEmpty()) {
            valid = false;
            idFld.setBackground(ApplicationLogin.BACKGROUND_ERROR);
        } else {
            idFld.setBackground(null);
        }
        if (nombreFld.getText().isEmpty()) {
            valid = false;
            nombreFld.setBackground(ApplicationLogin.BACKGROUND_ERROR);
        } else {
            nombreFld.setBackground(null);
        }
        return valid;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.LIST:
                int[] cols = {TableModel.ID, TableModel.NOMBRE, TableModel.DEPARTAMENTO};
                tabla.setModel(new TableModel(cols, model.getList()));
                break;
            case Model.CURRENT:
                idFld.setText(model.getCurrent().getId());
                nombreFld.setText(model.getCurrent().getNombre());
                departamentoFld.setText(model.getCurrent().getDepartamento());
                idFld.setBackground(null);
                nombreFld.setBackground(null);
                break;
        }
        panel.revalidate();
    }
}