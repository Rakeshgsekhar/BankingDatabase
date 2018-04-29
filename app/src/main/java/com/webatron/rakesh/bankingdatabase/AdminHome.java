package com.webatron.rakesh.bankingdatabase;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class AdminHome extends AppCompatActivity {

    Button b1,b2;
    SharedPreferences userdetails;
    SharedPreferences.Editor useredit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        b1 = (Button)findViewById(R.id.viewId);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent custview = new Intent(AdminHome.this,CustomerView.class);
                startActivity(custview);
            }
        });
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
            AlertDialog.Builder alert = new AlertDialog.Builder(AdminHome.this);
            alert.setTitle("Logout");
            alert.setMessage("Do you want continue");
            alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    userdetails = getSharedPreferences("loginuserdetails",MODE_PRIVATE);
                    useredit = userdetails.edit();
                    useredit.clear();
                    Intent loginView = new Intent(AdminHome.this,Login.class);
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
            AlertDialog alertbuild = alert.create();
            alertbuild.show();

        }
        else if(idtem == (R.id.passchng)){
            Intent passchng = new Intent(AdminHome.this,PasswordChange.class);
            startActivity(passchng);
            finish();


        }
        return super.onOptionsItemSelected(item);
    }
}
