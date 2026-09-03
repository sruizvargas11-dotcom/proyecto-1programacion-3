package una.eif206.presentation.reservas;

import una.eif206.logic.Reserva;
import una.eif206.presentation.AbstractTableModel;

import java.util.List;

public class TableModel extends AbstractTableModel<Reserva> {

    public static final int ID          = 0;
    public static final int ACTIVIDAD   = 1;
    public static final int FECHA       = 2;
    public static final int HORA_INICIO = 3;
    public static final int HORA_FIN    = 4;
    public static final int ESTADO      = 5;

    public TableModel(int[] cols, List<Reserva> rows) {
        super(cols, rows);
    }

    @Override
    protected void initColNames() {
        colNames = new String[6];
        colNames[ID]          = "ID";
        colNames[ACTIVIDAD]   = "Actividad";
        colNames[FECHA]       = "Fecha";
        colNames[HORA_INICIO] = "Hora Inicio";
        colNames[HORA_FIN]    = "Hora Fin";
        colNames[ESTADO]      = "Estado";
    }

    @Override
    protected Object getPropetyAt(Reserva e, int col) {
        switch (cols[col]) {
            case ID:          return e.getId();
            case ACTIVIDAD:   return e.getActividad();
            case FECHA:       return e.getFecha();
            case HORA_INICIO: return e.getHoraInicio();
            case HORA_FIN:    return e.getHoraFin();
            case ESTADO:      return e.getEstado();
            default:          return "";
        }
    }
}
