package es.gk2.janhout.gk2_android.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Hashtable;

import es.gk2.janhout.gk2_android.R;
import es.gk2.janhout.gk2_android.adaptadores.AdaptadorAutoCompleteTextView;
import es.gk2.janhout.gk2_android.modelos.Cliente;
import es.gk2.janhout.gk2_android.modelos.Localidad;
import es.gk2.janhout.gk2_android.modelos.Provincia;
import es.gk2.janhout.gk2_android.modelos.TipoDireccion;
import es.gk2.janhout.gk2_android.util.AsyncTaskGet;
import es.gk2.janhout.gk2_android.util.AsyncTaskPost;
import es.gk2.janhout.gk2_android.util.Constantes;

public class NuevoCliente extends AppCompatActivity implements AsyncTaskPost.OnProcessCompleteListener, AsyncTaskGet.OnProcessCompleteListener {

    private EditText inputNombreComercial;
    private EditText inputNIF;
    private EditText inputDireccion;
    private EditText inputNumero;
    private EditText inputBloque;
    private EditText inputPiso;
    private EditText inputPuerta;
    private EditText inputEmail;
    private EditText inputTelefono1;
    private EditText inputTelefono2;

    private AutoCompleteTextView inputProvincia;
    private AutoCompleteTextView inputLocalidad;
    private AutoCompleteTextView inputTipoDireccion;

    private ArrayList<Object> listaProvincias;
    private ArrayList<Object> listaLocalidades;
    private ArrayList<Object> listaTiposDireccion;

    private static final int CODIGO_COMPROBAR_DNI = 1;
    private static final int CODIGO_NUEVO_CLIENTE = 2;
    private static final int CODIGO_GET_PROVINCIAS = 3;
    private static final int CODIGO_GET_LOCALIDADES = 4;
    private static final int CODIGO_GET_TIPOS_DIRECCION = 5;

    private String PROVINCIA_SELECCIONADA;
    private String LOCALIDAD_SELECCIONADA;
    private String CODIGO_POSTAL_SELECCIONADO;
    private String TIPO_VIA_SELECCIONADA;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (PROVINCIA_SELECCIONADA != null) {
            outState.putString("Provincia", PROVINCIA_SELECCIONADA);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        PROVINCIA_SELECCIONADA = savedInstanceState.getString("Provincia");
        if (PROVINCIA_SELECCIONADA != null) {
            AsyncTaskGet cargarLocalidades = new AsyncTaskGet(NuevoCliente.this, NuevoCliente.this, Constantes.LOCALIDADES + PROVINCIA_SELECCIONADA, false, CODIGO_GET_LOCALIDADES);
            cargarLocalidades.execute(new Hashtable<String, String>());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_cliente);
        inicializarToolbar();

        listaProvincias = new ArrayList<>();
        listaLocalidades = new ArrayList<>();
        listaTiposDireccion = new ArrayList<>();

        inputNombreComercial = (EditText) findViewById(R.id.nuevoCliente_inputNombreComercial);
        inputNIF = (EditText) findViewById(R.id.nuevoCliente_NIF);
        inputDireccion = (EditText) findViewById(R.id.nuevoCliente_direccion);
        inputNumero = (EditText) findViewById(R.id.nuevoCliente_numero);
        inputBloque = (EditText) findViewById(R.id.nuevoCliente_bloque);
        inputPiso = (EditText) findViewById(R.id.nuevoCliente_piso);
        inputPuerta = (EditText) findViewById(R.id.nuevoCliente_puerta);
        inputEmail = (EditText) findViewById(R.id.nuevoCliente_email);
        inputTelefono1 = (EditText) findViewById(R.id.nuevoCliente_telefono1);
        inputTelefono2 = (EditText) findViewById(R.id.nuevoCliente_telefono2);
        inputProvincia = (AutoCompleteTextView) findViewById(R.id.nuevoCliente_provincia);
        inputLocalidad = (AutoCompleteTextView) findViewById(R.id.nuevoCliente_localidad);
        inputTipoDireccion = (AutoCompleteTextView) findViewById(R.id.nuevoCliente_tipoDireccion);

        iniciarSpinners();

        AsyncTaskGet cargarProvincias = new AsyncTaskGet(this, this, Constantes.PROVINCIAS, false, CODIGO_GET_PROVINCIAS);
        cargarProvincias.execute(new Hashtable<String, String>());
        AsyncTaskGet cargarTiposDireccion = new AsyncTaskGet(this, this, Constantes.TIPOS_DIRECCION, false, CODIGO_GET_TIPOS_DIRECCION);
        cargarTiposDireccion.execute(new Hashtable<String, String>());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nuevo_cliente, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_nuevoCliente_insertar) {
            nuevoCliente();
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void nuevoCliente() {
        // Primera comprobación. Mira si los campos obligatorios están en blanco.
        if (inputNombreComercial.getText().toString().equals("")) {
            inputNombreComercial.requestFocus();
            Toast.makeText(this, R.string.e_nuevoCliente_nombre_vacio, Toast.LENGTH_SHORT).show();
        } else if (inputNIF.getText().toString().equals("")) {
            inputNIF.requestFocus();
            Toast.makeText(this, R.string.e_nuevoCliente_nif_vacio, Toast.LENGTH_SHORT).show();
        } else if (inputProvincia.getText().toString().equals("")) {
            inputProvincia.requestFocus();
            Toast.makeText(this, R.string.e_nuevoCliente_provincia_vacia, Toast.LENGTH_LONG).show();
        } else if (inputLocalidad.getText().toString().equals("")) {
            inputLocalidad.requestFocus();
            Toast.makeText(this, R.string.e_nuevoCliente_localidad_vacia, Toast.LENGTH_LONG).show();
        } else if (inputTipoDireccion.getText().toString().equals("")) {
            inputTipoDireccion.requestFocus();
            Toast.makeText(this, R.string.e_nuevoCliente_tipoDireccion_vacio, Toast.LENGTH_LONG).show();
        } else if (inputDireccion.getText().toString().equals("")) {
            inputDireccion.requestFocus();
            Toast.makeText(this, R.string.e_nuevoCliente_direccion_vacio, Toast.LENGTH_SHORT).show();
        } else if (inputTelefono1.getText().toString().equals("")) {
            inputTelefono1.requestFocus();
            Toast.makeText(this, R.string.e_nuevoCliente_telefono1_vacio, Toast.LENGTH_SHORT).show();

        } else {
            //Segunda comprobación. Mira si existe el DNI del cliente.
            Hashtable<String, String> parametros = new Hashtable<>();
            parametros.put("nif", inputNIF.getText().toString());
            AsyncTaskGet comprobarCliente = new AsyncTaskGet(this, this, Constantes.CLIENTES_CONSULTA_NIF, false, CODIGO_COMPROBAR_DNI);
            comprobarCliente.execute(parametros);
        }
    }

    @Override
    public void resultadoPost(String location, int codigo) {
        switch (codigo) {
            case CODIGO_NUEVO_CLIENTE:
                if (location != null && !location.contains("login")) {
                    if (location != null && !location.contains("ver/0")) {
                        Toast.makeText(this, R.string.s_nuevoCliente_insertar_correcto, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(this, MostrarCliente.class);
                        Bundle b = new Bundle();
                        Cliente cliente = new Cliente(Integer.parseInt(location.substring(location.lastIndexOf("/") + 1)),
                                inputNombreComercial.getText().toString(), inputNIF.getText().toString(),
                                inputTelefono1.getText().toString(), inputTelefono2.getText().toString(),
                                inputEmail.getText().toString());
                        b.putParcelable("cliente", cliente);
                        i.putExtras(b);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(this, R.string.e_nuevoCliente_error_insertar, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, R.string.e_sesion_expirada, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(this, Login.class);
                    startActivity(i);
                    finish();
                }
                break;
        }
    }

    @Override
    public void resultadoGet(String respuesta, int codigo) {
        switch (codigo) {
            case CODIGO_COMPROBAR_DNI:
                //Si no existe el cliente (respuesta = 0), se añade el cliente
                if (respuesta.equals("0")) {
                    Hashtable<String, String> parametros = new Hashtable<>();
                    parametros.put("inputNombreComercial", inputNombreComercial.getText().toString());
                    parametros.put("inputNIF", inputNIF.getText().toString());
                    parametros.put("inputTipoVia", TIPO_VIA_SELECCIONADA);
                    parametros.put("inputVia", inputDireccion.getText().toString());
                    parametros.put("inputNumero", inputNumero.getText().toString());
                    parametros.put("inputBloque", inputBloque.getText().toString());
                    parametros.put("inputPiso", inputPiso.getText().toString());
                    parametros.put("inputPuerta", inputPuerta.getText().toString());
                    parametros.put("inputPais", "ESP");
                    parametros.put("inputProvincia", PROVINCIA_SELECCIONADA);
                    parametros.put("inputCodigoPostal", CODIGO_POSTAL_SELECCIONADO);
                    parametros.put("inputLocalidad", LOCALIDAD_SELECCIONADA);
                    parametros.put("inputEmail", "pepesillo@toloquepuedas.com");//inputEmail.getText().toString());
                    parametros.put("inputWeb", "");
                    parametros.put("inputObservaciones", "");
                    parametros.put("inputTel01", inputTelefono1.getText().toString());
                    parametros.put("inputTel02", inputTelefono2.getText().toString());
                    parametros.put("inputTelFax", "");
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

                    //Petición para añadir el cliente
                    AsyncTaskPost nuevoCliente = new AsyncTaskPost(this, this, Constantes.CLIENTES_ALTA_CLIENTE, CODIGO_NUEVO_CLIENTE);
                    nuevoCliente.execute(parametros);
                } else if (respuesta.equals("2")) {
                    //El NIF no sigue el formato requerido
                    inputNIF.requestFocus();
                    Toast.makeText(this, R.string.e_nuevoCliente_nif_erroneo, Toast.LENGTH_SHORT).show();
                } else {
                    //Si el NIF del cliente existe, se avisa al usuario y se le pone foco al campo
                    inputNIF.requestFocus();
                    Toast.makeText(this, R.string.e_nuevoCliente_nif_existe, Toast.LENGTH_SHORT).show();
                }
                break;

            case CODIGO_GET_LOCALIDADES:
                if (respuesta != null) {
                    JSONTokener token = new JSONTokener(respuesta);
                    JSONArray array;
                    try {
                        array = new JSONArray(token);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            listaLocalidades.add(new Localidad(obj));
                        }
                        AdaptadorAutoCompleteTextView adLocalidades = new AdaptadorAutoCompleteTextView(this, android.R.layout.simple_dropdown_item_1line, listaLocalidades);
                        inputLocalidad.setAdapter(adLocalidades);
                    } catch (JSONException e) {
                        listaLocalidades = null;
                    }
                }
                break;

            case CODIGO_GET_PROVINCIAS:
                if (respuesta != null) {
                    JSONTokener token = new JSONTokener(respuesta);
                    JSONArray array;
                    try {
                        array = new JSONArray(token);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            listaProvincias.add(new Provincia(obj));
                        }

                        AdaptadorAutoCompleteTextView adProvincias = new AdaptadorAutoCompleteTextView(this, android.R.layout.simple_dropdown_item_1line, listaProvincias);
                        inputProvincia.setAdapter(adProvincias);
                    } catch (JSONException e) {
                        listaProvincias = null;
                    }
                }
                break;

            case CODIGO_GET_TIPOS_DIRECCION:
                if (respuesta != null) {
                    JSONTokener token = new JSONTokener(respuesta);
                    JSONArray array;
                    try {
                        array = new JSONArray(token);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            listaTiposDireccion.add(new TipoDireccion(obj));
                        }
                        AdaptadorAutoCompleteTextView adTiposDireccion = new AdaptadorAutoCompleteTextView(this, android.R.layout.simple_dropdown_item_1line, listaTiposDireccion);
                        inputTipoDireccion.setAdapter(adTiposDireccion);
                    } catch (JSONException e) {
                        listaTiposDireccion = null;
                    }
                }
                break;
        }


    }

    public void iniciarSpinners() {

        inputProvincia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listaLocalidades.clear();
                inputLocalidad.setText("");
                AsyncTaskGet cargarLocalidades = new AsyncTaskGet(NuevoCliente.this, NuevoCliente.this, Constantes.LOCALIDADES + ((Provincia) listaProvincias.get(i)).getIdProvincia(), false, CODIGO_GET_LOCALIDADES);
                cargarLocalidades.execute(new Hashtable<String, String>());
                PROVINCIA_SELECCIONADA = ((Provincia) listaProvincias.get(i)).getIdProvincia();
            }
        });

        inputLocalidad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LOCALIDAD_SELECCIONADA = ((Localidad) listaLocalidades.get(i)).getIdLocalidad();
                CODIGO_POSTAL_SELECCIONADO = ((Localidad) listaLocalidades.get(i)).getCpLocalidad();
            }
        });

        inputTipoDireccion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TIPO_VIA_SELECCIONADA = ((TipoDireccion) listaTiposDireccion.get(i)).getIdTipoDireccion();
            }
        });
    }

    private void inicializarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
