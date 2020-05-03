package com.directionhelpers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.ggmap.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONObject;

public class PointsParser extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
    TaskLoadedCallback taskCallback;
    String directionMode = "driving";

    public PointsParser(MapFragment mContext, String directionMode) {
        this.taskCallback = (TaskLoadedCallback)mContext;
        this.directionMode = directionMode;
    }

    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
        List routes = null;

        try {
            JSONObject jObject = new JSONObject(jsonData[0]);
            Log.d("mylog", jsonData[0].toString());
            DataParser parser = new DataParser();
            Log.d("mylog", parser.toString());
            routes = parser.parse(jObject);
            Log.d("mylog", "Executing routes");
            Log.d("mylog", routes.toString());
        } catch (Exception var5) {
            Log.d("mylog", var5.toString());
            var5.printStackTrace();
        }

        return routes;
    }

    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
        PolylineOptions lineOptions = null;

        for(int i = 0; i < result.size(); ++i) {
            ArrayList<LatLng> points = new ArrayList();
            lineOptions = new PolylineOptions();
            List<HashMap<String, String>> path = (List)result.get(i);

            for(int j = 0; j < path.size(); ++j) {
                HashMap<String, String> point = (HashMap)path.get(j);
                double lat = Double.parseDouble((String)point.get("lat"));
                double lng = Double.parseDouble((String)point.get("lng"));
                LatLng position = new LatLng(lat, lng);
                points.add(position);
            }

            lineOptions.addAll(points);
            if (this.directionMode.equalsIgnoreCase("walking")) {
                lineOptions.width(10.0F);
                lineOptions.color(-65281);
            } else {
                lineOptions.width(20.0F);
                lineOptions.color(-16776961);
            }

            Log.d("mylog", "onPostExecute lineoptions decoded");
        }

        if (lineOptions != null) {
            this.taskCallback.onTaskDone(new Object[]{lineOptions});
        } else {
            Log.d("mylog", "without Polylines drawn");
        }

    }
}
