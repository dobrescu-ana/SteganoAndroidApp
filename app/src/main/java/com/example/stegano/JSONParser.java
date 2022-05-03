package com.example.stegano;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class JSONParser extends AsyncTask<String, Integer, ArrayList<ImageInfo>> {

    @Override
    public ArrayList<ImageInfo> doInBackground(String... strings) {

        try{
            ArrayList<ImageInfo> images = new ArrayList<ImageInfo>();
            URL url = new URL(strings[0]);

            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            InputStream inputStream = http.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null)
                builder.append(line);

            JSONObject object = new JSONObject(builder.toString());
            JSONObject photos = object.getJSONObject("photos");
            JSONArray dataArray = photos.getJSONArray("photo");
            int count = dataArray.length();

            for(int i = 0; i < 10; i++) {
                String farm = dataArray.getJSONObject(i).getString("farm");
                String server = dataArray.getJSONObject(i).getString("server");
                String id = dataArray.getJSONObject(i).getString("id");
                String secret = dataArray.getJSONObject(i).getString("secret");
                //image URL
                String picURL = "https://farm" + farm + ".static.flickr.com/" + server + "/" + id + "_" + secret + "_b.jpg";

                //get image from URL
                URL url_pic = new URL(picURL);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                //InputStream input = connection.getInputStream();
                Bitmap image_from_URL = BitmapFactory.decodeStream((InputStream)url_pic.getContent());
                images.add(new ImageInfo(picURL, id, image_from_URL));
                //input.close();
            }
            return images;

        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
