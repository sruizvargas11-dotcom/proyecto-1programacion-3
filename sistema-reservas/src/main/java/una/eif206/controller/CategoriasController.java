package una.eif206.controller;

import una.eif206.logic.CategoriaRecurso;
import una.eif206.logic.Service;
import una.eif206.model.CategoriasModel;
import una.eif206.view.CategoriasView;

public class CategoriasController {

    CategoriasView view;
    CategoriasModel model;

    public CategoriasController(CategoriasView view, CategoriasModel model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
        model.setList(Service.instance().findAllCategorias());
    }

    public void create(CategoriaRecurso e) throws Exception {
        Service.instance().createCategoria(e);
        model.setCurrent(new CategoriaRecurso());
        model.setList(Service.instance().findAllCategorias());
    }

    public void delete(CategoriaRecurso e) throws Exception {
        Service.instance().deleteCategoria(e);
        model.setCurrent(new CategoriaRecurso());
        model.setList(Service.instance().findAllCategorias());
    }

    public void update(CategoriaRecurso e) throws Exception {
        Service.instance().updateCategoria(e);
        model.setCurrent(new CategoriaRecurso());
        model.setList(Service.instance().findAllCategorias());
    }

    public void clear() {
        model.setCurrent(new CategoriaRecurso());
        model.setList(Service.instance().findAllCategorias());
    }

    public void edit(int row) {
        model.setCurrent(model.getList().get(row));
    }

    public void search(String descripcion) {
        CategoriaRecurso c = new CategoriaRecurso();
        c.setDescripcion(descripcion);
        model.setList(Service.instance().searchCategorias(c));
    }
}