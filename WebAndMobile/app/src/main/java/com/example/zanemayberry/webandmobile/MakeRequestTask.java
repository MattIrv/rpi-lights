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
    public static void makeRequest(String url, Direction curDir, Direction nextDir, int mode) throws IOException {
        int curRed = 0;
        int curGreen = 0;
        int curBlue = 0;
        if (curDir == Direction.UP) {
            curBlue = 255;
        } else if (curDir == Direction.RIGHT) {
            curRed = 255;
        } else if (curDir == Direction.DOWN) {
            curGreen = 255;
        } else if (curDir == Direction.LEFT) {
            curBlue = 255;
            curGreen = 255;
            curRed = 255;
        }
        int nextRed = 0;
        int nextGreen = 0;
        int nextBlue = 0;
        if (nextDir == Direction.UP) {
            nextBlue = 255;
        } else if (nextDir == Direction.RIGHT) {
            nextRed = 255;
        } else if (nextDir == Direction.DOWN) {
            nextGreen = 255;
        } else if (nextDir == Direction.LEFT) {
            nextBlue = 255;
            nextGreen = 255;
            nextRed = 255;
        }
        HttpClient client = new DefaultHttpClient();
        HttpPost request = new HttpPost(url);
        JSONObject firstLights = new JSONObject();
        JSONObject secondLights = new JSONObject();
        if (mode == 0) {
            try {
                firstLights.put("lightId", 0);
                firstLights.put("red", curRed);
                firstLights.put("green", curGreen);
                firstLights.put("blue", curBlue);
                firstLights.put("intensity", 1.0);
            }
            catch (JSONException e) {
                throw new IOException("Input value formatted incorrectly.");
            }
        }
        if (mode == 1) {
            try {
                firstLights.put("lightId", 0);
                firstLights.put("red", curRed);
                firstLights.put("green", curGreen);
                firstLights.put("blue", curBlue);
                firstLights.put("intensity", 1.0);
                secondLights.put("lightId", 16);
                secondLights.put("red", nextRed);
                secondLights.put("green", nextGreen);
                secondLights.put("blue", nextBlue);
                secondLights.put("intensity", 1.0);
            }
            catch (JSONException e) {
                throw new IOException("Input value formatted incorrectly.");
            }
        }
        if (mode == 2) {
            try {
                firstLights.put("lightId", 0);
                firstLights.put("red", nextRed);
                firstLights.put("green", nextGreen);
                firstLights.put("blue", nextBlue);
                firstLights.put("intensity", 1.0);
                secondLights.put("lightId", 16);
                secondLights.put("red", curRed);
                secondLights.put("green", curGreen);
                secondLights.put("blue", curBlue);
                secondLights.put("intensity", 1.0);
            }
            catch (JSONException e) {
                throw new IOException("Input value formatted incorrectly.");
            }
        }
        JSONArray lightArray = new JSONArray();
        lightArray.put(firstLights);
        lightArray.put(secondLights);
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
