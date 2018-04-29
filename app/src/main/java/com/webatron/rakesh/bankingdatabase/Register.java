package com.webatron.rakesh.bankingdatabase;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.net.URI;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Register extends AppCompatActivity {
    Button confirm;
    EditText name,email,phno,acno,username,password,amount;
    String Name,Email,Phone,Acno,Username,Password,Amount;
    int Amt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //BUTTON EDIT TEXT INITIALIZATION********************

        confirm = (Button)findViewById(R.id.confirm);
        name = (EditText)findViewById(R.id.name);
        email = (EditText)findViewById(R.id.emailid);
        phno = (EditText)findViewById(R.id.mobno);
        username = (EditText)findViewById(R.id.user);
        password = (EditText)findViewById(R.id.pass);
        acno = (EditText)findViewById(R.id.acno);
        amount = (EditText)findViewById(R.id.amount);

        //CONFIRM Button ON Click Listener***********Reads all data from EditText Fields. . . **********

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Name = name.getText().toString();
                Email = email.getText().toString();
                Phone = phno.getText().toString();
                Acno = acno.getText().toString();
                Amount = amount.getText().toString();
                Username = username.getText().toString();
                Password = password.getText().toString();
                new Connection().execute();

            }
        });





    }
    /* Connection request should be done on background, it should run in  background*/
    public class Connection extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL("http://192.168.43.130/test/test.php");

                /*Connection link to server address 192.168.43.130  (ifconfig  on Terminal wlsp9so
                  URL is used to get connection(use http instead of https */

                HttpURLConnection link = (HttpURLConnection)url.openConnection();
                link.setRequestMethod("POST");
                link.setConnectTimeout(10000);
                link.setReadTimeout(10000);
                link.setDoInput(true);
                link.setDoOutput(true);
                Uri.Builder build = new Uri.Builder();

                /* build append to add data to be send along with request to php file on server */

                build.appendQueryParameter("Name",Name);
                build.appendQueryParameter("EMAIL",Email);
                build.appendQueryParameter("MOBILENO",Phone);
                build.appendQueryParameter("AMOUNT",Amount);
                build.appendQueryParameter("ACCOUNTNO",Acno);
                build.appendQueryParameter("USER",Username);
                build.appendQueryParameter("PASS",Password);
                /* to encode date into query*/
                String query = build.build().getEncodedQuery();

                OutputStream output = link.getOutputStream();
                BufferedWriter sender = new BufferedWriter(new OutputStreamWriter(output,"UTF-8"));
                sender.write(query);
                sender.flush();
                sender.close();
                output.close();
                link.connect();

                int responseCode = link.getResponseCode(); //check connection status
                if(responseCode == 200){
                    InputStream response = link.getInputStream();
                    BufferedReader read = new BufferedReader(new InputStreamReader(response));
                    String readout;
                    StringBuilder strbld = new StringBuilder();
                    while((readout = read.readLine())!=null){
                        strbld.append(readout);

                    }

                    return (String.valueOf(strbld));

                }
                else{
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
            if(s.equals("Connected!!Successfully inserted !! ")){
                Toast.makeText(Register.this,s,Toast.LENGTH_SHORT).show();
                Intent log = new Intent(Register.this,Login.class);
                startActivity(log);
            }else{
                Toast.makeText(Register.this,s,Toast.LENGTH_SHORT).show();
            }

        }
    }
}
