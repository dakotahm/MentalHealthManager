
package com.boson.dakotahmoore.mentalhealthmanager;

/**
 * Created by Austin on 3/8/2017.
 */

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Glue
{
    static String  tag;
    //Pass in url and hashmap of request, get back json string
    static public String performPostCall(String requestURL, HashMap<String, String> postDataParams)
    {
        Log.d("debugging","in performPostCall");
        URL url;
        String response = "";
        OutputStreamWriter writer;
       // HashMap<String,Integer> test=new HashMap<String, Integer>();
       // test.put("user_id",1);
        JSONObject json = new JSONObject(postDataParams);

        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("content-type","application/json;charset=utf-8");

            conn.connect();
            OutputStream os = conn.getOutputStream();
            writer = new OutputStreamWriter(os);
            writer.write(json.toString());
            writer.flush();


            int responseCode=conn.getResponseCode();
            Log.d("debugging","here");


            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response = "failure";
            }
            writer.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    static private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException
    {
        Log.d("debugging","in getPostDataString");
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        Log.d(tag,result.toString());
        return result.toString();
    }
}
