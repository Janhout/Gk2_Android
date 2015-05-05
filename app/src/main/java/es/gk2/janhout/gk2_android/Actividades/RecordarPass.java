package es.gk2.janhout.gk2_android.Actividades;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Hashtable;

import es.gk2.janhout.gk2_android.Estaticas.AsyncTaskPost;
import es.gk2.janhout.gk2_android.Estaticas.Constantes;
import es.gk2.janhout.gk2_android.R;

public class RecordarPass extends ActionBarActivity implements AsyncTaskPost.OnProcessCompleteListener{

    private final static String PARAMETRO_USUARIO = "usuario";

    private static final int CODIGO_RECUPERAR_PASS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordar_pass);
    }

    public void recuperar(View v){
        EditText etUsuario = (EditText)findViewById(R.id.etUsuario);
        String email = etUsuario.getText().toString();
        recuperarPass(email);
        this.finish();
    }

    public void volverLogin(View v){
        this.finish();
    }

    private void recuperarPass(String usuario){
        Hashtable<String, String> parametros = new Hashtable<>();
        parametros.put(PARAMETRO_USUARIO, usuario);
        AsyncTaskPost a = new AsyncTaskPost(this, this, Constantes.URL_RECUPERAR_PASS, CODIGO_RECUPERAR_PASS);
        a.execute(parametros);
    }

    /*Le hemos enviado un correo a rafagr8@gmail.com con las instrucciones para
    recuperar su contraseña, por favor revíselo antes de 24 horas.
     */

    @Override
    public void resultadoPost(String location, int codigo) {
        if (location != null) {

        } else {
            Toast.makeText(this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}