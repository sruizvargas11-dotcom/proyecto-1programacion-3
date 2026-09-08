package una.eif206.controller;

import una.eif206.logic.CategoriaRecurso;
import una.eif206.logic.Service;
import una.eif206.model.CalendarizacionModel;
import una.eif206.view.CalendarizacionView;

public class CalendarizacionController {

    CalendarizacionView view;
    CalendarizacionModel model;

    public CalendarizacionController(CalendarizacionView view, CalendarizacionModel model) {
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
