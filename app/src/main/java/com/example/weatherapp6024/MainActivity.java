package com.example.weatherapp6024;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    TextView cityName;
    Button searchButton;
    TextView result;

    class Weather extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... address) {
            try {
                URL url=new URL(address[0]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                connection.connect();

                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                int data = isr.read();
                String content="";
                char ch;
                while (data > 0){
                    ch=(char)data;
                    content=content+ch;
                    data=isr.read();
                }
                return content;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public void search(View view){
        cityName=findViewById(R.id.cityName);
        searchButton=findViewById(R.id.searchButton);
        result=findViewById(R.id.result);

        String cName=cityName.getText().toString();
        String content;
        Weather weather=new Weather();
        try {
            content=weather.execute("https://openweathermap.org/data/2.5/weather?q="+
                    cName+"&appid=439d4b804bc8187953eb36d2a8c26a02").get();
            Log.i("contentData",content);

            JSONObject jsonObject=new JSONObject(content);
            String weatherData=jsonObject.getString("weather");
            String maintemp=jsonObject.getString("main");
            double visibility;
           // Log.i("weatherData",weatherData);

            JSONArray array=new JSONArray(weatherData);

            String main="";
            String descripton="";
            String temperature="";


            for (int i=0;i<array.length();i++){
                JSONObject weatherPart=array.getJSONObject(i);
                main=weatherPart.getString("main");
                descripton=weatherPart.getString("description");
            }

            JSONObject mainpart=new JSONObject(maintemp);
            temperature=mainpart.getString("temp");

            visibility=Double.parseDouble(jsonObject.getString("visibility"));
             int visibilityInKilometer=(int)visibility/1000;
            Log.i("Temperature",temperature);

           /* Log.i("main",main);
            Log.i("description",descripton); */

            String resultText = "Main :             "+main+
                           "\nDescription :    "+descripton +
                           "\nTemperature :    "+temperature+"°C" +
                           "\nvisibility :     "+visibilityInKilometer+"KM";

            result.setText(resultText);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
}