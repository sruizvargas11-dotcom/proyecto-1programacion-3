package una.eif206.view;

import una.eif206.ApplicationLogin;
import una.eif206.controller.ReservasController;
import una.eif206.logic.CategoriaRecurso;
import una.eif206.logic.Reserva;
import una.eif206.logic.enums.EstadoReserva;
import una.eif206.model.ReservasModel;
import una.eif206.model.ReservasTableModel;
import una.eif206.util.Highlighter;
import una.eif206.util.PdfReporter;
import una.eif206.util.ReservaExtraccion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ReservasView implements PropertyChangeListener {

    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("H:mm");

    private JPanel panel;
    private JTextField actividadFld;
    private JTextField fechaFld;
    private JTextField horaInicioFld;
    private JTextField horaFinFld;
    private JTextField fraseFld;
    private DefaultListModel<CategoriaRecurso> categoriasListModel;
    private JList<CategoriaRecurso> categoriasList;
    private JButton guardarFld;
    private JButton cancelarReservaFld;
    private JButton limpiarFld;
    private JButton imprimirFld;
    private JButton extraerIAFld;
    private JTable tabla;

    ReservasController controller;
    ReservasModel model;

    public ReservasView() {
        panel               = new JPanel(new BorderLayout(5, 5));
        actividadFld        = new JTextField(20);
        fechaFld             = new JTextField(10);
        horaInicioFld        = new JTextField(5);
        horaFinFld           = new JTextField(5);
        fraseFld             = new JTextField(20);
        categoriasListModel  = new DefaultListModel<>();
        categoriasList       = new JList<>(categoriasListModel);
        guardarFld           = new JButton("Crear reserva");
        cancelarReservaFld   = new JButton("Cancelar reserva");
        limpiarFld           = new JButton("Limpiar");
        imprimirFld          = new JButton("Imprimir PDF");
        extraerIAFld         = new JButton("Extraer AI");
        tabla                = new JTable();

        categoriasList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.add(new JLabel("Actividad:"));             form.add(actividadFld);
        form.add(new JLabel("Fecha (yyyy-MM-dd):"));     form.add(fechaFld);
        form.add(new JLabel("Hora inicio (HH:mm):"));    form.add(horaInicioFld);
        form.add(new JLabel("Hora fin (HH:mm):"));       form.add(horaFinFld);

        JPanel iaPanel = new JPanel(new BorderLayout(5, 5));
        iaPanel.setBorder(BorderFactory.createTitledBorder("Extraer reserva con IA"));
        iaPanel.add(fraseFld, BorderLayout.CENTER);
        iaPanel.add(extraerIAFld, BorderLayout.EAST);

        JScrollPane categoriasScroll = new JScrollPane(categoriasList);
        categoriasScroll.setBorder(BorderFactory.createTitledBorder("Categorías a reservar"));

        JPanel botones = new JPanel();
        botones.add(guardarFld);
        botones.add(cancelarReservaFld);
        botones.add(limpiarFld);
        botones.add(imprimirFld);

        JPanel south = new JPanel(new BorderLayout(5, 5));
        south.add(iaPanel, BorderLayout.NORTH);
        south.add(botones, BorderLayout.SOUTH);

        JPanel top = new JPanel(new BorderLayout(5, 5));
        top.add(form, BorderLayout.CENTER);
        top.add(categoriasScroll, BorderLayout.EAST);
        top.add(south, BorderLayout.SOUTH);

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

        extraerIAFld.addActionListener(e -> {
            String frase = fraseFld.getText();
            if (frase == null || frase.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Escriba una frase para extraer.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                ReservaExtraccion extraccion = controller.extraerConIA(frase);
                if (extraccion.getActividad() != null) actividadFld.setText(extraccion.getActividad());
                if (extraccion.getFecha() != null) fechaFld.setText(extraccion.getFecha());
                if (extraccion.getHoraInicio() != null) horaInicioFld.setText(extraccion.getHoraInicio());
                if (extraccion.getHoraFinal() != null) horaFinFld.setText(extraccion.getHoraFinal());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        Highlighter h = new Highlighter(Color.green);
        actividadFld.addMouseListener(h);
        fechaFld.addMouseListener(h);
        horaInicioFld.addMouseListener(h);
        horaFinFld.addMouseListener(h);
    }

    public JPanel getPanel() { return panel; }
    public void setController(ReservasController c) { this.controller = c; }
    public void setModel(ReservasModel m) { this.model = m; model.addPropertyChangeListener(this); }

    private boolean validate() {
        boolean valid = true;
        valid &= checkField(actividadFld);
        valid &= checkField(fechaFld);
        boolean horaInicioValida = checkHoraField(horaInicioFld);
        boolean horaFinValida = checkHoraField(horaFinFld);
        valid &= horaInicioValida;
        valid &= horaFinValida;
        if (horaInicioValida && horaFinValida) {
            LocalTime inicio = LocalTime.parse(horaInicioFld.getText(), FORMATO_HORA);
            LocalTime fin = LocalTime.parse(horaFinFld.getText(), FORMATO_HORA);
            if (!inicio.isBefore(fin)) {
                valid = false;
                horaInicioFld.setBackground(ApplicationLogin.BACKGROUND_ERROR);
                horaFinFld.setBackground(ApplicationLogin.BACKGROUND_ERROR);
                JOptionPane.showMessageDialog(panel, "La hora de inicio debe ser anterior a la hora de fin.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
            }
        }
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

    private boolean checkHoraField(JTextField fld) {
        if (!checkField(fld)) {
            return false;
        }
        try {
            LocalTime.parse(fld.getText(), FORMATO_HORA);
            fld.setToolTipText(null);
            return true;
        } catch (DateTimeParseException ex) {
            fld.setBackground(ApplicationLogin.BACKGROUND_ERROR);
            fld.setToolTipText("Formato: HH:mm");
            return false;
        }
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
            case ReservasModel.LIST:
                int[] cols = {ReservasTableModel.ID, ReservasTableModel.ACTIVIDAD, ReservasTableModel.FECHA,
                        ReservasTableModel.HORA_INICIO, ReservasTableModel.HORA_FIN, ReservasTableModel.ESTADO};
                tabla.setModel(new ReservasTableModel(cols, model.getList()));
                break;
            case ReservasModel.CATEGORIAS:
                categoriasListModel.clear();
                model.getCategorias().forEach(categoriasListModel::addElement);
                break;
            case ReservasModel.CURRENT:
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
