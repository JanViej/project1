package com.directionhelpers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.ggmap.MapFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchURL extends AsyncTask<String, Void, String> {
    MapFragment mContext;
    String directionMode = "driving";

    public FetchURL(MapFragment mContext) {
        this.mContext = mContext;
    }

    protected String doInBackground(String... strings) {
        String data = "";
        this.directionMode = strings[1];

        try {
            data = this.downloadUrl(strings[0]);
            //Log.d("mylog", "Background task data " + data.toString());
        } catch (Exception var4) {
            Log.d("Background Task", var4.toString());
        }

        return data;
    }

    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        PointsParser parserTask = new PointsParser(this.mContext, this.directionMode);
        parserTask.execute(s);
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";

            while((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("mylog", "Downloaded URL: " + data.toString());
            br.close();
        } catch (Exception var12) {
            Log.d("mylog", "Exception downloading URL: " + var12.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
    }
}
