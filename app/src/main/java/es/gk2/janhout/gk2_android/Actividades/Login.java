package es.gk2.janhout.gk2_android.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Hashtable;

import es.gk2.janhout.gk2_android.Estaticas.Constantes;
import es.gk2.janhout.gk2_android.Estaticas.Metodos;
import es.gk2.janhout.gk2_android.Estaticas.PostAsyncTask;
import es.gk2.janhout.gk2_android.R;

public class Login extends ActionBarActivity implements PostAsyncTask.OnProcessCompleteListener{

    private static final String PARAMENTRO_USUARIO = "usuario";
    private static final String PARAMENTRO_PASS = "pass";
    private static final int CODIGO_LOGIN = 1;

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

    public void login(View v){
        EditText etUsuario = (EditText)findViewById(R.id.etUsuario);
        EditText etPass = (EditText)findViewById(R.id.etPass);
        String usuario = etUsuario.getText().toString();
        String pass = etPass.getText().toString();

        if(!pass.trim().equals("") && !usuario.trim().equals(""))
            hacerLogin(usuario, pass);
    }

    public void recuperaPass(View v){
        Intent i = new Intent(this, RecordarPass.class);
        startActivity(i);
    }

    private void hacerLogin(String usuario, String pass){
        Hashtable<String, String> parametros = new Hashtable<>();
        parametros.put(PARAMENTRO_USUARIO, usuario);
        parametros.put(PARAMENTRO_PASS, pass);
        PostAsyncTask a = new PostAsyncTask(this, this, Constantes.URL_LOGIN, CODIGO_LOGIN);
        a.execute(parametros);
    }

    @Override
    public void resultadoPost(String location, int codigo) {
        if(location != null && !location.contains("login")) {
            loginCorrecto();
        } else{
            Toast.makeText(this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
        }
    }

    private void loginCorrecto(){
        Intent i = new Intent(this, Principal.class);
        i.putExtra("favorito", true);
        startActivity(i);
        finish();
    }
}
