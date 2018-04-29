package com.webatron.rakesh.bankingdatabase;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Home extends AppCompatActivity {
    Button profile,withdraw,deposit;
    SharedPreferences userdetails;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        profile = (Button)findViewById(R.id.profile);
        withdraw = (Button)findViewById(R.id.withdraw);
        deposit = (Button)findViewById(R.id.deposit);

        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent withdraw = new Intent(Home.this,Withdraw.class);
                startActivity(withdraw);
                finish();
            }
        });

        deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deposit = new Intent(Home.this,Deposit.class);
                startActivity(deposit);
                finish();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileView = new Intent(Home.this,Profile.class);
                startActivity(profileView);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intnt = new Intent(Home.this, Login.class);
        editor.clear();
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
            AlertDialog.Builder alert = new AlertDialog.Builder(Home.this);
            alert.setTitle("Logout");
            alert.setMessage("Do you want continue");
            alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    userdetails = getSharedPreferences("loginuserdetails",MODE_PRIVATE);
                    editor = userdetails.edit();
                    editor.clear();
                    Intent loginView = new Intent(Home.this,Login.class);
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
            Intent passchng = new Intent(Home.this,PasswordChange.class);
            startActivity(passchng);
            finish();


        }
        return super.onOptionsItemSelected(item);
    }
}
