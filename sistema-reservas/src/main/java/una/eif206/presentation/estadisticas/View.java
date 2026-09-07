package una.eif206.presentation.estadisticas;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import una.eif206.ApplicationLogin;
import una.eif206.util.PdfReporter;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class View implements PropertyChangeListener {

    private JPanel panel;
    private JTextField desdeFld;
    private JTextField hastaFld;
    private JButton generarFld;
    private JButton imprimirFld;
    private ChartPanel panelRecursos;
    private ChartPanel panelActividades;

    Controller controller;
    Model model;

    public View() {
        panel      = new JPanel(new BorderLayout(5, 5));
        desdeFld   = new JTextField(10);
        hastaFld   = new JTextField(10);
        generarFld = new JButton("Generar");
        imprimirFld = new JButton("Imprimir PDF");

        JPanel filtros = new JPanel();
        filtros.add(new JLabel("Desde (yyyy-MM-dd):"));
        filtros.add(desdeFld);
        filtros.add(new JLabel("Hasta (yyyy-MM-dd):"));
        filtros.add(hastaFld);
        filtros.add(generarFld);
        filtros.add(imprimirFld);

        JFreeChart chartRecursos = ChartFactory.createBarChart(
                "Recursos más reservados", "Recurso", "Cantidad",
                new DefaultCategoryDataset(), PlotOrientation.VERTICAL,
                false, true, false);
        JFreeChart chartActividades = ChartFactory.createBarChart(
                "Actividades más frecuentes", "Actividad", "Cantidad",
                new DefaultCategoryDataset(), PlotOrientation.VERTICAL,
                false, true, false);

        panelRecursos    = new ChartPanel(chartRecursos);
        panelActividades = new ChartPanel(chartActividades);

        JPanel graficos = new JPanel(new GridLayout(1, 2, 5, 5));
        graficos.add(panelRecursos);
        graficos.add(panelActividades);

        panel.add(filtros, BorderLayout.NORTH);
        panel.add(graficos, BorderLayout.CENTER);

        generarFld.addActionListener(e -> {
            if (validate()) {
                try {
                    controller.generar(desdeFld.getText(), hastaFld.getText());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        imprimirFld.addActionListener(e -> {
            List<String[]> filasRecursos = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : model.getDatosRecursos().entrySet()) {
                filasRecursos.add(new String[]{entry.getKey(), String.valueOf(entry.getValue())});
            }
            List<String[]> filasActividades = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : model.getDatosActividades().entrySet()) {
                filasActividades.add(new String[]{entry.getKey(), String.valueOf(entry.getValue())});
            }

            List<PdfReporter.Seccion> secciones = new ArrayList<>();
            secciones.add(new PdfReporter.Seccion("Recursos más reservados",
                    new String[]{"Recurso", "Cantidad"}, filasRecursos));
            secciones.add(new PdfReporter.Seccion("Actividades más frecuentes",
                    new String[]{"Actividad", "Cantidad"}, filasActividades));

            PdfReporter.imprimirMultiple(panel, "Reporte de Estadísticas", secciones);
        });
    }

    public JPanel getPanel() { return panel; }
    public void setController(Controller c) { this.controller = c; }
    public void setModel(Model m) { this.model = m; model.addPropertyChangeListener(this); }

    private boolean validate() {
        boolean valid = true;
        if (desdeFld.getText().isEmpty()) {
            valid = false;
            desdeFld.setBackground(ApplicationLogin.BACKGROUND_ERROR);
        } else { desdeFld.setBackground(null); }
        if (hastaFld.getText().isEmpty()) {
            valid = false;
            hastaFld.setBackground(ApplicationLogin.BACKGROUND_ERROR);
        } else { hastaFld.setBackground(null); }
        return valid;
    }

    private DefaultCategoryDataset toDataset(Map<String, Integer> datos, String serieNombre) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> entry : datos.entrySet()) {
            dataset.addValue(entry.getValue(), serieNombre, entry.getKey());
        }
        return dataset;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.DATOS_RECURSOS:
                panelRecursos.getChart().getCategoryPlot()
                        .setDataset(toDataset(model.getDatosRecursos(), "Reservas"));
                break;
            case Model.DATOS_ACTIVIDADES:
                panelActividades.getChart().getCategoryPlot()
                        .setDataset(toDataset(model.getDatosActividades(), "Reservas"));
                break;
        }
        panel.revalidate();
    }
}