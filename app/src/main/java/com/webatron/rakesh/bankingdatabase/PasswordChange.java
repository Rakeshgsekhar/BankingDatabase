package com.webatron.rakesh.bankingdatabase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class PasswordChange extends AppCompatActivity {

    Button chngpass;
    EditText oldpass,newpass,newpass2;
    String oldPass,newPass,newPass2,username;
    SharedPreferences userdetails;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);
        chngpass = (Button)findViewById(R.id.submitpass);
        oldpass = (EditText)findViewById(R.id.curntpass);
        newpass = (EditText)findViewById(R.id.newpasswrd);
        newpass2 = (EditText)findViewById(R.id.newpasswrd2);





        userdetails = getSharedPreferences("loginuserdetails",MODE_PRIVATE);
        editor = userdetails.edit();

        username = userdetails.getString("USERNAME","null");

        chngpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldPass = oldpass.getText().toString();
                newPass = newpass.getText().toString();
                newPass2 = newpass2.getText().toString();
                if(newPass.equals(newPass2)){

                    new doPasswordBackground().execute(username,oldPass,newPass);


                }else{
                    Toast.makeText(PasswordChange.this,"NEW PASSWORD DOES NOT MATCH",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public class doPasswordBackground extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL("http://192.168.43.130/test/passwordchange.php");
                HttpURLConnection link = (HttpURLConnection)url.openConnection();
                link.setRequestMethod("POST");

                link.setConnectTimeout(10000);
                link.setReadTimeout(10000);
                link.setDoOutput(true);
                link.setDoInput(true);

                Uri.Builder build = new Uri.Builder();

                build.appendQueryParameter("USER",params[0]);
                build.appendQueryParameter("PASS",params[1]);
                build.appendQueryParameter("NEWPASS",params[2]);

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
                Toast.makeText(PasswordChange.this,s,Toast.LENGTH_SHORT).show();

            }else{
                 Toast.makeText(PasswordChange.this,s,Toast.LENGTH_SHORT).show();
                 editor.putString("PASS",newPass);
                 editor.apply();
                Intent homeView = new Intent(PasswordChange.this,Login.class);
                startActivity(homeView);
                finish();
            }
        }
    }
}
