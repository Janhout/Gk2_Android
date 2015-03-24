package es.gk2.janhout.gk2_android.Actividades;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import es.gk2.janhout.gk2_android.R;


public class Login extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View v){
        EditText etUsuario = (EditText)findViewById(R.id.etUsuario);
        EditText etPass = (EditText)findViewById(R.id.etPass);
        String usuario = etUsuario.getText().toString();
        String pass = etPass.getText().toString();

        if(hacerLogin(usuario, pass)){
            Intent i = new Intent(this, Principal.class);
            startActivity(i);
        }
    }

    public void recuperaPass(View v){
        Intent i = new Intent(this, RecordarPass.class);
        startActivity(i);
    }

    private boolean hacerLogin(String usuario, String pass){
        return true;
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
