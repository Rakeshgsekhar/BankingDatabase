package com.webatron.rakesh.bankingdatabase;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class CustomerView extends AppCompatActivity {

    String name[],userName;
    ListView Lcust;
    ArrayAdapter<String> custArry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view);
        Lcust = (ListView)findViewById(R.id.custID);
        new doCustumerViewBackground().execute();
        Lcust.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userName = parent.getItemAtPosition(position).toString();
                Toast.makeText(CustomerView.this,userName,Toast.LENGTH_SHORT).show();
                Intent userdetails = new Intent(CustomerView.this,UserDetails.class);
                userdetails.putExtra("Name",userName);
                startActivity(userdetails);
            }
        });
    }

    public class doCustumerViewBackground extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://192.168.43.130/test/custumers.php");
                HttpURLConnection link = (HttpURLConnection)url.openConnection();
                link.setRequestMethod("POST");

                link.setConnectTimeout(10000);
                link.setReadTimeout(10000);
                link.setDoOutput(true);
                link.setDoInput(true);

                Uri.Builder build = new Uri.Builder();

                build.appendQueryParameter("STATUS","ADMIN");

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
                Toast.makeText(CustomerView.this,s,Toast.LENGTH_SHORT).show();

            }else{
                // Toast.makeText(Profile.this,s,Toast.LENGTH_SHORT).show();
                try {
                    JSONObject job = new JSONObject(s);
                    JSONArray jarray = job.getJSONArray("list");
                    name = new String[jarray.length()];
                    //Toast.makeText(CustomerView.this,jarray.length(),Toast.LENGTH_LONG).show();
                    for(int i=0;i<jarray.length();i++){
                        JSONObject job2 = jarray.getJSONObject(i);
                        String jname = job2.getString("NAME");
                        name[i]=jname;
                    }

                    custArry = new ArrayAdapter<String>(CustomerView.this,android.R.layout.simple_list_item_1,name);
                    Lcust.setAdapter(custArry);

                    /*String jemail = job2.getString("EMAIL");
                    String jphno = job2.getString("MOBILE_NO");
                    String jacno = job2.getString("ACCOUNT_NO");
                    String jbal = job2.getString("AMOUNT");
                   //name.setText(("NAME: "+jname));
                    //email.setText("EMAIL ID :"+jemail);
                    //phno.setText("PHONE NO :"+jphno);
                    //acno.setText("ACCOUNT NO :"+jacno);
                    //bal.setText("BALANCE :"+jbal);*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }
    }

}
