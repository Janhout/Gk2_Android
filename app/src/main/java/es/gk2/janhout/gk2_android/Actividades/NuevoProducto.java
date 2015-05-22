package es.gk2.janhout.gk2_android.actividades;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import es.gk2.janhout.gk2_android.R;
import es.gk2.janhout.gk2_android.util.AsyncTaskPost;

public class NuevoProducto extends AppCompatActivity implements AsyncTaskPost.OnProcessCompleteListener {

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

    ArrayList<EditText> componentes;



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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.lista_iva, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        impuesto.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nuevo_producto, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
        primerCheck();
    }

    @Override
    public void resultadoPost(String location, int codigo_peticion) {

    }

    public boolean primerCheck() {
        for (EditText componente : componentes) {
            if (componente.getText().toString().equals("")) {
                componente.requestFocus();
                Toast.makeText(this, R.string.e_no_vacio, Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }
}
