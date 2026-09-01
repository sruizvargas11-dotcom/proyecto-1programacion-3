package una.eif206.presentation.recursos;

import una.eif206.logic.Recurso;
import una.eif206.presentation.AbstractTableModel;
import java.util.List;

public class TableModel extends AbstractTableModel<Recurso> {

    public static final int ID          = 0;
    public static final int DESCRIPCION = 1;
    public static final int CATEGORIA   = 2;

    public TableModel(int[] cols, List<Recurso> rows) {
        super(cols, rows);
    }

    @Override
    protected void initColNames() {
        colNames = new String[3];
        colNames[ID]          = "ID";
        colNames[DESCRIPCION] = "Descripción";
        colNames[CATEGORIA]   = "Categoría";
    }

    @Override
    protected Object getPropetyAt(Recurso e, int col) {
        switch (cols[col]) {
            case ID:          return e.getId();
            case DESCRIPCION: return e.getDescripcion();
            case CATEGORIA:   return e.getCategoria() != null ? e.getCategoria().getDescripcion() : "";
            default:          return "";
        }
    }
}

