package una.eif206;

import una.eif206.controller.ActividadesController;
import una.eif206.controller.CalendarizacionController;
import una.eif206.controller.CambiarClaveController;
import una.eif206.controller.CategoriasController;
import una.eif206.controller.EstadisticasController;
import una.eif206.controller.FuncionariosController;
import una.eif206.controller.LoginController;
import una.eif206.controller.RecursosController;
import una.eif206.controller.ReservasController;
import una.eif206.logic.Service;
import una.eif206.model.ActividadesModel;
import una.eif206.model.CalendarizacionModel;
import una.eif206.model.CambiarClaveModel;
import una.eif206.model.CategoriasModel;
import una.eif206.model.EstadisticasModel;
import una.eif206.model.FuncionariosModel;
import una.eif206.model.LoginModel;
import una.eif206.model.RecursosModel;
import una.eif206.model.ReservasModel;
import una.eif206.util.Sesion;
import una.eif206.view.ActividadesView;
import una.eif206.view.CalendarizacionView;
import una.eif206.view.CambiarClaveView;
import una.eif206.view.CategoriasView;
import una.eif206.view.EstadisticasView;
import una.eif206.view.FuncionariosView;
import una.eif206.view.LoginView;
import una.eif206.view.RecursosView;
import una.eif206.view.ReservasView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ApplicationLogin extends JFrame {

    public static final Color BACKGROUND_ERROR = new Color(255, 102, 102);
    private JTabbedPane tabs;

    public ApplicationLogin() {
        if (Sesion.getUsuario() == null) {
            throw new IllegalStateException("No hay usuario en sesion");
        }

        setTitle("Sistema de Reserva de Recursos - " +
                Sesion.getUsuario().getId() + " (" +
                Sesion.getUsuario().getRol() + ")");
        setSize(1050, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Service.instance().stop();
            }
        });

        tabs = new JTabbedPane();

        //  Crear todos los MVCs
        FuncionariosView fView = new FuncionariosView();
        FuncionariosModel fModel = new FuncionariosModel();
        new FuncionariosController(fView, fModel);

        CategoriasView catView = new CategoriasView();
        CategoriasModel catModel = new CategoriasModel();
        new CategoriasController(catView, catModel);

        RecursosView recView = new RecursosView();
        RecursosModel recModel = new RecursosModel();
        new RecursosController(recView, recModel);

        ReservasView resView = new ReservasView();
        ReservasModel resModel = new ReservasModel();
        new ReservasController(resView, resModel);

        CalendarizacionView calView = new CalendarizacionView();
        CalendarizacionModel calModel = new CalendarizacionModel();
        new CalendarizacionController(calView, calModel);

        ActividadesView actView = new ActividadesView();
        ActividadesModel actModel = new ActividadesModel();
        new ActividadesController(actView, actModel);

        EstadisticasView estView = new EstadisticasView();
        EstadisticasModel estModel = new EstadisticasModel();
        new EstadisticasController(estView, estModel);

        //  Tabs segun rol
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

        JPanel topPanel = new JPanel(new BorderLayout());
        JButton cambiarClaveBtn = new JButton("Cambiar Clave");
        cambiarClaveBtn.addActionListener(e -> {
            CambiarClaveView cv = new CambiarClaveView();
            CambiarClaveModel cm = new CambiarClaveModel();
            new CambiarClaveController(cv, cm, Sesion.getUsuario());
            cv.setVisible(true);
        });
        topPanel.add(cambiarClaveBtn, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private static void doLogin() {
        LoginView loginView = new LoginView();
        LoginModel loginModel = new LoginModel();
        new LoginController(loginView, loginModel);
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