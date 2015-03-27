package es.gk2.janhout.gk2_android.Actividades;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import es.gk2.janhout.gk2_android.Estaticas.AsyncTaskGet;
import es.gk2.janhout.gk2_android.Estaticas.AsyncTaskPost;
import es.gk2.janhout.gk2_android.R;


public class Login extends ActionBarActivity {

    private static Context contexto;
    private AsyncTask<String, String, String> asyncTask;
    private String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_login);
        Login.contexto = getApplicationContext();
    }

    public void login(View v){
        EditText etUsuario = (EditText)findViewById(R.id.etUsuario);
        EditText etPass = (EditText)findViewById(R.id.etPass);
        String usuario = etUsuario.getText().toString();
        String pass = etPass.getText().toString();

        if(hacerLogin(usuario, pass)){
            Intent i = new Intent(this, Principal.class);
            startActivity(i);
        } else{
            Toast.makeText(this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
        }
    }

    public void recuperaPass(View v){
        Intent i = new Intent(this, RecordarPass.class);
        startActivity(i);
    }

    private boolean hacerLogin(String usuario, String pass){
        AsyncTaskPost runner=new AsyncTaskPost();

        asyncTask=runner.execute(usuario, pass);
        try {
            String asyncResultText=asyncTask.get();
            response = asyncResultText.trim();
        } catch (InterruptedException e1) {
            response = e1.getMessage();
        } catch (ExecutionException e1) {
            response = e1.getMessage();
        } catch (Exception e1) {
            response = e1.getMessage();
        }
        if(response.contains("Clientes")) {
            return true;
        } else {
            return false;
        }
    }

    public static Context getAppContext() {
        return Login.contexto;
    }
}
