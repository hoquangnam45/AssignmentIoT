package com.iot.hoquangnam.lockerinteractiveapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostJSONRequest extends AsyncTask<String, String, String> {
    StringBuffer response = new StringBuffer();
    private Activity context;

    public PostJSONRequest(Activity context) {
        this.context = context;
    }
    @Override
    protected void onPreExecute (){
        Toast.makeText(context, "Sending", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected String doInBackground(String... param) {
        // params comes from the execute() call: params[0] is the url.
        try {
            try {
                return HttpPost(param);
            } catch (JSONException e) {
                e.printStackTrace();
                return "Error!";
            }
        } catch (IOException e) {
            return "Unable to retrieve web page. URL may be invalid.";
        }
    }

    private JSONObject buidJsonObject(String... param) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        for (int i = 1; i < param.length; i += 2)
            jsonObject.accumulate(param[i], param[i+1]);
        return jsonObject;
    }

    private void setPostRequestContent(HttpURLConnection conn,
                                       JSONObject jsonObject) throws IOException {
        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(jsonObject.toString());
        //Log.i(MainActivity.class.toString(), jsonObject.toString());
        writer.flush();
        writer.close();
        os.close();
    }
    private String HttpPost(String... param) throws IOException, JSONException {
        URL url = new URL(param[0]);

        // 1. create HttpURLConnection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        // 2. build JSON object
        JSONObject jsonObject = buidJsonObject(param);
        Log.d(this.getClass().toString(), jsonObject.toString());
        // 3. add JSON content to POST request body
        setPostRequestContent(conn, jsonObject);

        int status = conn.getResponseCode();
        if (status != 200) {
            throw new IOException("Post failed with error code " + status);
        } else {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        }

        if (conn != null) {
            conn.disconnect();
        }

        String responseJSON = response.toString();

        // 5. return response message
        return responseJSON;
    }
}

