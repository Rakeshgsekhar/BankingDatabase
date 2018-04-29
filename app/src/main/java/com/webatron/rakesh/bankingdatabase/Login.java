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

public class Login extends AppCompatActivity {
    Button login,register;
    EditText User,Pass;
    String loginuser,loginpass;
    SharedPreferences userdetails;
    SharedPreferences.Editor useredit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button)findViewById(R.id.login);
        register = (Button)findViewById(R.id.register);

        User = (EditText)findViewById(R.id.username);
        Pass = (EditText)findViewById(R.id.password);

        //LOGIN ACTION TO TAKE checks usernam and password*****************
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginuser = User.getText().toString();
                loginpass = Pass.getText().toString();
                new doLoginBackground().execute(loginuser,loginpass);

            }
        });

        //Register Action call Register Activity************************

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(Login.this,Register.class);
                startActivity(register);
            }
        });
    }

    public class doLoginBackground extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL("http://192.168.43.130/test/login.php");
                HttpURLConnection link = (HttpURLConnection)url.openConnection();
                link.setRequestMethod("POST"); //Request method to send query input to php***
                //set timeout for connection and reading the connected
                link.setConnectTimeout(10000);
                link.setReadTimeout(10000);

                //set input anf output true

                link.setDoInput(true);
                link.setDoOutput(true);

                Uri.Builder build = new Uri.Builder();

                /*********Build appaned data to be send to php file**************/

                build.appendQueryParameter("USER",params[0]);
                build.appendQueryParameter("PASS",params[1]);

                String loginquery = build.build().getEncodedQuery(); /******appending data to sending url******/

                OutputStream output = link.getOutputStream();

                BufferedWriter sender = new BufferedWriter(new OutputStreamWriter(output,"UTF-8"));

                sender.write(loginquery);
                sender.flush();
                sender.close();
                output.close();
                link.connect();

                int responseCode = link.getResponseCode();

                if(responseCode == 200){
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

            if((s.contains("Error"))){

                Toast.makeText(Login.this,s,Toast.LENGTH_SHORT).show();

            }else{
                userdetails = getSharedPreferences("loginuserdetails",MODE_PRIVATE);
                useredit = userdetails.edit();
                useredit.putString("USERNAME",loginuser);
                useredit.putString("PASSWORD",loginpass);
                useredit.apply();
                if(s.contains("ADMIN"))
                {
                    Intent home = new Intent(Login.this, AdminHome.class);
                    startActivity(home);
                    Toast.makeText(Login.this, s, Toast.LENGTH_SHORT).show();
                    finish();

                }else if(s.contains("USER")) {
                    Intent home = new Intent(Login.this, Home.class);
                    startActivity(home);
                    Toast.makeText(Login.this, s, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
}
