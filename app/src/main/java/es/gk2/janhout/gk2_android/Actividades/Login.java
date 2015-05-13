package es.gk2.janhout.gk2_android.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Hashtable;

import es.gk2.janhout.gk2_android.util.AsyncTaskPost;
import es.gk2.janhout.gk2_android.util.Constantes;
import es.gk2.janhout.gk2_android.util.Metodos;
import es.gk2.janhout.gk2_android.R;

public class Login extends AppCompatActivity implements AsyncTaskPost.OnProcessCompleteListener{

    private static final String PARAMENTRO_USUARIO = "usuario";
    private static final String PARAMENTRO_PASS = "pass";
    private static final int CODIGO_LOGIN = 1;

    /* *************************************************************************
    **************************** Métodos on... ********************************
    *************************************************************************** */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String cookie = Metodos.leerPreferenciasCompartidasString(this, "cookieSesion");
        if (cookie.equals("")){
            setContentView(R.layout.activity_login);
        } else {
            loginCorrecto();
        }
    }

    /* *************************************************************************
    **************** Interfaz OnProcessCompleteListener ***********************
    *************************************************************************** */

    @Override
    public void resultadoPost(String location, int codigo) {
        if(location != null && !location.contains("login")) {
            loginCorrecto();
        } else{
            Toast.makeText(this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
        }
    }

    /* *************************************************************************
    **************************** Auxiliares ************************************
    *************************************************************************** */

    private void hacerLogin(String usuario, String pass){
        Hashtable<String, String> parametros = new Hashtable<>();
        parametros.put(PARAMENTRO_USUARIO, usuario);
        parametros.put(PARAMENTRO_PASS, pass);
        AsyncTaskPost a = new AsyncTaskPost(this, this, Constantes.URL_LOGIN, CODIGO_LOGIN);
        a.execute(parametros);
    }

    public void login(View v){
        EditText etUsuario = (EditText)findViewById(R.id.etUsuario);
        EditText etPass = (EditText)findViewById(R.id.etPass);
        String usuario = etUsuario.getText().toString();
        String pass = etPass.getText().toString();

        if(!pass.trim().equals("") && !usuario.trim().equals(""))
            hacerLogin(usuario, pass);
    }

    private void loginCorrecto(){
        Intent i = new Intent(this, Principal.class);
        i.putExtra("favorito", true);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
        finish();
    }

    public void recuperaPass(View v){
        Intent i = new Intent(this, RecordarPass.class);
        startActivity(i);
    }
}
