package com.example.zanemayberry.webandmobile;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by zanemayberry on 11/2/14.
 */
public class MakeRequestTask {
    public static void makeRequest(String url, int red, int green, int blue, double intensity) throws IOException {
        System.out.println(url);
        HttpClient client = new DefaultHttpClient();
        HttpPost request = new HttpPost(url);
        JSONObject singleLight = new JSONObject();
        try {
            singleLight.put("lightId", 0);
            singleLight.put("red", red);
            singleLight.put("green", green);
            singleLight.put("blue", blue);
            singleLight.put("intensity", intensity);
        }
        catch (JSONException e) {
            throw new IOException("Input value formatted incorrectly.");
        }
        JSONArray lightArray = new JSONArray();
        lightArray.put(singleLight);
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("lights", lightArray);
            requestObject.put("propagate", true);
        }
        catch (JSONException e) {
            throw new IOException("Input value formatted incorrectly.");
        }
        StringEntity se = new StringEntity(requestObject.toString());
        se.setContentType("application/json");
        request.setEntity(new StringEntity(requestObject.toString()));
        try {
            HttpResponse response = client.execute(request);
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() >= 300) {
                throw new IOException("Request failed with status " + status.getStatusCode());
            }
        }
        catch (IllegalArgumentException e) {
            throw new IOException("Input value formatted incorrectly.");
        }

    }
}
