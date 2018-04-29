package com.webatron.rakesh.bankingdatabase;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UserDetails extends AppCompatActivity {

    String username;
    TextView name,email,phno,acno,bal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        name = (TextView)findViewById(R.id.nameUser);
        email = (TextView)findViewById(R.id.eUser);

        phno = (TextView)findViewById(R.id.phnoUser);
        acno = (TextView)findViewById(R.id.acnoUser);

        bal = (TextView)findViewById(R.id.balUser);
        Intent recive = getIntent();
        username = recive.getStringExtra("Name");
        new doUserProfileBackground().execute(username);


    }

    public class doUserProfileBackground extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://192.168.43.130/test/profile.php");
                HttpURLConnection link = (HttpURLConnection)url.openConnection();
                link.setRequestMethod("POST");

                link.setConnectTimeout(10000);
                link.setReadTimeout(10000);
                link.setDoOutput(true);
                link.setDoInput(true);

                Uri.Builder build = new Uri.Builder();

                build.appendQueryParameter("USER",strings[0]);

                String withdrawquery = build.build().getEncodedQuery();

                OutputStream output = link.getOutputStream();

                BufferedWriter sender = new BufferedWriter(new OutputStreamWriter(output,"UTF-8"));

                sender.write(withdrawquery);
                sender.flush();
                sender.close();
                output.close();
                link.connect();

                int responsecode = link.getResponseCode();

                if(responsecode==200){
                    InputStream response = link.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response));
                    String readoutput;
                    StringBuilder strbuild = new StringBuilder();

                    while((readoutput = reader.readLine())!=null){
                        strbuild.append(readoutput);
                    }
                    return (strbuild.toString());
                }else{
                    return "Connection Error";
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.contains("Error")){
                Toast.makeText(UserDetails.this,s,Toast.LENGTH_SHORT).show();

            }else{
                // Toast.makeText(Profile.this,s,Toast.LENGTH_SHORT).show();
                try {
                    JSONObject job = new JSONObject(s);
                    JSONArray jarray = job.getJSONArray("list");

                    JSONObject job2 = jarray.getJSONObject(0);
                    String jname = job2.getString("NAME");
                    String jemail = job2.getString("EMAIL");
                    String jphno = job2.getString("MOBILE_NO");
                    String jacno = job2.getString("ACCOUNT_NO");
                    String jbal = job2.getString("AMOUNT");
                    name.setText((jname));
                    email.setText(jemail);
                    phno.setText(jphno);
                    acno.setText(jacno);
                    bal.setText(jbal);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }
}
