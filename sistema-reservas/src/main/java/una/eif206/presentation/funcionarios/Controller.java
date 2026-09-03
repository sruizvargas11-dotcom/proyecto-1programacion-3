package una.eif206.presentation.funcionarios;

import una.eif206.logic.Funcionario;
import una.eif206.logic.Service;

public class Controller {

    View view;
    Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
        model.setList(Service.instance().findAllFuncionarios());
    }

    public void create(Funcionario e) throws Exception {
        Service.instance().createFuncionario(e);
        model.setCurrent(new Funcionario());
        model.setList(Service.instance().findAllFuncionarios());
    }

    public void delete(Funcionario e) throws Exception {
        Service.instance().deleteFuncionario(e);
        model.setCurrent(new Funcionario());
        model.setList(Service.instance().findAllFuncionarios());
    }

    public void clear() {
        model.setCurrent(new Funcionario());
        model.setList(Service.instance().findAllFuncionarios());
    }

    public void edit(int row) {
        model.setCurrent(model.getList().get(row));
    }

    public void search(String nombre) {
        Funcionario f = new Funcionario();
        f.setNombre(nombre);
        model.setList(Service.instance().searchFuncionarios(f));
    }
    public void update(Funcionario f) throws Exception {
        Service.instance().updateFuncionario(f);
        model.setCurrent(new Funcionario());
        model.setList(Service.instance().findAllFuncionarios());
    }
}