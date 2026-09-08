package una.eif206.controller;

import una.eif206.logic.Service;
import una.eif206.model.EstadisticasModel;
import una.eif206.view.EstadisticasView;

public class EstadisticasController {

    EstadisticasView view;
    EstadisticasModel model;

    public EstadisticasController(EstadisticasView view, EstadisticasModel model) {
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