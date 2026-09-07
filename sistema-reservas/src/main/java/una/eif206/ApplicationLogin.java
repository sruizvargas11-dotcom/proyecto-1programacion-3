package una.eif206;

import una.eif206.logic.Service;
import una.eif206.logic.enums.UsuarioRol;
import una.eif206.presentation.Sesion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ApplicationLogin extends JFrame {

    public static final Color BACKGROUND_ERROR = new Color(255, 102, 102);
    private JTabbedPane tabs;

    public ApplicationLogin() {
        setTitle("Sistema de Reserva de Recursos - " +
            Sesion.getUsuario().getId() + " (" +
            Sesion.getUsuario().getRol() + ")");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Service.instance().stop();
            }
        });

        tabs = new JTabbedPane();

        // ?? Crear todos los MVCs ??
        una.eif206.presentation.funcionarios.View fView = new una.eif206.presentation.funcionarios.View();
        una.eif206.presentation.funcionarios.Model fModel = new una.eif206.presentation.funcionarios.Model();
        new una.eif206.presentation.funcionarios.Controller(fView, fModel);

        una.eif206.presentation.categorias.View catView = new una.eif206.presentation.categorias.View();
        una.eif206.presentation.categorias.Model catModel = new una.eif206.presentation.categorias.Model();
        new una.eif206.presentation.categorias.Controller(catView, catModel);

        una.eif206.presentation.recursos.View recView = new una.eif206.presentation.recursos.View();
        una.eif206.presentation.recursos.Model recModel = new una.eif206.presentation.recursos.Model();
        new una.eif206.presentation.recursos.Controller(recView, recModel);

        una.eif206.presentation.reservas.View resView = new una.eif206.presentation.reservas.View();
        una.eif206.presentation.reservas.Model resModel = new una.eif206.presentation.reservas.Model();
        new una.eif206.presentation.reservas.Controller(resView, resModel);

        una.eif206.presentation.calendarizacion.View calView = new una.eif206.presentation.calendarizacion.View();
        una.eif206.presentation.calendarizacion.Model calModel = new una.eif206.presentation.calendarizacion.Model();
        new una.eif206.presentation.calendarizacion.Controller(calView, calModel);

        una.eif206.presentation.actividades.View actView = new una.eif206.presentation.actividades.View();
        una.eif206.presentation.actividades.Model actModel = new una.eif206.presentation.actividades.Model();
        new una.eif206.presentation.actividades.Controller(actView, actModel);

        una.eif206.presentation.estadisticas.View estView = new una.eif206.presentation.estadisticas.View();
        una.eif206.presentation.estadisticas.Model estModel = new una.eif206.presentation.estadisticas.Model();
        new una.eif206.presentation.estadisticas.Controller(estView, estModel);

        // ?? Tabs segun rol ??
        switch (Sesion.getUsuario().getRol()) {
            case ADMIN:
                tabs.addTab("Funcionarios",    fView.getPanel());
                tabs.addTab("Categorias",      catView.getPanel());
                tabs.addTab("Recursos",        recView.getPanel());
                tabs.addTab("Calendarizacion", calView.getPanel());
                tabs.addTab("Actividades",     actView.getPanel());
                tabs.addTab("Estadisticas",    estView.getPanel());
                break;
            case FUNCIONARIO:
                tabs.addTab("Reservas",        resView.getPanel());
                tabs.addTab("Calendarizacion", calView.getPanel());
                tabs.addTab("Actividades",     actView.getPanel());
                tabs.addTab("Estadisticas",    estView.getPanel());
                break;
        }

        add(tabs);
    }

    private static void doLogin() {
        una.eif206.presentation.login.View loginView = new una.eif206.presentation.login.View();
        una.eif206.presentation.login.Model loginModel = new una.eif206.presentation.login.Model();
        new una.eif206.presentation.login.Controller(loginView, loginModel);
        loginView.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e) {}

            doLogin();

            if (Sesion.isLoggedIn()) {
                new ApplicationLogin().setVisible(true);
            }
        });
    }
}
