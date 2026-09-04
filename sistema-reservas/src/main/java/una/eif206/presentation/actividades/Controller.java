package una.eif206.presentation.actividades;

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

    public void cargarSemana(String fechaReferencia) throws Exception {
        model.setFechaReferencia(fechaReferencia);
        model.setMatriz(Service.instance().getActividadesSemana(fechaReferencia));
    }
}
