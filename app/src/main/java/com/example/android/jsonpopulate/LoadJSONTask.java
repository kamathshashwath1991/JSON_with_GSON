package com.example.android.jsonpopulate;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by shash on 6/19/2017.
 */

public class LoadJSONTask extends AsyncTask<String,Void,ResponseClass> {

    public interface Listener{
        void onLoaded(List<AndroidVersion> androidList);
        void onError();
    }

    private Listener mListener;

    public LoadJSONTask(Listener listener){
        this.mListener= listener;
    }


    @Override
    protected ResponseClass doInBackground(String... params) {

        try {
            String stringResponse= loadJSON(params[0]);
            Gson gson= new Gson();
            return gson.fromJson(stringResponse,ResponseClass.class);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        catch (JsonSyntaxException e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected void onPostExecute(ResponseClass responseClass) {
        if (responseClass!=null){
            mListener.onLoaded(responseClass.getAndroid());
        }
        else {
            mListener.onError();
        }
        super.onPostExecute(responseClass);
    }

    private String loadJSON(String jsonURL) throws IOException {
        URL url= new URL(jsonURL);
        HttpURLConnection connection= (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(1000);
        connection.setConnectTimeout(15000);
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.connect();

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder response= new StringBuilder();
        while ((line= in.readLine())!=null){
            response.append(line);
        }
        in.close();

        return response.toString();
    }
}
