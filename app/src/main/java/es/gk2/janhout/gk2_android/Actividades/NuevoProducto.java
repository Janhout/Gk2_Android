package es.gk2.janhout.gk2_android.actividades;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Hashtable;

import es.gk2.janhout.gk2_android.R;
import es.gk2.janhout.gk2_android.util.AsyncTaskGet;
import es.gk2.janhout.gk2_android.util.AsyncTaskPost;
import es.gk2.janhout.gk2_android.util.Constantes;
import es.gk2.janhout.gk2_android.util.Metodos;

public class NuevoProducto extends AppCompatActivity implements AsyncTaskPost.OnProcessCompleteListener, AsyncTaskGet.OnProcessCompleteListener {

    private EditText referencia;
    private EditText precio_compra;
    private EditText precio_venta;
    private EditText precio_impuestos;
    private EditText beneficio;
    private EditText unidad;
    private Spinner impuesto;
    private EditText descripcion;
    private CheckBox estado;
    private CheckBox controlStock;
    private CheckBox lotes;

    private double cImpuesto;
    private double cPrecioVenta;
    private double cPrecioCompra;
    private double cBeneficio;
    private double cPrecioImpuestos;

    ArrayList<EditText> componentes;

    private final int CODIGO_COMPROBAR_REFERENCIA = 1;
    private final int CODIGO_INSERTAR_PRODUCTO = 1;

    /* *************************************************************************
     **************************** Métodos on... ********************************
     *************************************************************************** */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_producto);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        referencia = (EditText) findViewById(R.id.nuevoProducto_referencia);
        precio_compra = (EditText) findViewById(R.id.nuevoProducto_precio_compra);
        precio_venta = (EditText) findViewById(R.id.nuevoProducto_precio_venta);
        precio_impuestos = (EditText) findViewById(R.id.nuevoProducto_precio_impuestos);
        impuesto = (Spinner) findViewById(R.id.nuevoProducto_impuesto);
        beneficio = (EditText) findViewById(R.id.nuevoProducto_beneficio);
        unidad = (EditText) findViewById(R.id.nuevoProducto_unidad);
        descripcion = (EditText) findViewById(R.id.nuevoProducto_descripcion);
        estado = (CheckBox) findViewById(R.id.nuevoProducto_estado);
        controlStock = (CheckBox) findViewById(R.id.nuevoProducto_control_stock);
        lotes = (CheckBox) findViewById(R.id.nuevoProducto_lotes);

        componentes = new ArrayList<>();
        componentes.add(referencia);
        componentes.add(precio_compra);
        componentes.add(beneficio);
        componentes.add(precio_venta);
        componentes.add(precio_impuestos);
        componentes.add(unidad);
        componentes.add(descripcion);

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.lista_iva, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        impuesto.setAdapter(adapter);
        impuesto.setSelection(1);
        impuesto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                recalcularPrecios(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                adapterView.setSelection(1);
            }
        });

        setListenerPrecios();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nuevo_producto, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_nuevo_producto) {
            nuevoProducto();
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void nuevoProducto() {
        //if (primerCheck()) {
            Hashtable<String, String> parametros = new Hashtable<>();
            parametros.put("q", referencia.getText().toString());
            AsyncTaskGet comprobarReferencia = new AsyncTaskGet(this, this, Constantes.PRODUCTOS_REFERENCIA, false, CODIGO_COMPROBAR_REFERENCIA);
            comprobarReferencia.execute(parametros);
       // }
    }

    private boolean primerCheck() {
        for (EditText componente : componentes) {
            if (componente.getText().toString().equals("")) {
                componente.requestFocus();
                Toast.makeText(this, R.string.e_no_vacio, Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    private void setListenerPrecios() {
        View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                    if (!hasFocus) {
                        if (!((EditText) view).getText().toString().equals(""))
                            recalcularPrecios(view);
                    }
                }
        };

        precio_compra.setOnFocusChangeListener(focusChangeListener);
        beneficio.setOnFocusChangeListener(focusChangeListener);
        precio_venta.setOnFocusChangeListener(focusChangeListener);
        precio_impuestos.setOnFocusChangeListener(focusChangeListener);
    }

    private void recalcularPrecios(View v) {
        cPrecioCompra = (Metodos.stringToDouble(precio_compra.getText().toString()));
        cImpuesto = (Metodos.stringToDouble(impuesto.getSelectedItem().toString())) / 100;

        if (v.equals(beneficio)) {
            cBeneficio = (Metodos.stringToDouble(beneficio.getText().toString()))/100;
            cPrecioVenta = cPrecioCompra + (cPrecioCompra * cBeneficio);
            cPrecioImpuestos = cPrecioVenta + (cPrecioVenta * cImpuesto);

            precio_venta.setText(Metodos.doubleToString(cPrecioVenta));
            precio_impuestos.setText(Metodos.doubleToString(cPrecioImpuestos));

        } else if (v.equals(precio_venta)) {
            cPrecioVenta = Metodos.stringToDouble(precio_venta.getText().toString());
            cPrecioImpuestos = cPrecioVenta + (cPrecioVenta * cImpuesto);
            cBeneficio = (100*(cPrecioVenta - cPrecioCompra))/cPrecioCompra;

            precio_impuestos.setText(Metodos.doubleToString(cPrecioImpuestos));
            beneficio.setText(Metodos.doubleToString(cBeneficio));

        } else if (v.equals(precio_impuestos)) {
            cPrecioImpuestos = Metodos.stringToDouble(precio_impuestos.getText().toString());
            cPrecioVenta = cPrecioImpuestos/(1 + cImpuesto);
            cBeneficio = (100*(cPrecioVenta - cPrecioCompra))/cPrecioCompra;

            precio_venta.setText(Metodos.doubleToString(cPrecioVenta));
            beneficio.setText(Metodos.doubleToString(cBeneficio));

        } else {
            cPrecioImpuestos = cPrecioVenta + (cPrecioVenta * cImpuesto);
            precio_impuestos.setText(Metodos.doubleToString(cPrecioImpuestos));
        }
    }

    @Override
    public void resultadoGet(String respuesta, int codigo_peticion) {
        switch (codigo_peticion) {
            case CODIGO_COMPROBAR_REFERENCIA:
                if (respuesta != null) {
                    if (respuesta.equals("[]")) {
                        Hashtable<String, String> params = new Hashtable<>();
                        params.put("accion", "insert");
                        params.put("referencia", referencia.getText().toString());
                        params.put("descripcion", descripcion.getText().toString());
                        params.put("notas", "klkg");
                        //TODO: necesitamos la lista de impuestos del servidor
                        params.put("tax", String.valueOf(impuesto.getSelectedItemPosition()));
                        params.put("precio_compra", precio_compra.getText().toString());
                        params.put("precio_venta", precio_venta.getText().toString());
                        params.put("precio_venta_tax", precio_impuestos.getText().toString());
                        params.put("estado", (estado.isChecked()) ? "1" : "0" );
                        params.put("control_stock", (controlStock.isChecked() ? "1" : "0"));
                        params.put("lotes", (lotes.isChecked() ? "1" : "0"));
                        //TODO: necesitamos la lista de unidades del servidor
                        params.put("medida", unidad.getText().toString());
                        params.put("id_a", "0");

                        AsyncTaskPost crearProducto = new AsyncTaskPost(this, this, Constantes.PRODUCTOS_INSERTAR, CODIGO_INSERTAR_PRODUCTO);
                        crearProducto.execute(params);
                    } else Toast.makeText(this, R.string.e_nuevo_producto_referencia_existe, Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void resultadoPost(String location, int codigo_peticion) {
        switch (codigo_peticion) {
            case CODIGO_INSERTAR_PRODUCTO:
                if (location != null && !location.contains("ver/0")) {
                    Toast.makeText(this, R.string.s_nuevo_producto_succeed, Toast.LENGTH_SHORT).show();
                }
        }
    }
}