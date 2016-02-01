package com.example.altam.pasoseguro;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Vibrator;

/**
 * Created by Usuario on 31/01/2016.
 */
public class AsyncTaskVibrate extends AsyncTask<String, Void, String> {
    private volatile boolean isRunning = true;
    private Context context;
    Vibrator v;
    SoundPool sP;
    int alarmSound = -1;

    public AsyncTaskVibrate(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        int cont;

        while (isRunning) {
            try {
                cont = 0;
                //int streamId = sP.play(alarmSound, 1.0f, 1.0f, 1, 0, 1);
                while(cont < 3){
                    //SOUND
                    int streamId = sP.play(alarmSound, 1.0f, 1.0f, 1, 0, 1);

                    // Vibrate for 500 milliseconds
                    v.vibrate(1000);

                    Thread.sleep(1500);

                    sP.stop(streamId);

                    cont++;
                }
               Thread.sleep(60000); //Espera 60 segundos para volver a vibrar

            } catch (InterruptedException e) {
                isRunning = false;
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        isRunning = true;
        //Instancio el SoundPool
        sP = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
        alarmSound = sP.load(context,R.raw.alarm2, 1);

        v = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    protected void onPostExecute(String result) {

    }
}