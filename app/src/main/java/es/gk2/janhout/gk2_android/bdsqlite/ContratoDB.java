package es.gk2.janhout.gk2_android.bdsqlite;

import android.provider.BaseColumns;

public class ContratoDB {

    private ContratoDB() {
    }

    public static abstract class TablaCliente implements BaseColumns {
        public static final String TABLA = "cliente";
        public static final String ID = "id";
        public static final String NOMBRE_COMERCIAL = "nombre_comercial";
        public static final String NIF = "nif";
        public static final String TELEFONO01 = "telefono01";
        public static final String TELEFONO02 = "telefono02";
        public static final String EMAIL = "email";
    }

    public static abstract class TablaFactura implements BaseColumns {
        public static final String TABLA = "factura";
        public static final String NUMERO_FACTURA = "numero_factura";
        public static final String FECHA_FACTURA = "fecha_factura";
        public static final String ESTADO_FACTURA = "estado_factura";
        public static final String IMPORTE_FACTURA = "importe_factura";
        public static final String IMPORTE_PAGADO = "importe_pagado";
        public static final String IMPRESO = "impreso";
        public static final String ENVIDADO = "envidado";
        public static final String ID_IMPRESION = "idimpresion";
    }
}