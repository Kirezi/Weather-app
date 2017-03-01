package com.example.patrickkizz.weatherapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {



    EditText cityText;
    String myCity;
    String link;
    String main;
    String description;
    TextView resultTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        resultTextview=(TextView)findViewById(R.id.resultTextview);


        cityText =(EditText)findViewById(R.id.cityEditview);


    }

    public void weather(View view){


       if(cityText.getText().toString().isEmpty()){

           Toast.makeText(getApplicationContext(),"enter a country",Toast.LENGTH_LONG).show();
       }
        else {


           myCity = cityText.getText().toString();
           try {
               String encoded = URLEncoder.encode(myCity, "UTF-8");
               link = "http://api.openweathermap.org/data/2.5/weather?q=" + encoded + "&appid=602f4e660e150a691f767ead2150450d";

               DownloadableTask downloadableTask = new DownloadableTask();
               downloadableTask.execute(link);
           } catch (UnsupportedEncodingException e) {
               e.printStackTrace();
               Toast.makeText(getApplicationContext(), "Enter a valid country", Toast.LENGTH_LONG);
           }
           InputMethodManager mgr=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
           mgr.hideSoftInputFromWindow(cityText.getWindowToken(),0);

       }






    }


    public class DownloadableTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;

                    result += current;

                    data = reader.read();

                }

                return result;

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"enter a valid country",Toast.LENGTH_LONG);
        }


        return null;
        }



        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String message="";
            try {
                JSONObject myResult= new JSONObject(result);
                String weatherInfo=myResult.getString("weather");
                Log.i("weather content",weatherInfo);

                JSONArray arr = new JSONArray(weatherInfo);

                for (int i = 0; i < arr.length(); i++) {

                    JSONObject jsonPart = arr.getJSONObject(i);

                    main=jsonPart.getString("main");
                   description= jsonPart.getString("description");
                    if(main!=""&&description!=""){

                        message+=main+":"+description+"\r\n";
                    }
                    if (message !="") {
                        resultTextview.setText(message);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"enter a valid country",Toast.LENGTH_LONG);
                    }


                }



            }


            catch (JSONException e) {
                Toast.makeText(getApplicationContext(),"enter a valid country",Toast.LENGTH_LONG).show();
            }



        }

        }
    }

