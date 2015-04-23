package es.gk2.janhout.gk2_android.Fragmentos;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;

import es.gk2.janhout.gk2_android.Actividades.MostrarCliente;
import es.gk2.janhout.gk2_android.Estaticas.Constantes;
import es.gk2.janhout.gk2_android.Estaticas.GetAsyncTask;
import es.gk2.janhout.gk2_android.Estaticas.Metodos;
import es.gk2.janhout.gk2_android.R;

public class FragmentoDatosCliente extends Fragment implements GetAsyncTask.OnProcessCompleteListener {

    private int idCliente;
    private GetAsyncTask asyncTask;
    private JSONObject cliente;

    private TextView nif;
    private TextView nombreComercial;
    private TextView direccion;
    private TextView telefonos;
    private TextView email;
    private TextView numeroCuenta;

    private Button bt_email;
    private Button bt_telefono;
    private Button bt_facturas;

    private String s_nif;
    private String s_nombreComercial;
    private String s_direccion;
    private String s_telefonos;
    private String s_email;
    private String s_numeroCuenta;

    private static final int CODIGO_PEDIR_CLIENTE = 1;

    public FragmentoDatosCliente() {
    }

    /* *************************************************************************
     **************************** Métodos on... ********************************
     *************************************************************************** */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_datos_cliente, container, false);
        idCliente = getArguments().getInt("idCliente");
        inicializarViews(v);
        cargarLCliente();
        return v;
    }

    /* *************************************************************************
     **************************** Auxialiares **********************************
     *************************************************************************** */

    private void cargarLCliente(){
        String url;
        url = Constantes.CLIENTES_DETALLE + idCliente;
        asyncTask = new GetAsyncTask(getActivity(), this, url, false, CODIGO_PEDIR_CLIENTE);
        Hashtable<String, String> parametros = null;
        asyncTask.execute(parametros);
    }

    private void inicializarEventosBotones(){
        bt_facturas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verFacturas();
            }
        });

        bt_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarEmail();
            }
        });

        bt_telefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamarTelefono();
            }
        });
    }

    private void inicializarViews(View v){
        nombreComercial = (TextView)v.findViewById(R.id.mostrarCliente_nombrecomercial);
        nif = (TextView)v.findViewById(R.id.mostrarCliente_nif);
        telefonos = (TextView)v.findViewById(R.id.mostrarCliente_telefonos);
        direccion = (TextView)v.findViewById(R.id.mostrarCliente_direccion);
        email = (TextView)v.findViewById(R.id.mostrarCliente_email);
        numeroCuenta = (TextView)v.findViewById(R.id.mostrarCliente_numeroCuenta);
        bt_email = (Button)v.findViewById(R.id.mostrarCliente_bt_email);
        bt_telefono = (Button)v.findViewById(R.id.mostrarCliente_bt_telefono);
        bt_facturas = (Button)v.findViewById(R.id.mostrarCliente_bt_facturas);

        inicializarEventosBotones();

        Metodos.botonAwesomeComponente(getActivity(), bt_facturas, getString(R.string.icono_facturas));
        Metodos.botonAwesomeComponente(getActivity(), bt_email, getString(R.string.icono_email));
        Metodos.botonAwesomeComponente(getActivity(), bt_telefono, getString(R.string.icono_telefono));
    }

    private void leerDatosJSON(){
        if(cliente != null) {
            try {
                s_nombreComercial = cliente.getString("cliente_name");
                s_nif = cliente.getString("cliente_nif");
                String telf1 = cliente.getString("cliente_tlf1");
                String telf2 = cliente.getString("cliente_tlf2");
                s_telefonos = "";
                if (!telf1.equals("") && !telf2.equals("")) {
                    s_telefonos = telf1 + " / " + telf2;
                } else if (!telf1.equals("")) {
                    s_telefonos = telf1;
                } else if (!telf2.equals("")) {
                    s_telefonos = telf2;
                }
                s_direccion = cliente.getString("cliente_direccion");
                s_email = cliente.getString("cliente_mail");
                s_numeroCuenta = cliente.getString("cliente_ccc");
            } catch (JSONException e) {
            }
        }
    }

    private void mostrarDatosCliente(){
        leerDatosJSON();
        direccion.setText(s_direccion);
        nombreComercial.setText(s_nombreComercial);
        nif.setText(s_nif);
        telefonos.setText(s_telefonos);
        email.setText(s_email);
        numeroCuenta.setText(s_numeroCuenta);
    }

    /* *************************************************************************
     ******************** Interfaz OnProcessCompleteListener *******************
     *************************************************************************** */

    @Override
    public void resultadoGet(String respuesta, int codigo) {
        if(respuesta != null) {
            try {
                cliente = new JSONObject(respuesta);
            } catch (JSONException e) {
                cliente = null;
            }
            mostrarDatosCliente();
        }
    }

    /* *************************************************************************
     ******************************* Métodos Botones ***************************
     *************************************************************************** */

    public void enviarEmail(){
        String mail;
        try {
            mail = cliente.getString("cliente_mail");
        } catch (JSONException e) {
            mail = "";
        }
        if(!mail.trim().equals("")){
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{mail.trim()});
            try {
                startActivity(Intent.createChooser(i, getString(R.string.enviar_email)));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getActivity(), getString(R.string.no_app_disponible), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void llamarTelefono(){
        String telefono01, telefono02;
        try {
            telefono01 = cliente.getString("cliente_tlf1").trim();
        } catch (JSONException e) {
            telefono01 = "";
        }
        try {
            telefono02 = cliente.getString("cliente_tlf2").trim();
        } catch (JSONException e) {
            telefono02 = "";
        }

        String uri = "";
        if(!telefono01.equals("") && !telefono02.equals("")){
            seleccionarTelefono(telefono01, telefono02);
        } else if(!telefono01.equals("")) {
            uri = "tel:" + telefono01;
        } else if(!telefono01.equals("")) {
            uri = "tel:" + telefono02;
        }

        if(!uri.equals("")) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        }
    }

    private void verFacturas(){
        Bundle bundle = new Bundle();
        bundle.putInt("idCliente", idCliente);
        MostrarCliente.fragmentoActual = MostrarCliente.ListaFragmentosCliente.facturas;
        Fragment fragmento = new FragmentoListaFacturas();
        fragmento.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
        transaction.replace(R.id.relativeLayoutCliente, fragmento);
        transaction.addToBackStack(null);
        ((MostrarCliente)getActivity()).setTituloActividad("Facturas - " + s_nombreComercial);
        transaction.commit();
    }

    /* *************************************************************************
     ******************************* Auxiliares ********************************
     *************************************************************************** */

    private void seleccionarTelefono(String telefono1, String telefono2){
        final CharSequence telfs[] = new CharSequence[] {telefono1, telefono2};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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


}
