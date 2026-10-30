package com.micompanero.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONObject;

/** Guarda las alarmas en el teléfono y las programa con AlarmManager.setAlarmClock (exacto, atraviesa Doze). */
public class AlarmaStore {
    static SharedPreferences sp(Context c) { return c.getSharedPreferences("alarmas", Context.MODE_PRIVATE); }

    static JSONArray read(Context c) {
        try { return new JSONArray(sp(c).getString("lista", "[]")); } catch (Exception e) { return new JSONArray(); }
    }

    static void write(Context c, JSONArray a) { sp(c).edit().putString("lista", a.toString()).apply(); }

    static PendingIntent pending(Context c, int id, String title, String body) {
        Intent i = new Intent(c, AlarmaReceiver.class);
        i.setAction("com.micompanero.app.ALARMA");
        i.putExtra("id", id);
        i.putExtra("title", title);
        i.putExtra("body", body);
        return PendingIntent.getBroadcast(c, id, i, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    static void cancelAll(Context c) {
        AlarmManager am = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        JSONArray a = read(c);
        for (int k = 0; k < a.length(); k++) {
            JSONObject o = a.optJSONObject(k);
            if (o == null) continue;
            am.cancel(pending(c, o.optInt("id"), o.optString("title"), o.optString("body")));
        }
        write(c, new JSONArray());
    }

    static void scheduleAll(Context c) {
        AlarmManager am = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        JSONArray a = read(c);
        long now = System.currentTimeMillis();
        Intent show = new Intent(c, MainActivity.class);
        PendingIntent showPi = PendingIntent.getActivity(c, 0, show, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        for (int k = 0; k < a.length(); k++) {
            JSONObject o = a.optJSONObject(k);
            if (o == null) continue;
            long at = o.optLong("at");
            if (at <= now) continue;
            am.setAlarmClock(new AlarmManager.AlarmClockInfo(at, showPi),
                    pending(c, o.optInt("id"), o.optString("title"), o.optString("body")));
        }
    }
}
