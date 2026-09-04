package una.eif206.presentation.categorias;

import una.eif206.logic.CategoriaRecurso;
import una.eif206.presentation.AbstractTableModel;
import java.util.List;

public class TableModel extends AbstractTableModel<CategoriaRecurso> {

    public static final int ID          = 0;
    public static final int DESCRIPCION = 1;

    public TableModel(int[] cols, List<CategoriaRecurso> rows) {
        super(cols, rows);
    }

    @Override
    protected void initColNames() {
        colNames = new String[2];
        colNames[ID]          = "ID";
        colNames[DESCRIPCION] = "Descripción";
    }

    @Override
    protected Object getPropetyAt(CategoriaRecurso e, int col) {
        switch (cols[col]) {
            case ID:          return e.getId();
            case DESCRIPCION: return e.getDescripcion();
            default:          return "";
        }
    }
}
