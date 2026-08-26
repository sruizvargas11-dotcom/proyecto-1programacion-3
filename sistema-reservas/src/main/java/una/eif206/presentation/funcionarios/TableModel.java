package una.eif206.presentation.funcionarios;

import una.eif206.logic.Funcionario;
import una.eif206.presentation.AbstractTableModel;
import java.util.List;

public class TableModel extends AbstractTableModel<Funcionario> {

    public static final int ID       = 0;
    public static final int NOMBRE   = 1;
    public static final int TELEFONO = 2;

    public TableModel(int[] cols, List<Funcionario> rows) {
        super(cols, rows);
    }

    @Override
    protected void initColNames() {
        colNames = new String[3];
        colNames[ID]       = "ID";
        colNames[NOMBRE]   = "Nombre";
        colNames[TELEFONO] = "Teléfono";
    }

    @Override
    protected Object getPropetyAt(Funcionario e, int col) {
        switch (cols[col]) {
            case ID:       return e.getId();
            case NOMBRE:   return e.getNombre();
            case TELEFONO: return e.getTelefono();
            default:       return "";
        }
    }
}