package com.example.whatstheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView textView;
    public void getweather(View view){
       // editText.getText ().toString ();
        try{

        downloade task=new downloade();
        String encodedcity= URLEncoder.encode (editText.getText ().toString (),"UTF-8");
        task.execute ( "https://samples.openweathermap.org/data/2.5/weather?q="+ encodedcity+"&appid=b6907d289e10d714a6e88b30761fae22" );// this api is taking the text which come form edit text
// when you wantto down the keyboard
        InputMethodManager inputMethodManager=(InputMethodManager)getSystemService ( Context.INPUT_METHOD_SERVICE );
        inputMethodManager.hideSoftInputFromWindow ( editText.getWindowToken (),0 );}
        catch (Exception e){
            Toast.makeText ( getApplicationContext (),"Could not find the weather",Toast.LENGTH_SHORT ).show ();
            e.printStackTrace ();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        //   ImageView imageView=findViewById ( R.id.imageView );
        editText=findViewById ( R.id.editText );
        textView=findViewById ( R.id.resultextview );


    }
    public class downloade extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            String result="";
            URL url;
            HttpURLConnection httpURLConnection=null;
            try{
                url=new URL (urls[0]);
                //  url=new URL("http://localhost:7007/jsontest");
                httpURLConnection=(HttpURLConnection) url.openConnection();
                InputStream in=httpURLConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while (data!=-1){
                    char current=(char)data;
                    result+=current;
                    data=reader.read();
                }
                return result;
                //   return result="{\"coord\":{\"lon\":-0.13,\"lat\":51.51},\"weather\":[{\"id\":300,\"main\":\"Drizzle\",\"description\":\"light intensity drizzle\",\"icon\":\"09d\"}],\"base\":\"stations\",\"main\":{\"temp\":280.32,\"pressure\":1012,\"humidity\":81,\"temp_min\":279.15,\"temp_max\":281.15},\"visibility\":10000,\"wind\":{\"speed\":4.1,\"deg\":80},\"clouds\":{\"all\":90},\"dt\":1485789600,\"sys\":{\"type\":1,\"id\":5091,\"message\":0.0103,\"country\":\"GB\",\"sunrise\":1485762037,\"sunset\":1485794875},\"id\":2643743,\"name\":\"London\",\"cod\":200}";
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText ( getApplicationContext (),"Could not find the weather",Toast.LENGTH_SHORT ).show ();
                return null;
            }

//            return result;
        }
        // both above and this method are different this method is for the user interface
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute ( s );
            Log.i ( "JSON",s );
            try{
                JSONObject jsonobject=new JSONObject (s);
                String weatherinfo=jsonobject.getString ( "weather" );
                Log.i ( "weathe",weatherinfo );
                JSONArray jsonArray=new JSONArray ( weatherinfo );
                String message="";
                for (int i=0;i<jsonArray.length ();i++)
                {
                    JSONObject object=jsonArray.getJSONObject ( i );
              String main=  object.getString ( "main" ) ;
                    String description= object.getString ( "description" ) ;
                    if (!main.equals ( "" ) && !description.equals ( "" )){
                      message+=main +":"+description +"\r\n";
                    }


                }
                if (!message.equals ( "" )) {
                    textView.setText ( message );
                }else {
                    Toast.makeText ( getApplicationContext (),"Could not find the weather",Toast.LENGTH_SHORT ).show ();
                }
            }
            catch(Exception e){
                Toast.makeText ( getApplicationContext (),"Could not find the weather",Toast.LENGTH_SHORT ).show ();
                e.printStackTrace();
            }


        }
    }




}
