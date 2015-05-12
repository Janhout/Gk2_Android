package es.gk2.janhout.gk2_android.Fragmentos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import es.gk2.janhout.gk2_android.Estaticas.AsyncTaskGet;
import es.gk2.janhout.gk2_android.Estaticas.Constantes;
import es.gk2.janhout.gk2_android.Estaticas.Metodos;
import es.gk2.janhout.gk2_android.R;

public class FragmentoDatosCliente extends Fragment implements AsyncTaskGet.OnProcessCompleteListener,
        MostrarCliente.ItemMenuPulsadoMostrarCliente {

    private MostrarCliente actividad;
    private int idCliente;
    private JSONObject cliente;

    private TextView nif;
    private TextView nombreComercial;
    private TextView direccion;
    private TextView telefonos;
    private TextView email;
    private TextView numeroCuenta;

    private Button favorito;

    private String s_nif;
    private String s_nombreComercial;
    private String s_direccion;
    private String s_telefonos;
    private String s_email;
    private String s_numeroCuenta;
    private boolean s_favorito;

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
        idCliente = getArguments().getInt("idCliente");
        MostrarCliente.setInicio(false);
        inicializarViews(v);
        cargarCliente();
        return v;
    }

    /* *************************************************************************
     **************************** Auxialiares **********************************
     *************************************************************************** */

    private void cargarCliente() {
        String url;
        url = Constantes.CLIENTES_DETALLE + idCliente;
        AsyncTaskGet asyncTask = new AsyncTaskGet(getActivity(), this, url, false, CODIGO_PEDIR_CLIENTE);
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

    private void leerDatosJSON() {
        if (cliente != null) {
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
                s_favorito = cliente.getInt("favorito") == 1;
            } catch (JSONException ignored) {
            }
        }
    }

    private void mostrarDatosCliente() {
        leerDatosJSON();
        direccion.setText(s_direccion);
        nombreComercial.setText(s_nombreComercial);
        nif.setText(s_nif);
        telefonos.setText(s_telefonos);
        email.setText(s_email);
        numeroCuenta.setText(s_numeroCuenta);

        if (s_favorito) {
            Metodos.botonAwesomeComponente(getActivity(), favorito, getString(R.string.icono_clientes_favoritos));
        } else {
            Metodos.botonAwesomeComponente(getActivity(), favorito, getString(R.string.icono_clientes_no_favoritos));
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
                        cliente = new JSONObject(respuesta);
                    } catch (JSONException e) {
                        cliente = null;
                    }
                    mostrarDatosCliente();
                    break;
                case CODIGO_SET_FAVORITO:
                    s_favorito = !s_favorito;
                    if (s_favorito) {
                        Metodos.botonAwesomeComponente(getActivity(), favorito, getString(R.string.icono_clientes_favoritos));
                    } else {
                        Metodos.botonAwesomeComponente(getActivity(), favorito, getString(R.string.icono_clientes_no_favoritos));
                    }
                    break;
            }
        } else {
            Metodos.redireccionarLogin(getActivity());
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
        if (s_favorito) {
            url = Constantes.UNSET_FAVORITO + idCliente;
        } else {
            url = Constantes.SET_FAVORITO + idCliente;
        }
        AsyncTaskGet h = new AsyncTaskGet(getActivity(), this, url, false, CODIGO_SET_FAVORITO);
        h.execute(new Hashtable<String, String>());
    }

    /* *************************************************************************
     ******************************* Auxiliares ********************************
     *************************************************************************** */

    private void seleccionarTelefono(String telefono1, String telefono2) {
        final CharSequence telfs[] = new CharSequence[]{telefono1, telefono2};
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

    /* *************************************************************************
     ************************** Métodos Botones Menú ***************************
     *************************************************************************** */

    public void enviarEmail() {
        if (!s_email.trim().equals("")) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{s_email.trim()});
            try {
                startActivity(Intent.createChooser(i, getString(R.string.enviar_email)));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getActivity(), getString(R.string.no_app_disponible), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void llamarTelefono() {
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
        if (!telefono01.equals("") && !telefono02.equals("")) {
            seleccionarTelefono(telefono01, telefono02);
        } else if (!telefono01.equals("")) {
            uri = "tel:" + telefono01;
        } else if (!telefono01.equals("")) {
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
        bundle.putInt("idCliente", idCliente);
        bundle.putString("query", query);
        MostrarCliente.fragmentoActual = MostrarCliente.ListaFragmentosCliente.facturas;
        Fragment fragmento = new FragmentoListaFacturas();
        fragmento.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
        transaction.replace(R.id.relativeLayoutCliente, fragmento);
        transaction.addToBackStack(null);
        ((MostrarCliente) getActivity()).setTituloActividad("Facturas - " + s_nombreComercial);
        transaction.commit();
    }
}