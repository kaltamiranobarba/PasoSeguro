package com.example.altam.pasoseguro;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by dev02 on 1/19/16.
 */
public class RunnableVibrate implements Runnable {

    private volatile boolean isRunning = true;
    private Context context;

    public RunnableVibrate(Context context) {
        this.context = context;
    }

    public void terminate() {
        isRunning = false;
    }

    public void init() {
        isRunning = true;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                //do work
                Vibrator v = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                v.vibrate(500);

                Thread.sleep(1000);

            } catch (InterruptedException e) {
                isRunning = false;
            }
        }

    }

}