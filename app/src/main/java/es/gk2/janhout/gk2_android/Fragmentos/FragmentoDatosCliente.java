package es.gk2.janhout.gk2_android.fragmentos;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;

import es.gk2.janhout.gk2_android.actividades.MostrarCliente;
import es.gk2.janhout.gk2_android.modelos.Cliente;
import es.gk2.janhout.gk2_android.util.AsyncTaskGet;
import es.gk2.janhout.gk2_android.util.Constantes;
import es.gk2.janhout.gk2_android.util.Metodos;
import es.gk2.janhout.gk2_android.R;

public class FragmentoDatosCliente extends Fragment implements AsyncTaskGet.OnProcessCompleteListener,
        MostrarCliente.ItemMenuPulsadoMostrarCliente {

    private MostrarCliente actividad;
    private Cliente clienteMostrar;
    private JSONObject clienteJSON;

    private TextView nif;
    private TextView nombreComercial;
    private TextView direccion;
    private TextView telefonos;
    private TextView email;
    private TextView numeroCuenta;

    private Button favorito;

    private static final int CODIGO_PEDIR_CLIENTE = 1;
    private static final int CODIGO_SET_FAVORITO = 2;

    public FragmentoDatosCliente() {
    }

    /* *************************************************************************
     **************************** Métodos on... ********************************
     *************************************************************************** */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        actividad = (MostrarCliente)activity;
        actividad.setEscuchadorMenu(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_datos_cliente, container, false);
        clienteMostrar = getArguments().getParcelable("cliente");
        actividad.setInicio(false);
        inicializarViews(v);
        cargarCliente();
        return v;
    }

    /* *************************************************************************
     **************************** Auxialiares **********************************
     *************************************************************************** */

    private void cargarCliente() {
        String url;
        url = Constantes.CLIENTES_DETALLE + clienteMostrar.getId();
        AsyncTaskGet asyncTask = new AsyncTaskGet(actividad, this, url, false, CODIGO_PEDIR_CLIENTE);
        asyncTask.execute(new Hashtable<String, String>());
    }

    private void inicializarEventosBotones() {
        favorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarFavorito();
            }
        });
    }

    private void inicializarViews(View v) {
        nombreComercial = (TextView) v.findViewById(R.id.mostrarCliente_nombrecomercial);
        nif = (TextView) v.findViewById(R.id.mostrarCliente_nif);
        telefonos = (TextView) v.findViewById(R.id.mostrarCliente_telefonos);
        direccion = (TextView) v.findViewById(R.id.mostrarCliente_direccion);
        email = (TextView) v.findViewById(R.id.mostrarCliente_email);
        numeroCuenta = (TextView) v.findViewById(R.id.mostrarCliente_numeroCuenta);

        favorito = (Button) v.findViewById(R.id.bt_favorito);

        inicializarEventosBotones();
    }

    private void completarDatosCliente() {
        if (clienteJSON != null) {
            clienteMostrar.datosAdicionales(clienteJSON);
            actividad.setCliente(clienteMostrar);
        }
    }

    private void mostrarDatosCliente() {
        completarDatosCliente();
        direccion.setText(clienteMostrar.getDireccion());
        nombreComercial.setText(clienteMostrar.getNombre_comercial());
        nif.setText(clienteMostrar.getNif());
        String telfs = "";
        if (!clienteMostrar.getTelefono01().equals("") && !clienteMostrar.getTelefono02().equals("")) {
            telfs = clienteMostrar.getTelefono01() + " / " + clienteMostrar.getTelefono02();
        } else if (!clienteMostrar.getTelefono01().equals("")) {
            telfs = clienteMostrar.getTelefono01();
        } else if (!clienteMostrar.getTelefono02().equals("")) {
            telfs = clienteMostrar.getTelefono02();
        }
        telefonos.setText(telfs);
        email.setText(clienteMostrar.getEmail());
        numeroCuenta.setText(clienteMostrar.getNumero_cuenta());

        boolean telefono = true;
        boolean email = true;
        String telf1 = clienteMostrar.getTelefono01().replaceAll(" ", "");
        String telf2 = clienteMostrar.getTelefono02().replaceAll(" ", "");
        if(!TextUtils.isDigitsOnly(telf1)){
            telf1 = "";
        }
        if(!TextUtils.isDigitsOnly(telf2)){
            telf2 = "";
        }
        if(telf1.equals("") && telf2.equals("")){
            telefono = false;
        }
        if(clienteMostrar.getEmail().trim().equals("")){
            email = false;
        }

        actividad.setMostrarEmail(email);
        actividad.setMostrarTelefono(telefono);
        actividad.invalidateOptionsMenu();

        if (clienteMostrar.isFavorito()) {
            Metodos.botonAwesomeComponente(actividad, favorito, getString(R.string.icono_clientes_favoritos));
        } else {
            Metodos.botonAwesomeComponente(actividad, favorito, getString(R.string.icono_clientes_no_favoritos));
        }
    }

    /* *************************************************************************
     ******************** Interfaz OnProcessCompleteListener *******************
     *************************************************************************** */

    @Override
    public void resultadoGet(String respuesta, int codigo) {
        if (respuesta != null) {
            switch (codigo) {
                case CODIGO_PEDIR_CLIENTE:
                    try {
                        clienteJSON = new JSONObject(respuesta);
                    } catch (JSONException e) {
                        clienteJSON = null;
                    }
                    mostrarDatosCliente();
                    break;
                case CODIGO_SET_FAVORITO:
                    clienteMostrar.setFavorito(!clienteMostrar.isFavorito());
                    if (clienteMostrar.isFavorito()) {
                        Metodos.botonAwesomeComponente(actividad, favorito, getString(R.string.icono_clientes_favoritos));
                    } else {
                        Metodos.botonAwesomeComponente(actividad, favorito, getString(R.string.icono_clientes_no_favoritos));
                    }
                    break;
            }
        } else {
            Metodos.redireccionarLogin(actividad);
        }
    }

    /* *************************************************************************
     ******************** Interfaz ItemMenuPulsado *****************************
     *************************************************************************** */

    @Override
    public void itemMenuPulsadoMostrarCliente(int itemMenu, String query) {
        switch (itemMenu) {
            case R.id.action_llamar:
                llamarTelefono();
                break;
            case R.id.action_email:
                enviarEmail();
                break;
            case R.id.action_facturas:
                verFacturas(query);
                break;
        }
    }

    /* *************************************************************************
     ******************************* Métodos Botones ***************************
     *************************************************************************** */

    private void cambiarFavorito() {
        String url;
        if (clienteMostrar.isFavorito()) {
            url = Constantes.UNSET_FAVORITO + clienteMostrar.getId();
        } else {
            url = Constantes.SET_FAVORITO + clienteMostrar.getId();
        }
        AsyncTaskGet h = new AsyncTaskGet(actividad, this, url, false, CODIGO_SET_FAVORITO);
        h.execute(new Hashtable<String, String>());
    }

    /* *************************************************************************
     ******************************* Auxiliares ********************************
     *************************************************************************** */

    private void seleccionarTelefono(String telefono1, String telefono2) {
        final CharSequence telfs[] = new CharSequence[]{telefono1, telefono2};
        AlertDialog.Builder builder = new AlertDialog.Builder(actividad);
        builder.setTitle(getString(R.string.selecciona_telefono));
        builder.setItems(telfs, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String uri = "tel:" + telfs[which].toString();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        });
        builder.show();
    }

    /* *************************************************************************
     ************************** Métodos Botones Menú ***************************
     *************************************************************************** */

    public void enviarEmail() {
        if (!clienteMostrar.getEmail().trim().equals("")) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{clienteMostrar.getEmail().trim()});
            try {
                startActivity(Intent.createChooser(i, getString(R.string.enviar_email)));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(actividad, getString(R.string.no_app_disponible), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void llamarTelefono() {
        String telefono01, telefono02;

        telefono01 = clienteMostrar.getTelefono01().trim();
        telefono02 = clienteMostrar.getTelefono02().trim();

        String uri = "";
        if (!telefono01.equals("") && !telefono02.equals("")) {
            seleccionarTelefono(telefono01, telefono02);
        } else if (!telefono01.equals("")) {
            uri = "tel:" + telefono01;
        } else if (!telefono02.equals("")) {
            uri = "tel:" + telefono02;
        }

        if (!uri.equals("")) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        }
    }

    private void verFacturas(String query) {
        Bundle bundle = new Bundle();
        bundle.putInt("idCliente", clienteMostrar.getId());
        bundle.putBoolean("todas", false);
        bundle.putString("query", query);
        MostrarCliente.fragmentoActual = MostrarCliente.ListaFragmentosCliente.facturas;
        //Fragment fragmento = new FragmentoListaFacturas();
        Fragment fragmento = new FragmentoContenedorListaFacturas();
        fragmento.setArguments(bundle);
        FragmentTransaction transaction = actividad.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.relativeLayoutCliente, fragmento);
        transaction.addToBackStack(null);
        actividad.setTituloActividad("Facturas - " + clienteMostrar.getNombre_comercial());
        transaction.commit();
    }
}