package es.gk2.janhout.gk2_android.Actividades;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Hashtable;

import es.gk2.janhout.gk2_android.Estaticas.Constantes;
import es.gk2.janhout.gk2_android.Estaticas.GetAsyncTask;
import es.gk2.janhout.gk2_android.Estaticas.PostAsyncTask;
import es.gk2.janhout.gk2_android.R;

public class NuevoCliente extends ActionBarActivity implements PostAsyncTask.OnProcessCompleteListener, GetAsyncTask.OnProcessCompleteListener {
    private EditText inputNombreComercial;
    private EditText inputNIF;
    private EditText inputDireccion;
    private EditText inputEmail;
    private EditText inputTelefono1;
    private EditText inputTelefono2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_cliente);
        inputNombreComercial = (EditText) findViewById(R.id.nuevoCliente_inputNombreComercial);
        inputNIF = (EditText) findViewById(R.id.nuevoCliente_NIF);
        inputDireccion = (EditText) findViewById(R.id.nuevoCliente_direccion);
        inputEmail = (EditText) findViewById(R.id.nuevoCliente_email);
        inputTelefono1 = (EditText) findViewById(R.id.nuevoCliente_telefono1);
        inputTelefono2 = (EditText) findViewById(R.id.nuevoCliente_telefono2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nuevo_cliente, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void nuevoCliente(View v) {
        // Primera comprobación. Mira si los campos obligatorios están en blanco.
        if (inputNombreComercial.getText().toString().equals("")) {
            inputNombreComercial.requestFocus();
            Toast.makeText(this, R.string.e_nuevoCliente_nombre_vacio, Toast.LENGTH_SHORT).show();
        } else if (inputNIF.getText().toString().equals("")) {
            inputNIF.requestFocus();
            Toast.makeText(this, R.string.e_nuevoCliente_nif_vacio, Toast.LENGTH_SHORT).show();
        } else if (inputDireccion.getText().toString().equals("")) {
            inputDireccion.requestFocus();
            Toast.makeText(this, R.string.e_nuevoCliente_direccion_vacio, Toast.LENGTH_SHORT).show();
        } else if (inputTelefono1.getText().toString().equals("")) {
            inputTelefono1.requestFocus();
            Toast.makeText(this, R.string.e_nuevoCliente_telefono1_vacio, Toast.LENGTH_SHORT).show();
        } else {
            //Segunda comprobación. Mira si existe el DNI del cliente.
            GetAsyncTask hebraComprobarCliente = new GetAsyncTask(this, this, Constantes.CLIENTES_CONSULTA_NIF + "?nif=" + inputNIF.getText().toString(), false, true);
            hebraComprobarCliente.execute();
        }
    }

    @Override
    public void resultadoPost(String respuesta) {
        Log.v("respuesta", respuesta);
    }

    @Override
    public void resultadoGet(String respuesta) {
        //Si no existe el DNI del cliente, añade el registro.
        if (respuesta.equals("0")) {
            Hashtable<String, String> parametros = new Hashtable<>();
            parametros.put("inputNombreComercial", inputNombreComercial.getText().toString());
            parametros.put("inputNIF", inputNIF.getText().toString());
            parametros.put("inputTipoVia", "CL");
            parametros.put("inputVia", inputDireccion.getText().toString());
            parametros.put("inputNumero", "1");
            parametros.put("inputBloque", "2");
            parametros.put("inputPiso", "3");
            parametros.put("inputPuerta", "4");
            parametros.put("inputPais", "ESP");
            parametros.put("inputProvincia", "06");
            parametros.put("inputCodigoPostal", "06171");
            parametros.put("inputLocalidad", "06001950");
            parametros.put("inputEmail", inputEmail.getText().toString());
            parametros.put("inputWeb", "lolo.es");
            parametros.put("inputObservaciones", "sdkgjksdljg");
            parametros.put("inputTel01", inputTelefono1.getText().toString());
            parametros.put("inputTel02", inputTelefono2.getText().toString());
            parametros.put("inputTelFax", "215412515");
            parametros.put("tarifa", "NOR");
            parametros.put("inputDescuento", "0");
            parametros.put("inputBancoTitulo", "");
            parametros.put("inputEntidad", "");
            parametros.put("inputOficina", "");
            parametros.put("inputDC", "");
            parametros.put("inputCuenta", "");
            parametros.put("inputIBAN", "");
            parametros.put("inputModoIva", "1");
            parametros.put("inputIrpf", "0");
            parametros.put("action", "insert");
            PostAsyncTask myTask = new PostAsyncTask(this, this, Constantes.CLIENTES_ALTA_CLIENTE, false);
            myTask.execute(parametros);
        } else {
            inputNIF.requestFocus();
            Toast.makeText(this, R.string.e_nuevoCliente_nif_existe, Toast.LENGTH_SHORT).show();
        }
    }
}
