package una.eif206;

import una.eif206.logic.Service;
import una.eif206.presentation.Sesion;
import una.eif206.presentation.funcionarios.Controller;
import una.eif206.presentation.funcionarios.Model;
import una.eif206.presentation.funcionarios.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ApplicationLogin extends JFrame {

    public static final Color BACKGROUND_ERROR = new Color(255, 102, 102);

    private JTabbedPane tabs;

    public ApplicationLogin() {
        setTitle("Sistema de Reserva de Recursos");
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

        View fView   = new View();
        Model fModel = new Model();
        new Controller(fView, fModel);
        tabs.addTab("Funcionarios", fView.getPanel());

        add(tabs);
    }
//Prueba de configuracion de rama Thomas
    private static void doLogin() {
        una.eif206.presentation.login.View loginView =
                new una.eif206.presentation.login.View();
        una.eif206.presentation.login.Model loginModel =
                new una.eif206.presentation.login.Model();
        new una.eif206.presentation.login.Controller(loginView, loginModel);
        loginView.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        "javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e) {}

            doLogin();

            if (Sesion.isLoggedIn()) {
                new ApplicationLogin().setVisible(true);
            }
        });
    }
}
