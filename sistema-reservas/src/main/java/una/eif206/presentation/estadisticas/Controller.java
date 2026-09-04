package una.eif206.presentation.estadisticas;

import una.eif206.logic.Service;

public class Controller {

    View view;
    Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
    }

    public void generar(String desde, String hasta) throws Exception {
        model.setDatosRecursos(Service.instance().getEstadisticasRecursos(desde, hasta));
        model.setDatosActividades(Service.instance().getEstadisticasActividades(desde, hasta));
    }
}