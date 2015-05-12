package es.gk2.janhout.gk2_android.util;

import android.os.AsyncTask;

public class Temporizador extends AsyncTask<Void, Void, Boolean> {

    private OnTimerCompleteListener listener;
    private int delay;

    public Temporizador(int delay, OnTimerCompleteListener listener){
        this.delay = delay;
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            Thread.sleep(delay);
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        listener.temporizadorCompletado(false);
    }

    @Override
    protected void onPostExecute(Boolean s) {
        if(s) {
            listener.temporizadorCompletado(true);
        }
    }

    public interface OnTimerCompleteListener{
        public void temporizadorCompletado(boolean correcto);
    }
}
