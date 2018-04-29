package com.webatron.rakesh.bankingdatabase;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.regex.Pattern;

public class Deposit extends AppCompatActivity {

    Button btnd;
    String amt;
    String username,password;
    EditText amtdeposit;
    SharedPreferences userdetails;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        btnd = (Button)findViewById(R.id.btndeposit);
        amtdeposit = (EditText)findViewById(R.id.depositamt);
        userdetails = getSharedPreferences("loginuserdetails",MODE_PRIVATE);
        editor = userdetails.edit();

        btnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amt = amtdeposit.getText().toString();
                username = userdetails.getString("USERNAME","null");
                password = userdetails.getString("PASSWORD","null");

                AlertDialog.Builder alertbuild = new AlertDialog.Builder(Deposit.this);
                alertbuild.setTitle("CONFIRMATION");
                alertbuild.setMessage("DO yo Want to Continue");
                alertbuild.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new doDepositBackground().execute(username,password,amt);
                    }
                });

                alertbuild.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = alertbuild.create();
                alert.show();


            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intnt = new Intent(Deposit.this,Home.class);
        startActivity(intnt);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homemenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int idtem = item.getItemId();
        if(idtem == (R.id.menuid)){
            //Toast.makeText(Home.this,"LOG OUT!!!",Toast.LENGTH_SHORT).show();
            AlertDialog.Builder alert = new AlertDialog.Builder(Deposit.this);
            alert.setTitle("Logout");
            alert.setMessage("Do you want continue");
            alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    userdetails = getSharedPreferences("loginuserdetails",MODE_PRIVATE);
                    editor = userdetails.edit();
                    editor.clear();
                    Intent loginView = new Intent(Deposit.this,Login.class);
                    startActivity(loginView);
                    finish();
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertbuilder = alert.create();
            alertbuilder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public class doDepositBackground extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL("http://192.168.43.130/test/deposit.php");
                HttpURLConnection link = (HttpURLConnection)url.openConnection();
                link.setRequestMethod("POST");

                link.setConnectTimeout(10000);
                link.setReadTimeout(10000);
                link.setDoOutput(true);
                link.setDoInput(true);

                Uri.Builder build = new Uri.Builder();

                build.appendQueryParameter("USER",params[0]);
                build.appendQueryParameter("PASS",params[1]);
                build.appendQueryParameter("AMOUNT",params[2]);

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
                Toast.makeText(Deposit.this,s,Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(Deposit.this,s,Toast.LENGTH_SHORT).show();
                Intent homedepo = new Intent(Deposit.this,Home.class);
                startActivity(homedepo);
                finish();
                /*String resultstr[];
                      resultstr=s.split(Pattern.quote("?"));*/

            }
        }
    }
}
