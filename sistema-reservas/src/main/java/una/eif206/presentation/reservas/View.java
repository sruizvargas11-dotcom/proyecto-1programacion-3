package una.eif206.presentation.reservas;

import una.eif206.ApplicationLogin;
import una.eif206.logic.CategoriaRecurso;
import una.eif206.logic.Reserva;
import una.eif206.logic.enums.EstadoReserva;
import una.eif206.presentation.Highlighter;
import una.eif206.util.PdfReporter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class View implements PropertyChangeListener {

    private JPanel panel;
    private JTextField actividadFld;
    private JTextField fechaFld;
    private JTextField horaInicioFld;
    private JTextField horaFinFld;
    private DefaultListModel<CategoriaRecurso> categoriasListModel;
    private JList<CategoriaRecurso> categoriasList;
    private JButton guardarFld;
    private JButton cancelarReservaFld;
    private JButton limpiarFld;
    private JButton imprimirFld;
    private JTable tabla;

    Controller controller;
    Model model;

    public View() {
        panel               = new JPanel(new BorderLayout(5, 5));
        actividadFld        = new JTextField(20);
        fechaFld             = new JTextField(10);
        horaInicioFld        = new JTextField(5);
        horaFinFld           = new JTextField(5);
        categoriasListModel  = new DefaultListModel<>();
        categoriasList       = new JList<>(categoriasListModel);
        guardarFld           = new JButton("Crear reserva");
        cancelarReservaFld   = new JButton("Cancelar reserva");
        limpiarFld           = new JButton("Limpiar");
        imprimirFld          = new JButton("Imprimir PDF");
        tabla                = new JTable();

        categoriasList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.add(new JLabel("Actividad:"));             form.add(actividadFld);
        form.add(new JLabel("Fecha (yyyy-MM-dd):"));     form.add(fechaFld);
        form.add(new JLabel("Hora inicio (HH:mm):"));    form.add(horaInicioFld);
        form.add(new JLabel("Hora fin (HH:mm):"));       form.add(horaFinFld);

        JScrollPane categoriasScroll = new JScrollPane(categoriasList);
        categoriasScroll.setBorder(BorderFactory.createTitledBorder("Categorías a reservar"));

        JPanel botones = new JPanel();
        botones.add(guardarFld);
        botones.add(cancelarReservaFld);
        botones.add(limpiarFld);
        botones.add(imprimirFld);

        JPanel top = new JPanel(new BorderLayout(5, 5));
        top.add(form, BorderLayout.CENTER);
        top.add(categoriasScroll, BorderLayout.EAST);
        top.add(botones, BorderLayout.SOUTH);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        guardarFld.addActionListener(e -> {
            if (validate()) {
                try {
                    controller.create(actividadFld.getText(), fechaFld.getText(),
                            horaInicioFld.getText(), horaFinFld.getText(),
                            categoriasList.getSelectedValuesList());
                    JOptionPane.showMessageDialog(panel, "RESERVA CREADA");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelarReservaFld.addActionListener(e -> {
            int op = JOptionPane.showConfirmDialog(panel, "¿Cancelar la reserva seleccionada?");
            if (op == JOptionPane.YES_OPTION) {
                try {
                    controller.cancelar(model.getCurrent());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        limpiarFld.addActionListener(e -> {
            controller.clear();
            categoriasList.clearSelection();
            tabla.clearSelection();
        });

        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tabla.getSelectedRow();
                if (row >= 0) controller.edit(row);
            }
        });

        imprimirFld.addActionListener(e ->
                PdfReporter.imprimirDesdeTabla(panel, "Reporte de Reservas", tabla));

        Highlighter h = new Highlighter(Color.green);
        actividadFld.addMouseListener(h);
        fechaFld.addMouseListener(h);
        horaInicioFld.addMouseListener(h);
        horaFinFld.addMouseListener(h);
    }

    public JPanel getPanel() { return panel; }
    public void setController(Controller c) { this.controller = c; }
    public void setModel(Model m) { this.model = m; model.addPropertyChangeListener(this); }

    private boolean validate() {
        boolean valid = true;
        valid &= checkField(actividadFld);
        valid &= checkField(fechaFld);
        valid &= checkField(horaInicioFld);
        valid &= checkField(horaFinFld);
        if (categoriasList.getSelectedValuesList().isEmpty()) {
            valid = false;
            JOptionPane.showMessageDialog(panel, "Seleccione al menos una categoría.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
        }
        return valid;
    }

    private boolean checkField(JTextField fld) {
        if (fld.getText().isEmpty()) {
            fld.setBackground(ApplicationLogin.BACKGROUND_ERROR);
            return false;
        }
        fld.setBackground(null);
        return true;
    }

    private void limpiarFormulario() {
        actividadFld.setText("");
        fechaFld.setText("");
        horaInicioFld.setText("");
        horaFinFld.setText("");
        actividadFld.setBackground(null);
        fechaFld.setBackground(null);
        horaInicioFld.setBackground(null);
        horaFinFld.setBackground(null);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.LIST:
                int[] cols = {TableModel.ID, TableModel.ACTIVIDAD, TableModel.FECHA,
                        TableModel.HORA_INICIO, TableModel.HORA_FIN, TableModel.ESTADO};
                tabla.setModel(new TableModel(cols, model.getList()));
                break;
            case Model.CATEGORIAS:
                categoriasListModel.clear();
                model.getCategorias().forEach(categoriasListModel::addElement);
                break;
            case Model.CURRENT:
                Reserva actual = model.getCurrent();
                boolean hayCancelable = actual != null && actual.getId() != null
                        && !actual.getId().isEmpty() && actual.getEstado() != EstadoReserva.CANCELADA;
                cancelarReservaFld.setEnabled(hayCancelable);
                if (actual == null || actual.getId() == null || actual.getId().isEmpty()) {
                    limpiarFormulario();
                }
                break;
        }
        panel.revalidate();
    }
}
