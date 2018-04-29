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
import java.util.regex.Pattern;

public class Profile extends AppCompatActivity {

    TextView name,email,phno,acno,bal;
    SharedPreferences userdetails;
    SharedPreferences.Editor editor;
    String username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name = (TextView)findViewById(R.id.nameView);
        email = (TextView)findViewById(R.id.emailview);
        phno = (TextView)findViewById(R.id.phoneview);
        acno = (TextView)findViewById(R.id.acnoview);
        bal = (TextView)findViewById(R.id.balanceview);
        userdetails = getSharedPreferences("loginuserdetails",MODE_PRIVATE);
        editor = userdetails.edit();

        username = userdetails.getString("USERNAME","null");

        new doProfileBackground().execute(username);


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
            AlertDialog.Builder alert = new AlertDialog.Builder(Profile.this);
            alert.setTitle("Logout");
            alert.setMessage("Do you want continue");
            alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    userdetails = getSharedPreferences("loginuserdetails",MODE_PRIVATE);
                    editor = userdetails.edit();
                    editor.clear();
                    Intent loginView = new Intent(Profile.this,Login.class);
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

    public class doProfileBackground extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL("http://192.168.43.130/test/profile.php");
                HttpURLConnection link = (HttpURLConnection)url.openConnection();
                link.setRequestMethod("POST");

                link.setConnectTimeout(10000);
                link.setReadTimeout(10000);
                link.setDoOutput(true);
                link.setDoInput(true);

                Uri.Builder build = new Uri.Builder();

                build.appendQueryParameter("USER",params[0]);

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
                Toast.makeText(Profile.this,s,Toast.LENGTH_SHORT).show();

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
                    name.setText(("NAME: "+jname));
                    email.setText("EMAIL ID :"+jemail);
                    phno.setText("PHONE NO :"+jphno);
                    acno.setText("ACCOUNT NO :"+jacno);
                    bal.setText("BALANCE :"+jbal);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }
}
