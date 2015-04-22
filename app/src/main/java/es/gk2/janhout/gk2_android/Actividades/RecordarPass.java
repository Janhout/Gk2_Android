package es.gk2.janhout.gk2_android.Actividades;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Hashtable;

import es.gk2.janhout.gk2_android.Estaticas.Constantes;
import es.gk2.janhout.gk2_android.Estaticas.PostAsyncTask;
import es.gk2.janhout.gk2_android.R;

public class RecordarPass extends ActionBarActivity implements PostAsyncTask.OnProcessCompleteListener{

    private final String paramentro_usuario = "usuario";

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
        parametros.put(paramentro_usuario, usuario);
        PostAsyncTask a = new PostAsyncTask(this, this, Constantes.URL_LOGIN, true);
        a.execute(parametros);
    }

    //http://rafagr8.gk2web.com/login/recovery_process
    /*Le hemos enviado un correo a rafagr8@gmail.com con las instrucciones para
    recuperar su contraseña, por favor revíselo antes de 24 horas.
     */

    /*TODO
    * Gestionar resultado
    *
    * */
    @Override
    public void resultadoPost(String respuesta) {
        if(respuesta != null) {

        } else{
            Toast.makeText(this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
        }
        finish();
    }
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recordar_pass, menu);
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
    }*/
}