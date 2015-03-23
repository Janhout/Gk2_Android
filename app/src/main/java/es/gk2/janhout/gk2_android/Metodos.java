package es.gk2.janhout.gk2_android;

import android.content.Context;
import android.content.SharedPreferences;

public class Metodos {

    public static String leerPreferenciasCompartidasString(Context contexto, String key){
        SharedPreferences sharedPref = contexto.getSharedPreferences(contexto.getString(R.string.preferencias_compartidas), Context.MODE_PRIVATE);
        String resultado = sharedPref.getString(key, "");
        return resultado;
    }

    public static void escribirPreferenciasCompartidasString(Context contexto, String key, String valor){
        SharedPreferences sharedPref = contexto.getSharedPreferences(contexto.getString(R.string.preferencias_compartidas), Context.MODE_PRIVATE);
        String resultado = sharedPref.getString(key, "");
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, valor);
        editor.apply();
    }

    public static int leerPreferenciasCompartidasInt(Context contexto, String key){
        SharedPreferences sharedPref = contexto.getSharedPreferences(contexto.getString(R.string.preferencias_compartidas), Context.MODE_PRIVATE);
        int resultado = sharedPref.getInt(key, 0);
        return resultado;
    }

    public static void escribirPreferenciasCompartidasInt(Context contexto, String key, int valor){
        SharedPreferences sharedPref = contexto.getSharedPreferences(contexto.getString(R.string.preferencias_compartidas), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, valor);
        editor.apply();
    }
}