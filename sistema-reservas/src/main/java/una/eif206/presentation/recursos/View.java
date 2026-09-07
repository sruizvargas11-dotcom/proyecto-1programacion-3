package una.eif206.presentation.recursos;

import una.eif206.ApplicationLogin;
import una.eif206.logic.CategoriaRecurso;
import una.eif206.logic.Recurso;
import una.eif206.presentation.Highlighter;
import una.eif206.util.PdfReporter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class View implements PropertyChangeListener {

    private JPanel panel;
    private JTextField idFld;
    private JTextField descripcionFld;
    private JComboBox<CategoriaRecurso> categoriaFld;
    private JComboBox<CategoriaRecurso> filtroFld;
    private JButton guardarFld;
    private JButton modificarFld;
    private JButton cancelarFld;
    private JButton borrarFld;
    private JButton imprimirFld;
    private JTable tabla;

    private boolean loadingCategorias = false;

    Controller controller;
    Model model;

    public View() {
        panel          = new JPanel(new BorderLayout(5, 5));
        idFld          = new JTextField(10);
        descripcionFld = new JTextField(20);
        categoriaFld   = new JComboBox<>();
        filtroFld      = new JComboBox<>();
        guardarFld     = new JButton("Guardar");
        modificarFld   = new JButton("Modificar");
        cancelarFld    = new JButton("Limpiar");
        borrarFld      = new JButton("Borrar");
        imprimirFld    = new JButton("Imprimir PDF");
        tabla          = new JTable();

        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.add(new JLabel("ID:"));          form.add(idFld);
        form.add(new JLabel("Descripción:")); form.add(descripcionFld);
        form.add(new JLabel("Categoría:"));   form.add(categoriaFld);

        JPanel botones = new JPanel();
        botones.add(guardarFld);
        botones.add(modificarFld);
        botones.add(cancelarFld);
        botones.add(borrarFld);
        botones.add(imprimirFld);
        form.add(new JLabel(""));
        form.add(botones);

        JPanel filtroPanel = new JPanel();
        filtroPanel.add(new JLabel("Filtrar por categoría:"));
        filtroPanel.add(filtroFld);

        JPanel norte = new JPanel(new BorderLayout());
        norte.add(form, BorderLayout.NORTH);
        norte.add(filtroPanel, BorderLayout.SOUTH);

        panel.add(norte, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        guardarFld.addActionListener(e -> {
            if (validate()) {
                try {
                    controller.create(take());
                    JOptionPane.showMessageDialog(panel, "REGISTRO APLICADO");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        modificarFld.addActionListener(e -> {
            if (validate()) {
                try {
                    controller.update(take());
                    JOptionPane.showMessageDialog(panel, "REGISTRO MODIFICADO");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelarFld.addActionListener(e -> controller.clear());

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

        filtroFld.addActionListener(e -> {
            if (loadingCategorias) return;
            CategoriaRecurso seleccionada = (CategoriaRecurso) filtroFld.getSelectedItem();
            controller.filtrarPorCategoria(seleccionada);
        });

        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tabla.getSelectedRow();
                if (row >= 0) controller.edit(row);
            }
        });

        imprimirFld.addActionListener(e -> {
            String[] columnas = {"ID", "Descripción", "Categoría"};
            List<String[]> filas = new ArrayList<>();
            for (Recurso r : model.getList()) {
                String cat = r.getCategoria() != null ? r.getCategoria().getDescripcion() : "";
                filas.add(new String[]{r.getId(), r.getDescripcion(), cat});
            }
            PdfReporter.imprimir(panel, "Reporte de Recursos", columnas, filas);
        });

        Highlighter h = new Highlighter(Color.green);
        descripcionFld.addMouseListener(h);
    }

    public JPanel getPanel() { return panel; }
    public void setController(Controller c) { this.controller = c; }
    public void setModel(Model m) { this.model = m; model.addPropertyChangeListener(this); }

    public Recurso take() {
        Recurso r = new Recurso();
        r.setId(idFld.getText());
        r.setDescripcion(descripcionFld.getText());
        r.setCategoria((CategoriaRecurso) categoriaFld.getSelectedItem());
        return r;
    }

    private boolean validate() {
        boolean valid = true;
        if (idFld.getText().isEmpty()) {
            valid = false;
            idFld.setBackground(ApplicationLogin.BACKGROUND_ERROR);
        } else { idFld.setBackground(null); }
        if (descripcionFld.getText().isEmpty()) {
            valid = false;
            descripcionFld.setBackground(ApplicationLogin.BACKGROUND_ERROR);
        } else { descripcionFld.setBackground(null); }
        if (categoriaFld.getSelectedItem() == null) {
            valid = false;
            categoriaFld.setBackground(ApplicationLogin.BACKGROUND_ERROR);
        } else { categoriaFld.setBackground(null); }
        return valid;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.LIST:
                int[] cols = {TableModel.ID, TableModel.DESCRIPCION, TableModel.CATEGORIA};
                tabla.setModel(new TableModel(cols, model.getList()));
                break;
            case Model.CATEGORIAS:
                loadingCategorias = true;
                categoriaFld.removeAllItems();
                for (CategoriaRecurso c : model.getCategorias()) categoriaFld.addItem(c);

                filtroFld.removeAllItems();
                filtroFld.addItem(null);
                for (CategoriaRecurso c : model.getCategorias()) filtroFld.addItem(c);
                loadingCategorias = false;
                break;
            case Model.CURRENT:
                idFld.setText(model.getCurrent().getId());
                descripcionFld.setText(model.getCurrent().getDescripcion());
                categoriaFld.setSelectedItem(model.getCurrent().getCategoria());
                idFld.setBackground(null);
                descripcionFld.setBackground(null);
                categoriaFld.setBackground(null);
                break;
        }
        panel.revalidate();
    }
}
