package es.gk2.janhout.gk2_android.bdsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.database.SQLException;

import java.util.ArrayList;
import java.util.List;

import es.gk2.janhout.gk2_android.Util.Cliente;

public class GestorClienteDB {

    private AyudanteDB abd;
    private SQLiteDatabase bd;

    public GestorClienteDB(Context c) {
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

    public long insert(Cliente objeto) {
        ContentValues valores = new ContentValues();
        valores.put(ContratoDB.TablaCliente.ID, objeto.getId());
        valores.put(ContratoDB.TablaCliente.NOMBRE_COMERCIAL, objeto.getNombre_comercial());
        valores.put(ContratoDB.TablaCliente.NIF, objeto.getNif());
        valores.put(ContratoDB.TablaCliente.TELEFONO01, objeto.getTelefono01());
        valores.put(ContratoDB.TablaCliente.TELEFONO02, objeto.getTelefono02());
        valores.put(ContratoDB.TablaCliente.EMAIL, objeto.getEmail());
        try {
            return bd.insert(ContratoDB.TablaCliente.TABLA, null, valores);
        } catch (SQLException e) {
            return -1;
        }
    }

    public int delete(Cliente objeto) {
        String condicion = ContratoDB.TablaCliente.ID + " = ?";
        String[] argumentos = {objeto.getId() + ""};
        return bd.delete(ContratoDB.TablaCliente.TABLA, condicion, argumentos);
    }

    public int update(Cliente objeto) {
        ContentValues valores = new ContentValues();
        valores.put(ContratoDB.TablaCliente.ID, objeto.getId());
        valores.put(ContratoDB.TablaCliente.NOMBRE_COMERCIAL, objeto.getNombre_comercial());
        valores.put(ContratoDB.TablaCliente.NIF, objeto.getNif());
        valores.put(ContratoDB.TablaCliente.TELEFONO01, objeto.getTelefono01());
        valores.put(ContratoDB.TablaCliente.TELEFONO02, objeto.getTelefono02());
        valores.put(ContratoDB.TablaCliente.EMAIL, objeto.getEmail());
        String condicion = ContratoDB.TablaCliente.ID + " = ?";
        String[] argumentos = {objeto.getId() + ""};
        return bd.update(ContratoDB.TablaCliente.TABLA, valores, condicion, argumentos);
    }

    public List<Cliente> select(String condicion, String[] parametros, String order) {
        List<Cliente> alf = new ArrayList<>();
        Cursor cursor = bd.query(ContratoDB.TablaCliente.TABLA, null,
                condicion, parametros, null, null, order);
        cursor.moveToFirst();
        Cliente objeto;
        while (!cursor.isAfterLast()) {
            objeto = getRow(cursor);
            alf.add(objeto);
            cursor.moveToNext();
        }
        cursor.close();
        return alf;
    }

    public static Cliente getRow(Cursor c) {
        Cliente objeto = new Cliente();
        objeto.setId(c.getInt(1));
        objeto.setNombre_comercial(c.getString(2));
        objeto.setTelefono01(c.getString(3));
        objeto.setTelefono02(c.getString(4));
        objeto.setEmail(c.getString(5));
        return objeto;
    }

    public Cursor getCursor(String condicion, String[] parametros, String order) {
        return bd.query(ContratoDB.TablaCliente.TABLA, null, condicion, parametros, null, null, order);
    }

    public Cursor getCursor() {
        return getCursor(null, null, null);
    }

    public List<Cliente> select() {
        return select(null, null, null);
    }
}
