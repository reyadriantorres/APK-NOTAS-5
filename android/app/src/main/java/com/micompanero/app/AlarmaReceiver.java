package com.micompanero.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;

public class AlarmaReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context c, Intent i) {
        Intent s = new Intent(c, AlarmaService.class);
        s.putExtra("id", i.getIntExtra("id", 1));
        s.putExtra("title", i.getStringExtra("title"));
        s.putExtra("body", i.getStringExtra("body"));
        ContextCompat.startForegroundService(c, s);
    }
}
