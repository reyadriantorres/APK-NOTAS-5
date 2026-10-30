package com.micompanero.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/** Tras reiniciar el teléfono o actualizar la app, vuelve a programar las alarmas guardadas. */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context c, Intent i) {
        AlarmaStore.scheduleAll(c);
    }
}
