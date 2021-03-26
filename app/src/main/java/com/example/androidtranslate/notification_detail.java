package com.example.androidtranslate;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class notification_detail extends AppCompatActivity {
    DB_Controller_Firebase controller;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);
        controller = new DB_Controller_Firebase(this,"",null,1);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Notification Detail");


        TextView txttitledet = findViewById(R.id.dtitle);
        TextView txtmsgdet = findViewById(R.id.dmsg);
        Intent show = getIntent();
        String x = show.getStringExtra("aa");
        String y = show.getStringExtra("bb");
        txttitledet.setText(x);
        txtmsgdet.setText(y);


    }
    //back
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        startActivity(new Intent(getApplicationContext(),Notification.class));
        return true;
    }
    //
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menudel, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setMode(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    private void setMode(int selectedMode) {
        switch (selectedMode) {
            case R.id.delete:
                showDialog();
        }
    }

    private void showDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("Are you sure you want to delete this record?");

        // set pesan dari dialog
        alertDialogBuilder

                .setIcon(R.drawable.remove)
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        Intent show = getIntent();
                        String x = show.getStringExtra("aa");
                        String y = show.getStringExtra("bb");
                        controller.delete_notif(x,y);
                       // notification_detail.this.finish();

                        Intent back = new Intent(notification_detail.this,Notification.class);
                        startActivity(back);
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();

    }


}

