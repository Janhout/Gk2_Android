package es.gk2.janhout.gk2_android.bdsqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AyudanteDB extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "gk2.db";
    public static final int DATABASE_VERSION = 1;

    public AyudanteDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql;
        sql = "create table " + ContratoDB.TablaCliente.TABLA +
                " (" + ContratoDB.TablaCliente._ID +
                " integer primary key autoincrement, " +
                ContratoDB.TablaCliente.ID + " integer, " +
                ContratoDB.TablaCliente.NOMBRE_COMERCIAL + " text, " +
                ContratoDB.TablaCliente.NIF + " text, " +
                ContratoDB.TablaCliente.TELEFONO01 + " text, " +
                ContratoDB.TablaCliente.TELEFONO02 + " text, " +
                ContratoDB.TablaCliente.EMAIL + " text)";
        db.execSQL(sql);

        sql = "create table " + ContratoDB.TablaFactura.TABLA +
                " (" + ContratoDB.TablaFactura._ID +
                " integer primary key autoincrement, " +
                ContratoDB.TablaFactura.NUMERO_FACTURA + " text, " +
                ContratoDB.TablaFactura.FECHA_FACTURA + " text, " +
                ContratoDB.TablaFactura.ESTADO_FACTURA + " integer, " +
                ContratoDB.TablaFactura.IMPORTE_FACTURA + " real, " +
                ContratoDB.TablaFactura.IMPORTE_PAGADO + " real, " +
                ContratoDB.TablaFactura.IMPRESO + " integer, " +
                ContratoDB.TablaFactura.ENVIDADO + " integer, " +
                ContratoDB.TablaFactura.ID_IMPRESION + " integer)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop database";
        db.execSQL(sql);
        onCreate(db);
    }
}
