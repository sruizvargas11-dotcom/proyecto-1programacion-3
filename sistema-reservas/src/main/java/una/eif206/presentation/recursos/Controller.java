package una.eif206.presentation.recursos;

import una.eif206.logic.CategoriaRecurso;
import una.eif206.logic.Recurso;
import una.eif206.logic.Service;

public class Controller {

    View view;
    Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
        model.setCategorias(Service.instance().findAllCategorias());
        model.setList(Service.instance().findAllRecursos());
    }

    public void create(Recurso e) throws Exception {
        Service.instance().createRecurso(e);
        model.setCurrent(new Recurso());
        model.setList(Service.instance().findAllRecursos());
    }

    public void delete(Recurso e) throws Exception {
        Service.instance().deleteRecurso(e);
        model.setCurrent(new Recurso());
        model.setList(Service.instance().findAllRecursos());
    }

    public void update(Recurso e) throws Exception {
        Service.instance().updateRecurso(e);
        model.setCurrent(new Recurso());
        model.setList(Service.instance().findAllRecursos());
    }

    public void clear() {
        model.setCurrent(new Recurso());
        model.setList(Service.instance().findAllRecursos());
    }

    public void edit(int row) {
        model.setCurrent(model.getList().get(row));
    }

    public void filtrarPorCategoria(CategoriaRecurso categoria) {
        if (categoria == null) {
            model.setList(Service.instance().findAllRecursos());
        } else {
            model.setList(Service.instance().findRecursosByCategoria(categoria));
        }
    }
}