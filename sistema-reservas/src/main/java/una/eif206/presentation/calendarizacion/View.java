package una.eif206.presentation.calendarizacion;

import una.eif206.ApplicationLogin;
import una.eif206.logic.CategoriaRecurso;
import una.eif206.logic.Recurso;
import una.eif206.util.PdfReporter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class View implements PropertyChangeListener {

    private JPanel panel;
    private JTextField fechaFld;
    private JComboBox<CategoriaRecurso> categoriaFld;
    private JButton cargarFld;
    private JButton imprimirFld;
    private JTable tabla;

    Controller controller;
    Model model;

    public View() {
        panel        = new JPanel(new BorderLayout(5, 5));
        fechaFld     = new JTextField(10);
        categoriaFld = new JComboBox<>();
        cargarFld    = new JButton("Cargar");
        imprimirFld  = new JButton("Imprimir PDF");
        tabla        = new JTable();
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtros.add(new JLabel("Fecha (yyyy-MM-dd):"));
        filtros.add(fechaFld);
        filtros.add(new JLabel("Categoria:"));
        filtros.add(categoriaFld);
        filtros.add(cargarFld);
        filtros.add(imprimirFld);

        panel.add(filtros, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        cargarFld.addActionListener(e -> {
            try {
                controller.cargar(
                        fechaFld.getText(),
                        (CategoriaRecurso) categoriaFld.getSelectedItem()
                );
            } catch (Exception ex) {
                fechaFld.setBackground(ApplicationLogin.BACKGROUND_ERROR);
                JOptionPane.showMessageDialog(panel, ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        imprimirFld.addActionListener(e ->
                PdfReporter.imprimirDesdeTabla(panel, "Reporte de Calendarización", tabla));
    }

    public JPanel getPanel() { return panel; }
    public void setController(Controller c) { this.controller = c; }
    public void setModel(Model m) { this.model = m; model.addPropertyChangeListener(this); }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.CATEGORIAS:
                categoriaFld.removeAllItems();
                for (CategoriaRecurso c : model.getCategorias()) {
                    categoriaFld.addItem(c);
                }
                break;
            case Model.MATRIZ:
                actualizarTabla();
                break;
        }
        panel.revalidate();
    }

    private void actualizarTabla() {
        String[][] matriz = model.getMatriz();
        List<Recurso> recursos = model.getRecursos();
        if (matriz == null || matriz.length == 0 || recursos == null || recursos.isEmpty()) return;

        String[] headers = new String[recursos.size() + 1];
        headers[0] = "Hora";
        for (int i = 0; i < recursos.size(); i++) {
            headers[i + 1] = recursos.get(i).getDescripcion();
        }

        String[][] data = new String[matriz.length][headers.length];
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < headers.length; j++) {
                data[i][j] = (j < matriz[i].length && matriz[i][j] != null) ? matriz[i][j] : "";
            }
        }

        DefaultTableModel tableModel = new DefaultTableModel(data, headers) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabla.setModel(tableModel);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(60);
        for (int i = 1; i < headers.length; i++) {
            tabla.getColumnModel().getColumn(i).setPreferredWidth(200);
        }
    }
}