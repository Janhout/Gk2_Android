package es.gk2.janhout.gk2_android.bdsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.database.SQLException;

import java.util.ArrayList;
import java.util.List;

import es.gk2.janhout.gk2_android.modelos.Factura;

public class GestorFacturaDB {

    private AyudanteDB abd;
    private SQLiteDatabase bd;

    public GestorFacturaDB(Context c) {
        abd = new AyudanteDB(c);
    }

    public void open() {
        bd = abd.getWritableDatabase();
    }

    public void openRead() {
        bd = abd.getReadableDatabase();
    }

    public void close() {
        abd.close();
    }

    public long insert(Factura objeto) {
        ContentValues valores = new ContentValues();
        valores.put(ContratoDB.TablaFactura.NUMERO_FACTURA, objeto.getNumeroFactura());
        valores.put(ContratoDB.TablaFactura.FECHA_FACTURA, objeto.getFechaFactura());
        valores.put(ContratoDB.TablaFactura.ESTADO_FACTURA, objeto.getEstadoFactura());
        valores.put(ContratoDB.TablaFactura.IMPORTE_FACTURA, objeto.getImporteFactura());
        valores.put(ContratoDB.TablaFactura.IMPORTE_PAGADO, objeto.getImportePagado());
        valores.put(ContratoDB.TablaFactura.IMPRESO, objeto.getImpreso());
        valores.put(ContratoDB.TablaFactura.ENVIDADO, objeto.getEnviado());
        valores.put(ContratoDB.TablaFactura.ID_IMPRESION, objeto.getIdImpresion());
        try {
            return bd.insert(ContratoDB.TablaFactura.TABLA, null, valores);
        }  catch (SQLException e) {
            return -1;
        }
    }

    public int delete(Factura objeto) {
        String condicion = ContratoDB.TablaFactura.NUMERO_FACTURA + " = ?";
        String[] argumentos = {objeto.getNumeroFactura() + ""};
        return bd.delete(ContratoDB.TablaFactura.TABLA, condicion, argumentos);
    }

    public int update(Factura objeto) {
        ContentValues valores = new ContentValues();
        valores.put(ContratoDB.TablaFactura.NUMERO_FACTURA, objeto.getNumeroFactura());
        valores.put(ContratoDB.TablaFactura.FECHA_FACTURA, objeto.getFechaFactura());
        valores.put(ContratoDB.TablaFactura.ESTADO_FACTURA, objeto.getEstadoFactura());
        valores.put(ContratoDB.TablaFactura.IMPORTE_FACTURA, objeto.getImporteFactura());
        valores.put(ContratoDB.TablaFactura.IMPORTE_PAGADO, objeto.getImportePagado());
        valores.put(ContratoDB.TablaFactura.IMPRESO, objeto.getImpreso());
        valores.put(ContratoDB.TablaFactura.ENVIDADO, objeto.getEnviado());
        valores.put(ContratoDB.TablaFactura.ID_IMPRESION, objeto.getIdImpresion());
        String condicion = ContratoDB.TablaFactura.NUMERO_FACTURA + " = ?";
        String[] argumentos = {objeto.getNumeroFactura() + ""};
        return bd.update(ContratoDB.TablaFactura.TABLA, valores, condicion, argumentos);
    }

    public List<Factura> select(String condicion, String[] parametros, String order) {
        List<Factura> alf = new ArrayList<>();
        Cursor cursor = bd.query(ContratoDB.TablaFactura.TABLA, null,
                condicion, parametros, null, null, order);
        cursor.moveToFirst();
        Factura objeto;
        while (!cursor.isAfterLast()) {
            objeto = getRow(cursor);
            alf.add(objeto);
            cursor.moveToNext();
        }
        cursor.close();
        return alf;
    }

    public static Factura getRow(Cursor c) {
        Factura objeto = new Factura();
        objeto.setNumeroFactura(c.getString(1));
        objeto.setFechaFactura(c.getString(2));
        objeto.setEstadoFactura(c.getInt(3));
        objeto.setImporteFactura(c.getFloat(4));
        objeto.setImportePagado(c.getFloat(5));
        objeto.setImpreso(c.getInt(6));
        objeto.setEnviado(c.getInt(7));
        objeto.setIdImpresion(c.getInt(8));
        return objeto;
    }

    public Cursor getCursor(String condicion, String[] parametros, String order) {
        return bd.query(ContratoDB.TablaFactura.TABLA, null, condicion, parametros, null, null, order);
    }

    public Cursor getCursor() {
        return getCursor(null, null, null);
    }

    public List<Factura> select() {
        return select(null, null, null);
    }
}
