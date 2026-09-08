package una.eif206.controller;

import una.eif206.logic.Service;
import una.eif206.model.ActividadesModel;
import una.eif206.view.ActividadesView;

public class ActividadesController {

    ActividadesView view;
    ActividadesModel model;

    public ActividadesController(ActividadesView view, ActividadesModel model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
    }

    public void cargarSemana(String fechaReferencia) throws Exception {
        model.setFechaReferencia(fechaReferencia);
        model.setMatriz(Service.instance().getActividadesSemana(fechaReferencia));
    }
}
