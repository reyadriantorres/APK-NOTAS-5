package com.micompanero.app;

import android.content.Context;
import android.content.Intent;
import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import org.json.JSONArray;
import org.json.JSONObject;

@CapacitorPlugin(name = "Alarma")
public class AlarmaPlugin extends Plugin {

    /** Reemplaza TODAS las alarmas por la lista recibida: {alarms:[{id,title,body,at(ms)}]} */
    @PluginMethod
    public void programar(PluginCall call) {
        Context c = getContext();
        JSArray arr = call.getArray("alarms");
        JSONArray out = new JSONArray();
        try {
            if (arr != null) {
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);
                    JSONObject n = new JSONObject();
                    n.put("id", o.getInt("id"));
                    n.put("title", o.optString("title"));
                    n.put("body", o.optString("body"));
                    n.put("at", o.getLong("at"));
                    out.put(n);
                }
            }
        } catch (Exception e) {
            call.reject("Datos de alarma inválidos");
            return;
        }
        AlarmaStore.cancelAll(c);
        AlarmaStore.write(c, out);
        AlarmaStore.scheduleAll(c);
        JSObject r = new JSObject();
        r.put("total", out.length());
        call.resolve(r);
    }

    /** Apaga la alarma que esté sonando. */
    @PluginMethod
    public void detener(PluginCall call) {
        Context c = getContext();
        c.stopService(new Intent(c, AlarmaService.class));
        call.resolve();
    }
}
