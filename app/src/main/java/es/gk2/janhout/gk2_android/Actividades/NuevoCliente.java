package es.gk2.janhout.gk2_android.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import es.gk2.janhout.gk2_android.Adaptadores.AdaptadorSpinner;
import es.gk2.janhout.gk2_android.Estaticas.AsyncTaskGet;
import es.gk2.janhout.gk2_android.Estaticas.AsyncTaskPost;
import es.gk2.janhout.gk2_android.Estaticas.Constantes;
import es.gk2.janhout.gk2_android.R;
import es.gk2.janhout.gk2_android.Util.Localidad;
import es.gk2.janhout.gk2_android.Util.Provincia;
import es.gk2.janhout.gk2_android.Util.TipoDireccion;

public class NuevoCliente extends ActionBarActivity implements AsyncTaskPost.OnProcessCompleteListener, AsyncTaskGet.OnProcessCompleteListener {

    private Toolbar toolbar;
    private EditText inputNombreComercial;
    private EditText inputNIF;
    private EditText inputDireccion;
    private EditText inputEmail;
    private EditText inputTelefono1;
    private EditText inputTelefono2;

    private Spinner inputProvincia;
    private Spinner inputLocalidad;
    private Spinner inputTipoDireccion;

    private AdaptadorSpinner adProvincias;
    private AdaptadorSpinner adLocalidades;
    private AdaptadorSpinner adTiposDireccion;

    private List<Provincia> listaProvincias;
    private List<Localidad> listaLocalidades;
    private List<TipoDireccion> listaTiposDireccion;

    private static final int CODIGO_COMPROBAR_DNI = 1;
    private static final int CODIGO_NUEVO_CLIENTE = 2;
    private static final int CODIGO_GET_PROVINCIAS = 3;
    private static final int CODIGO_GET_LOCALIDADES = 4;
    private static final int CODIGO_GET_TIPOS_DIRECCION = 5;

    private static String PROVINCIA_SELECCIONADA;
    private static String LOCALIDAD_SELECCIONADA;
    private static String CODIGO_POSTAL_SELECCIONADO;
    private static String TIPO_VIA_SELECCIONADA;


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
        inputEmail = (EditText) findViewById(R.id.nuevoCliente_email);
        inputTelefono1 = (EditText) findViewById(R.id.nuevoCliente_telefono1);
        inputTelefono2 = (EditText) findViewById(R.id.nuevoCliente_telefono2);
        inputProvincia = (Spinner) findViewById(R.id.nuevoCliente_provincia);
        inputLocalidad = (Spinner) findViewById(R.id.nuevoCliente_localidad);
        inputTipoDireccion = (Spinner) findViewById(R.id.nuevoCliente_tipoDireccion);

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
            AsyncTaskGet hebraComprobarCliente = new AsyncTaskGet(this, this, Constantes.CLIENTES_CONSULTA_NIF, false, CODIGO_COMPROBAR_DNI);
            hebraComprobarCliente.execute(parametros);
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
                        b.putInt("cliente", Integer.parseInt(location.substring(location.lastIndexOf("/")+1)));
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
                    parametros.put("inputNumero", "1");
                    parametros.put("inputBloque", "2");
                    parametros.put("inputPiso", "3");
                    parametros.put("inputPuerta", "4");
                    parametros.put("inputPais", "ESP");
                    parametros.put("inputProvincia", PROVINCIA_SELECCIONADA);
                    parametros.put("inputCodigoPostal", CODIGO_POSTAL_SELECCIONADO);
                    parametros.put("inputLocalidad", LOCALIDAD_SELECCIONADA);
                    parametros.put("inputEmail", "hahasjhfjahsf@kas.com");//inputEmail.getText().toString());
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

                    //Petición para añadir el cliente
                    AsyncTaskPost nuevoCliente = new AsyncTaskPost(this, this, Constantes.CLIENTES_ALTA_CLIENTE, CODIGO_NUEVO_CLIENTE);
                    nuevoCliente.execute(parametros);
                } else if(respuesta.equals("2")) {
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
                        if (adLocalidades != null) {
                            adLocalidades.notifyDataSetChanged();
                        }
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
                        if (adProvincias != null) {
                            adProvincias.notifyDataSetChanged();
                        }
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
                        if (adTiposDireccion != null) {
                            adTiposDireccion.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        listaTiposDireccion = null;
                    }
                }
                break;
        }


    }

    /**
     * Este método se encarga de crear los adaptadores de los spinners, añadíselos
     * y crear los listener para los spinners.
     */
    public void iniciarSpinners() {
        //Se crean los adaptadores para los spinners
        adTiposDireccion = new AdaptadorSpinner(this, R.layout.detalle_spinner, R.id.tvSpinner, listaTiposDireccion);
        adLocalidades = new AdaptadorSpinner(this, R.layout.detalle_spinner, R.id.tvSpinner, listaLocalidades);
        adProvincias = new AdaptadorSpinner(this, R.layout.detalle_spinner, R.id.tvSpinner, listaProvincias);

        //Se le pone el adaptador a los spinners
        inputProvincia.setAdapter(adProvincias);
        inputLocalidad.setAdapter(adLocalidades);
        inputTipoDireccion.setAdapter(adTiposDireccion);

        /**
         * Listener spinner de provincias. Cada vez que se selecciona una provincia del spinner, se vacía
         * la lista de localidades (por defecto aparece seleccionada Álava al iniciar la app, así que se
         * cargan las localidades de Álava) para que no se sigan añadiendo localidades de otras provincias
         * al spinner. Tras vaciar la lista, se crea una hebra para volver a llenar la lista de localidades,
         * pero esta vez, de la provincia elegida. Finalmente, se guarda en una variable el código de la provincia
         * seleccionada.
         */
        inputProvincia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                listaLocalidades.clear();
                AsyncTaskGet cargarLocalidades = new AsyncTaskGet(NuevoCliente.this, NuevoCliente.this, Constantes.LOCALIDADES + listaProvincias.get(i).getIdProvincia(), false, CODIGO_GET_LOCALIDADES);
                cargarLocalidades.execute(new Hashtable<String, String>());
                PROVINCIA_SELECCIONADA = listaProvincias.get(i).getIdProvincia();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /**
         * Listener spinner de localidades. Cuando se selecciona una localidad, se guardan en dos variables distintas
         * el código de la localidad seleccionada y el código postal de la misma.
         */
        inputLocalidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                LOCALIDAD_SELECCIONADA = listaLocalidades.get(i).getIdLocalidad();
                CODIGO_POSTAL_SELECCIONADO = listaLocalidades.get(i).getCpLocalidad();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Igual que el anterior, pero guarda el tipo de vía.
        inputTipoDireccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TIPO_VIA_SELECCIONADA = listaTiposDireccion.get(i).getIdTipoDireccion();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * Inicializa la toolbar y se le pone el logo de la app.
     */
    private void inicializarToolbar() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
