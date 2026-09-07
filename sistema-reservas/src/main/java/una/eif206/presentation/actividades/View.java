package una.eif206.presentation.actividades;

import una.eif206.ApplicationLogin;
import una.eif206.util.PdfReporter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.TreeMap;

public class View implements PropertyChangeListener {

    private static final String[] DIAS = {
            "LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO", "DOMINGO"
    };

    private JPanel panel;
    private JTextField fechaFld;
    private JButton cargarFld;
    private JButton imprimirFld;
    private JTable tabla;
    private DefaultTableModel tableModel;

    Controller controller;
    Model model;

    public View() {
        panel     = new JPanel(new BorderLayout(5, 5));
        fechaFld  = new JTextField(10);
        cargarFld = new JButton("Cargar semana");
        imprimirFld = new JButton("Imprimir PDF");
        tabla     = new JTable();

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Hora");
        for (String dia : DIAS) {
            tableModel.addColumn(dia);
        }
        tabla.setModel(tableModel);

        JPanel top = new JPanel();
        top.add(new JLabel("Fecha de referencia (yyyy-MM-dd):"));
        top.add(fechaFld);
        top.add(cargarFld);
        top.add(imprimirFld);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        cargarFld.addActionListener(e -> {
            if (fechaFld.getText().isEmpty()) {
                fechaFld.setBackground(ApplicationLogin.BACKGROUND_ERROR);
                return;
            }
            fechaFld.setBackground(null);
            try {
                controller.cargarSemana(fechaFld.getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        imprimirFld.addActionListener(e ->
                PdfReporter.imprimirDesdeTabla(panel, "Reporte de Actividades", tabla));
    }

    public JPanel getPanel() { return panel; }
    public void setController(Controller c) { this.controller = c; }
    public void setModel(Model m) { this.model = m; model.addPropertyChangeListener(this); }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Model.MATRIZ.equals(evt.getPropertyName())) {
            pintarMatriz(model.getMatriz());
        }
        panel.revalidate();
    }

    private void pintarMatriz(Map<String, Map<String, String>> matriz) {
        tableModel.setRowCount(0);
        if (matriz == null) {
            return;
        }
        Map<String, Map<String, String>> ordenado = new TreeMap<>(matriz);
        for (Map.Entry<String, Map<String, String>> fila : ordenado.entrySet()) {
            Object[] row = new Object[DIAS.length + 1];
            row[0] = fila.getKey();
            Map<String, String> porDia = fila.getValue();
            for (int i = 0; i < DIAS.length; i++) {
                String texto = (porDia != null) ? porDia.get(DIAS[i]) : null;
                row[i + 1] = (texto != null) ? texto : "";
            }
            tableModel.addRow(row);
        }
    }
}