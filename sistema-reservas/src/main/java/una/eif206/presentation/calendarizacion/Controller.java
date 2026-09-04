package una.eif206.presentation.calendarizacion;

import una.eif206.logic.CategoriaRecurso;
import una.eif206.logic.Service;

public class Controller {

    View view;
    Model model;

    public Controller(View view, Model model) {
        this.view  = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
        model.setCategorias(Service.instance().findAllCategorias());
    }

    public void cargar(String fecha, CategoriaRecurso categoria) throws Exception {
        if (fecha == null || fecha.isEmpty())
            throw new Exception("Debe ingresar una fecha");
        if (categoria == null)
            throw new Exception("Debe seleccionar una categoria");
        model.setFecha(fecha);
        model.setCategoria(categoria);
        model.setRecursos(Service.instance().findRecursosByCategoria(categoria));
        model.setMatriz(Service.instance().getCalendario(fecha, categoria));
    }
}
